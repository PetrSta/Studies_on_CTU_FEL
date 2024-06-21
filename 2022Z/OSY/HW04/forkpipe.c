#include <sys/types.h>
#include <sys/wait.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <stdbool.h>
#include <signal.h>

bool sigterm = false;

void sigterm_handler() {
    sigterm = true;
}

int main(int argc, char *argv[]) {

    //fork has not happened yet - we get main process id
    int parentId = getpid();

    //place to store fileDescriptors
    int fileDescriptor[2];

    //SIGTERM handling
    pid_t genChild, nsdChild;
    if(pipe(fileDescriptor) == -1) {
        fprintf(stderr, "Pipe creation error.\n");
        return 2;
    }

    //GEN
    //check if child process was created successfully
    //fork() returns 0 in child process
    if ((genChild = fork()) == 0) {
        signal(SIGTERM, sigterm_handler);

        if(close(fileDescriptor[0]) == -1) {
            fprintf(stderr, "fileDescriptor closing ERROR.\n");
            return 2;
        }
        //dup first fileDescriptor
        if(dup2(fileDescriptor[1], STDOUT_FILENO) == -1) {
            fprintf(stderr, "dup2 genChild ERROR.\n");
            return 2;
        }
        if(close(fileDescriptor[1]) == -1) {
            fprintf(stderr, "fileDescriptor closing ERROR.\n");
            return 2;
        }

        //print random numbers to stdout
        while(!sigterm) {
            if (printf("%d %d\n", rand() % 4096, rand() % 4096) < 0) {
                fprintf(stderr, "printf ERROR.\n");
                return 2;
            }
            fflush(stdout);
            sleep(1);
        }
        fprintf(stderr, "GEN TERMINATED\n");
        return 0;
    } else if (genChild < 0) {
        //if child process creation failed
        fprintf(stderr, "fork ERROR.\n");
        return 2;
    }

    //NSD
    //check if child process was created
    //fork() returns 0 in child process
    if((nsdChild = fork()) == 0) {

        if(close(fileDescriptor[1]) == -1) {
            fprintf(stderr, "fileDescriptor closing ERROR.\n");
            return 2;
        }
        //dup first fileDescriptor
        if(dup2(fileDescriptor[0], STDIN_FILENO) == -1) {
            fprintf(stderr, "dup2 nsdChild ERROR.\n");
            return 2;
        }
        if(close(fileDescriptor[0]) == -1) {
            fprintf(stderr, "fileDescriptor closing ERROR.\n");
            return 2;
        }

        //call nsd binary file
        if(execl("./nsd", "nsd", NULL) == -1) {
            fprintf(stderr, "execl ERROR.\n");
            return 2;
        }
    } else if (nsdChild < 0) {
        //if child process creation failed
        fprintf(stderr, "fork ERROR.\n");
        return 2;
    }

    //pprocess (parent process)
    if (getpid() == parentId) {

        //close fileDescriptor to be sure
        if(close(fileDescriptor[0]) == -1) {
            fprintf(stderr, "fileDescriptor closing ERROR.\n");
            exit(2);
        }
        if(close(fileDescriptor[1]) == -1) {
            fprintf(stderr, "fileDescriptor closing ERROR.\n");
            exit(2);
        }

        //wait 5 seconds and then kill genChild
        sleep(5);
        if(kill(genChild, SIGTERM) == -1) {
            fprintf(stderr, "genChild kill ERROR.\n");
            exit(2);
        }

        bool wrongReturnStatus = false;

        //wait for child processes
        int genReturnStatus, nsdReturnStatus;
        if (waitpid(genChild, &genReturnStatus, 0) == -1) {
            fprintf(stderr, "waitpid ERROR.\n");
            exit(2);
        }
        if (waitpid(nsdChild, &nsdReturnStatus, 0) == -1) {
            fprintf(stderr, "waitpid ERROR.\n");
            exit(2);
        }

        if(!WIFEXITED(genReturnStatus)) {
            wrongReturnStatus = true;
        }
        if(!WIFEXITED(nsdReturnStatus)) {
            wrongReturnStatus = true;
        }

        //check if children did not end with ERROR
        if(genReturnStatus != 0 || nsdReturnStatus != 0 || wrongReturnStatus) {
            fprintf(stdout, "ERROR\n");
            exit(1);
        } else {
            fprintf(stdout, "OK\n");
            exit(0);
        }
    }
}