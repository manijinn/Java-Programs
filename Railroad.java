/* Usmaan Baig
   Dr. Steinberg
   COP3503 Fall 2022
   Programming Assignment 5
*/

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.lang.StringBuilder;


public class Railroad {

    //By design, I left the variables and fields package private.

    static Integer tracks;
    // Stores the edges to be later sorted

    static ArrayList<Edge> sortedEdges;
    // parent and rank for the disjoint set

    static Integer [] parent;
    static Integer[] rank;

    // Arraylist to store the verticies in
    static ArrayList<String> verticies;

    // Hashmaps to store the string and their associated key. 
    // The first hashmap returns a STRING when a key is given, The 2nd hashmap returns a KEY if a string is given
    static HashMap<Integer, String> fetchString;
    static HashMap<String, Integer> fetchKey;

    public Railroad(int tracks, String file) throws Exception {

        Railroad.tracks = tracks;

        sortedEdges = new ArrayList<>(tracks);

        verticies = new ArrayList<String>(tracks);

        fetchString = new HashMap<Integer, String>(tracks);

        fetchKey = new HashMap<String, Integer>(tracks);

        Scan(file);
    }

    // This method is the Kruskal algorithim
    String buildRailroad() {

        Integer u;
        Integer v;
        int total = 0;

        StringBuilder output = new StringBuilder();
        parent = new Integer [fetchString.size()];
        rank = new Integer [fetchString.size()];

        Arrays.fill(parent, 0);
        Arrays.fill(rank, 0);
        
        verticies.trimToSize();

        // The makeSet part of Kruskals
        parent = disjointSet.makeSet(verticies, parent);

        // Sort the sortedEdges from least weight to greatest weight, the sorting part of Kruskals
        weightSort ws = new weightSort();
        Collections.sort(sortedEdges, ws);

        
        for (int k = 0; k < tracks; k++) {

            u = fetchKey.get(sortedEdges.get(k).start);

            v = fetchKey.get(sortedEdges.get(k).end);
          
            // The findSet part of Kruskals, and below it, the union part
            if (disjointSet.findSet(u, parent) != disjointSet.findSet(v, parent)) {

                disjointSet.union(u, v, parent, rank);

                // Here we append the edges to the StringBuilder "output." If u comes before v lexicographically, we print u before v. Otherwise we print v first.
                // We also add the weights to get the total weight
                if (fetchString.get(u).compareTo(fetchString.get(v)) < 0) {

                    output.append(fetchString.get(u) + "---" + fetchString.get(v) + "\t" + "$" + sortedEdges.get(k).weight + "\n");

                    total += sortedEdges.get(k).weight;

                }

                else {

                    output.append(fetchString.get(v) + "---" + fetchString.get(u) + "\t" + "$" + sortedEdges.get(k).weight + "\n");

                    total += sortedEdges.get(k).weight;

                }
            }
        }
        
        output.append("The cost of the railroad is $" + total + ".");
        
        return output.toString();

    }
       

    static void Scan(String file) throws Exception {

        String tempStart;
        String tempEnd;
        Integer tempWeight;

        Integer position = 0;

        Scanner scan = new Scanner(new File(file));

        int i = 0;


        // Scan files. Take the first vertex, 2nd vetex and weight one at a time, then move to next line
        while (scan.hasNextLine()) {

            tempStart = scan.next();
            tempEnd = scan.next();
            tempWeight = scan.nextInt();
         
            if (scan.hasNextLine()) {

                scan.nextLine();

            }

            // Make new edge with collected information and add to sortedEdges (its not sorted yet)
            Edge edge = new Edge(tempStart, tempEnd, tempWeight);

            sortedEdges.add(edge);

            i++;

            // If edge is not within the fetchString hashmap, add edge to both hashmaps via their source vertex.
            if (!fetchString.containsValue(edge.start)) {
                verticies.add(edge.start);
          
                fetchString.put(position, edge.start);

                fetchKey.put(edge.start, position);

                // Position assigns the key index to the verticies when they get put into the hashmaps.
                position++;

            }

            // We will also look at the dest vertx and add it to the hashmaps
            if (!fetchString.containsValue(edge.end)) {

                verticies.add(edge.end);
              
                fetchString.put(position, edge.end);
    
                fetchKey.put(edge.end, position);
    
                position++;
            }
    
        }

        scan.close();

    }
}


// Helper classes below 


class Edge {

    // The source vertex, or "start" or the rail route.
    String start; 
    // The dest vertex, or the "end" of the rail route.
    String end;
    // rail route cost.
    Integer weight;

        public Edge(String start, String end, Integer weight) {

            this.start = start;
            this.end = end;
            this.weight = weight;

        }
        
}


// My sort class. We make sure the two edges are lexicographically sorted between their start and end vertex, before we compare the two edges together.
class weightSort implements Comparator<Edge> {

    private static String temp;

    // See whether edge1 weighs less than edge2, or vice versa. The edge that weighs less will be sorted to come before the heavier edge
    public int compare(Edge edge1, Edge edge2) {
        if (edge1.weight < edge2.weight) return -1;

        else if (edge1.weight > edge2.weight) return 1;

        // If both edges have the same weight, we will now compare them lexagraphically instead.
        else {

            // Before we compare edge1 and edge 2, we must make sure each edge itself is lexicographically correct.
            // For example, say an edge is a Orlando---Gainesville route. We must sort it so it become Gainesville---Orlando instead.
            if (edge2.start.compareTo(edge2.end) > 0) { 

                temp = edge2.start;
                edge2.start = edge2.end;
                edge2.end = temp;

            }
           
            if (edge1.start.compareTo(edge1.end) > 0) {

                temp = edge1.start;
                edge1.start = edge1.end;
                edge1.end = temp;

            }

            // Now we compare edge1 and edge2. If edge 1 precedes edge2, return -1;
            if (edge1.start.compareTo(edge2.start) < 0) {

                return -1;
            }
    
            else return 1; 

        }
    }

}


class disjointSet {

    public static Integer [] makeSet(ArrayList<String> verticies, Integer [] parent) {

        // For all unique veritcies, make sets consisting of themselves initially. Each index is its own parent. Return parent array.
        for (int i = 0; i < verticies.size(); i++) {

            parent[i] = Railroad.fetchKey.get(verticies.get(i));

        }

        return parent;
    }


    public static Integer findSet (Integer token, Integer [] parent) {
    
        // If element within index matches token, then the token is the parent of parent[token]
        if (!parent[token].equals(token)) {

            parent[token] = findSet(parent[token], parent);
            
        }
        
        // Return value, representing the parent
        return parent[token];

    }


    public static void union(Integer u, Integer v, Integer [] parent, Integer[] rank) {

        // Find the root parent of u and v
        Integer uParent = findSet(u, parent), vParent = findSet(v, parent);
 
        // the root parent of u and v are the same, they are in the same set, and we simply return the value of the parent.
        if (uParent == vParent) {

            return;

        }

        // Otherwise we determine the rank of the sets when we merge them.
        if (rank[uParent] < rank[vParent]) {

            parent[uParent] = vParent;

        }

        else if (rank[uParent] > rank[vParent]) {

            parent[vParent] = uParent;

        }

        // If the ranks are the same, we do a normal union.
        else {

            parent[vParent] = uParent;

            rank[uParent]++;

        }
    }
}
