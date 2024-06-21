#include <functional>
#include "bst_tree.h"

void bst_tree::insert(long long data) {
    node * newNode = new node(data);
    node * deadEnd = nullptr;

    // if BST is empty we just need to place node to root
    if(!root) {
        if(root.compare_exchange_weak(deadEnd, newNode)) {
            return;
        }
    }

    node * currentNode = root;

    while(true) {
        // check if node belongs to left side
        if(data < currentNode->data) {
            // if we reached the end insert node
            if(!currentNode->left) {
                if(currentNode->left.compare_exchange_weak(deadEnd, newNode)){
                    return;
                }
            }
            // otherwise we didnt reach the end, and we need to go deeper
            else {
                currentNode = currentNode->left;
            }
        }
        // only other option is that node belongs to right side
        else {
            // if we reached the end insert node
            if(!currentNode->right) {
                if(currentNode->right.compare_exchange_weak(deadEnd, newNode)){
                    return;
                }
            }
            // otherwise we didnt reach the end, and we need to go deeper
            else {
                currentNode = currentNode->right;
            }
        }
    }
}

bst_tree::~bst_tree() {
    // Rekurzivni funkce pro pruchod stromu a dealokaci pameti prirazene jednotlivym
    // uzlum
    std::function<void(node*)> cleanup = [&](node * n) {
        if(n != nullptr) {
            cleanup(n->left);
            cleanup(n->right);

            delete n;
        }
    };
    cleanup(root);
}
