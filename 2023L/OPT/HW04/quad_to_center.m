function [x0, y0, r] = quad_to_center(d,e,f)
% function [x0 y0 r] = quad_to_center(d,e,f)
%
% INPUT: 
% quadric coordinates of a circle
%
% OUTPUT: 
% cartesian coordinates of the circle
 
% x2 + y2 + dx + ey + f = 0
% https://www.quora.com/How-do-I-rewrite-y-2+4x-20-2y-x-2-into-circle-form
x0 = -d / 2;
y0 = -e / 2;
r = sqrt( x0.^2 + y0.^2 - f );
end
