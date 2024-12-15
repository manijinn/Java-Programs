/* Usmaan Baig
   Dr. Steinberg
   COP3503 Fall 2022
   Programming Assignment 3
*/

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class GreedyChildren {

    private int [] children;
    private int [] candies;

    // happy = happy children amount
    // angry = angry children amount
    private int happy;
    private int angry;

    public GreedyChildren(int candyNum, int childNum, String childF, String candyF) throws Exception {

        this.children = new int[childNum];
        this.candies = new int[candyNum];

        read(childF, candyF);
    }


    public void read(String childF, String candyF) throws Exception {

        // Make scanner to scan files
        Scanner scan1 = new Scanner(new File(childF));
        int i = 0;

        // Input data into each of the arrays.
        while (scan1.hasNextInt()) {

            children[i++] = scan1.nextInt();
        }

        scan1.close();


        Scanner scan2 = new Scanner(new File(candyF));
        i = 0;

        while (scan2.hasNextInt()) {

            candies[i++] = scan2.nextInt();
        }

        scan2.close();

        // Sort the arrays, this is the setup before we use the greedy algorithim. By sorting,
        // we can reduce the amount of "skips" in the candy array and find a valid candy for a child quickly.
        Arrays.sort(children);
        Arrays.sort(candies);
    }


    public void greedyCandy() {

        // First we start a loop that goes through all of the children.
        for (int i = 0; i < children.length; i++) {

            if (happy == children.length) {

                break;
            }

            // If the sweetness of candy i exceeds child i's greediness, we give it to them. We also make sure to mark the
            // candy as given away, by setting it to -1. Then increment happy.
            if (children[i] <= candies[i] && candies[i] != -1) {

                happy++;
                candies[i] = -1;
            }

            // We assume if the previous if statement didn't work, we found an unsuitable candy. The kid is now angry, so we increment angry.
            else {

                angry++;

                // So we traverse the candies array (starting at the next index) to find a suitable candy. If found, decrement angry and increment happy. Then break the loop. 
                // This is the implemented greedy algorithim- we don't want to waste any candy, so once the loop is over we go back to the orginal index (the candy that was first rejected).
                // This is also why we sorted, the more we traverse the array, the higher the sweetness, and the higher chance to find a suitable candy.
                for (int j = i + 1; j < candies.length; j++) {

                    if (children[i] <= candies[j] && candies[j] != -1) {

                        happy++;
                        angry--;
                        candies[j] = -1;
                        break;
                    }
                }
            }
        }
    }


    public void display() {

        // Very simple output
        System.out.println("There are " + happy + " happy children.");
        System.out.println("There are " + angry + " angry children.");
    }
}