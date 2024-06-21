int WRITE = 4;
int READ = 3;
int INPUT = 0;
int OUTPUT = 1;

//template function just refactored
int isNumber(char character) {
    return character >= '0' && character <= '9';
}

//template function just refactored
int isSpace (char character) {
    return character == ' ' || character == '\n';
}

//simply count how long buffer is
int myStringLength(void *buffer) {
    int length = 1;
    int index = 0;
    char* charBuffer = (char*)buffer;
    while(!isSpace(charBuffer[index++])) {
        length++;
    }
    return length;
}

void convertToHexadecimal(unsigned int number, char *buffer) {
    char hexBuffer[20];
    int index = 0;
    int bufferIndex = 0;
    //set begging of hexadecimal format
    buffer[index++] = '0';
    buffer[index++] = 'x';
    //number == 0 -> edge case
    if(number == 0) {
        buffer[index++] = '0';
    } else {
        while(number != 0) {
            //ASCII
            if(number % 16 < 10) {
                hexBuffer[bufferIndex++] = (number % 16) + 48;
            } else {
                hexBuffer[bufferIndex++] = (number % 16) + 87;
            }
            number = number / 16;
        }
        for(int i = bufferIndex - 1; i >= 0; i--) {
            buffer[index++] = hexBuffer[i];
        }
    }
    buffer[index] = '\n';
}

//syscall 3 is equal to read and syscall 4 is equal to write
//either read input of given length or print output of given length
int myReadAndWrite(int inputOrOutput, void *buffer, int howLong, int syscall) {
    int bytesUsed;
    asm volatile ("int $0x80;" : "=a"(bytesUsed) : "0"(syscall), "b"(inputOrOutput), "c"(buffer), "d"(howLong) : "memory");
    return bytesUsed;
}

//exit program with given value
int myExit(int exitValue) {
    int returnValue;
    asm volatile ("int $0x80;" : "=a"(returnValue) : "0"(1), "b"(exitValue) : "memory");
    return returnValue;
}

//convert given number to hexadecimal and then print it using myReadAndWrite
static void myHexadecimalPrint(unsigned int numberToPrint) {
    char buffer[20];
    convertToHexadecimal(numberToPrint, buffer);
    int returnValue = myReadAndWrite(OUTPUT, buffer, myStringLength(buffer), WRITE);
    if(returnValue < 0) {
        myExit(1);
    }
}

//without any libraries this is basically main
void _start() {
    int index;
    char buffer[20];
    int numberOfDigits = 0;
    unsigned int number = 0;
    unsigned int charsLeft = 0;
    for(;; index++, charsLeft--) {
        //if nothing is read try reading input
        if(charsLeft == 0) {
            int returnValue = myReadAndWrite(INPUT, buffer, sizeof(buffer), READ);
            if(returnValue < 0) {
                myExit(1);
            }
            index = 0;
            charsLeft = returnValue;
        }
        //if we have a number, and we have finished reading input print the number
        if(numberOfDigits > 0 && (charsLeft == 0 || !isNumber(buffer[index]))) {
            myHexadecimalPrint(number);
            numberOfDigits = 0;
            number = 0;
        }
        //if there are no chars in buffer and current buffer index is not a number or space exit
        if(charsLeft == 0 || (!isNumber(buffer[index]) && !isSpace(buffer[index]))) {
            myExit(0);
        }
        //if we found a number edit variables accordingly
        if(isNumber(buffer[index])) {
            number = number * 10 + buffer[index] - '0';
            numberOfDigits++;
        }
    }
}
