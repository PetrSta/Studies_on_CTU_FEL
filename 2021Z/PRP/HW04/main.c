#include <stdio.h>
#include <math.h>

void initialize_array(int long *array,int long array_range){
    for (int long y = 0; y < array_range; y += 1) {
        array[y] = 0;
    }
}

void find_primes(int long * primes_list){
    int swap = 1;
    //int long count_primes = 0; //pro případné zjištění počtu prvočísel
    int long index = 0;
    const int long max_prime = 1000000;
    for(int long i = 2; i <= max_prime; i += 1){
        for(int long j = 2; j <= sqrt(i); j += 1) {
            if (i % j == 0) {
                swap = 0;
                break;
            }else{
                swap = 1;
            }
        }
        if(swap == 1){
            primes_list[index] = i;
            index += 1;
            //count_primes += 1; //pro případné zjištění počtu prvočísel
        }
    }
}

void prime_factorization_print(int long number_input, int long *result_list, int long result_list_range) {
    printf("Prvociselny rozklad cisla %ld je:\n", number_input);
    int first = 1;
    int long squared = 0;
    int long prime = result_list[0];
    for (int long i = 0; i <= result_list_range; i += 1) {
        if (prime == 0) {
            printf("\n");
            break;
        } else if (prime == result_list[i]) {
            squared += 1;
        } else if (prime != result_list[i]) {
            if (squared > 1 && first == 1) {
                printf("%ld^%ld", prime, squared);
            } else if (squared > 1 && first != 1) {
                printf(" x %ld^%ld", prime, squared);
            } else if (squared <= 1 && first == 1) {
                printf("%ld", prime);
            } else if (squared <= 1 && first != 1) {
                printf(" x %ld", prime);
            }
            first = 0;
            squared = 1;
            prime = result_list[i];
        }
    }
}

void prime_factorization(int long number_input, int long * primes_list){
    int long result_list_range = (log2l(number_input)+2);
    int long result_list[result_list_range];
    initialize_array(result_list, result_list_range);
    int long new_index = 0;
    int long result_index = 0;
    int long new_number = number_input;
    while (primes_list[new_index] != 0 && new_number > 1) {
        if (new_number % primes_list[new_index] == 0) {
            result_list[result_index] = primes_list[new_index];
            new_number = new_number / (primes_list[new_index]);
            result_index += 1;
        } else if (new_number % primes_list[new_index] != 0) {
            new_index += 1;
        }
    }
    prime_factorization_print(number_input, result_list, result_list_range);
}

int main(void) {
    const int long list_range = 80000;
    int long primes_list[list_range];
    initialize_array(primes_list, list_range);
    find_primes(primes_list);
    int long number_input;
    char control;
    while (scanf("%ld%c", &number_input, &control) != EOF) {
        if (control != '\n' || number_input < 0) {
            fprintf(stderr, "Error: Chybny vstup!\n");
            return 100;
        } else if (number_input == 1) {
            printf("Prvociselny rozklad cisla %ld je:\n%ld\n", number_input, number_input);
        } else if (number_input == 0) {
            return 0;
        } else {
            prime_factorization(number_input, primes_list);
        }
        control = '\0';
    }
    return 0;
}
