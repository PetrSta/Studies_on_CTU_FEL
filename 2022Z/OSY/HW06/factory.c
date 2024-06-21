#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>
#include <stdbool.h>
//to check comments for code go to osyPages
//NUZKY:2
//VRTACKA:6
//OHYBACKA:1
//SVARECKA:1
//LAKOVNA:3
//SROUBOVAK:2
//FREZA:1

//structs and enums

typedef struct shelf {
    bool valid;
    int productName;
    int whichPhase;
}shelf_t;

typedef struct worker {
    pthread_t id;
    char *name;
    int fieldOfWork;
    bool workPossible;
    struct worker *nextWorker;
}worker_t;

enum place {
    NUZKY, VRTACKA, OHYBACKA, SVARECKA, LAKOVNA, SROUBOVAK, FREZA,
    PLACE_COUNT
};

const char *place_str[PLACE_COUNT] = {
        [NUZKY] = "nuzky",
        [VRTACKA] = "vrtacka",
        [OHYBACKA] = "ohybacka",
        [SVARECKA] = "svarecka",
        [LAKOVNA] = "lakovna",
        [SROUBOVAK] = "sroubovak",
        [FREZA] = "freza",
};

enum product {
    A, B, C,
    PRODUCT_COUNT
};

const char *product_str[PRODUCT_COUNT] = {
        [A] = "A",
        [B] = "B",
        [C] = "C",
};

#define PHASE_COUNT 6
const int warehouseStructure[PRODUCT_COUNT][PHASE_COUNT] = {
        [A] = {NUZKY, VRTACKA, OHYBACKA, SVARECKA, VRTACKA, LAKOVNA},
        [B] = {VRTACKA, NUZKY, FREZA, VRTACKA, LAKOVNA, SROUBOVAK},
        [C] = {FREZA, VRTACKA, SROUBOVAK, VRTACKA, FREZA, LAKOVNA}
};

//global variables etc.

//ints for amount of workers
int workersActive = 0;
int workersWaiting = 0;
//2d array to store products
volatile int warehouse[PRODUCT_COUNT][PHASE_COUNT];
//array to store ready workplaces
int workplaces[PLACE_COUNT];
//first and last worker added
worker_t *lastWorker = NULL;
worker_t *firstWorker = NULL;
//mutexes and conditions
pthread_mutex_t workerMutex;
pthread_mutex_t endMutex;
pthread_cond_t workerCond;
pthread_cond_t workerEndCond;

//functions

//function from template that finds string in array and returns index of string in array
int find_string_in_array(const char **array, int length, char *what)
{
    for (int i = 0; i < length; i++)
        if (strcmp(array[i], what) == 0)
            return i;
    return -1;
}

//simple function to find worker of selected name
worker_t* findWorker(char *name) {
    worker_t *tmpWorker = firstWorker;
    //check if workers name is the one we are looking for otherwise check next worker
    while(tmpWorker != NULL) {
        if(strcmp(tmpWorker->name, name) == 0) {
            return tmpWorker;
        } else {
            tmpWorker = tmpWorker->nextWorker;
        }
    }
    return NULL;
}

//function designed to let worker know how long he should sleep
int checkProductionTime(int  fieldOfWork) {
    //simple if-else mess to check which value we want to return
    if (fieldOfWork == 0) {
        return 100 * 1000;
    } else if (fieldOfWork == 1) {
        return 200 * 1000;
    } else if (fieldOfWork == 2) {
        return 150 * 1000;
    } else if (fieldOfWork == 3) {
        return 300 * 1000;
    } else if (fieldOfWork == 4) {
        return 400 * 1000;
    } else if (fieldOfWork == 5) {
        return 250 * 1000;
    } else if (fieldOfWork == 6) {
        return 500 * 1000;
    }
    //something went wrong
    return -1;
}

//function designed to check if any worker is available in parsed fieldOfWork
bool checkFieldOfWork(int fieldOfWork) {
    worker_t *tmpWorker = firstWorker;
    //check every worker in list if it has same specialization as we want
    while(tmpWorker != NULL) {
        if(tmpWorker->workPossible && tmpWorker->fieldOfWork == fieldOfWork) {
            return true;
        } else {
            tmpWorker = tmpWorker->nextWorker;
        }
    }
    return false;
}

//condition to check if any new work is able to occur
bool workIsPossible() {
    //go through every possible field of work and check if worker for it exists and if there are free workplaces for it
    for(int productName = 0; productName < PRODUCT_COUNT; productName++) {
        for(int whichPhase = 0; whichPhase < PHASE_COUNT; whichPhase++) {
            int fieldOfWork = warehouseStructure[productName][whichPhase];
            if(warehouse[productName][whichPhase] > 0 && checkFieldOfWork(fieldOfWork) && workplaces[fieldOfWork] > 0) {
                return true;
            }
        }
    }
    return false;
}

