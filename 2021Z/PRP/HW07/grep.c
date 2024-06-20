#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include <regex.h>

typedef struct{
    char* line;
    int size;
    int capacity;
}line_string;

//different return values
enum{
    NOT_LINE_END = 0,
    LINE_END = 1
};

//initializing line_string
void construct_line(line_string* readLine){
    char *temporary = (char*) malloc(10 * sizeof(char));
    if(temporary == NULL){
        fprintf(stderr, "Out of memory.\n");
        exit(1);
    }
    readLine->line = temporary;
    readLine->size = 0;
    readLine->capacity = 10;
    readLine->line[0] = '\0';
}

//used to reallocate for more memory
void reallocate_line_mem(line_string* readLine){
    readLine->capacity *= 2;
    char *temporary = realloc(readLine->line, readLine->capacity * sizeof(char));
    if(temporary == NULL){
        fprintf(stderr, "Out of memory.\n");
        exit(1);
    }
    readLine->line = temporary;
}

//free the line
void free_struct(line_string* readLine){
    if(readLine->line != NULL){
        free(readLine->line);
    }
}

//read the line and save it into the struct
int read_line(line_string* readLine, FILE* f){
    int end_read_line = NOT_LINE_END;
    while(!end_read_line){
        char ch = fgetc(f);
        if(readLine->size + 2 >= readLine->capacity){
            reallocate_line_mem(readLine);
        }
        switch(ch){
            case '\n':
                end_read_line = LINE_END;
                break;
            case EOF:
                end_read_line = EOF;
                break;
            default:
                readLine->line[readLine->size++] = ch;
                break;
        }
    }
    readLine->line[readLine->size++] = '\0';
    return end_read_line;
}

//open the file we are searching for the pattern
void open_file(FILE** f, char* FILE_name){
    *f = fopen(FILE_name, "r");
    if(*f == NULL){
        fprintf(stderr, "File open has failed.\n");
        exit(1);
    }
}

//close the opened file
void close_file(FILE** f){
    if(fclose(*f) == EOF){
        fprintf(stderr, "File open has failed.\n");
        exit(1);
    }
}

//find the requested pattern
int regular_expressions(char *line, char *search_pattern){
    regex_t preg;
    int control = 0;
    if((regcomp(&preg, search_pattern, REG_EXTENDED)) != 0){
        fprintf(stderr, "Regcomp function has failed.\n");
        exit(1);
    }
    regmatch_t indexes[2];
    int re;
    re = regexec(&preg, line, 2, indexes,0);
    if(re != REG_NOMATCH && re != 0){
        fprintf(stderr, "Regexec function has failed.\n");
        exit(1);
    }else if(re == 0){
        fprintf(stdout,"%s\n", line);
        control = 1;
    }
    regfree(&preg);
    return control;
}

int main(int argc, char *argv[]) {
    line_string line;
    FILE* f;
    int control = 0;
    int finish_reading = NOT_LINE_END;
    //if file was not given
    if (argc == 2){
        while(finish_reading != EOF){
            construct_line(&line);
            finish_reading = read_line(&line, stdin);
            if(regular_expressions(line.line, argv[1]) == 1){
                control = 1;
            }
        }
        free_struct(&line);
    //if file was given
    }else if(argc == 3){
        open_file(&f, argv[2]);
        while(finish_reading != EOF){
            construct_line(&line);
            finish_reading = read_line(&line, f);
            if(regular_expressions(line.line, argv[1]) == 1){
                control = 1;
            }
            free_struct(&line);
        }
        close_file(&f);
    //if regular expressions are used
    }
    else if(argc == 4) {
        open_file(&f, argv[3]);
        while(finish_reading != EOF){
            construct_line(&line);
            finish_reading = read_line(&line, f);
            if(regular_expressions(line.line, argv[2]) == 1){
                control = 1;
            }
            free_struct(&line);
        }
        close_file(&f);
    }
    if(control == 0){
        return 1;
    }else{
        return 0;
    }
}
