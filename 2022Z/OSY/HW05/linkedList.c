#include <stdlib.h>
#include <stdio.h>

typedef struct {
    int amountOfWords;
    char* word;
}data_t;

typedef struct node{
    data_t data;
    struct node *next;
} node_t;

node_t* head;
node_t* start;
node_t* tail;

void push(int amountOfWords, char* word) {
    node_t *node = (node_t*)malloc(sizeof(node_t));
    node->data.amountOfWords = amountOfWords;
    node->data.word = word;
    node->next = NULL;

    //if head already exists link it with this new node
    if(head != NULL) {
        head->next = node;
    }

    //set new head
    head = node;

    //if tail does not exist set it up
    if(tail == NULL) {
        tail = node;
    }

    //if first does not exist set it up, should be only once per program start
    if(start == NULL) {
        start = node;
    }
}

node_t *pop() {

    node_t* poppedElement;

    //check for wrong usage of pop
    if(tail != NULL) {
        poppedElement = tail;
    } else {
        fprintf(stderr, "ERROR tail does not exist -> cannot use pop.\n");
        exit(2);
    }

    //change tail after pop, if head == tail set both to NULL -> empty list -> forcing push to create new
    if(tail->next == NULL) {
        tail = NULL;
        head = NULL;
    } else {
        tail = tail->next;
    }

    return poppedElement;
}

void freeList() {

    if(start == NULL) {
        exit(0);
    }

    while(start->next != NULL) {
        node_t* tmp = start;
        start = start->next;
        free(tmp);
    }

    if(start->next == NULL) {
        free(start);
    }
}