//function to find where worker is able to work
void selectWork(int fieldOfWork, shelf_t *selectedWork) {
    for (int whichPhase = PHASE_COUNT - 1; whichPhase >= 0; whichPhase--) {
        for (int productName = 0; productName < PRODUCT_COUNT; productName++) {
            //if we reach spot where workers specialization is wanted and is requested to be worked on assign it to our
            //variable(selectedWork) -> used in workerThread
            if(warehouseStructure[productName][whichPhase] == fieldOfWork) {
                if(warehouse[productName][whichPhase] > 0) {
                    selectedWork->whichPhase = whichPhase;
                    selectedWork->productName = productName;
                    selectedWork->valid = true;
                    return;
                }
            }
        }
    }
}

//function that lets workers know that stdin ended -> same if every worker was asked to leave
void letWorkersKnowStdinEnded() {
    worker_t *tmpWorker = firstWorker;
    //wait for all workers to wait for change and for no possibility of work
    while (workersWaiting != workersActive || workIsPossible()) {
        pthread_cond_wait(&workerEndCond, &endMutex);
    }
    //assign boolean value until we reach end of worker list
    while (tmpWorker != NULL) {
        fprintf(stderr, "Letting %s know stdin ended.\n", tmpWorker->name);
        tmpWorker->workPossible = false;
        tmpWorker = tmpWorker->nextWorker;
    }
    pthread_cond_broadcast(&workerCond);
}

//function that simply joins all worker threads
void joinThreads() {
    worker_t *tmpWorker = firstWorker;
    while (tmpWorker != NULL) {
//        for(int i = 0; i < workersActive; i++) {
        fprintf(stderr, "Joining %s.\n", tmpWorker->name);
        pthread_join(tmpWorker->id, NULL);
        tmpWorker = tmpWorker->nextWorker;
//        }
    }
}

//function designed to go through worker list and free all workers
void freeWorkerList() {
    if(firstWorker == NULL) {
        fprintf(stderr,"Cannot free worker list.");
    }
    while (firstWorker->nextWorker != NULL) {
        worker_t *tmpWorker = firstWorker;
        firstWorker = firstWorker->nextWorker;
        free(tmpWorker->name);
        free(tmpWorker);
    }
    if(firstWorker->nextWorker == NULL) {
        free(firstWorker->name);
        free(firstWorker);
    }
}

//function designed for worker to do work -> work is equal to sleep for predetermined amount of time
void* workerThread(void *arg) {
    worker_t *worker = (worker_t *)arg;
    pthread_mutex_lock(&workerMutex);
    workersActive++;
    shelf_t *selectedWork = malloc(sizeof(shelf_t));
    while(worker->workPossible) {
        selectedWork->valid = false;
        //condition designed for worker to wait
        while(true) {
            //if worker was requested to leave -> end
            if (!worker->workPossible) {
                workersActive--;
                free(selectedWork);
                pthread_mutex_unlock(&workerMutex);
                return NULL;
            }
            //check if any workPossibilities are currently open
            //check if workplaces are open
            selectWork(worker->fieldOfWork, selectedWork);
            if(selectedWork->valid && workplaces[worker->fieldOfWork > 0]) {
                break;
            }
            workersWaiting++;
            pthread_cond_broadcast(&workerEndCond);
            pthread_cond_wait(&workerCond, &workerMutex);
            workersWaiting--;
        }
        //if reached here worker can start work -> sleep for predetermined time
        workplaces[worker->fieldOfWork]--;
        warehouse[selectedWork->productName][selectedWork->whichPhase]--;
        pthread_mutex_unlock(&workerMutex);

        //print information about work
        printf("%s %s %d %s\n", worker->name, place_str[worker->fieldOfWork],
               selectedWork->whichPhase + 1, product_str[selectedWork->productName]);
        //"do" work -> only sleep for predetermined time in milliseconds
        int workTime = checkProductionTime(worker->fieldOfWork);
        usleep(workTime);

        pthread_mutex_lock(&workerMutex);
        workplaces[worker->fieldOfWork] += 1;
        //if our product was fully completed let user know
        if(selectedWork->whichPhase + 1 == PHASE_COUNT) {
            printf("done %s\n", product_str[selectedWork->productName]);
        }
            //otherwise increment our next in-between product
        else {
            warehouse[selectedWork->productName][selectedWork->whichPhase + 1]++;
            pthread_cond_broadcast(&workerCond);
        }
    }
    free(selectedWork);
    workersActive--;
    pthread_mutex_unlock(&workerMutex);
    return NULL;
}

