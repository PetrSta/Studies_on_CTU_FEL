#include "sort.h"
#include <iostream>

void radix_par(std::vector<std::string *> &vector_to_sort, const MappingFunction &mappingFunction,
               unsigned long alphabet_size, unsigned long string_lengths, int recursiveIndex) {

    // vector of vectors used to store strings during sorting
    std::vector<std::vector<std::string*>> vectorOfBuckets (alphabet_size, std::vector<std::string*>(0));

    // add strings to buckets for beginning and later for recursion
    for(unsigned long i = 0; i < vector_to_sort.size(); i++) {
        std::string* currentString = vector_to_sort.at(i);
        unsigned long index = mappingFunction(currentString->at(recursiveIndex));
        vectorOfBuckets[index].push_back(currentString);
    }

    // if we still can sort go deeper into recursion -> we can sort if we didnt already reach end of strings
    if((unsigned long)recursiveIndex < string_lengths - 1) {
        #pragma omp parallel for schedule (dynamic)
        for(unsigned long i = 0; i < vectorOfBuckets.size(); i++) {
            std::vector<std::string*> &bucket = vectorOfBuckets[i];
            // go into recursion only if our bucket is not empty, otherwise there is no point going into recursion
            if(!bucket.empty()) {
                radix_par(bucket, mappingFunction, alphabet_size,
                          string_lengths, recursiveIndex + 1);
            }
        }
    }

    // vector used to tell us where we will need to place strings
    std::vector<unsigned long> bucketStarts (alphabet_size, 0);

    // fill bucketStarts vector
    for(unsigned long i = 0; i < alphabet_size; i++) {
        // first bucket starts at 0 -> we can skip that iteration
        if(i == 0) {
            continue;
        }
        // otherwise bucket starts at the index calculate by:
        // taking previous bucket start and adding previous bucket size
        bucketStarts[i] = bucketStarts[i - 1] + vectorOfBuckets[i - 1].size();
    }

    #pragma omp parallel for
    for(unsigned long i = 0; i < vectorOfBuckets.size(); i++) {
        for(unsigned long j = 0; j < vectorOfBuckets[i].size(); j++) {
            // use previously constructed vector to fill vector_to_sort with correctly sorted data
            vector_to_sort[bucketStarts[i] + j] = vectorOfBuckets[i][j];
        }
    }
}