package alg;

/*
input 1: 10 -> OK
input 2: 11 -> OK
input 3: 42 -> OK
input 4: 20 -> OK
input 5: 143 -> OK
input 6: 163391 -> OK
input 7: 166279 -> OK
input 8: 5746528 -> OK
input 9: 21896468 -> OK
input 10: 82458528 -> OK
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    //global variables
    static int numberOfSources;
    static int numberOfJunctions;
    static int rootID;

    //recursive function
    public static void scoutRiver(node_t scoutedNode) {
        //recursively scout river and set value of river sources to 0
        if (scoutedNode.left.nodeNumber == -1 && scoutedNode.right.nodeNumber != -1) {
            //go deeper
            scoutRiver(scoutedNode.right);
        } else if (scoutedNode.right.nodeNumber == -1 && scoutedNode.left.nodeNumber != -1) {
            //go deeper
            scoutRiver(scoutedNode.left);
        } else if (scoutedNode.left.nodeNumber != -1 && scoutedNode.right.nodeNumber != -1) {
            //go deeper
            scoutRiver(scoutedNode.left);
            scoutRiver(scoutedNode.right);
        }

        //variables used for comparison
        int closedOption_1 = scoutedNode.left.valueClosed + scoutedNode.right.valueClosed;
        int closedOption_2 = scoutedNode.left.valueOpen + scoutedNode.right.valueOpen + scoutedNode.distanceToLeftNode + scoutedNode.distanceToRightNode;
        int openOption_1 = scoutedNode.left.valueOpen + scoutedNode.distanceToLeftNode + scoutedNode.right.valueClosed;
        int openOption_2 = scoutedNode.right.valueOpen + scoutedNode.distanceToRightNode + scoutedNode.left.valueClosed;

        //comparison
        scoutedNode.valueClosed = Math.max(closedOption_1, closedOption_2);
        scoutedNode.valueOpen = Math.max(openOption_1, openOption_2);
    }

    public static void main(String[] args) throws Exception {
        //start reading input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());

        //load first line -> size info
        numberOfSources = Integer.parseInt(tokenizer.nextToken());
        numberOfJunctions = Integer.parseInt(tokenizer.nextToken());

        //create array to store input
        node_t[] nodes = new node_t[numberOfSources + numberOfJunctions];

        //load rest of input
        for (int i = 0; i < numberOfJunctions; i++) {
            //load newNode information
            tokenizer = new StringTokenizer(reader.readLine());
            int nodeNumber = Integer.parseInt(tokenizer.nextToken());
            if(nodes[nodeNumber] == null) {
                nodes[nodeNumber] = new node_t();
            }
            nodes[nodeNumber].nodeNumber = nodeNumber;
            //first node is always root
            if(i == 0) {
                rootID = nodes[nodeNumber].nodeNumber;
            }
            //link left child
            int leftNodeNumber = Integer.parseInt(tokenizer.nextToken());
            if(nodes[leftNodeNumber] == null) {
                //tmp dummy node or river source
                nodes[leftNodeNumber] = new node_t();
                nodes[leftNodeNumber].nodeNumber = -1;
                nodes[leftNodeNumber].valueClosed = 0;
                nodes[leftNodeNumber].valueOpen = 0;
            }
            nodes[nodeNumber].left = nodes[leftNodeNumber];
            nodes[nodeNumber].distanceToLeftNode = Integer.parseInt(tokenizer.nextToken());
            //link right child
            int rightNodeNumber = Integer.parseInt(tokenizer.nextToken());
            if(nodes[rightNodeNumber] == null) {
                //tmp dummy node or river source
                nodes[rightNodeNumber] = new node_t();
                nodes[rightNodeNumber].nodeNumber = -1;
                nodes[rightNodeNumber].valueClosed = 0;
                nodes[rightNodeNumber].valueOpen = 0;
            }
            nodes[nodeNumber].right = nodes[rightNodeNumber];
            nodes[nodeNumber].distanceToRightNode = Integer.parseInt(tokenizer.nextToken());
        }

        //root of tree
        node_t root = nodes[rootID];

        boolean leftBranchIsEmpty = false;
        boolean rightBranchIsEmpty = false;

        //check left branch
        if (root.left.nodeNumber != -1) {
            scoutRiver(root.left);
        } else {
            leftBranchIsEmpty = true;
        }

        //check right branch
        if (root.right.nodeNumber != -1) {
            scoutRiver(root.right);
        } else {
            rightBranchIsEmpty = true;
        }

        //check which result is the correct one
        int bestResult;
        int closedValue = 0;
        int openValue = 0;

        if (rightBranchIsEmpty && leftBranchIsEmpty) {
            //simple solution
            System.out.println(root.distanceToLeftNode + root.distanceToRightNode);
            System.exit(0);
        } else if (leftBranchIsEmpty) {
            //tree branches only to right side from root
            closedValue = root.right.valueClosed;
            openValue = root.right.valueOpen + root.distanceToLeftNode + root.distanceToRightNode;
        } else if (rightBranchIsEmpty) {
            //tree branches only to left side from root
            closedValue = root.left.valueClosed;
            openValue = root.left.valueOpen + root.distanceToLeftNode + root.distanceToRightNode;
        } else {
            //tree branches to both sides from root
            closedValue = root.left.valueClosed + root.right.valueClosed;
            openValue = root.left.valueOpen + root.distanceToLeftNode + root.right.valueOpen + root.distanceToRightNode;
        }

        bestResult = Math.max(closedValue, openValue);
        System.out.println(bestResult);

    }
}
