#include "mem_alloc.h"
#include <stdio.h>
#include <stddef.h>
#include <stdbool.h>

/*
 * Template for 11malloc. If you want to implement it in C++, rename
 * this file to mem_alloc.cc.
 */

static inline void *nbrk(void *address);

#ifdef NOVA

/**********************************/
/* nbrk() implementation for NOVA */
/**********************************/

static inline unsigned syscall2(unsigned w0, unsigned w1)
{
    asm volatile("   mov %%esp, %%ecx    ;"
                 "   mov $1f, %%edx      ;"
                 "   sysenter            ;"
                 "1:                     ;"
                 : "+a"(w0)
                 : "S"(w1)
                 : "ecx", "edx", "memory");
    return w0;
}

static void *nbrk(void *address)
{
    return (void *)syscall2(3, (unsigned)address);
}
#else

/***********************************/
/* nbrk() implementation for Linux */
/***********************************/

static void *nbrk(void *address)
{
    void *current_brk = sbrk(0);
    if (address != NULL) {
        int ret = brk(address);
        if (ret == -1)
            return NULL;
    }
    return current_brk;
}

#endif

struct header {
    unsigned long size;
    bool isAllocated;
};

struct header* head = NULL;

//because we are returning adress after header we need a way to find start of header
struct header* getStartOfHeader(void* ptr) {
    return (struct header*)((char*)ptr - sizeof(struct header));
}

//if our previously taken memory does not have enough continuously free blocks, we need to ask for new one
struct header* createHeader(unsigned long size) {
    //test if we have space for memory
    void* currentBrk = nbrk(0);
    void* request = nbrk((char*)currentBrk + size + sizeof(struct header));
    if(request == (void*)0) {
        //printf("Not enough space for the request");
        return NULL;
    }
    //printf("currentlySetBreak is: %p\n", nbrk(0));

    //if we have enough space create tmp header and return it
    struct header* tmpHeader = (struct header*)currentBrk;
    //printf("Created header is%p\n", tmpHeader);
    tmpHeader->size = size;
    tmpHeader->isAllocated = true;
    return tmpHeader;
}

//return 0 if malloc fails otherwise return start of allocated memory
void *my_malloc(unsigned long size)
{ 
    //printf("requested size is: %ld\n", size);
    //printf("currentBreak is: %p\n", nbrk(0));
    //printf("Size of header is: %d\n", sizeof(struct header));
    struct header* blockHeader;
    bool spaceFound = false;
    //check if we can start or if malloc cant even be attempted
    if(!size) {
        return NULL;
    }
    //check status of head an act accordingly
    if(head == NULL) {
        blockHeader = createHeader(size);
        //printf("My_malloc head == NULL case blockHeader: %p\n", blockHeader);
        if(blockHeader == NULL) {
            return NULL;
        } else {
            head = blockHeader;
        }
    } else {
        //head is always first malloc
        blockHeader = head;
        struct header* currentBrk = (struct header*)nbrk(0);
        while(blockHeader < currentBrk) {
            //check if we can fit requested size in previously freed blocks -> traversing headers
            if(!blockHeader->isAllocated) {
                struct header* currentHeader = blockHeader;
                //printf("Current header is: %p\n", currentHeader);
                unsigned long availableCapacity = 0;
                unsigned long requestedAmount = size + sizeof(struct header);
                unsigned long currentBlockSize;
                //find maximum available space
                while(availableCapacity < size + sizeof(struct header) && !currentHeader->isAllocated && currentHeader < currentBrk) {
                    currentBlockSize = currentHeader->size + sizeof(struct header);
                    availableCapacity += currentBlockSize;
                    currentHeader = (struct header*)((char*)currentHeader + currentBlockSize);
                }
                //setup header if we matched perfectly
                if(availableCapacity == requestedAmount) {
                    spaceFound = true;
                    blockHeader->size = size;
                    blockHeader->isAllocated = true;
                }
                //if we have found bigger space create two headers to split the space into two blocks
                else if(availableCapacity > requestedAmount) {
                    spaceFound = true;
                    blockHeader->size = size;
                    blockHeader->isAllocated = true;
                    struct header* overflowHeader = (struct header*)((char*)blockHeader + sizeof(struct header) + blockHeader->size);
                    //printf("OverflowHeader is: %p\n", overflowHeader);
                    overflowHeader->size = availableCapacity - blockHeader->size - 2*sizeof(struct header);
                    overflowHeader->isAllocated = false;
                } 
                //if we have found block of smaller size than we need -> set our header to first header after found block and search again
                else {
                    blockHeader = currentHeader;
                }
            }
            //if we already found big enough spot we dont want to search again
            if(spaceFound) {
                break;
            }
            //find next header
            blockHeader = (struct header*)((char*)blockHeader + sizeof(struct header) + blockHeader->size);
        }
        if(!spaceFound) {
            blockHeader = createHeader(size);
            if(!blockHeader) {
                return NULL;
            }
        }
    }
    struct header* returnValue = (struct header*)((char*)blockHeader + sizeof(struct header));
    //printf("Return value is: %p\n", returnValue);
    return returnValue;
}

//return negative value if free fails otherwise return 0
int my_free(void *address)
{
    if(!address) {
        return -1;
    }
    void* currentBreak = nbrk(0);
    //if address is ot valid ->fail
    //printf("Currentbrk in free is:%p\n", currentBreak);
    //printf("Address in free is:%p\n", address);
    //if address is bigger than current break -> fail
    if((char*)address > (char*)currentBreak) {
        //printf("Address was bigger than current break.\n");
        return -1;
    }
    //free block by changing header
    struct header* tmpHeader = getStartOfHeader(address);
    if((char*)head > (char*)tmpHeader) {
        //printf("Address is too low to be a header.\n");
        return -1;
    }
    //printf("Header in free is:%p\n", tmpHeader);
    if(!tmpHeader->isAllocated) {
        //printf("Requested header was already deallocated.\n");
        return -1;
    }
    tmpHeader->isAllocated = false;
    return 0;
}
