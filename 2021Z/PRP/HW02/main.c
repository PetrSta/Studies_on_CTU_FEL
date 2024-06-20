#include <stdio.h>

int main(void) {
    int number;
    double amount = 0;
    double positive = 0;
    double negative = 0;
    double even = 0;
    double odd = 0;
    double count = 0;
    int max = -10000;
    int min = 10000;
    while(scanf("%d", &number) != EOF){
        count += number;
        amount += 1;
        if(number < -10000 || number > 10000){
            printf("\nError: Vstup je mimo interval!\n");
            return 100;
        } else{
            if(amount == 1){
                printf("%d", number);
            } else {
                printf(", %d", number);
            }
            if(number > 0){
                positive += 1;
            }
            if(number < 0){
                negative += 1;
            }
            if(number % 2 == 0){
                even += 1;
            }
            if(number % 2 != 0){
                odd += 1;
            }
            if(number > max){
                max = number;
            }
            if(number < min){
                min = number;
            }
        }
    }
    double percentage_positive;
    percentage_positive = (positive / amount)*100;
    double percentage_negative;
    percentage_negative = (negative / amount)*100;
    double percentage_even;
    percentage_even = (even / amount)*100;
    double percentage_odd;
    percentage_odd = (odd / amount)*100;
    double avg;
    avg = count/amount;
    printf("\nPocet cisel: %.0lf\n", amount);
    printf("Pocet kladnych: %.0lf\n", positive);
    printf("Pocet zapornych: %.0lf\n", negative);
    printf("Procento kladnych: %.2lf\n", percentage_positive);
    printf("Procento zapornych: %.2lf\n", percentage_negative);
    printf("Pocet sudych: %.0lf\n", even);
    printf("Pocet lichych: %.0lf\n", odd);
    printf("Procento sudych: %.2lf\n", percentage_even);
    printf("Procento lichych: %.2lf\n", percentage_odd);
    printf("Prumer: %.2lf\n", avg);
    printf("Maximum: %d\n", max);
    printf("Minimum: %d\n", min);
    return 0;
}
