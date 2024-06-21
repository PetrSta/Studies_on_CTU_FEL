#include <stdio.h>
#include <stdlib.h>

typedef struct {
    int amountOfWords;
    char* word;
}data_t;

typedef struct node {
    data_t data;
    struct node *next;
} node_t;

//head == first element in list
//tail == last element in list
node_t* head;
node_t* tail;

node_t* pop() {
    //check if head exists -> pop cannot be called without existing head
    if(head == NULL) {
        fprintf(stderr, "ERROR pop called without existing tail.\n");
    }
    //save popped node to return it
    node_t *poppedElement = tail;
    //TODO check if this can be used to end threads as condition
    //if head was last element in list, we no longer have any list
    // -> destroy head and tail to force future pops to create new linked list
    if(head->next == NULL) {
        tail = NULL;
        head = NULL;
    } else {
        //otherwise move head to next element (FIFO)
        head = tail->next;
    }
    return poppedElement;
}

void push(int amountOfWords, char* word) {
    node_t* node = (node_t*)malloc(sizeof(node_t));
    node->data.amountOfWords = amountOfWords;
    node->data.word = word;
    //if head exists link it with new node
    if(tail != NULL) {
        tail->next = node;
    }
    //move head to be last added node
    tail = node;
    //if tail is not yet initialized it is the first node in list
    if(head == NULL) {
        tail = node;
    }
}
