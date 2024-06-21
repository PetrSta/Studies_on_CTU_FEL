function x = fit_temps(t, T, omega)
    matrix_size = size(t);
    oneV = ones(matrix_size);
    left_side_matrix = [oneV, t, sin(omega * t), cos(omega * t)];
    x = left_side_matrix \ T;
end