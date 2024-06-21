package alg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        //get initial values
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());

        int hallwayLength = Integer.parseInt(tokenizer.nextToken());
        int firstLightPower = Integer.parseInt(tokenizer.nextToken());
        int secondLightPower = Integer.parseInt(tokenizer.nextToken());

        //create field with exit times of robot 1 from each sector of the hallway
        int[] firstRobotExitTimes = new int[hallwayLength];
        tokenizer = new StringTokenizer(reader.readLine());
        firstRobotExitTimes[0] = Integer.parseInt(tokenizer.nextToken());
        for(int i = 1; i < hallwayLength; i++) {
            firstRobotExitTimes[i] = Integer.parseInt(tokenizer.nextToken()) + firstRobotExitTimes[i-1];
        }

        //create field with exit times of robot 2 from each sector of the hallway
        int[] secondRobotExitTimes = new int[hallwayLength];
        tokenizer = new StringTokenizer(reader.readLine());
        secondRobotExitTimes[0] = firstRobotExitTimes[hallwayLength-1];
        for(int i = 1; i < hallwayLength; i++) {
            secondRobotExitTimes[i] = secondRobotExitTimes[i-1] - Integer.parseInt(tokenizer.nextToken());
        }

        //find out the amount of time each sector of hallway is lit by robot 1 and robot 2
        int[] lightStartRobot1 = new int[hallwayLength];
        int[] lightEndRobot1 = new int[hallwayLength];
        int startIndex1 = firstLightPower + 1;
        int finalSectorIndex = hallwayLength - 1;
        for(int i = 0; i < hallwayLength; i++) {
            //times that robot starts to light up sectors
            if (i - startIndex1 < 0) {
                lightStartRobot1[i] = 0;
            } else {
                lightStartRobot1[i] = firstRobotExitTimes[i - startIndex1];
            }
            //times that robot ends to light up sectors
            if (i + firstLightPower >= hallwayLength) {
                lightEndRobot1[i] = firstRobotExitTimes[finalSectorIndex];
            } else {
                lightEndRobot1[i] = firstRobotExitTimes[i + firstLightPower];
            }
        }

        //find out the amount of time each sector of hallway is lit by robot 2
        int[] lightStartRobot2 = new int[hallwayLength];
        int[] lightEndRobot2 = new int[hallwayLength];
        int startIndex2 = secondLightPower + 1;
        for(int i = hallwayLength-1; i >= 0; i--) {
            //times that robot2 starts to light up sectors
            if (i + startIndex2 >= hallwayLength) {
                lightStartRobot2[i] = 0;
            } else {
                lightStartRobot2[i] = secondRobotExitTimes[i + startIndex2];
            }
            //times that robot2 ends to light up sectors
            if (i - secondLightPower < 0) {
                lightEndRobot2[i] = secondRobotExitTimes[0];
            } else {
                lightEndRobot2[i] = secondRobotExitTimes[i - secondLightPower];
            }
        }

        //calculate time when both robots were lighting up the same sector for each sector
        int[] sectorLightBoth = new int[hallwayLength];
        boolean firstPositive = false;
        for(int i = 0; i < hallwayLength; i++) {
            sectorLightBoth[i] = Math.min(lightEndRobot1[i], lightEndRobot2[i]) - Math.max(lightStartRobot1[i], lightStartRobot2[i]);
            if(sectorLightBoth[i] > 0) {
                firstPositive = true;
            }
            if(sectorLightBoth[i] < 0) {
                sectorLightBoth[i] = 0;
                if(firstPositive) {
                    break;
                }
            }
        }

        //final calculation
        int min = (lightEndRobot1[0] - lightStartRobot1[0])*2 + (lightEndRobot2[0] - lightStartRobot2[0])*2 - sectorLightBoth[0];
        int max = min;
        int resultingSectorLight;
        for(int i = 1; i < hallwayLength; i++) {
            resultingSectorLight = ((lightEndRobot1[i] - lightStartRobot1[i])*2 + (lightEndRobot2[i] - lightStartRobot2[i])*2) - sectorLightBoth[i];
            if (min > resultingSectorLight) {
                min = resultingSectorLight;
            }
            if (max < resultingSectorLight) {
                max = resultingSectorLight;
            }
        }
        System.out.println(min + " " + max);
    }
}