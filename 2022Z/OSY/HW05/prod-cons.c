#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>
#include <semaphore.h>
#include "linkedList.c"

//TODO use global variable for index

int exit_value = 0;
int numberOfThreads;
pthread_mutex_t listMutex;
pthread_mutex_t writeMutex;
sem_t semList;

void* producerThread() {
    int amountOfWords;
    int returnValue;
    char* word;

    while ((returnValue = scanf("%d %ms", &amountOfWords, &word)) == 2) {

        if(amountOfWords < 0) {
            fprintf(stderr, "ERROR invalid input.\n");
            exit_value = 1;
            free(word);
            break;
        }

        pthread_mutex_lock(&listMutex);
        push(amountOfWords, word);
        sem_post(&semList);
        pthread_mutex_unlock(&listMutex);
    }

    //make sure consumer thread is awake for +1 run to see that list is empty -> end condition
    for(int i = 0; i < numberOfThreads; i++) {
        sem_post(&semList);
    }
    if(returnValue != 2 && returnValue != EOF) {
        fprintf(stderr, "ERROR invalid input.\n");
        exit_value = 1;
    }

    pthread_exit(0);
}

void* consumerThread(void* arg) {
    int index = *(int*) arg;
    int amountOfWords;
    char* word;

    //TODO find end condition -> currently only tmp condition
    while(1) {

        sem_wait(&semList);
        pthread_mutex_lock(&listMutex);

        if(tail == NULL) {
            pthread_mutex_unlock(&listMutex);
            break;
        }

        node_t* poppedElement = pop();
        amountOfWords = poppedElement->data.amountOfWords;
        word = poppedElement->data.word;
        pthread_mutex_unlock(&listMutex);

        pthread_mutex_lock(&writeMutex);
        //print out index of thread
        fprintf(stdout, "Thread %d:", index);

        //print out requested amount of words
        while(amountOfWords != 0) {
            fprintf(stdout, " %s", word);
            amountOfWords -= 1;
        }
        fprintf(stdout, "\n");

        free(poppedElement->data.word);
        free(poppedElement);
        pthread_mutex_unlock(&writeMutex);

    }
    pthread_exit(0);
}

int main(int argc, char * argv[]) {

    //get number of threads requested and check for wrong request
    int maxThreads = sysconf(_SC_NPROCESSORS_ONLN);

    if (argc == 1) {
        numberOfThreads = 1;
    } else if (atoi(argv[1]) >= 1 && atoi(argv[1]) <= (maxThreads)) {
        numberOfThreads = atoi(argv[1]);
    } else {
        fprintf(stderr, "ERROR wrong amount of threads requested (not enough threads available on PC).\n");
        exit(1);
    }

    //initialize semaphores and mutexes
    int* threadIndexes = (int*) calloc(sizeof(int), numberOfThreads);
    sem_init(&semList, 0, 0);
    pthread_mutex_init(&writeMutex, NULL);
    pthread_mutex_init(&listMutex, NULL);

    //create threads
    pthread_t producer;
    pthread_t threads [numberOfThreads];

    if (pthread_create(&producer, NULL, &producerThread, NULL) != 0) {
        perror("Failed to create thread.\n");
        exit(1);
    }

    for (int i = 0; i < numberOfThreads; i++) {
        threadIndexes[i] = i + 1;
        if (pthread_create(&threads[i], NULL, &consumerThread, &threadIndexes[i]) != 0) {
            perror("Failed to create thread.\n");
            exit(1);
        }
    }

    //join threads
    if (pthread_join(producer, NULL) != 0) {
        fprintf(stderr, "Failed to join producer thread.\n");
        exit(2);
    }

    for (int i = 0; i < numberOfThreads; i++) {
        if (pthread_join(threads[i], NULL) != 0) {
            fprintf(stderr, "Failed to join consumer threads.\n");
            exit(2);
        }
    }

    //destroy everything
    sem_destroy(&semList);
    pthread_mutex_destroy(&listMutex);
    pthread_mutex_destroy(&writeMutex);
    free(threadIndexes);

    return exit_value;
}
