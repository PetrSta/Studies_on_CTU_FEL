package cz.cvut.fel.pjv;

public class TreeImpl implements Tree {

    //initialize variables
    Node root;
    int[] values;

    public Node setTreeRecursion(int startIndex, int endIndex) {
        if(startIndex == endIndex) {
            return new NodeImpl(this.values[startIndex]);
        } else if(this.values.length == 1) {
            return new NodeImpl(this.values[0]);
        } else {
            //calculate middle
            int middle = (endIndex - startIndex + 1) / 2 + startIndex;
            //new binaryTree node
            NodeImpl newNode = new NodeImpl(this.values[middle]);
            //add subTrees using recursion
            newNode.leftNode = setTreeRecursion(startIndex, middle - 1);
            if(middle + 1 <= endIndex) {
                newNode.rightNode = setTreeRecursion(middle + 1, endIndex);
            } else {
                newNode.rightNode = null;
            }
            return newNode;
        }
    }

    @Override
    public void setTree(int[] values) {
        this.values = values;
        //check if values is not empty
        if(this.values.length > 0) {
            this.root = setTreeRecursion(0, values.length - 1);
        } else {
            this.root = null;
        }
    }

    @Override
    public Node getRoot() {
        return this.root;
    }

    private String addSubTrees(Node node, int depthLevel) {
        StringBuilder subTreeString = new StringBuilder();
        //depending on how deep the number is, add empty spaces
        for (int i = 0; i < depthLevel; i++) {
            subTreeString.append(" ");
        }
        //add the number itself in required format
        subTreeString.append("- ");
        subTreeString.append(node.getValue()).append("\n");
        //add subTrees of this subTree
        if(node.getLeft() != null) {
            subTreeString.append(addSubTrees(node.getLeft(), depthLevel + 1));
        }
        if(node.getRight() != null) {
            subTreeString.append(addSubTrees(node.getRight(), depthLevel + 1));
        }
        return subTreeString.toString();
    }

    @Override
    public String toString() {
        StringBuilder mainTreeString = new StringBuilder();
        //empty tree representation
        if(getRoot() == null) {
            return "";
        } else {
            //root build
            mainTreeString.append("- ");
            mainTreeString.append(this.root.getValue()).append("\n");
            //subTrees build
            if(this.root.getLeft() != null) {
                mainTreeString.append(addSubTrees(this.root.getLeft(), 1));
            }
            if(this.root.getRight() != null) {
                mainTreeString.append(addSubTrees(this.root.getRight(), 1));
            }
        }
        return mainTreeString.toString();
    }
}