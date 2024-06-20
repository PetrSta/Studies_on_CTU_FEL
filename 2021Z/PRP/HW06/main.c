#include <stdio.h>
//načítaní matice

void clear (void)
{
    while ( getchar() != '\n' );
}

int error(){
    fprintf(stderr, "Error: Chybny vstup!\n");
    return 100;
}

int load_matrix(int row, int column, int *matrix){
    for(int r = 0; r < row; r += 1){
        for(int c = 0; c < column; c += 1){
            if(scanf("%d", matrix) != 1){
                return 0;
            }
            matrix++;
        }
    }
    return 1;
}

int main() {
    int row_1, column_1;
    if(scanf("%d %d", &row_1, &column_1) != 2 || row_1 < 0 || column_1 < 0){
        return error();
    }
    //loading 1st matrix
    int matrix_1[row_1][column_1];
    load_matrix(row_1, column_1, matrix_1[0]);
    //loading operator
    char operator;
    clear();
    if (scanf("%c", &operator) != 1){
        return error();
    }else if(!(operator == '-' || operator == '+' || operator == '*')){
        return error();
    }
    //loading 2nd matrix
    int row_2, column_2 = 0;
    if(scanf("%d %d", &row_2, &column_2) != 2 || row_2 < 0 || column_2 < 0){
        return error();
    }if(operator == '*' && column_1 != row_2){
        return error();
    }if((operator == '-' || operator == '+') && (row_1 != row_2 || column_1 != column_2)){
        return error();
    }
    int matrix_2[row_2][column_2];
    load_matrix(row_2, column_2, matrix_2[0]);
    //using operator to create result matrix
    if(operator == '-'){
        int result_matrix[row_1][column_1];
        fprintf(stdout,"%d %d\n", row_1, column_1);
        for(int r = 0; r < row_1; r += 1){
            for(int c = 0; c < column_1; c += 1){
                result_matrix[r][c] = matrix_1[r][c] - matrix_2[r][c];
                if(c != column_1 - 1){
                    fprintf(stdout,"%d ", result_matrix[r][c]);
                }
                if(c == column_1 - 1){
                    fprintf(stdout,"%d\n", result_matrix[r][c]);
                }
            }
        }
    }else if(operator == '+'){
        int result_matrix[row_1][column_1];
        fprintf(stdout,"%d %d\n", row_1, column_1);
        for(int r = 0; r < row_1; r += 1){
            for(int c = 0; c < column_1; c += 1){
                result_matrix[r][c] = matrix_1[r][c] + matrix_2[r][c];
                if(c != column_1 - 1){
                    fprintf(stdout,"%d ", result_matrix[r][c]);
                }
                if(c == column_1 - 1){
                    fprintf(stdout,"%d\n", result_matrix[r][c]);
                }
            }
        }
    }else{
        int result_matrix[row_1][column_2];
        fprintf(stdout,"%d %d\n", row_1, column_2);
        for(int r = 0; r < row_1; r += 1){
            for(int c = 0; c < column_2; c += 1){
                result_matrix[r][c] = 0;
            }
        }
        for(int r = 0; r < row_1; r += 1){
            for(int c = 0; c < column_2; c += 1){
                for(int r_c = 0; r_c < column_1; r_c += 1){
                    result_matrix[r][c] += matrix_1[r][r_c] * matrix_2[r_c][c];
                    if(r_c == column_1 - 1){
                    }
                }
            }
        }
        for(int r = 0; r < row_1; r += 1){
            for(int c = 0; c < column_2; c += 1){
                if(c != column_2 - 1){
                    fprintf(stdout, "%d ", result_matrix[r][c]);
                }
                if(c == column_2 - 1){
                    fprintf(stdout, "%d\n", result_matrix[r][c]);
                }
            }
        }
    }
    return 0;
}
