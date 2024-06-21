function [U,C,b0] = fitaff(A,k)
% function [U,C,b0] = fitaff(A,k)
%
% INPUT: 
% A: m-by-n matrix
%    with data
% k: scalar, dimension of affine approximation
%
% OUTPUT:
% U: m-by-k matrix
%	columns form an orthonormal basis
% C: k-by-n matrix
%	columns contain coordinates w.r.t the basis
% b0: m-by-1 matrix
%	point of the affine space
%

aSize = size(A, 2);
onesMatrix = ones(1, aSize).';
centroid = A * onesMatrix * (1/aSize);
centralizedA = A - centroid;

b0 = centroid;
% U, C can be obtained using fitlin
[U, C] = fitlin(centralizedA, k);