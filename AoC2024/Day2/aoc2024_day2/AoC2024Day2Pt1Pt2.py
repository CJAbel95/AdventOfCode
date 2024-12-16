#
#   AoC2024Day2Pt1Pt2.py -- Day 2 of 2024 Advent of Code
#
#   Description:
#
#   by Christopher Abel
#
#   Revision History
#   ----------------
#   12/11/2024 -- Original
#
# ----------------------------------------
import sys


def read_from_file(filename):
    try:
        with open(filename, 'r', newline='') as file_obj:
            linesList = [line.strip().split() for line in file_obj.readlines()]
            reportsList = [[int(level) for level in line] for line in linesList]
    except FileNotFoundError:
        print(f'Could not open {filename}')
        sys.exit(0)
    return reportsList


def report_is_safe(report):
    # Form list of delta values: deltas[i] = report[i + 1] - report[i]
    deltas = list(map(lambda x, y: x - y, report[1:len(report)], report[0:len(report) - 1]))
    #
    # Check whether deltas includes both positive and negative values
    is_safe = not (any([delta > 0 for delta in deltas]) and any([delta < 0 for delta in deltas]))
    #
    # Check whether any abs(delta[i]) is less than 1 or greater than 3
    is_safe = is_safe and not any([abs(delta) < 1 or abs(delta) > 3 for delta in deltas])
    #
    return 1 if is_safe else 0


def report_safe_single_bad(report):
    """
    Loop through elements of list report.  For each element, create a modified report that excludes that
    element and check whether the modified report is good, using report_is_safe().
    :param report: List of integers, each of which represents a "level."
    :return: 1 if the report is good after removing at most one bad level; 0 otherwise.
    """
    is_safe = 0
    i = 0
    while (i < len(report)) and (is_safe == 0):
        # Construct modified list, excluding one element
        report_mod = report[0:i] + report[i+1:len(report)]
        # Check whether the report is safe without the excluded element
        is_safe = report_is_safe(report_mod)
        # Go on to next element
        i += 1
    return is_safe


def main():
    filename = 'input.txt'
    reports = read_from_file(filename)

    # ------------------------------------
    #   Part 1 -- Number of safe reports. Should be 369 safe reports.
    #
    num_safe_reports = 0
    for report in reports:
        num_safe_reports += report_is_safe(report)
    print(f'Part 1 -- Number of Safe Reports = {num_safe_reports}')

    # ------------------------------------
    #   Part 2 -- Number of reports that are safe, excluding at most one bad level.
    #
    num_safe_reports_single_bad = 0
    for report in reports:
        num_safe_reports_single_bad += report_safe_single_bad(report)
    print(f'Part 2 -- Number of Safe Reports with at most One Bad Level = {num_safe_reports_single_bad}')


if __name__ == '__main__':
    main()