function [a, b, r] = minimaxfit(x,y)

xSize1 = size(x.', 1) * 2;
xSize2 = size(x.', 2) + 2;

firstRange = size(x.', 1);
secondRange = size(x.', 2);

transposedX = x.';
toMinimize = zeros(xSize2, 1);
toMinimize(end) = 1;

firstLeftSide = zeros(xSize1, xSize2);
firstRightSide = zeros(xSize1, 1);

for firstOffset = 1:firstRange
    firstIndex = 2 * firstOffset;

    for secondIndex = 1:secondRange
        value = transposedX(firstOffset, secondIndex);
        firstLeftSide(firstIndex, secondIndex) = -value;
        firstLeftSide(firstIndex - 1, secondIndex) = value;
    end

    firstLeftSide(firstIndex, end - 1) = -1;
    firstLeftSide(firstIndex, end) = -1;
    firstLeftSide(firstIndex - 1, end - 1) = 1;
    firstLeftSide(firstIndex - 1, end) = -1; 
end

for offset = 1:firstRange
    index = 2 * offset;
    value = y(offset);
    firstRightSide(index) = -value;
    firstRightSide(index - 1) = value;
end

secondLeftSide = zeros(xSize2);
secondRightSide = zeros(xSize2, 1);
lowerBound = -inf;
upperBound = inf;

[output, finalValue] = linprog(toMinimize, firstLeftSide, firstRightSide, ...
    secondLeftSide, secondRightSide, lowerBound, upperBound);
a = output(1:end - 2);
b = output(end - 1);
r = finalValue;

end