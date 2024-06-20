#include <stdio.h>
#include <stdlib.h>
#include <math.h>

typedef struct {
    unsigned char red,green,blue;
} RGB;

void writeToHistogram(int *histogram, unsigned char red, unsigned char green, unsigned char blue) {
    // calculate histogram value
    int histogramValue = round(0.2126 * red + 0.7152 * green + 0.0722 * blue);
    // add to histogram index, depending on value
    if(histogramValue >= 0 && histogramValue <= 50) {
        histogram[0] += 1;
    }
    else if(histogramValue > 50 && histogramValue <= 101) {
        histogram[1] += 1;
    }
    else if(histogramValue > 101 && histogramValue <= 152) {
        histogram[2] += 1;
    }
    else if(histogramValue > 152 && histogramValue <= 203) {
        histogram[3] += 1;
    }
    else if(histogramValue > 203 && histogramValue <= 255) {
        histogram[4] += 1;
    }
}

void createNewPicture(int x, int y, RGB* new_picture, RGB* picture, int *histogram) {
    int newRed, newGreen, newBlue;
    for (int i = 0; i < x * y; i++) {
        //first row
        if (i < x) {
            // copy from old picture
            new_picture[i] = picture[i];
            // add value to histogram
            writeToHistogram(histogram, new_picture[i].red, new_picture[i].green, new_picture[i].blue);
        }
        //last row, (x - 1) * (y - 1) should be last index of second to last row
        else if (i >= (x) * (y - 1)) {
            // copy from old picture
            new_picture[i] = picture[i];
            // add value to histogram
            writeToHistogram(histogram, new_picture[i].red, new_picture[i].green, new_picture[i].blue);
        }
        //first column
        else if (i % x == 0) {
            // copy from old picture
            new_picture[i] = picture[i];
            // add value to histogram
            writeToHistogram(histogram, new_picture[i].red, new_picture[i].green, new_picture[i].blue);
        }
        //last column
        else if (i % x == x - 1) {
            // copy from old picture
            new_picture[i] = picture[i];
            // add value to histogram
            writeToHistogram(histogram, new_picture[i].red, new_picture[i].green, new_picture[i].blue);
        }
        //main body
        else {
            newRed = -picture[i - 1].red + 5 * picture[i].red - picture[i + 1].red - picture[i - x].red -
                     picture[i + x].red;
            newGreen = -picture[i - 1].green + 5 * picture[i].green - picture[i + 1].green - picture[i - x].green -
                       picture[i + x].green;
            newBlue = -picture[i - 1].blue + 5 * picture[i].blue - picture[i + 1].blue - picture[i - x].blue -
                      picture[i + x].blue;
            //check if RGB values are in the limit, if not change them to be in the limit
            if (newRed < 0) {
                newRed = 0;
            } else if (newRed > 255) {
                newRed = 255;
            }
            if (newGreen < 0) {
                newGreen = 0;
            } else if (newGreen > 255) {
                newGreen = 255;
            }
            if (newBlue < 0) {
                newBlue = 0;
            } else if (newBlue > 255) {
                newBlue = 255;
            }
            //write the values into new picture
            new_picture[i].red = newRed;
            new_picture[i].green = newGreen;
            new_picture[i].blue = newBlue;
            // add value to histogram
            writeToHistogram(histogram, new_picture[i].red, new_picture[i].green, new_picture[i].blue);
        }
    }
}


int main(int argc, char* argv[]){
    // open requested file
    FILE *filePointer = fopen(argv[1], "rb");

    int x, y, max;
    // read initial information
    if (fscanf(filePointer, "P6 %d %d %d\n", &x, &y, &max) != 3) {
        return 1;
    }

    //memory allocation for RGB picture
    RGB *picture = (RGB*)malloc(x * y * sizeof(RGB));
    //read the picture
    if(!fread(picture, 3 * x, y, filePointer)) {
        return 1;
    }

    // close original picture
    fclose(filePointer);

    // create new picture
    RGB* new_picture = (RGB*)malloc(x * y * sizeof(RGB));

    // histogram
    // 0 = 0 to 50
    // 1 = 51 to 101
    // 2 = 102 to 152
    // 3 = 153 to 203
    // 4 = 204 to 255
    int histogram[] = {0, 0, 0, 0, 0};

    // creating the new picture
    createNewPicture(x, y, new_picture, picture, &histogram[0]);

    // free picture memory
    free(picture);

    // write output.ppm
    FILE *outputFile = fopen("output.ppm", "wb");
    fprintf(outputFile, "P6\n%d\n%d\n255\n",x,y);
    fwrite(new_picture, 3 * x, y, outputFile);
    fclose(outputFile);

    // free new_picture memory
    free(new_picture);

    // write histogram to file
    FILE *histogramOutput = fopen("output.txt", "wb");
    fprintf(histogramOutput, "%d %d %d %d %d ", histogram[0], histogram[1], histogram[2], histogram[3], histogram[4]);
    fclose(histogramOutput);
}