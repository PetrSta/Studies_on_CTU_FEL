#include <stdio.h>
#include <string.h>
#include <malloc.h>

//function that places user input into array
int load_array(char **char_array){
    //index == 1 because of strncat preparation - line 13
    int index = 1;
    int capacity = 100;
    char temporary;
    *char_array = malloc(capacity * sizeof(char));
    //preparing array for use of strncat function
    *char_array[0] = '\0';
    //filling the array + reallocating memory
    while((scanf("%c", &temporary) == 1)){
        if(temporary != '\n'){
            index += 1;
            strncat(*char_array, &temporary, 1);
        }
        else if(temporary == '\n'){
            break;
        }
        if(index >= capacity){
            *char_array = realloc(*char_array, (capacity + 50) * sizeof(char));
            capacity = capacity + 50;
        }
        //if entry has invalid char in it break and change it for later control
        int entry_control = 0;
        if(temporary >= 'a' && temporary <= 'z'){
            entry_control = 1;
        }
        if(temporary >= 'A' && temporary <= 'Z'){
            entry_control = 1;
        }
        if(entry_control == 0){
            return 0;
        }
    }
    return 1;
}

//function used to move in ASCII accordingly
int move_in_ASCII(char *entry, int key, int index){
    //depending if letter is capital or not and if it "crosses" symbols instead of letters
    // calculate ASCII number of coresponding letter
    int result;
    if(entry[index] <= 122 && entry[index] >= 97 && (entry[index] + key) > 122){
        if((64 + (entry[index] + key - 122)) > 90){
            result = (64 + (entry[index] + key - 116));
        }
        else{
            result = (64 + (entry[index] + key - 122));
        }
    }
    else if(entry[index] >= 64 && entry[index] <= 90 && (entry[index] + key) > 90){
        if(((entry[index]) + key + 6) > 122){
            result = (64 + ((entry[index]) + key - 116));
        }
        else{
            result = ((entry[index]) + key + 6);
        }
    }
    else {
        result = (entry[index] + key);
    }
    return result;
}

//function that deciphers the text
int decipher(char *encrypted, char *listened){
    int key = 0;
    int biggest_identity = 0;
    //find the required index for arrays
    int index_max = 0;
    while(encrypted[index_max] != '\0'){
        index_max += 1;
    }
    for(int potential_key = 0; potential_key <= 51; potential_key += 1){
        int identity = 0;
        int index_listened = 0;
        for(int n = 0; n < index_max; n += 1){
            int current_char = move_in_ASCII(encrypted, potential_key, n);
            if(listened[index_listened] == current_char){
                identity += 1;
            }
            index_listened += 1;
        }
        //check which potential_key has the biggest identity
        if(identity > biggest_identity){
            biggest_identity = identity;
            key = potential_key;
        }
    }
    //create deciphered message + print it
    for(int m = 0; m < index_max; m += 1){
        int result_char = move_in_ASCII(encrypted, key, m);
        fprintf(stdout,"%c", result_char);
    }
    fprintf(stdout,"\n");
    return 0;
}

int main() {
    char *encrypted;
    char *listened;
    //check for invalid characters
    if (!load_array(&encrypted)) {
        fprintf(stderr, "Error: Chybny vstup!\n");
        free(encrypted);
        return 100;
    }
    //check for invalid characters
    if (!load_array(&listened)) {
        fprintf(stderr, "Error: Chybny vstup!\n");
        free(encrypted);
        free(listened);
        return 100;
    }
    //if user input has invalid length, end program
    if(strlen(encrypted) != strlen(listened)){
        fprintf(stderr, "Error: Chybna delka vstupu!\n");
        free(encrypted);
        free(listened);
        return 101;
    }
    //call decipher
    decipher(encrypted, listened);
    free(encrypted);
    free(listened);
    return 0;
}
