package cz.cvut.fel.pjv;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Lab01 {

   public static String get_decimal_input(Scanner scanner) {
      int decimal_places = 0;
      System.out.println("Zadej pocet desetinnych mist: ");
      try {
         decimal_places = scanner.nextInt();
      } catch(InputMismatchException a) {
         System.out.println("Chyba - musi byt zadan kladny int!");
         System.exit(-1);
      }
      if(decimal_places < 0) {
         System.out.println("Chyba - musi byt zadane kladne cislo!");
         System.exit(-1);
      }
      return "%." + decimal_places + "f";
   }

   public static int get_operation_input(Scanner scanner){
      System.out.println("Vyber operaci (1-soucet, 2-rozdil, 3-soucin, 4-podil):");
      try {
         return scanner.nextInt();
      } catch(InputMismatchException b) {
         System.out.println("Chybna volba!");
         System.exit(-1);
      }
      return 0;
   }

   public static double get_double_input(Scanner scanner){
      try {
         return scanner.nextDouble();
      } catch(InputMismatchException c) {
         System.out.println("Chyba - musi byt zadan float!");
         System.exit(-1);
      }
      return 0;
   }

   public static void increment(Scanner scanner){
      //get the numbers and calculate result
      System.out.println("Zadej scitanec: ");
      double first_number = get_double_input(scanner);
      System.out.println("Zadej scitanec: ");
      double second_number = get_double_input(scanner);
      double result = first_number + second_number;
      //get amount of decimal places and print the result
      String decimal_format = get_decimal_input(scanner);
      System.out.printf(decimal_format + " + " + decimal_format + " = " + decimal_format + "\n", first_number, second_number, result);
   }

   public static void subtract(Scanner scanner){
      //get the numbers and calculate result
      System.out.println("Zadej mensenec: ");
      double first_number = get_double_input(scanner);
      System.out.println("Zadej mensitel: ");
      double second_number = get_double_input(scanner);
      double result = first_number - second_number;
      //get amount of decimal places and print the result
      String decimal_format = get_decimal_input(scanner);
      System.out.printf(decimal_format + " - " + decimal_format + " = " + decimal_format + "\n", first_number, second_number, result);
   }

   public static void multiply(Scanner scanner){
      //get the numbers and calculate result
      System.out.println("Zadej cinitel: ");
      double first_number = get_double_input(scanner);
      System.out.println("Zadej cinitel: ");
      double second_number = get_double_input(scanner);
      double result = first_number * second_number;
      //get amount of decimal places and print the result
      String decimal_format = get_decimal_input(scanner);
      System.out.printf(decimal_format + " * " + decimal_format + " = " + decimal_format + "\n", first_number, second_number, result);
   }

   public static void divide(Scanner scanner){
      //get the numbers result
      System.out.println("Zadej delenec: ");
      double first_number = get_double_input(scanner);
      System.out.println("Zadej delitel: ");
      double second_number = get_double_input(scanner);
      //check division by 0
      if(second_number == 0) {
         System.out.println("Pokus o deleni nulou!");
         System.exit(-1);
      }
      //calculate result
      double result = first_number / second_number;
      //get amount of decimal places and print the result
      String decimal_format = get_decimal_input(scanner);
      System.out.printf(decimal_format + " / " + decimal_format + " = " + decimal_format + "\n", first_number, second_number, result);
   }

   public static void start() {
      Scanner scanner = new Scanner(System.in);
      //definition of variables
      int operator = get_operation_input(scanner);
      //check for possible error
      if(operator != 1 && operator != 2 && operator != 3 && operator != 4) {
         System.out.println("Chybna volba!");
         System.exit(-1);
      } else if(operator == 1) {
         increment(scanner);
      } else if(operator == 2) {
         subtract(scanner);
      } else if(operator == 3) {
         multiply(scanner);
         //operator must be equal to 4
      } else {
         divide(scanner);
      }
   }
}
