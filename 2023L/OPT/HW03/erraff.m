function d = erraff(A)
% function d = erraff(A)
%
% INPUT: 
% A: m-by-n matrix
%    with data
%
% OUTPUT:
% d: m-by-1 matrix
%
aSize = size(A, 2);
b0 = A * ones(1, aSize).' * (1/aSize);
editedA = A - b0 * ones(1, aSize);
[~, eigenValues] = eig(editedA * editedA.');
eigenValuesSize = size(eigenValues, 2);
sumMatrix = zeros(eigenValuesSize, 1);

eigenValuesTrace = trace(eigenValues);
outerLoopLimit = eigenValuesSize;
firstInnerLoopLimit = eigenValuesSize + 1;
secondInnerLoopLimit = outerLoopLimit;
for i = 1 : outerLoopLimit
    subtraction = 0;
    for j = firstInnerLoopLimit - i : secondInnerLoopLimit
        subtraction = subtraction + eigenValues(j, j);
    end
    sumMatrix(i, 1) = eigenValuesTrace - subtraction;
end

d = sumMatrix;