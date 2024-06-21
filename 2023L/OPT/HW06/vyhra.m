function [x] = vyhra(c,k)

toMinimize = [0 0 0 0 0 -1];
firstLeftSide = zeros(3,6);
values = -c;

firstLeftSide(:, end) = 1;

firstLeftSide(1,1) = values(1);
firstLeftSide(1,2) = values(2);

firstLeftSide(2,2) = values(2);
firstLeftSide(2,3) = values(3);
firstLeftSide(2,4) = values(4);

firstLeftSide(3,4) = values(4);
firstLeftSide(3,5) = values(5);

firstRightSide = zeros(3,1);
secondLeftSide = [1, 1, 1, 1, 1, 0];
secondRightSide = k;
lowerBound = [0 0 0 0 0 -inf];

linSolution = linprog(toMinimize, firstLeftSide, firstRightSide, secondLeftSide, secondRightSide, lowerBound);

x = linSolution(1:end-1);

end