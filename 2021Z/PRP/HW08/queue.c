#include <stdio.h>
#include <malloc.h>
#include <string.h>
#include <stdbool.h>
#include "queue.h"

// creates a new queue with a given size
queue_t *create_queue(int capacity) {
    queue_t *queue = malloc(sizeof(queue_t));
    if (queue == NULL) {
        return false;
    }
    queue->buffer = malloc(capacity * sizeof(void *));
    if (queue->buffer == NULL) {
        return false;
    }
    queue->capacity = capacity;
    queue->size = 0;
    queue->head = 0;
    queue->tail = 0;
    return queue;
}

// deletes the queue and all allocated memory
void delete_queue(queue_t *queue) {
    if (queue != NULL) {
        if (queue->buffer != NULL) {
            free(queue->buffer);
        }
        free(queue);
    }
}

// inserts a reference to the element into the queue
// returns: true on success; false otherwise
bool push_to_queue(queue_t *queue, void *data) {
    if (queue->size == queue->capacity) {
        return false;
    }
    queue->buffer[queue->head] = data;
    queue->head++;
    if (queue->head == queue->capacity){
        queue->head = 0;
    }
    queue->size++;
    return true;
}

// gets the first element from the queue and removes it from the queue
// returns: the first element on success; NULL otherwise
void *pop_from_queue(queue_t *queue) {
    if (queue->size == 0) {
        return NULL;
    }
    void* result = queue->buffer[queue->tail];
    queue->tail++;
    if (queue->tail == queue->capacity){
        queue->tail = 0;
    }
    queue->size--;
    return result;
}


// gets idx-th element from the queue, i.e., it returns the element that
// will be popped after idx calls of the pop_from_queue()
// returns: the idx-th element on success; NULL otherwise
void *get_from_queue(queue_t *queue, int idx) {
    if (queue == NULL) {
        return NULL;
    }
    if (idx >= queue->size || idx < 0) {
        return NULL;
    }
    if(queue->tail + idx > queue->capacity){
        idx = (queue->tail + idx) % queue->capacity;
    }
    return queue->buffer[idx];
}

// gets number of stored elements
int get_queue_size(queue_t *queue) {
    return queue->size;
}
