#include <queue>
#include "bfs.h"
#include <map>

std::shared_ptr<const state> bfs(std::shared_ptr<const state> root) {
    std::shared_ptr<const state> goal = nullptr;

    // queue is not thread safe, however we can use two vectors to simulate it in a thread safe way
    std::vector< std::shared_ptr<const state> > readOnly;
    std::vector< std::shared_ptr<const state> > writeOnly;
    // because we want to return goal with the lowest ID we need to store goals and compare them
    std::vector< std::shared_ptr<const state> > goals;
    // map to store nodes we already visited to prevent going in circles, no cost is taken into consideration in BFS
    std::map<unsigned long long, int> visited;


    // push first states into our "queue"
    for(auto &nextState : root->next_states()) {
        readOnly.push_back(nextState);
    }

    // while we can expand, expand, ends also if we find goal
    while(!readOnly.empty()) {
        // parallel loop over readOnly vector
        unsigned int readOnlySize = readOnly.size();
        #pragma omp parallel for
        for(unsigned int i = 0; i < readOnlySize; i++) {
            std::shared_ptr<const state> currentNode = readOnly[i];

            // if we visited this node for the first time add it to visited map
            if (visited.find(currentNode->get_identifier()) == visited.end()) {
                #pragma omp critical
                // add to map with dummy value, no need for actual value
                visited[currentNode->get_identifier()] = 0;
            }
            // check if the node was already visited, if it was already visited skip it
            else {
                continue;
            }

            // check if node is goal and if so add it to goals and skip rest of actions
            if(currentNode->is_goal()) {
                #pragma omp critical
                goals.push_back(currentNode);
                continue;
            }
            // if the node was not visited and the node is not a goal,
            // get next states of the node and push them into writeOnly vector
            std::vector<std::shared_ptr<const state>> nextStates = currentNode->next_states();
            for(unsigned int j = 0; j < nextStates.size(); j++) {
                // if the nextNode is goal push it into goals
                if(nextStates[j]->is_goal()) {
                    #pragma omp critical
                    goals.push_back(nextStates[j]);
                }
                #pragma omp critical
                writeOnly.push_back(nextStates[j]);
            }
        }
        // if we found goal(s) stop searching, all goals are from the same level
        if(!goals.empty()) {
            break;
        }
        // otherwise copy writeOnly contents into readOnly and clear writeOnly vector for new nodes
        // in this way we simulate queue
        else {
            readOnly = writeOnly;
            writeOnly.clear();
        }
    }

    // set goal to meaningful value, if goals are empty we didn't find anything return root and let us know using print
    if(!goals.empty()) {
        goal = goals[0];
    } else {
        printf("Goal not found");
        return root;
    }

    // reached only if goals are not empty, loop through goals and find the best one
    for(auto & currentGoal : goals) {
        if(currentGoal->get_identifier() < goal->get_identifier()) {
            goal = currentGoal;
        }
    }
    return goal;
}