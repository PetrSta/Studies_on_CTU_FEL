#include <stdio.h>

int main() {
    int width;
    int height;
    // zjištění šířky a výšky a zda splňují podmínky
    if (scanf("%d %d", &width, &height) == 2){
    } else {
        fprintf(stderr,"Error: Chybny vstup!\n");
        return 100;
    }
    if(width < 3 || width > 69 || height < 3 || height > 69){
        fprintf(stderr,"Error: Vstup mimo interval!\n");
        return 101;
    }
    if(width % 2 == 0){
        fprintf(stderr,"Error: Sirka neni liche cislo!\n");
        return 102;
    }
    // cyklus zajišťující vykreslení střechy
    int roof_width = 1;
    while(roof_width != width){
        if(roof_width == 1){
            for(int slope_left = 0; slope_left < (width - roof_width) / 2; slope_left += 1){
                printf(" ");
            }
            printf("X");
            printf("\n");
            roof_width += 2;
        }
        while(roof_width != 1 && roof_width != width){
            for(int slope_left = 0; slope_left < (width - roof_width) / 2; slope_left += 1){
                printf(" ");
            }
            printf("X");
            for(int roof_fill = 0; roof_fill < width - ((width - roof_width)+2); roof_fill += 1){
                printf(" ");
            }
            printf("X");
            printf("\n");
            roof_width += 2;
        }
        // cyklus vykreslení horní hrany domu
        for(int house_top_width = 0; house_top_width != width; house_top_width += 1){
            printf("X");
        }
        printf("\n");
        // cyklus vykreslení vnitřku domu
        for(int house_fill_height = 0; house_fill_height != (height-2); house_fill_height += 1){
            printf("X");
            for(int house_fill = 0; house_fill != (width-2); house_fill +=1){
                printf(" ");
            }
            printf("X");
            printf("\n");
        }
        // cyklus vykreslení spodní hrany domu
        for(int house_bottom_width = 0; house_bottom_width != width; house_bottom_width += 1){
            printf("X");
        }
        printf("\n");
    }
    return 0;
}
