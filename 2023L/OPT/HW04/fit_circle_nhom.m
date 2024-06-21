function [d, e, f] = fit_circle_nhom(X)
% function [d e f] = fit_circle_nhom(X)
%
% INPUT: 
% X: n-by-2 vector
%    with data
%
%
% OUTPUT: 
% quadric coordinates of the circle

x = X(:,1);
y = X(:,2);
fill = ones(length(x), 1);

leftSide = [x, y, fill];
rightSide = (x.^2 + y.^2);
result = (leftSide \ -rightSide)';

d = result(1);
e = result(2);
f = result(3);

end
