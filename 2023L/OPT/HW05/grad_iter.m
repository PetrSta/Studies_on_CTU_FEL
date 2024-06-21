function [x, y, r] = grad_iter(X, x0, y0, r0, a)
% [x y r] = grad_iter(X, x0, y0, r0, a)
%
% makes the gradient method iteration. 
%
% INPUT: 
% X: n-by-2 matrix
%    with data
% x0, y0 are the coordinates of the circle center.
% r0 is the circle radius
% a is the stepsize
%
% OUTPUT: 
% coordinates and radius after one step of gradient method.

data = X;
centerX = x0;
centerY = y0;
radius = r0;
stepsize = a;
dataSize = size(data);
gradient = zeros(1,3);

for offset = 1:dataSize
    xForOffset = data(offset, 1);
    yForOffset = data(offset, 2);

    radiusForOffset = dist([xForOffset yForOffset], centerX, centerY, 0);
    multiplier = (radiusForOffset - radius) / (radiusForOffset);

    xPart = gradient(1) - 2 * ( (xForOffset - centerX) * multiplier);
    yPart = gradient(2) - 2 * ( (yForOffset - centerY) * multiplier);
    zPart = gradient(3) - 2 * (radiusForOffset - radius);
    gradient(:) = [xPart, yPart, zPart];
end

x = centerX - (stepsize * gradient(1));
y = centerY - (stepsize * gradient(2));
r = radius - (stepsize * gradient(3));

end
 