//main function though which user input is handled
int main() {
    //initialize everything
    pthread_cond_init(&workerCond, NULL);
    pthread_cond_init(&workerEndCond, NULL);
    pthread_mutex_init(&workerMutex, NULL);
    pthread_mutex_init(&endMutex, NULL);

    while (1) {
        //initializing variables for further use and checking loaded line of text -> template
        char *line, *cmd, *arg1, *arg2, *arg3, *saveptr;
        int s = scanf(" %m[^\n]", &line);
        if (s == EOF) {
            break;
        }
        if (s == 0) {
            continue;
        }
        //assigning variables from loaded line of text -> template
        cmd = strtok_r(line, " ", &saveptr);
        arg1 = strtok_r(NULL, " ", &saveptr);
        arg2 = strtok_r(NULL, " ", &saveptr);
        //probably redundant -> kept it because it is in template
        arg3 = strtok_r(NULL, " ", &saveptr);

        if (strcmp(cmd, "start") == 0 && arg1 && arg2 && !arg3) {
            //find field of work
            int fieldOfWork = find_string_in_array(place_str, PLACE_COUNT, arg2);
            //check if function found string
            if (fieldOfWork == -1) {
                fprintf(stderr,"find_string_in_array failed - START \n.");
            }
            //check for correct input after setup new worker data and broadcast possible change for waiting workers
            if (fieldOfWork >= 0) {
                worker_t *tmpWorker = malloc(sizeof(worker_t));
                tmpWorker->nextWorker = NULL;
                tmpWorker->name = strdup(arg1);
                tmpWorker->fieldOfWork = fieldOfWork;
                tmpWorker->workPossible = true;
                //if this is first worker created we need to set up our "head" and "tail", in this code first worker and
                //last worker
                if (firstWorker == NULL) {
                    firstWorker = tmpWorker;
                }
                //link workers in list
                if (lastWorker != NULL) {
                    lastWorker->nextWorker = tmpWorker;
                }
                lastWorker = tmpWorker;
                //start worker thread
                pthread_create(&(tmpWorker->id), NULL, workerThread, (void *)tmpWorker);
                pthread_cond_broadcast(&workerCond);
            }
        } else if (strcmp(cmd, "make") == 0 && arg1 && !arg2) {
            //find product name
            int productName = find_string_in_array(product_str, PRODUCT_COUNT, arg1);
            //check if function found string
            if (productName == -1) {
                fprintf(stderr,"find_string_in_array failed - MAKE.  \n");
            }
            //check correct input, after increment requests for product and broadcast possible change for
            // waiting workers otherwise if something is wrong let us know
            if (productName >= 0) {
                warehouse[productName][0]++;
            } else {
                fprintf(stderr,"Wrong make request. \n");
            }
            pthread_cond_broadcast(&workerCond);
        } else if (strcmp(cmd, "end") == 0 && arg1 && !arg2) {
            //let worker know he was requested to leave factory
            //if worker was not found let us know
            worker_t *tmpWorker = findWorker(arg1);
            if (tmpWorker != NULL) {
                tmpWorker->workPossible = false;
            } else {
                fprintf(stderr,"Unable to find worker. \n");
            }
            pthread_cond_broadcast(&workerCond);
        } else if (strcmp(cmd, "add") == 0 && arg1 && !arg2) {
            //find type of work
            int workplaceIndex = find_string_in_array(place_str, PLACE_COUNT, arg1);
            //check if function found string
            if (workplaceIndex == -1) {
                fprintf(stderr,"find_string_in_array failed - ADD. \n");
            }
            //check if workplace can be added -> otherwise let us know something might be wrong
            if (workplaceIndex >= 0) {
                workplaces[workplaceIndex]++;
            } else {
                fprintf(stderr,"Workplace was negative when increment was requested. \n");
            }
            pthread_cond_broadcast(&workerCond);
        } else if (strcmp(cmd, "remove") == 0 && arg1 && !arg2) {
            //find type of work
            int workplaceIndex = find_string_in_array(place_str, PLACE_COUNT, arg1);
            //check if function found string
            if (workplaceIndex == -1) {
                fprintf(stderr,"find_string_in_array failed - REMOVE. \n");
            }
            //check if workplace can be removed -> otherwise let us know something might be wrong
            if (workplaces[workplaceIndex] > 0) {
                workplaces[workplaceIndex]--;
            } else {
                workplaces[workplaceIndex]--;
                fprintf(stderr,"Decrementing workplace to negative value. \n");
            }
        } else {
            fprintf(stderr,"Invalid command: %s\n", line);
        }
        free(line);
    }

    //no comments here since every function is commented see in earlier lines of code (exception for destroys)
    pthread_mutex_lock(&endMutex);
    letWorkersKnowStdinEnded();

    joinThreads();
    pthread_mutex_unlock(&endMutex);

    //destroy everything
    pthread_cond_destroy(&workerCond);
    pthread_cond_destroy(&workerEndCond);
    pthread_mutex_destroy(&workerMutex);
    pthread_mutex_destroy(&endMutex);

    if(firstWorker != NULL) {
        freeWorkerList();
    } else {
        fprintf(stderr,"No worker memory to be freed. \n");
    }
    return 0;
}