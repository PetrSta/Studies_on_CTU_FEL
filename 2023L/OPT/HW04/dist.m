function d = dist(X,x0,y0,r)
% function d = dist(X,x0,y0,r)
%
% INPUT: 
% X: n-by-2 vector
%    with data
% x0, y0 are the coordinates of the circle center.
% r is the circle radius
%
% OUTPUT: 
% d: 1-by-N vector of *signed* distances of all points 
%    from the circumference. 
 
firstPart = (X(:,1) - x0).^2;
secondPart = (X(:,2) - y0).^2;

d = sqrt(firstPart + secondPart) - r;

end
