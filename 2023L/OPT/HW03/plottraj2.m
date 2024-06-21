function plottraj2(C)
% function plottraj2(C)
%
% INPUT: 
% C: 2-by-m matrix
%    with data
%
cSize = size(C, 2);
forLoopLimit = cSize - 1;

hold on

for i = 1 : forLoopLimit
    plot([C(1, i) C(1, i + 1)], [C(2, i) C(2, i + 1)], "r");
end

hold off