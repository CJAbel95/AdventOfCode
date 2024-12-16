#
# AoC2024Day3Pt1Pt2.py -- Advent of Code 2024, Day 3, Parts 1 and 2
#
# Description:
#
# by Christopher J. Abel
#
# Revision History
# ----------------
# 12/14/2024 -- Original
#

import re

def main():
    print('Advent of Code 2024 -- Day 3')
    filename = 'input.txt'
    # Read all lines of input file into a list.
    with open(filename, 'r', newline='') as file_in:
        lines = file_in.readlines()

    # ----------------------------
    #   Part 1 -- Sum of products of all multiplication operations.
    #       Should be 163931492.
    #
    sum_mults = 0
    mult_op_list = []
    #
    # For each line from the file, use a regular expression to search
    # for patterns of the form 'mult(number, number)'.  Append the list
    # of all substrings that match this pattern to the list mult_op_list.
    for line in lines:
        mult_op_list += re.findall(r'mul\([0-9]+,[0-9]+\)', line)
    #
    # Create a new list -- mult_pairs -- containing
    # lists of number pairs by stripping 'mult()' from each element
    # of mult_op_list, and then splitting the resulting string by the comma.
    # For example, a string such as 'mult(11,8)' will be transformed into
    # ['11', '8'].
    mult_pairs = [elem.strip('mul()').split(',') for elem in mult_op_list]
    #
    # For each number pair, convert each number to an integer, and multiply
    # both members of the pair together. Accumulate the products in sum_mults.
    for pair in mult_pairs:
        sum_mults += int(pair[0]) * int(pair[1])
    print(f'Part 1 -- Sum of the results of all multiplications = {sum_mults}')

    # ----------------------------
    #   Part 2 -- Sum of all products; accumulation is disabled by a "don't()"
    #           function, and re-enabled by a "do()".  Should be 76911921.
    #
    mul_pattern = r'mul\([0-9]+,[0-9]+\)'
    dont_pattern = r'don\'t\(\)'
    do_pattern = r'do\(\)'
    # Search line for three patterns: "mul(number, number)", "don't()", and "do()"
    pattern = f'({mul_pattern})|({dont_pattern})|({do_pattern})'
    cond_mult_op_list = []
    # For each line from the file, use a regular expression to search
    # for any of the three patterns.  Append the list
    # of all substrings that match any of these patterns to the list cond_mult_op_list.
    #
    # Note that for each match, the findall() function returns a tuple of three strings,
    # with two of the strings empty.
    for line in lines:
        cond_mult_op_list += [match[0] or match[1] or match[2] for match in re.findall(pattern, line)]
    #
    # Begin with accumulation enabled.
    # For each operation in the operation list,
    #   -- disable accumulation (accum_product = False) if the operation is "don't()"
    #   -- enable accumulation (accum_product = True) if the operation is "do()"
    #   -- accummulate the product of numbers if the operation is "mul()" and if accum_product = True.
    cond_sum_mults = 0
    accum_product = True
    for operation in cond_mult_op_list:
        if operation == "don't()":
            accum_product = False
        elif operation == 'do()':
            accum_product = True
        else:
            mult_pair = operation.strip('mul()').split(',')
            if accum_product:
                cond_sum_mults += int(mult_pair[0]) * int(mult_pair[1])
    print(f'Part 2 -- Sum of the results of multiplications with conditions = {cond_sum_mults}')


if __name__ == '__main__':
    main()