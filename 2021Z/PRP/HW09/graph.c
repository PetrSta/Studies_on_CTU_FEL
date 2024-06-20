#include <stdio.h>
#include <stdlib.h>
#include "graph.h"

// Allocate a new graph and return a reference to it.
graph_t* allocate_graph(){
    // getting memory for 20 edges
    graph_t *temporary_graph = malloc(sizeof(graph_t));
    if(temporary_graph == NULL){
        return NULL;
    }
    graph_t *graph = temporary_graph;
    graph->capacity = 50;
    graph->from = malloc(graph->capacity * sizeof(int));
    graph->to = malloc(graph->capacity * sizeof(int));
    graph->cost = malloc(graph->capacity * sizeof(int));
    graph->num_of_edges = 0;
    return graph;
}

// Free all allocated memory and set reference to the graph to NULL.
void free_graph(graph_t **graph){
    free((*graph)->from);
    free((*graph)->to);
    free((*graph)->cost);
    free(*graph);
    *graph = NULL;
}

// Load a graph from the text file.
void load_txt(const char *fname, graph_t *graph){
    FILE *graph_file;
    graph_file = fopen(fname, "r");
    int highest_node = 0;
    int last_highest_node = 0;
    int from;
    int to;
    int cost;
    while(fscanf(graph_file, "%d %d %d", &from, &to, &cost) == 3){
        //reallocate for bigger memory size
        if(graph->num_of_edges + 1 == graph->capacity){
            if(highest_node == last_highest_node || graph->capacity >= highest_node * 3){
                int *temporary_from = realloc(graph->from, (graph->capacity + 300) * sizeof(int));
                if(temporary_from == NULL){
                    fprintf(stderr, "Realloc failed.\n");
                    exit(1);
                }
                graph->from = temporary_from;
                int *temporary_to = realloc(graph->to, (graph->capacity + 300) * sizeof(int));
                if(temporary_to == NULL){
                    fprintf(stderr, "Realloc failed.\n");
                    exit(1);
                }
                graph->to = temporary_to;
                int *temporary_cost = realloc(graph->cost, (graph->capacity + 300) * sizeof(int));
                if(temporary_cost == NULL){
                    fprintf(stderr, "Realloc failed.\n");
                    exit(1);
                }
                graph->cost = temporary_cost;
                graph->capacity += 300;
            }
            else{
                //average number of edges from each node is 3
                int *temporary_from = realloc(graph->from, highest_node * 3 * sizeof(int));
                if(temporary_from == NULL){
                    fprintf(stderr, "Realloc failed.\n");
                    exit(1);
                }
                graph->from = temporary_from;
                int *temporary_to = realloc(graph->to, highest_node * 3 * sizeof(int));
                if(temporary_to == NULL){
                    fprintf(stderr, "Realloc failed.\n");
                    exit(1);
                }
                graph->to = temporary_to;
                int *temporary_cost = realloc(graph->cost, highest_node * 3 * sizeof(int));
                if(temporary_cost == NULL){
                    fprintf(stderr, "Realloc failed.\n");
                    exit(1);
                }
                graph->cost = temporary_cost;
                graph->capacity = highest_node * 3;
                last_highest_node = highest_node;
            }
        }
        if(from > highest_node){
            highest_node = from;
        }
        if(to > highest_node){
            highest_node = to;
        }
        graph->from[graph->num_of_edges] = from;
        graph->to[graph->num_of_edges] = to;
        graph->cost[graph->num_of_edges] = cost;
        graph->num_of_edges++;
    }
    fclose(graph_file);
}

// Load a graph from the binary file.
void load_bin(const char *fname, graph_t *graph) {
    FILE *graph_file;
    graph_file = fopen(fname, "rb");
    int highest_node = 0;
    int last_highest_node = 0;
    int from;
    int to;
    int cost;
    while(fread(&from, sizeof(int), 1, graph_file) + fread(&to, sizeof(int), 1, graph_file) +
    fread(&cost, sizeof(int), 1, graph_file)){
        if(graph->num_of_edges + 1 == graph->capacity){
            if(highest_node == last_highest_node || graph->capacity >= highest_node * 3){
                int *temporary_from = realloc(graph->from, (graph->capacity + 300) * sizeof(unsigned int));
                if(temporary_from == NULL){
                    fprintf(stderr, "Realloc failed.\n");
                    exit(1);
                }
                graph->from = temporary_from;
                int *temporary_to = realloc(graph->to, (graph->capacity + 300) * sizeof(unsigned int));
                if(temporary_to == NULL){
                    fprintf(stderr, "Realloc failed.\n");
                    exit(1);
                }
                graph->to = temporary_to;
                int *temporary_cost = realloc(graph->cost, (graph->capacity + 300) * sizeof(unsigned int));
                if(temporary_cost == NULL){
                    fprintf(stderr, "Realloc failed.\n");
                    exit(1);
                }
                graph->cost = temporary_cost;
                graph->capacity += 300;
            }
            else{
                //average number of edges from each node is 3
                int *temporary_from = realloc(graph->from, highest_node * 3 * sizeof(unsigned int));
                if(temporary_from == NULL){
                    fprintf(stderr, "Realloc failed.\n");
                    exit(1);
                }
                graph->from = temporary_from;
                int *temporary_to = realloc(graph->to, highest_node * 3 * sizeof(unsigned int));
                if(temporary_to == NULL){
                    fprintf(stderr, "Realloc failed.\n");
                    exit(1);
                }
                graph->to = temporary_to;
                int *temporary_cost = realloc(graph->cost, highest_node * 3 * sizeof(unsigned int));
                if(temporary_cost == NULL){
                    fprintf(stderr, "Realloc failed.\n");
                    exit(1);
                }
                graph->cost = temporary_cost;
                graph->capacity = highest_node * 3;
                last_highest_node = highest_node;
            }
        }
        if(from > highest_node){
            highest_node = from;
        }
        if(to > highest_node){
            highest_node = to;
        }
        graph->from[graph->num_of_edges] = from;
        graph->to[graph->num_of_edges] = to;
        graph->cost[graph->num_of_edges] = cost;
        graph->num_of_edges++;
    }
    fclose(graph_file);
}

// Save the graph to the text file.
void save_txt(const graph_t * const graph, const char *fname){
    FILE* target_file;
    target_file = fopen(fname, "w");
    for(unsigned i = 0; i < graph->num_of_edges; i++){
        fprintf(target_file, "%d %d %d\n", graph->from[i], graph->to[i], graph->cost[i]);
    }
    fclose(target_file);
}

// Save the graph to the binary file.
void save_bin(const graph_t * const graph, const char *fname){
    FILE* target_file;
    target_file = fopen(fname, "wb");
    for(unsigned i = 0; i < graph->num_of_edges; i++){
        fwrite(&graph->from[i], sizeof(graph->from[i]), 1, target_file);
        fwrite(&graph->to[i], sizeof(graph->to[i]), 1, target_file);
        fwrite(&graph->cost[i], sizeof(graph->cost[i]), 1, target_file);
    }
    fclose(target_file);
}
