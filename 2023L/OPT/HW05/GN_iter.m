function [x, y, r] = GN_iter(X, x0, y0, r0)
% [x y r] = GN_iter(X, x0, y0, r0)
%
% makes the Gauss-Newton iteration. 
%
% INPUT: 
% X: n-by-2 matrix
%    with data
% x0, y0 are the coordinates of the circle center.
% r0 is the circle radius
%
% OUTPUT: 
% coordinates and radius after one step of GN method.

data = X;
centerX = x0;
centerY = y0;
radius = r0;
dataSize = size(data, 1);
G = zeros(dataSize, 1);
N = zeros(dataSize, 3);

for offset = 1:dataSize
    xForOffset = data(offset, 1);
    yForOffset = data(offset, 2);

    radiusForOffset = dist([xForOffset yForOffset], centerX, centerY, 0);
    G(offset) = radiusForOffset - radius;

    xPart = -((xForOffset - centerX) / (radiusForOffset)); 
    yPart = -((yForOffset - centerY) / (radiusForOffset));
    N(offset,:) = [xPart, yPart, -1];
end

GN = -( (N.' * N) \ (N.' * G) );

x = centerX + GN(1);
y = centerY + GN(2);
r = radius + GN(3);

end
 
