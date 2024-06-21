#include "iddfs.h"
#include <map>


// simple dfs edited for id-dfs
void dfs(std::shared_ptr<const state> currentNode, std::map<unsigned long long, unsigned int> visited, int limit,
         std::vector< std::shared_ptr<const state> > &goals) {
    // if node is goal we can push it to vector
    if(currentNode->is_goal()) {
        goals.push_back(currentNode);
    }

    // if we reached final depth we cannot go deeper -> id-dfs property
    if(limit <= 0) {
        return;
    }

    // for loop, we go deeper into recursion if the nextNode was not previously visited or has better cost
    // each layer of recursion has its own visited map, passed by value not by reference, it is by design
    for(unsigned int i = 0; i < currentNode->next_states().size(); i++) {
        std::shared_ptr<const state> nextNode = currentNode->next_states()[i];
        auto skip = false;
        // if the node was never visited before add it to visited map
        if (visited.find(nextNode->get_identifier()) == visited.end()) {
            // add to map with value representing cost of the node when it was found
//            visited[nextNode->get_identifier()] = nextNode->current_cost();
            visited[nextNode->get_identifier()] = 0;

        }
        // if we visited the node before
        else {
            //check if this path does not have better cost
            // if yes rewrite stored node and go into recursion
//            if(nextNode->current_cost() < visited[nextNode->get_identifier()]) {
//                visited[nextNode->get_identifier()] = nextNode->current_cost();
//            }
            // otherwise skip recursion for this node
//            else {
            skip = true;
//            }
        }
        if(skip) {
            continue;
        }
        // go into recursion
        dfs(nextNode, visited, limit - 1, goals);
    }
    return;
}

std::shared_ptr<const state> iddfs(std::shared_ptr<const state> root) {
    auto maxDepth = 0;
    std::shared_ptr<const state> goal = nullptr;
    //  map to store visited nodes
    std::map<unsigned long long, unsigned int> visited;
    // vector to store found goals, we need the one with the lowest ID
    std::vector< std::shared_ptr<const state> > goals;

    // search until we find goal using id-dfs -> calling dfs with increasing maxDepth
    while(true) {
        dfs(root, visited, maxDepth, goals);
        if(!goals.empty()) {
            break;
        } else {
            maxDepth++;
        }
    }

    // set goal to not be nullptr, if goals are empty we didn't find anything return root and let us know via print
    if(!goals.empty()) {
//        printf("Size of goals is: %zu\n", goals.size());
        goal = goals[0];
//        for(unsigned int i = 0; i < goals.size(); i++) {
//            printf("Goal %d is: %s and costs: %d with ID: %llu\n",
//                   i, goals[i]->to_string().c_str(), goals[i]->current_cost(), goals[i]->get_identifier());
//        }
    } else {
        printf("Goal not found\n");
        return root;
    }

    // reached only if goals are not empty, loop through goals and find the best one
    for(unsigned int i = 0; i < goals.size(); i++) {
        auto currentGoal = goals[i];
        auto goalCost = goal->current_cost();
        auto currentGoalCost = currentGoal->current_cost();
        if(currentGoalCost < goalCost) {
            goal = currentGoal;
        }
        else if(currentGoalCost == goalCost) {
            auto goalID = goal->get_identifier();
            auto currentGoalID = currentGoal->get_identifier();
            if(currentGoalID < goalID) {
                goal = currentGoal;
            }
        }
    }
    return goal;
}