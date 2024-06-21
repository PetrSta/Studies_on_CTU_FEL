function [x] = vyhra2(c,k,m)

toMinimize = [0 0 0 -1];
firstLeftSide = zeros(3,4);
values = -c;

firstLeftSide(:, end) = 1;

for index = 1:3
    firstLeftSide(index,index) = values(index);
end

firstRightSide = zeros(3,1);
secondLeftSide = [1, 1, 1, 0];
secondRightSide = k;
lowerBound = [m m m -inf];


linSolution = linprog(toMinimize, firstLeftSide, firstRightSide, ...
    secondLeftSide, secondRightSide, lowerBound);

x = linSolution(1:end-1);

end