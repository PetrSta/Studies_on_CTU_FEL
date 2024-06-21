function [x0, y0, r] = fit_circle_ransac(X, num_iter, threshold)
% function [x0 y0 r] = fit_circle_ransac(X, num_iter, threshold)
%
% INPUT: 
% X: n-by-2 vector
%    with data
% num_iter: number of RANSAC iterations
% threshold: maximal  distance of an inlier from the circumference
%
% OUTPUT: 
% cartesian coordinates of the circle
%https://en.wikipedia.org/wiki/Random_sample_consensus

best_ransac_circle = [0, 0, 0];
mostInliners = 0;
data_length = length(X(:,1));

for i = 1:num_iter
    index = randi([1 ,data_length], 1, 3);
    [d, e, f] = fit_circle_nhom(X(index,:));
    [X0, Y0, R] =  quad_to_center(d, e, f);
    
    distancesFromCircle = abs(dist(X, X0, Y0, R));
    limit = length(distancesFromCircle);
    currentInliners = 0;
    
    for j = 1 : limit
        if distancesFromCircle(j) < threshold
            currentInliners = currentInliners + 1;
        end
    end
    
    if currentInliners > mostInliners
        mostInliners = currentInliners;
        best_ransac_circle = [X0, Y0, R];
    end
end

x0 = best_ransac_circle(1);
y0 = best_ransac_circle(2);
r = best_ransac_circle(3);

end
