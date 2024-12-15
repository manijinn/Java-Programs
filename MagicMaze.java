/** Usmaan Baig
 *  COP3503 Computer Science 2
 *  Programming Assignment 2
 *  Fall 2022
 */

import java.io.File;
import java.util.Scanner;

class MagicMaze {

    char [][] maze;

    // This array will keep track of all the spaces we've been through. It is the backtracking strategy I used.
    int [][] used;

    String name;

    // The rows and cols are used for navigating the maze[][] array and used[][] array.
    int rows;

    int cols;
    
    public MagicMaze(String name, int rows, int cols) throws Exception {

        this.name = name;

        this.rows = rows;

        this.cols = cols;

        maze = new char [this.rows][this.cols];

        used = new int [this.rows][this.cols];

        Scan();
    }

    public void Scan() throws Exception {

        // Scan contents of maze files
        Scanner scanIn = new Scanner(new File(name));
        String splitrow;

        int i = 0;
        int j = 0;

        // Scan the file. Splitrow is used to get a (string) line from the file, then I used charAt() to "pick apart" the string.
        while (scanIn.hasNextLine()) {

            splitrow = scanIn.nextLine();

            while (j < cols) {

                maze[i][j] = splitrow.charAt(j);

                j++;
            }

            i++;
            j = 0;

        }

        scanIn.close();
    }

    public boolean solveMagicMaze() {

        return solveMagicMazeR(rows - 1, 0);
        
    }

    // i is the y axis, or rows. j is the x axis, or cols.
    public boolean solveMagicMazeR(int i, int j) {

        // Checks to see if we are within bounds. Return false if we are on an invalid square.
        if (!boundsCheck(i, j)) {
            
            return false;
            
        }
        
        // If square contains 'X', success! We made it to the exit!
        if (maze[i][j] == 'X') {

            return true;
        }
        // This checks to see if we are on a teleport pad.
        if (isTeleportPad(i, j)) {
            
            // We send i and j, and an array containing the coordinates of the other teleport pad are returned.
            int [] temp = teleport(i, j);

            i = temp[0];
            j = temp[1];
           
        }

        // Backtracking, we mark current squares as "used", meaning we have been here and we do not need to go here again. boundsCheck checks
        // to see if the current square we are on is used or not, as well if it's a wall or out of bounds.
        // We also check to see if the current square is a teleporter or not, if it is, we skip marking the teleporter as used. Marking a teleporter as "used"
        // could trap us if we have used the teleporter before and recursed back, such as in Maze 3.
        if (!isTeleportPad(i, j)) {

            used[i][j] = 1;
        }

        // Recursion steps. If a method call returned true then we have our answer, and we skip all future recursions by returning true repeatedly.

        // Going up
        if (solveMagicMazeR(i - 1, j)) {
            return true;
        }
        
        // Going right
        if (solveMagicMazeR(i, j + 1)) {
            return true;
        }
        
        // Going down
        if (solveMagicMazeR(i + 1, j)) {
            return true;
        }

        // Going left
        if (solveMagicMazeR(i, j - 1)) {
            return true;
        }

        // Return false if we otherwise don't find the X
        return false;

    }

    // This method manages the teleportation
    public int [] teleport(int i, int j) {

        // This is where we will store the other side of the teleporter (its coordinates). h and k are the coordinate of the opposite end of the teleporter.
        int [] temp = new int[2];

        // Scan through the maze.
        for (int h = 0; h < rows; h++) {
            for (int k = 0; k < cols; k++) {

                // If we find a spot that has the same teleporter number as the one we are currently on, we put the h and k into the array.
                if (maze[i][j] == maze[h][k]) {

                    // If the coordinates are the same, we skip this square, because this is the square we are already on. 
                    if (i == h && k == j) {
                        
                        continue;

                    }

                    temp[0] = h;
                    temp[1] = k;

                } 
            }
        }

        return temp;

    }

    // Checks to see if current square is a teleport pad.
    public boolean isTeleportPad(int i, int j) {

        if (maze[i][j] != '@' && maze[i][j] != '*' && maze[i][j] != 'X') {

            return true;
        }

        return false;
    }

    // Conditions to verify if we are within bounds.
    public boolean boundsCheck(int i, int j) {

        if (i <= -1) {
            return false;
        }

        if (i >= rows) {
            return false;
        }

        if (j >= cols) {
            return false;
        }

        if (j <= -1) {
            return false;
        }

        if (maze[i][j] == '@') {
            return false;
        }

        // Backtracking step, we see if the square we are on has already been visited, if true, we return false, declaring this path to be bad.
        if (used[i][j] == 1) {
            return false;
        }

        return true;
    } 
}

