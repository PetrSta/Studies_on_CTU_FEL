package cz.cvut.fel.pjv;

import java.lang.Math;

public class Lab02 {
   public void start() {
      homework();
   }

   private void clean_array(double[] stored_numbers) {
      for(int i = 0; i < 10; i++) {
         stored_numbers[i] = 0;
      }
   }

   private double calculate_average(double[] stored_numbers, int stored_count) {
      //calculate the average of given numbers
      double sum_of_numbers = 0;
      for(int i = 0; i < stored_count; i++) {
         sum_of_numbers += stored_numbers[i];
      }
      //average
      return sum_of_numbers / stored_count;
   }

   private double calculate_deviation(double[] stored_numbers, double average, int stored_count) {
      //1st calculate variance and then calculate deviation using Variance
      double variance = 0;
      for(int i = 0; i < stored_count; i++) {
         variance += Math.pow((stored_numbers[i] - average), 2);
      }
      variance /= stored_count;
      //deviation
      return Math.sqrt(variance);
   }

   private void calculate_and_print_result(double[] stored_numbers, int stored_count) {
      double average = calculate_average(stored_numbers, stored_count);
      double deviation = calculate_deviation(stored_numbers, average, stored_count);
      //print result in given format
      System.out.printf("%2d %.3f %.3f\n", stored_count, average, deviation);
   }

   private void homework() {
      TextIO textTesting = new TextIO();
      //initialize variables to store information about numbers
      int stored_count = 0;
      double[] stored_numbers = new double[10];
      clean_array(stored_numbers);
      //initialize variables to store information about strings
      String line = textTesting.getLine();
      int line_count = 0;
      //go through input until getLine from TextIO returns "" (eof)
      while(!"".equals(line)) {
         //if for some bizarre reason array attempted to load more than 10 numbers -> end the program
         if(stored_count > 10 || stored_count < 0) {
            System.err.println("Error: Number array issue.");
            System.exit(-1);
         }
         line_count++;
         //check if a number was loaded from the line
         if(!TextIO.isInteger(line) && !TextIO.isFloat(line) && !TextIO.isDouble(line)) {
            System.err.printf("A number has not been parsed from line %d\n", line_count);
         } else {
            stored_numbers[stored_count] += Double.parseDouble(line);
            stored_count++;
         }
         //print average and deviation after 10 numbers were loaded
         if(stored_count == 10) {
            calculate_and_print_result(stored_numbers, stored_count);
            clean_array(stored_numbers);
            stored_count = 0;
         }
         //load next line
         line = textTesting.getLine();
      }
      //eof detected -> inform user and print statistics if more than 1 number is stored
      System.err.println("End of input detected!");
      if(stored_count > 1) {
         calculate_and_print_result(stored_numbers, stored_count);
      }
   }
}

