function [x, y, r, success] = LM_iter(X, x0, y0, r0, mu)
% [x y r success] = LM_iter(X, x0, y0, r0, mu)
%
% makes the Levenberg-Marquardt iteration. 
%
% INPUT: 
% X: n-by-2 matrix
%    with data
% x0, y0 are the coordinates of the circle center.
% r0 is the circle radius
% mu is the damping factor (the factor which multiplies the
% regularizing identity matrix)

% OUTPUT: 
% success == 1 if the iteration is successful, i.e. 
% value of criterion f is decreased after the update 
% of x. 
% success == 0 in the oposite case. 
%
% x,y,r are updated parameters if success == 1. 
% x,y,r = x0,y0,r0 if success == 0.

data = X;
centerX = x0;
centerY = y0;
radius = r0;
damping_factor = mu;

dataSize = size(data, 1);
L = zeros(dataSize, 1);
M = zeros(dataSize, 3);

for offset = 1:dataSize
    xForOffset = data(offset, 1);
    yForOffset = data(offset, 2);

    radiusForOffset = dist([xForOffset yForOffset], centerX, centerY, 0);
    L(offset) = radiusForOffset - radius;

    xPart = -((xForOffset - centerX) / (radiusForOffset)); 
    yPart = -((yForOffset - centerY) / (radiusForOffset));
    M(offset,:) = [xPart, yPart, -1];
end

LM = -( (M.' * M + damping_factor * eye(3)) \ (M.' * L));

x = centerX + LM(1);
y = centerY + LM(2);
r = radius + LM(3);
success = 0;

objectiveFunction = get_objective_function(data);
objectiveValue = objectiveFunction([x, y, r]);

if sum(L.^2) > objectiveValue
    success = 1;
end

end

