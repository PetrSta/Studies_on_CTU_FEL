function x = fit_wages(t, M)
    matrix_size = size(t);
    oneV = ones(matrix_size);
    left_side_matrix = [oneV, t];
    x = left_side_matrix \ M;
end