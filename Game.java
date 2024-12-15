/* Usmaan Baig
   Dr. Steinberg
   COP3503 Fall 2022
   Programming Assignment 1
*/

import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Game {

    private int board [][];
    private char moves [];

    // Keep size for future use
    private int size;

    public Game(int size, String filename) throws IOException {

        this.size = size;

        board = setBoard(board, size);
        
        readMoves(filename);
        
    }
    
    public void readMoves(String filename) throws IOException {
        
        int i = 0;
        moves = new char[15];

        // Scan contents of move<>.txt files
        Scanner scanIn = new Scanner(new File(filename));

        while (scanIn.hasNextLine()) {

            moves[i] = scanIn.nextLine().charAt(0);

            i++;
        }
    }

    public int play(int winner) {

        // x and y are the coordinates of the board, with (0,0) being the top-right of the board and (7,7) being th bottom right.
        // i tracks the index for moves
        int y = 0;
        int x = 0;
        int i = 0;

        // If testing for P2 victory, reset board, get random p1 moves
        if (winner == 2) {

            board = setBoard(board, size);
            moves = getMyMoves(moves);
        }

        

        // This while loop plays each turn between P1 and P2
        while (board[7][7] != 1 && board[7][7] != 2) {

            // Time-saving scenarios, if piece is on bottom or right edge, simply continue down or right (respectively)
            // Set board to 1 or 2 next, depending on who reached the edge beforehand.
            if (x == 7) {

                if (board[y][x] == 2) {

                    y++;

                    board[y][x] = 1;

                    continue;
                }

                else {

                    y++; 

                    board[y][x] = 2;

                    continue;
                }
            }

            else if (y == 7) {
                
                if (board[y][x] == 2) {

                    x++;

                    board[y][x] = 1;

                    continue;
                }

                else {

                    x++;  

                    board[y][x] = 2;

                    continue;
                }
            }

            // If winner == 1, we are testing if P1 wins.
            if (winner == 1) {

                // Smart scenario, if we are top left of winning square, just go diagonal
                if (x == 6 && y == 6 && board[6][6] == 2) {
                    
                    x++;
                    y++;
                }

                // Smart scenario, if P2 entered square (5, 5) and P2's next moves is "d", P1 avoids defeat by going "b".
                else if (moves[i] == 'd' && board[5][5] == 2) {
                    
                    y++;
                }

                // if P2 goes "d", we go "d" as well
                else if (moves[i] == 'd') {

                    y++;
                    x++;
                }

                // Reverse P2's moves to keep piece on diagonal path to bottom right. P1'S move preemptively counters P2's
                else if (moves[i] == 'r') {

                    y++;
                }

                else {

                    x++;
                }

                // Make current square equal to one, indicating P1 moved onto it.
                board[y][x] = 1;
    
                // Same as above. These are P2's moves. The extra if statment here is to ensure that Player 2 doesn't move out of bounds.
                if (y != 7 && x != 7) {

                    if (moves[i] == 'd') {

                        y++;
                        x++;
                    }

                    else if (moves[i] == 'r') {

                        x++;
                    }

                    else {

                        y++;
                    }

                    // Make current square equal to 2, meaning that P2 moved onto it.
                    board[y][x] = 2;
                }
            }
            
            // If P2 must wwin
            if (winner == 2) {
                
                // P1'S moves.
                if (moves[i] == 'd') {

                    y++;
                    x++;
                }

                else if (moves[i] == 'r') {

                    x++;
                }

                else {

                    y++;
                }

                board[y][x] = 1;

                // P2's moves
                if (y != 7 && x != 7) {

                    if (x == 6 && y == 6 && board[6][6] == 1) {
                        
                        x++;
                        y++;
                    }

                    // Smart Scenario, If P1 entered square (5, 5), that is bad for P2. So we offset P1's route. We "b" or "r" depending on P1's next move.
                    else if (board[5][5] == 1 && moves[i + 1] != 'b') {

                        y++;
                    }

                    else if (board[5][5] == 1 && moves[i + 1] != 'r') {

                        x++;
                    }
    
                    // Smart Scenario, different from above in that this is if P2 was entered square (5, 5). This block accounts for if P1 goes "b" or "r".
                    else if (moves[i] == 'r' && x == 6) {
                        
                        x++;
                    }

                    else if (moves[i] == 'b' && y == 6) {
    
                        y++;
                    }

                    // Regular moves, counteract P1's moves to get piece back onto diagonal route top left to bottom right. Counter the offset.
                    else if (moves[i] == 'd') {
    
                        y++;
                        x++;
                    }
    
                    else if (moves[i] == 'r') {
    
                        y++;
                    }
    
                    else {
    
                        x++;
                    }

                    board[y][x] = 2;
                }
            }

            i++;
        }

        // Winning conditions
        if (board[7][7] == 1) {

            return 1;
        }

        return 2;
    }

    // Randomly generate P1's moves if testing P2's strategy.
    public static char[] getMyMoves(char moves[]) {

        moves = new char[15];

        int[] temp;

        Random rand = new Random();

        temp = rand.ints(15, 0, 3).toArray();

        for (int i = 0; i < 15; i++) {

            if (temp[i] == 0) {
                moves[i] = 'd';
            }

            else if (temp[i] == 1) {
                moves[i] = 'b';
            }

            else {
                moves[i] = 'r';
            }
        }

        return moves;
    }

    // Set and reset board.
    public static int[][] setBoard(int board[][], int size) {

        board = new int[size][size];
        return board;
    }
}
