function [d, e, f] = fit_circle_hom(X)
% function [d e f] = fit_circle_hom(X)
%
% INPUT: 
% X: n-by-2 vector
%    with data
%
% OUTPUT: 
% quadric coordinates of the circle
% materials 7.3

x = X(:,1);
y = X(:,2);

matrix = [x.^2 + y.^2, x, y, ones(length(x),1)];
[eigenVectors, eigenValues] = eig(matrix.' * matrix);

eigenValuesColumn = diag(eigenValues);
columnLength = length(eigenValuesColumn);
min = intmax;
index = 0;
for i = 1:columnLength
    if eigenValuesColumn(i) < min
        min = eigenValuesColumn(i);
        index = i;
    end
end

result = eigenVectors(:, index);

a = result(1);
d = result(2) / a;
e = result(3) / a;
f = result(4) / a;

end
