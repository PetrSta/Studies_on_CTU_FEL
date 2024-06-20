package cz.cvut.fel.pjv;

public class NodeImpl implements Node {

    //initialize variables
    public Node rightNode;
    public Node leftNode;
    public int number;

    public void setNumber(int number) {
        this.number = number;
    }

    //builder
    public NodeImpl(int number) {
        this.number = number;
    }

    //rightNode getter
    @Override
    public Node getRight() {
        return this.rightNode;
    }

    //leftNode getter
    @Override
    public Node getLeft() {
        return this.leftNode;
    }

    //value getter
    @Override
    public int getValue() {
        return this.number;
    }

}
