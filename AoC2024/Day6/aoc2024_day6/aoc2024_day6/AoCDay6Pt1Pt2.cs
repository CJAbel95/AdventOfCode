using System;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Security.Policy;
using System.Text;
using System.Threading.Tasks;

namespace aoc2024_day6 {
    internal class AoCDay6Pt1Pt2 {
        static void Main(string[] args) {
            string filein = "C:\\Users\\abelc\\Documents\\Software Development Work\\Advent of Code Projects\\AoC2024\\Day6\\aoc2024_day6\\input.txt";
            /*
             * Create LabGuard object using the provided map.
             * Call the printGuardInto() and printLabDim() methods to print out the initial position and direction of the guard,
             * and the dimensions of the lab (rows and columns).  Then call printLabMap() to print out the map prior to
             * any guard movement.
             */ 
            LabGuard game = new LabGuard(filein);
            game.printGuardInfo();
            game.printLabDim();
            Console.WriteLine("Original map");
            game.printLabMap();
            /*
             * Part 1 -- Number of spots covered by guard during patrol of original map.
             * Should be 5162.
             */
            game.playGame(0);
            Console.WriteLine("Lab map after patrol is complete");
            game.printLabMap();
            Console.WriteLine("\nPart 1 --\nNumber of X's = {0}", game.getNumX());
        }
    }

    internal class LabGuard {
        /*
         * Class that contains a map of the lab, and the methods to move the
         * guard through the lab.
         * 
         *      Attributes
         */
        private int fileRdStatus;
        // 2D List of Lists of char, containing a map of the lab
        private List<List<char>> labMap;
        private int numRows, numCols;   // Number of rows and columns in the lab
        private int guardRow, guardCol; // Current position (row, column) of guard
        private int numMoves; // How many guard movements have been made since the start of the guard's patrol.
        private GuardDir guardDir; // Direction (North, East, South, West) in which guard is currently pointing.

        public LabGuard(string filename) {
            /*
             * Constructor
             */
            labMap = new List<List<char>>();
            guardRow = -1;
            guardCol = -1;
            guardDir = GuardDir.Unknown;
            // Create map of lab and get guard initial position from text file.
            fileRdStatus = ReadLabFile(filename);
            numMoves = 0;
        }

        int ReadLabFile(string filename) {
            int status = 0;
            int line_num = 0;
            char[] guardChars = { '^', '>', 'v', '<' };
            string line;
            char[] lineChars;

            using (StreamReader fileinstr = new StreamReader(filename)) {
                while((line = fileinstr.ReadLine()) != null) {
                    lineChars = line.ToCharArray();
                    int guardIndex = Array.FindIndex(lineChars, lineChar => guardChars.Contains<char>(lineChar));
                    if (guardIndex >= 0) {
                        guardRow = line_num;
                        guardCol = guardIndex;
                        int guardCharIndex = Array.IndexOf(guardChars, lineChars[guardIndex]);
                        switch (guardCharIndex) {
                            case 0:
                                guardDir = GuardDir.North; break;
                            case 1:
                                guardDir = GuardDir.East; break;
                            case 2:
                                guardDir = GuardDir.South; break;
                            case 3:
                                guardDir = GuardDir.West; break;
                            default:
                                guardDir = GuardDir.Unknown;
                                break;
                        }
                    }
                    labMap.Add(lineChars.ToList());
                    line_num++;
                }
            }
            numRows = line_num;
            numCols = labMap.ElementAt(0).Count;
            return status;
        }

        public int[] getGuardPos() { return new int[] {guardRow, guardCol}; }

        public GuardDir getGuardDir() { return guardDir; }

        public List<List<char>> getLabMap() { return labMap; }

        public int[] getLabDim() { return new int[] {numRows, numCols}; }

        public int getNumX() {
            int numX = 0;
            char[] xchars = { 'X', '^', '>', 'v', '<' };
            foreach (List<char> labRow in labMap) {
                foreach (char labpos in labRow) {
                    if (xchars.Contains(labpos)) numX++;
                }
            }
            return numX;
        }

        public void playGame(int numM=-1) {
            while (!guardOutOfBounds() && (numM <= 0 || numMoves < numM)) {
                makeMove();
                numMoves++;
            }
            Console.WriteLine("Patrol finished after {0} moves", numMoves);
        }

        public Boolean nextObstruction() {
            Boolean path_obstructed = false;
            switch (guardDir) {
                case GuardDir.North:
                    if (guardRow > 0 && labMap.ElementAt(guardRow - 1).ElementAt(guardCol) == '#') path_obstructed=true; 
                    break;
                case GuardDir.East:
                    if (guardCol < (numCols - 1) && labMap.ElementAt(guardRow).ElementAt(guardCol + 1) == '#') path_obstructed = true;
                    break;
                case GuardDir.South:
                    if (guardRow < (numRows - 1) && labMap.ElementAt(guardRow + 1).ElementAt(guardCol) == '#') path_obstructed = true;
                    break;
                case GuardDir.West:
                    if (guardCol > 0 && labMap.ElementAt(guardRow).ElementAt(guardCol - 1) == '#') path_obstructed = true;
                    break;
                default:
                    break;
            }
            return path_obstructed;
        }

        public void makeMove() {
            if (nextObstruction()) {
                rotateDir();
            }
            else {
                labMap[guardRow][guardCol] = 'X';
                switch (guardDir) {
                    case GuardDir.North: 
                        guardRow--;
                        if (!guardOutOfBounds()) labMap[guardRow][guardCol] = '^';
                        break;
                    case GuardDir.East: 
                        guardCol++;
                        if (!guardOutOfBounds()) labMap[guardRow][guardCol] = '>';
                        break;
                    case GuardDir.South: 
                        guardRow++;
                        if (!guardOutOfBounds()) labMap[guardRow][guardCol] = 'v';
                        break;
                    case GuardDir.West: 
                        guardCol--;
                        if (!guardOutOfBounds()) labMap[guardRow][guardCol] = '<';
                        break;
                    default: break;
                }
            }
        }

        public void printLabMap() {
            foreach (List<char> row in  labMap) {
                foreach (char elem in row) {
                    Console.Write(elem);
                }
                Console.WriteLine();
            }
            Console.WriteLine();
        }

        public void rotateDir() {
            switch (guardDir) {
                case GuardDir.North:
                    guardDir = GuardDir.East;
                    labMap[guardRow][guardCol] = '>';
                    break;
                case GuardDir.East:
                    guardDir = GuardDir.South;
                    labMap[guardRow][guardCol] = 'v';
                    break;
                case GuardDir.South:
                    guardDir = GuardDir.West;
                    labMap[guardRow][guardCol] = '<';
                    break;
                case GuardDir.West:
                    guardDir = GuardDir.North;
                    labMap[guardRow][guardCol] = '^';
                    break;
                default: break;
            }
        }

        public void printGuardInfo() {
            System.Console.WriteLine($"Guard is currently at row={guardRow}, col={guardCol}\tFacing {guardDir}");
        }

        public void printLabDim() { Console.WriteLine("The lab has {0} rows and {1} columns", numCols, numRows); }

        public Boolean guardOutOfBounds() {
            return (guardRow < 0 || guardRow > (numRows - 1) || guardCol < 0 || guardCol > (numCols - 1));
        }

    }

    internal enum GuardDir { North, East, South, West, Unknown }
}
