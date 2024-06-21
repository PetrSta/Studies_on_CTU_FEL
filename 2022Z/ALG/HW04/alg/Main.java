package alg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Main {
    //variables
    static int numberOfLines;
    static int numberOfColumns;
    static int maximumNumberOfFires;
    static int startX;
    static int startY;
    static int endX;
    static int endY;
    static LinkedList<Info> linkedList = new LinkedList<>();
    //used to check that input was loaded correctly
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    //function used to check that input loaded correctly
    private static void printArray(int[][] Array) {
        for(int y = 0; y < numberOfLines; y++) {
            for(int x = 0; x < numberOfColumns; x++) {
                if(x == startX && y == startY) {
                    System.out.print(ANSI_CYAN + Array[x][y] + ANSI_RESET + " ");
                } else if (x == endX && y == endY) {
                    System.out.print(ANSI_CYAN + Array[x][y] + ANSI_RESET + " ");
                } else {
                    System.out.print(Array[x][y] + " ");
                }
                if(x == numberOfColumns - 1) {
                    System.out.print("\n");
                }
            }
        }
    }

    private static void driveRescueVehicle(int[][][] firesMap, int[][] cityLayout) {
        Info currentPoppedSquare;
        while(!linkedList.isEmpty()) {
            currentPoppedSquare = linkedList.remove();
            int x = currentPoppedSquare.x;
            int y = currentPoppedSquare.y;
            int bestDistance = currentPoppedSquare.bestDist;
            int bestFireAmount = currentPoppedSquare.bestFireAmount;
            if(cityLayout[x][y] == 1) {
                bestFireAmount++;
            }
            if(bestFireAmount <= maximumNumberOfFires) {
                if(firesMap[x][y][bestFireAmount] == 0 || firesMap[x][y][bestFireAmount] > bestDistance) {
                    firesMap[x][y][bestFireAmount] = bestDistance;
                } else {
                    continue;
                }
                if(x == endX && y == endY) {
                    continue;
                }
                if(x + 1 < numberOfColumns) {
                    linkedList.add(new Info(x + 1, y,bestDistance + 1, bestFireAmount));
                }
                if(x - 1 >= 0) {
                    linkedList.add(new Info(x - 1, y,bestDistance + 1, bestFireAmount));
                }
                if(y + 1 < numberOfLines) {
                    linkedList.add(new Info(x, y + 1,bestDistance + 1, bestFireAmount));
                }
                if(y - 1 >= 0) {
                    linkedList.add(new Info(x, y - 1,bestDistance + 1, bestFireAmount));
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //initialize buffered reader and tokenizer
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());

        //load 1st line
        numberOfLines = Integer.parseInt(tokenizer.nextToken());
        numberOfColumns = Integer.parseInt(tokenizer.nextToken());
        maximumNumberOfFires = Integer.parseInt(tokenizer.nextToken());

        //load 2nd line
        tokenizer = new StringTokenizer(reader.readLine());
        startY = Integer.parseInt(tokenizer.nextToken()) - 1;
        startX = Integer.parseInt(tokenizer.nextToken()) - 1;

        //load 3rd line
        tokenizer = new StringTokenizer(reader.readLine());
        endY = Integer.parseInt(tokenizer.nextToken()) - 1;
        endX = Integer.parseInt(tokenizer.nextToken()) - 1;

        //load map of city and distance to target
        int [][] cityLayout = new int[numberOfColumns][numberOfLines];
        int[][][] firesMap = new int[numberOfColumns][numberOfLines][maximumNumberOfFires + 1];
        for(int y = 0; y < numberOfLines; y++) {
            tokenizer = new StringTokenizer(reader.readLine());
            for(int x = 0; x < numberOfColumns; x++) {
                cityLayout[x][y] = Integer.parseInt(tokenizer.nextToken());
            }
        }
        linkedList.push(new Info(startX, startY, 0, 0));
        driveRescueVehicle(firesMap, cityLayout);

        //check that input loaded correctly
//        printArray(cityLayout);
//        System.out.println();
//        printArray(targetDistance);

        int result = 0;
        for(int i = 0; i < maximumNumberOfFires + 1; i++) {
            if(result == 0 || firesMap[endX][endY][i] < result) {
                result = firesMap[endX][endY][i];
            }
        }
        System.out.println(result);
    }
}