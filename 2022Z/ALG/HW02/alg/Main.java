package alg;

//TODO cant ignore line -> in some solutions empty line can be part of solution in the middle,
// for some reason this solution cant find this


//TODO add if condition for situations where there is not enough lines for solution
// -> for example if first station is placed on line 3 of 4 and we need 3 stations

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    /*
    expected outputs for pub inputs:
    input 1: 28 -> OK
    input 2: 10 -> OK
    input 3: 130 -> OK
    input 4: 100 -> OK
    input 5: 575 -> OK
    input 6: 648 -> OK
    input 7: 197 -> OK
    input 8: 746 -> OK
    input 9: 746 -> OK
    input 10: 126 -> OK
     */
    static int bestSolution = 0;

    private static boolean distanceOfStations (Pair[]selectedStations, int fireStationDistance,int currentStationX, int currentStationY) {
        int distance;
        for (Pair selectedStation : selectedStations) {
            if (selectedStation != null) {
                //check if streets don't have station on them already
                if(selectedStation.x == currentStationX || selectedStation.y == currentStationY) {
                    return false;
                }
                //check distance of stations
                distance = Math.abs(selectedStation.x - currentStationX) + Math.abs(selectedStation.y - currentStationY);
                if (distance < fireStationDistance) {
                    return false;
                }
            }
        }
        return true;
    }

    //recursive function to search for solution
    private static void tourOfCity (int y, int currentCost, int numberOfColumns, int numberOfLines, int numberOfFireStations, int numberOfSelected, int fireStationDistance, Pair[]selectedStations, int[][] cityLayout) {
        for (;y < numberOfLines; y++) {
            selectedStations[numberOfSelected] = null;
            //check if we have enough space for solution
            if (!(numberOfSelected + (numberOfLines - y) >= numberOfFireStations)) {
                break;
            }
            for (int x = 0; x < numberOfColumns; x++) {
                selectedStations[numberOfSelected] = null;
                if(distanceOfStations(selectedStations, fireStationDistance, x, y)) {
                    //trying to filter out bad branches
                    if(currentCost + cityLayout[x][y] < bestSolution || bestSolution == 0) {
                        selectedStations[numberOfSelected] = new Pair(x, y);
                        if(numberOfSelected < numberOfFireStations - 1) {
                            tourOfCity(y + 1, currentCost + cityLayout[x][y], numberOfColumns, numberOfLines, numberOfFireStations, numberOfSelected + 1, fireStationDistance, selectedStations, cityLayout);
                        } else {
                            bestSolution = currentCost + cityLayout[x][y];
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //variables
        //read first line of input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        int numberOfLines = Integer.parseInt(tokenizer.nextToken());
        int numberOfColumns = Integer.parseInt(tokenizer.nextToken());
        int fireStationDistance = Integer.parseInt(tokenizer.nextToken());
        int numberOfFireStations = Integer.parseInt(tokenizer.nextToken());

        //read and save the layout of city
        int[][] cityLayout = new int[numberOfColumns][numberOfLines];
        for (int y = 0; y < numberOfLines; y++) {
            tokenizer = new StringTokenizer(reader.readLine());
            for (int x = 0; x < numberOfColumns; x++) {
                cityLayout[x][y] = Integer.parseInt(tokenizer.nextToken());
                //find the highest cost station
            }
        }

        //array to store selected stations for distance check
        Pair[] selectedStations = new Pair[numberOfFireStations];
        //get solution
        tourOfCity(0, 0, numberOfColumns, numberOfLines, numberOfFireStations, 0, fireStationDistance, selectedStations, cityLayout);

        System.out.println(bestSolution);

    }
}