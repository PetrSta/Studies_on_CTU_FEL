package alg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.Queue;
import java.util.LinkedList;

public class Main {

    static int peak = 0;
    static int numberOfSignposts;
    static int numberOfRoads;
    static int maximumNumberOfPayments;
    static Signpost[] signposts;

    public static void takeATour(int signpost, int canPay, int j, int[][] resultTable, int[] topologicalOrder) {
        int enjoyment = 0;
        for(Road road : signposts[signpost].Roads) {
            int roadPayment = 0;
            if(!road.isFree) {
                roadPayment = 1;
            }
            if(roadPayment > canPay) {
                continue;
            } else {
                int tmpEnjoyment = road.Enjoyment;
                if(road.higherSignpost == peak && tmpEnjoyment > enjoyment) {
                    enjoyment = tmpEnjoyment;
                } else {
                    int maximumEnjoyment = 0;
                    for(int i = 0; i <= canPay - roadPayment; i++) {
                        int currentMaximumEnjoyment = resultTable[i][road.higherSignpost];
                        if(currentMaximumEnjoyment > maximumEnjoyment) {
                            maximumEnjoyment = currentMaximumEnjoyment;
                        }
                    }
                    if(maximumEnjoyment != 0 && tmpEnjoyment + maximumEnjoyment > enjoyment) {
                        enjoyment = tmpEnjoyment + maximumEnjoyment;
                    }
                }
            }
        }
        resultTable[canPay][topologicalOrder[j]] = enjoyment;
    }

    private static void findAndPrintResult(int[][] resultTable) {
        int result = 0;
        for(int i = maximumNumberOfPayments; i >= 0; i--) {
            for(int j = numberOfSignposts - 1; j > 0; j--) {
                if(resultTable[i][j] > result) {
                    //check if we can go back
                    for(int k = maximumNumberOfPayments - i; k >= 0 ; k--) {
                        //0 is not valid result
                        if (resultTable[k][j] != 0) {
                            result = resultTable[i][j];
                            break;
                        }
                    }
                }
            }
        }
        System.out.println(result);
    }
    private static int[] topologicalSorting(int[] degreeOfSignposts) {
        //visited is also used as index
        int visited = 0;
        Queue<Integer> queue = new LinkedList<>();
        int[] topologicalSort = new int[numberOfSignposts];
        for(int i = 0; i < numberOfSignposts; i++) {
            if(degreeOfSignposts[i] == 0) {
                queue.add(i);
            }
        }
//        System.err.print("\nTopological order:\n");
        while(!queue.isEmpty()) {
            int removedSignPost = queue.remove();
//            System.err.printf("%d ",(removedSignPost + 1));
            topologicalSort[visited] = removedSignPost;
            visited++;
            for(Road road : signposts[removedSignPost].Roads) {
                degreeOfSignposts[road.higherSignpost]--;
                if(degreeOfSignposts[road.higherSignpost] == 0) {
                    queue.add(road.higherSignpost);
                }
            }
            if(queue.size() + visited == numberOfSignposts) {
                for(int i = 0; i < queue.size(); i++) {
                    removedSignPost = queue.remove();
//                    System.err.printf("%d ",(removedSignPost + 1));
                    topologicalSort[visited] = removedSignPost;
                    visited++;
                }
            }
        }
//        System.err.print("\n");
        //remove from queue
        if(visited != numberOfSignposts) {
            System.err.println("Topological sort is not possible. \nvisited is: " + visited + "\nnumberOfSignposts is: " + numberOfSignposts);
            return null;
        }
        return topologicalSort;
    }

    public static void main(String[] args) throws IOException {
        //initialize buffered reader and tokenizer
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());

        //load 1st line
        numberOfSignposts = Integer.parseInt(tokenizer.nextToken());
        numberOfRoads = Integer.parseInt(tokenizer.nextToken());
        maximumNumberOfPayments = Integer.parseInt(tokenizer.nextToken());
        //create list of Signposts
        signposts = new Signpost[numberOfSignposts];
        for(int i = 0; i < numberOfSignposts; i++) {
            signposts[i] = new Signpost(i);
        }

        //degree as in it was graph node aka. number of roads leading to this signpost
        int[] degreeOfSignposts = new int [numberOfSignposts];

        //load rest of input
        for(int i = 0; i < numberOfRoads; i++) {
            tokenizer = new StringTokenizer(reader.readLine());
            int lowerSignPost = Integer.parseInt(tokenizer.nextToken()) - 1;
            int higherSignPost = Integer.parseInt(tokenizer.nextToken()) - 1;
            int Enjoyment = Integer.parseInt(tokenizer.nextToken());
            boolean isFree = Integer.parseInt(tokenizer.nextToken()) == 0;
            Road tmpRoad = new Road(Enjoyment, higherSignPost, isFree);
            degreeOfSignposts[higherSignPost]++;
            signposts[lowerSignPost].addRoad(tmpRoad);
        }

        int[] topologicalOrder = topologicalSorting(degreeOfSignposts);
        if(topologicalOrder == null) {
            System.err.println("Topological sorting failed.");
            System.exit(1);
        }

        int[][] resultTable = new int[maximumNumberOfPayments + 1][numberOfSignposts];
        //-2 since we can skip final element of topological order -> always is 1(0)
        for(int i  = 0; i <= maximumNumberOfPayments; i++) {
            for(int j = numberOfSignposts - 2; j >= 0; j--) {
                takeATour(topologicalOrder[j], i, j, resultTable, topologicalOrder);
            }
        }

        findAndPrintResult(resultTable);
    }
}