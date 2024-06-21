function drawfitline(A)
% function drawfitline(A)
%
% INPUT: 
% A: m-by-2 matrix
%    with data
%
aSize = size(A, 1);
onesMatrix = ones(1, aSize);
[U, C, b0] = fitaff(A.', 1);
B = (b0 * onesMatrix + U * C).';

hold on

firstForLoopLimit = aSize - 1;
secondForLoopLimit = aSize;
scatter(A(:, 1), A(:, 2), "r", "+");

for j = 1 : firstForLoopLimit
    plot([B(j, 1) B(aSize, 1)], [B(j, 2), B(aSize, 2)])
end

for i = 1 : secondForLoopLimit
    plot([A(i, 1) B(i, 1)], [A(i, 2) B(i, 2)], "r")
end

axis equal
hold off
end