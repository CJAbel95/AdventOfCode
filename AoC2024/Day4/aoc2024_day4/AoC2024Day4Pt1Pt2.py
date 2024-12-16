#
# AoC2024Day4Pt1Pt2.py
#
# Description: Day 4, parts 1 and 2, of the 2024 Advent of Code
#
# by Christopher J. Abel
#
# Revision History
# ----------------
# 12/14/2024 -- Original
#


def find_xmas(array, i, j):
    """
    With an 'X' character present at array[i][j], look along each of the 8 possible directions (left, upper left, up,
    upper right, right, lower right, down, lower left) starting with this character to see if 'X' 'M' 'A' 'S' is
    present.  For each direction that XMAS is found, increment xmas_count, and return this total count.
    :param array: Two-dimensional array of characters.
    :param i: Row of array in which 'X' is found
    :param j: Column of array in which 'X' is found
    :return: xmas_count -- number of instances of 'XMAS' based at array[i][j]
    """
    nrows = len(array)
    ncols = len(array[0])
    xmas_count = 0
    # Look the left of the current X character
    if j >= 3 and (array[i][j] + array[i][j-1] + array[i][j-2] + array[i][j-3] == 'XMAS'):
        xmas_count += 1
    # Look along upper left diagonal
    if j >= 3 and i >= 3 and (array[i][j] + array[i-1][j-1] + array[i-2][j-2] + array[i-3][j-3] == 'XMAS'):
        xmas_count += 1
    # Look straight up
    if i >= 3 and (array[i][j] + array[i - 1][j] + array[i - 2][j] + array[i - 3][j] == 'XMAS'):
        xmas_count += 1
    # Look along upper right diagonal
    if j <= ncols - 4 and i >= 3 and (array[i][j] + array[i-1][j+1] + array[i-2][j+2] + array[i-3][j+3] == 'XMAS'):
        xmas_count += 1
    # Look to the right
    if j <= ncols - 4 and (array[i][j] + array[i][j+1] + array[i][j+2] + array[i][j+3] == 'XMAS'):
        xmas_count += 1
    # Look along the lower right diagonal
    if j <= ncols-4 and i <= nrows-4 and (array[i][j] + array[i+1][j+1] + array[i+2][j+2] + array[i+3][j+3] == 'XMAS'):
        xmas_count += 1
    # Look straight down
    if i <= nrows-4 and (array[i][j] + array[i+1][j] + array[i+2][j] + array[i+3][j] == 'XMAS'):
        xmas_count += 1
    # Look along the lower left diagonal
    if j >= 3 and i <= nrows-4 and (array[i][j] + array[i+1][j-1] + array[i+2][j-2] + array[i+3][j-3] == 'XMAS'):
        xmas_count += 1
    #
    return xmas_count


def find_cross_mas(array, i, j):
    a_center = False
    mas_left_right = False
    mas_right_left = False
    if array[i + 1][j + 1] == 'A': a_center = True
    if ((array[i][j] == 'M' and array[i + 2][j + 2] == 'S')
            or (array[i][j] == 'S' and array[i + 2][j + 2] == 'M')): mas_left_right = True
    if ((array[i][j+2] == 'M' and array[i + 2][j] == 'S')
            or (array[i][j+2] == 'S' and array[i + 2][j] == 'M')): mas_right_left = True
    return a_center and mas_left_right and mas_right_left


def main():
    filename = 'input.txt'

    with open(filename, 'r', newline='') as file_in:
        lines = file_in.readlines()
    char_array = []
    for line in lines:
        # For each line read from the file, strip off trailing whitespace, and convert to list of characters.
        # Append list for each line as a row in char_array.
        char_array.append(list(line.strip()))

    # ----------------------------------
    #   Part 1 -- Find the number of times that 'XMAS' appears in the puzzle.
    #               Should be 2401.
    #
    nrows = len(char_array)
    ncols = len(char_array[0])
    xmas_count = 0
    for row in range(0, nrows):
        for col in range(0, ncols):
            if char_array[row][col] == 'X':
                xmas_count += find_xmas(char_array, row, col)
    print(f'Part 1 -- XMAS appears {xmas_count} times')

    # ----------------------------------
    #   Part 2 -- Find the number of times that 'MAS' and 'MAS' appear in a cross orientation
    #       Should be 1822.
    #
    cross_mas_count = 0
    for row in range(0, nrows - 2):
        for col in range(0, ncols - 2):
            if find_cross_mas(char_array, row, col): cross_mas_count += 1
    print(f'Part 2 -- Cross-MAS appears {cross_mas_count} times')


if __name__ == '__main__':
    main()