/* Name: Usmaan Baig
 * Dr. Andrew Steinberg
 * COP3503 Fall 2022
 * Programming Assignment 4
 */

import java.lang.StringBuilder;


public class LCS {

    private String line1;
    private String line2;

    private StringBuilder sequence;

    // Table is used for calculating the length while direction is ued for printing the LCS
    private int table [][];
    private char direction [][];

    public LCS(String line1, String line2) {

        this.line1 = line1;
        this.line2 = line2;

        int x = line1.length();
        int y = line2.length();

        // Increase the size of the 2d array's by 1. We start the arrays on i = 1 and j = 1 however. We leave the first row and column untounched.
        table = new int[x + 1][y + 1];
        direction = new char[x + 1][y + 1];

        sequence = new StringBuilder(x + y);
    }


    public void lcsDynamicSol() {

        int x = line1.length();
        int y = line2.length();


        // We start on i = 1 and j = 1. This is beacuse we pull from the previous index, and if i = 0 or j = 0, we will be pulling from an
        // index that doesnt exist, resulting in an error (index out of bounds exception). 
        for (int i = 1; i <= x; i++) {

            // We record length of sequence into "table" and we record the direction from where we pulled the previous length into "direction."
            for (int j = 1; j <= y; j++) {

                // Case if symbols match.
                if (line1.charAt(i - 1) == line2.charAt(j - 1)) {

                    table[i][j] = table[i - 1][j - 1] + 1;

                    direction[i][j] = 'd';
                }

                // Case if above index has larger or same count.
                else if (table[i - 1][j] >= table[i][j - 1]) {

                    table[i][j] = table[i - 1][j];

                    direction[i][j] = 'u';
                }

                // Case if left index has larger or same count.
                else {

                    table[i][j] = table[i][j - 1];

                    direction[i][j] = 'l';
                }
            }
        }

        
        // This loop goes over the direction array. Starting from the bottom-right, we make our way to the top-left. (x and y start at 7).
        while (x > 0 || y > 0) {
           
            // Break if x or y = 0.
            if (x == 0 || y == 0) {

                break;
            }

            // We go diagonal.
            if (direction[x][y] == 'd') {

                sequence.append(line1.charAt(x - 1));

                x--;
                y--;
            }
          
            // We go up.
            else if (direction[x][y] == 'u') {

                x--;
            }

            // We go left, 'l'.
            else {

                y--;
            }
        }
    
        // Reverse the sequence since we went in reverse order, going to the top-left.
        sequence.reverse();
    }


    // Send to LCSRunner.
    public String getLCS() { 
       
        return sequence.toString();
    }
}