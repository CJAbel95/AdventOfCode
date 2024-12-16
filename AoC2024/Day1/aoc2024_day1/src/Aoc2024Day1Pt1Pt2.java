import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Project: aoc2024_day1_pt1
 * File: Aoc2024Day1Pt1Pt2.java
 *
 * Description: The problem gives two lists of 1000 positive integers.
 *          Part 1 -- Sort the lists in ascending order and calculate the total
 *                  absolute distance between corresponding elements (i.e. sum(abs(list1(i) - list2(i)))).
 *          Part 2 -- Calculate the "similarity score" -- for each element in list 1,
 *                      -- calculate the product of element * (number of times it is matched in list 2)
 *                      -- Add this product to the total similarity score.
 *
 * Author: Christopher J. Abel
 * Revision History
 * ----------------
 * 12/10/2024 -- Original
 */

public class Aoc2024Day1Pt1Pt2 {
    public static void main(String[] args) {
        String filename = "input.txt";  // Name of input file containing both lists
        File file_in = new File(filename);
        ArrayList<Integer> list1 = new ArrayList<>();
        ArrayList<Integer> list2 = new ArrayList<>();

        // Read both lists from text file
        try {
            // Create scanner to read from file
            Scanner filescan = new Scanner(file_in);
            while (filescan.hasNextInt()) {
                list1.add(filescan.nextInt());
                list2.add(filescan.nextInt());
            }
            // Close file scanner
            filescan.close();
        }
        catch (FileNotFoundException ex1) {
            System.out.printf("File %s not found", filename);
            System.exit(0);
        }

        // Sort both lists
        list1.sort(null);
        list2.sort(null);

        /* ----------------------------
         *  Part 1 -- Calculate and report total distance between lists
         */
        // Calculate the absolute distance between corresponding elements
        // of the lists, and add to total.
        int total_dist = 0;
        for (int i = 0; i < list1.size(); i++) total_dist += Math.abs(list1.get(i) - list2.get(i));

        // Print total distance -- answer should be 1879048
        System.out.printf("The total distance = %d\n", total_dist);

        /* ----------------------------
         *  Part 2 -- Calculate and report similarity score
         *
         *  For each element in list1, find the indices of the first and last occurrence of the element
         *  in list2 (both will be reported as -1 if the element is not in list2).
         *
         *  Add element1 * (last_index - first_index + 1) to similarity score.  Answer should be 21024792.
         */
        long similarity_score = 0;
        for (int i = 0; i < list1.size(); i++) {
            int list1_curr = list1.get(i);
            int index1st = list2.indexOf(list1_curr);
            if (index1st > -1) {
                similarity_score += (long) list1_curr * (list2.lastIndexOf(list1_curr) - index1st + 1);
            }
        }
        System.out.printf("The similarity score = %d\n", similarity_score);
    }
}
