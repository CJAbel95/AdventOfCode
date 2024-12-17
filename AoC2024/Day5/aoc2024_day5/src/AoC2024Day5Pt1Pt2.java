import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
         *      Should be 5087.
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
        System.out.printf("\tTotal number of rules = %d\n", rules_map.size());
        System.out.printf("\tTotal number of updates = %d\n\tNumber of good updates = %d\n", num_updates, num_good_updates);
        System.out.printf("\tSum of middle page numbers of good updates = %d\n", sum_middle_pg);

        /* ************************************************************
         *      Part 2 -- Sum of middle page numbers of "bad" updates, after sorting
         *                  Should be 4971.
         *      For each update in the update_list, if it is bad (check_update(update, rules_map) == false), then
         *      create a new update with the page numbers sorted in proper order.  Accumulate the middle page numbers
         *      in the sorted updates to sum_middle_pg.
         */
        int num_bad_updates = 0;
        sum_middle_pg = 0;
        int[] update_sorted;
        for (int[] update : update_list) {
            if (!check_update(update, rules_map)) {
                num_bad_updates++;
                update_sorted = sort_update(update, rules_map);
                sum_middle_pg += update_sorted[update_sorted.length / 2];
            }
        }
        System.out.println("\nPart 2");
        System.out.printf("\tTotal number of originally bad updates = %d\n", num_bad_updates);
        System.out.printf("\tSum of middle page numbers of sorted updates = %d\n", sum_middle_pg);
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
        /**
         * Method to check whether an update (array of integers, each of which represents a page number) is in "good"
         * order.  Beginning with the second page in the update array, compare all earlier page numbers in the array
         * with the pages in the value ArrayList selected by the current page in the rules map.
         *
         * For example, for an update such as [43, 15, ..., 71, ...], if the rules map includes
         * {71, [..., 15, ...]}, then this is not a good update, since page 15 should be after page 71.
         *
         * @param update    An int array containing an ordered list of page numbers in an update.
         * @param rules     A HashMap with an Integer key, and an ArrayList of Integers as value. The key represents
         *                  a page number, and the value represents a list of page numbers which must come after
         *                  the key page number in a "good" update.
         * @return update_good  A boolean that is true if the update is good, and false if not good.
         */
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

    public static int[] sort_update(int[] update, HashMap<Integer, ArrayList<Integer>> rules) {
        /**
         * Sort pages in update array by the following algorithm:
         *  1. Copy the pages in update to a HashSet -- page_set.
         *  2. Select one page at a time from page_set, and check every page in page_set to see if it is
         *      an element of the rule list corresponding to that page. There will be at least one page
         *      in page_set which has none of the other pages in its page list.
         *  3. Select the first page in page_set for which none of the other pages are in its page list (i.e.
         *      none of the other pages follow it). Add this page to the last open position in the sorted update
         *      array, and delete it from page_set.
         *  4. Loop back to step 2 until page_set is empty.
         *
         * @param update    Array of integers containing page numbers in an update.
         * @param rules     Map of page numbers (keys) to array lists of page numbers that must follow (be printed
         *                  later than) the key.
         * @return update_sorted    Array of page numbers in update, sorted in the order that they must be printed.
         */
        HashSet<Integer> page_set = (HashSet<Integer>) IntStream.of(update).boxed().collect(Collectors.toSet());
        int num_pages = update.length;
        int[] update_sorted = new int[num_pages];
        int i = num_pages - 1;
        while (page_set.size() > 0) {
            for (int page : page_set) {
                if (is_last(page, page_set, rules)) {
                    update_sorted[i--] = page;
                    page_set.remove((Integer) page);
                    break;
                }
            }
        }
        return update_sorted;
        }

    public static boolean is_last(int page, HashSet<Integer> page_set, HashMap<Integer, ArrayList<Integer>> rules) {
        /**
         * Check whether a given page number is behind every other page remaining in a HashSet of page numbers.
         * Treat page as the key to the rules map, and get the rule list of integers selected by this key.
         * This list contains that page numbers that must follow the key page.
         *
         * Compare every element of HashSet page_set with this list.  If none of the page numbers in page_set is
         * an element of the rule list, then the key page would follow every other page in page_set; return true
         * in this case.
         *
         * @param page  Key page number; every other page in the page_set is compared with the numbers in the
         *              rules map with this page as a key.
         * @param page_set  HashSet containing all page numbers in the current update
         * @param rules HashMap containing page numbers (Integer) as keys, and ArrayList<Integer> lists of page numbers
         *              that must follow the key page.
         * @return page_is_last true if none of the pages in page_set are elements of the rules list corresponding
         *              to the key page.
         */
        boolean page_is_last = true;
        if (rules.containsKey(page)) {
            for (int page_val : page_set) {
                if (rules.get(page).contains(page_val)) page_is_last = false;
            }
        }
        return page_is_last;
    }
}
