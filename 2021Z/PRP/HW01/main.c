#include <stdio.h>

int main(void) {
    int n, m;
    (scanf("%d", &n));
    (scanf("%d", &m));
        if (m < -10000 || m > 10000 || n < -10000 || n > 10000) {
            printf("Vstup je mimo interval!\n");
    } else {
            printf("Desitkova soustava: %d %d\n", n, m);
            printf("Sestnactkova soustava: %x %x\n", n, m);
            int soucet;
            soucet = n+m;
            printf("Soucet: %d + %d = %d\n", n, m, soucet);
            int rozdil;
            rozdil = n-m;
            printf("Rozdil: %d - %d = %d\n", n, m, rozdil);
            int nasobek;
            nasobek = n*m;
            printf("Soucin: %d * %d = %d\n", n, m, nasobek);
            if (m != 0) {
                int podil;
                podil = n/m;
                printf("Podil: %d / %d = %d\n", n, m, podil);
            } else {
                printf("Nedefinovany vysledek!\n");
            }
            double a, b;
            a = n;
            b = m;
            double prumer;
            prumer = (a+b)/2;
            printf("Prumer: %.1lf\n", prumer);
    }
    return 0;
}
