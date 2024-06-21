function [U,C] = fitlin(A,k)
% function [U,C] = fitlin(A,k)
%
% INPUT: 
% A: m-by-n matrix
%    with data
% k: scalar, dimension of linear approximation
%
% OUTPUT:
% U: m-by-k matrix
%	columns form an orthonormal basis
% C: k-by-n matrix
%	columns contain coordinates w.r.t the basis
%

matrixForEigenValues = A*A.';
[eigenVectors, ~] = eig(matrixForEigenValues);

ortonormalBasis = eigenVectors(:, end - k + 1:end);
U = ortonormalBasis;

leftCMatrix = ortonormalBasis.';
C = leftCMatrix * A;