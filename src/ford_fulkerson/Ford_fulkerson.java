//ID : W1727389 by Alnafis Chowdhury
package ford_fulkerson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

class QuickFindUF {

    private final int[] id;    // id[i] = component identifier of i
    private final int count;   // number of components

    public QuickFindUF(int N) {
        count = N;
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }

    }

    public int count() {
        return count;
    }

    /* Returns the component identifier for the component containing site
     * <tt>p</tt>.
     *
     * @param p the integer representing one site
     * @return the component identifier for the component containing site
     * <tt>p</tt>
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= p < N
     */
    public int find(int p) {
        return id[p];
    }

    /* Are the two sites <tt>p</tt> and <tt>q</tt> in the same component?
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @return <tt>true</tt> if the two sites <tt>p</tt> and <tt>q</tt> are in
     * the same component, and <tt>false</tt> otherwise
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= p < N and 0
     * <= q < N
     */
    public boolean connected(int p, int q) {
        /* To be completed
         */
        return false;
    }

    /* Merges the component containing site<tt>p</tt> with the component
     * containing site <tt>q</tt>.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= p < N and 0
     * <= q < N
     */
    public void union(int p, int q) {
        
    }
}

/////////////////////////////////////////////////////////
public class Ford_fulkerson {

    private final int verticesCount;
    private final float[][] adjacencylist;

    //to get the count of vertices
    public int getverticesCount() {
        return verticesCount;
    }

    public float[][] getAdj() {
        return adjacencylist;
    }

    public Ford_fulkerson(int verticesCount) {
        this.verticesCount = verticesCount;
        adjacencylist = new float[verticesCount][verticesCount];
        for (int i = 0; i < verticesCount; i++) {
            for (int j = 0; j < verticesCount; j++) {
                adjacencylist[i][j] = 0;
            }
        }
    }
    //Adds the edge to calculate max flow
    public void addEdge(int i, int j, float weight) {
        adjacencylist[i][j] = weight;
    }

    public boolean hasEdge(int i, int j) {
        return adjacencylist[i][j] != 0;
    }
    //Removes The Edge to find best path
    public void removeEdge(int i, int j) {
        adjacencylist[i][j] = 0;
    }

