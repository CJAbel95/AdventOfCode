import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Project: aoc2024_day5
 * File: AoC2024Day5Pt1Pt2.java
 * <p>
 * Description: 2024 Advent of Code -- Day 5, Parts 1 and 2
 * <p>
 * Author: Christopher J. Abel
 * Revision History
 * ----------------
 * 12/15/2024 -- Original
 */

public class AoC2024Day5Pt1Pt2 {
    public static void main(String[] args) {
        String filename = "input.txt";
        File file_in = new File(filename);
        HashMap<Integer, ArrayList<Integer>> rules_map = new HashMap<>();
        ArrayList<int[]> update_list = new ArrayList<>();

        // Read input file; populate rules map and list of updates from file
        read_file(file_in, rules_map, update_list);

        /* ************************************************************
         *      Part 1 -- Sum of middle page numbers of good updates
         *
         * For each update (array of numbers containing pages to be updated) in update_list:
         *  1. Increment the num_updates counter.
         *  2. Check to see if the update is good by passing update and rules map
         *      to check_update().
         *  3. If the rule is good, increment num_good_updates and add the middle page in
         *      the array of page numbers in the update to sum_middle_pg.
         *
         * Note that the code assumes that the length of the update array is odd; thus,
         * the "middle" page in the array has index update.length/2.
         */
        int num_updates = 0;
        int num_good_updates = 0;
        int sum_middle_pg = 0;
        for(int[] update : update_list) {
            num_updates++;
            if (check_update(update, rules_map)) {
                num_good_updates++;
                sum_middle_pg += update[update.length/2];
            }
        }
        System.out.println("Part 1");
        System.out.printf("\tTotal number of updates = %d\n\tNumber of good updates = %d\n", num_updates, num_good_updates);
        System.out.printf("\tSum of middle page numbers of good updates = %d\n", sum_middle_pg);
    }

    public static void read_file(File file_in, HashMap<Integer, ArrayList<Integer>> rules, ArrayList<int[]> updates) {
        try {
            Scanner filescan = new Scanner(file_in);
            boolean rules_complete = false;
            while (filescan.hasNextLine()) {
                // Read the next line from the file into a string
                String line = filescan.nextLine();
                // If the line is empty, then we have finished reading in the rules --> rules_complete = true
                if (line.isEmpty()) rules_complete = true;
                // If we are not finished with the rules (rules_complete = false), then parse the line to get the rule
                else if (!rules_complete) {
                    // The line will be of the form "key|value".  Split the line, forming ["key", "value"]
                    String[] nums = line.split("\\|");
                    // Parse the "key" and "value" strings into integers
                    int key = Integer.parseInt(nums[0]);
                    int value = Integer.parseInt(nums[1]);
                    // Create an entry in the rule map for key, if it does not already exist
                    if (!rules.containsKey(key)) rules.put(key, new ArrayList<Integer>());
                    // Add value to the ArrayList associated with key
                    rules.get(key).add(value);
                }
                else {
                    String[] nums = line.split(",");
                    int[] update = Arrays.stream(nums).mapToInt(Integer::parseInt).toArray();
                    updates.add(update);
                }
            }
            filescan.close();
        }
        catch (FileNotFoundException ex1) {
            System.out.printf("File %s not found", file_in.toString());
            System.exit(0);
        }
    }

    public static boolean check_update(int[] update, HashMap<Integer, ArrayList<Integer>> rules) {
        int num_pages = update.length;
        boolean update_good = true;
        int page_num = 1;
        ArrayList<Integer> rule_list;
        while(update_good && page_num < num_pages) {
            if (rules.containsKey(update[page_num])) {
                rule_list = rules.get(update[page_num]);
                for (int i = 0; i < page_num; i++) {
                    update_good = update_good && !rule_list.contains(update[i]);
                }
            }
            page_num++;
        }
        return update_good;
    }
}