    public List<Integer> VConnector(int vertex) {
        List<Integer> edges = new ArrayList<>();
        for (int i = 0; i < verticesCount; i++) {
            if (hasEdge(vertex, i)) {
                edges.add(i);
            }
        }
        return edges;
    }
    //Printing The Graph
    public void printGraph() {
        for (int i = 0; i < verticesCount; i++) {
            List<Integer> edges = VConnector(i);
            System.out.print(i + ": ");
            for (int j = 0; j < edges.size(); j++) {
                System.out.print(edges.get(j) + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Ford_fulkerson g = new Ford_fulkerson(6);
        System.out.println("---------------------------------------------");
        System.out.println("Printing Flow Of A Network:");
        System.out.println("Graph:");

        // add Edges From the graph
        g.addEdge(0, 1, 1);
        g.addEdge(0, 4, 4);
        g.addEdge(1, 2, 1);
        g.addEdge(1, 3, 2);
        g.addEdge(2, 3, 1);
        g.addEdge(2, 4, 2);
        g.addEdge(3, 4, 1);
        g.addEdge(1, 5, 4);
        g.addEdge(4, 5, 1);
        
      
        
        // print Graph
        g.printGraph();

        Scanner input = null;
        try {
            input = new Scanner(new File("bridge_1.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found:");
        }

        int N = input.nextInt();

        System.out.println("---------------------------------------------");
        System.out.println("Parser. Data Attained from text file: ");
        System.out.println("Number of vertices: " + N);

        Ford_fulkerson graph = new Ford_fulkerson(N);

        QuickFindUF uf;
        uf = new QuickFindUF(N);
        long start = System.currentTimeMillis();

        while (input.hasNextInt()) {
            int p = input.nextInt();
            int q = input.nextInt();
            int c = input.nextInt();
            graph.addEdge(p, q, c);

            if (uf.connected(p, q)) {
                continue;
            }
            uf.union(p, q);
            System.out.println(p + " " + q + " " + c);

        }

        // Printing Ford-Fulkerson Max Flow Algorithm 
        System.out.print("Ford-Fulkerson Max Flow: ");
        System.out.println(FordFulkerson(g, 0, 5));

        while (input.hasNextInt()) {
            int p = input.nextInt();
            int q = input.nextInt();
            int c = input.nextInt();
            graph.addEdge(p, q, c);
            System.out.println(p + " -> " + q);
            System.out.println(c);
        }
        long now = System.currentTimeMillis();
        double elapsed = (now - start) / 1000.0;
        System.out.println("Elapsed time = " + elapsed + " seconds");
        System.out.println();
        System.out.println(uf.count() + " Components Total:");

//       
    }

    public static float FordFulkerson(Ford_fulkerson g, int source, int destination) {
        // error proof
        if (source == destination) {
            return 0;
        }
        int V = g.getverticesCount();

        // create residual graph
        Ford_fulkerson residualGraph = new Ford_fulkerson(V);
        for (int i = 0; i < V; i++) {
            System.arraycopy(g.getAdj()[i], 0, residualGraph.getAdj()[i], 0, V);
        }

        // filled by BFS to store path
        int parent[] = new int[V];

        float max_flow = 0; // max flow value

        // while a path exists from source to destination loop
        while (bfs(residualGraph, source, destination, parent)) {
            // to store path flow
            float path_flow = Float.MAX_VALUE;

            // find maximum flow of path filled by bfs
            for (int i = destination; i != source; i = parent[i]) {
                int j = parent[i];
                path_flow = Math.min(path_flow, residualGraph.getAdj()[j][i]);
            }

            // update residual graph capacities
            // reverse edges along the path
            for (int i = destination; i != source; i = parent[i]) {
                int j = parent[i];
                residualGraph.getAdj()[j][i] -= path_flow;
                residualGraph.getAdj()[i][j] += path_flow;
            }

            // Add path flow to max flow
            max_flow += path_flow;
        }

        return max_flow;
    }

    public static boolean bfs(Ford_fulkerson residualGraph, int source, int destination, int parent[]) {
        // array to store visited vertices
        boolean[] visited = new boolean[residualGraph.getverticesCount()];
        for (int i = 0; i < residualGraph.getverticesCount(); i++) {
            visited[i] = false;
        }

        LinkedList<Integer> q = new LinkedList<>(); // queue-like

        // visit source
        q.add(source);
        visited[source] = true;
        parent[source] = -1;

        // It will loop through all vertices
        while (!q.isEmpty()) {
            int i = q.poll();
            // checking VConnector of vertex i
            for (Integer j : residualGraph.VConnector(i)) {
                // if not visited and positive value then visit
                if ((visited[j] == false) && (residualGraph.getAdj()[i][j] > 0)) {
                    q.add(j);
                    visited[j] = true;
                    parent[j] = i;
                }
            }
        }

        // returns the boolean that tells us if we ended up at the destination
        return visited[destination];
    }

}

// References:
//https://stackabuse.com/graphs-in-java-breadth-first-search-bfs/
//https://stackabuse.com/graphs-in-java-depth-first-search-dfs/
//https://www.geeksforgeeks.org/iterative-depth-first-traversal/
//https://steemit.com/programming/@drifter1/programming-java-graph-maximum-flow-algorithm-ford-fulkerson
//https://algorithms.tutorialhorizon.com/max-flow-problem-ford-fulkerson-algorithm/
//https://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/
//Tutorial 2 - Module Algorithm Skeleton Code
//Tutorial 8 - Skeletonn Pseucode by teachers
