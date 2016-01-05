import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Anish Ramasekar on 12/5/15.
 */

public class kConnectivity {
    public static int nodes = 6;
    public static double[][] dist = new double[nodes][nodes];
    public static int[][] graph = new int[nodes][nodes];
    static node[] n = new node[nodes];
    public static int k = 3;//k connectivity

    public static void main(String args[]) {
        n[0] = new node(0, 0);
        n[1] = new node(0, 1.9);
        n[2] = new node(0, 3);
        n[3] = new node(1, 0);
        n[4] = new node(1, 2.1);
        n[5] = new node(1, 3.5);

        /*n[0] = new node(0, 0);
        n[1] = new node(1, 0);
        n[2] = new node(0, 1);
        n[3] = new node(1, 1);*/

        calcDist();
        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < nodes; j++) {
                //System.out.printf("%.2f+\t", (float) dist[i][j]);
                System.out.print(graph[i][j] + "\t");
            }

            System.out.println();
        }

        connectivity(graph);
        greedyAlgo(k, graph, dist);
    }

    public static class node {
        double x;
        double y;

        node(double a, double b) {
            this.x = a;
            this.y = b;
        }
    }

    public static void calcDist() {
        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < nodes; j++) {
                double xtemp = Math.pow(n[j].x - n[i].x, 2);
                double ytemp = Math.pow(n[j].y - n[i].y, 2);
                dist[i][j] = (i == j) ? 0 : Math.pow(xtemp + ytemp, 0.5);
                if (dist[i][j] > 1)
                    graph[i][j] = 0;
                else if (i != j)
                    graph[i][j] = 1;
            }
        }
    }

    //input:k, graph, distance
    public static void greedyAlgo(int k, int[][] graph, double[][] dist) {
        int[][] rGraph = new int[nodes][nodes];//this is my g double dash
        int[][] tempGraph = new int[nodes][nodes];//used for second iteration
        boolean[][] visited = new boolean[nodes][nodes];
        boolean[][] visitedTwo = new boolean[nodes][nodes];

        int count = 0;
        do {
            count++;
            double minValue = Double.MAX_VALUE;
            int indexI = 0;
            int indexJ = 0;

            //for getting min value omega
            for (int i = 0; i < nodes; i++) {
                for (int j = 0; j < nodes; j++) {
                    if (dist[i][j] < minValue && i != j && visited[i][j] != true) {
                        minValue = dist[i][j];
                        indexI = i;
                        indexJ = j;
                    }
                }
            }

            visited[indexI][indexJ] = true;
            rGraph[indexI][indexJ] = 1;
        } while (connectivity(rGraph) != true);

        for (int m = 0; m < nodes; m++) {
            for (int j = 0; j < nodes; j++) {
                System.out.print(rGraph[m][j] + "\t");
            }
            System.out.println();
        }

        System.out.println("Count " + count);

        //connectivity(rGraph);

        int iteration=0;
        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < nodes; j++) {
                tempGraph[i][j] = rGraph[i][j];
                if(tempGraph[i][j]==1)
                    iteration++;
            }
        }

        while(iteration>0) {
            double maxValue = 0;
            int indexI = 0;
            int indexJ = 0;

            //for getting min value omega
            for (int i = 0; i < nodes; i++) {
                for (int j = 0; j < nodes; j++) {
                    if (dist[i][j] > maxValue && i != j && visitedTwo[i][j] != true && rGraph[i][j] == 1) {
                        maxValue = dist[i][j];
                        indexI = i;
                        indexJ = j;
                    }
                }
            }

            visitedTwo[indexI][indexJ] = true;
            tempGraph[indexI][indexJ] = 0;

            if (connectivity(tempGraph) == false && indexI!=indexJ) {
                tempGraph[indexI][indexJ] = 1;
            }

            iteration--;
        }

        for (int m = 0; m < nodes; m++) {
            for (int j = 0; j < nodes; j++) {
                System.out.print(tempGraph[m][j]+ "\t");
            }
            System.out.println();
        }

        connectivity(tempGraph);

    }

    public static boolean bfs(int rGraph[][], int s, int k, int n, int flag) {
        boolean[] visited = new boolean[nodes];
        Arrays.fill(visited, false);
        int[] parent = new int[nodes];
        Queue<Integer> q = new LinkedList();
        q.add(s);
        visited[s] = true;
        parent[s] = -1;

        while (!q.isEmpty()) {
            int u = q.peek();
            q.poll();

            for (int v = 0; v < nodes; v++) {
                if (visited[v] == false && rGraph[u][v] > 0) {
                    q.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        if (flag == 0) {
            for (int i = 0; i < nodes; i++) {
                if (i != s && i != k && visited[i] != true) {
                    System.out.println(i);
                    return false;
                }
            }
        } else if (flag == 1) {
            for (int i = 0; i < nodes; i++) {
                if (i != s && i != k && visited[i] != true && i != n) {
                    System.out.println(i);
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean connectivity(int[][] subgraph) {
        //check if my connectivity is present if 1 or 2 nodes removed
        //starting with the one case
        int[][] residual = new int[nodes][nodes];
        for (int i = 0; i < nodes; i++) {
            for (int m = 0; m < nodes; m++) {
                for (int j = 0; j < nodes; j++) {
                    residual[m][j] = subgraph[m][j];
                }
            }
            residual = setZero(residual, i);
            for(int a=0;a<nodes;a++) {
                if(a!=i) {
                    if (!bfs(residual,a, i, 0, 0)) {
                        System.out.println("Not k connected " + i);
                        return false;
                    }
                }
            }
        }

        //this is for the 2 case
        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < nodes; j++) {

                for (int m = 0; m < nodes; m++) {
                    for (int n = 0; n < nodes; n++) {
                        residual[m][n] = subgraph[m][n];
                    }
                }

                residual = setZero(residual, i);
                residual = setZero(residual, j);

                for(int a=0;a<nodes;a++) {
                    if(a!=i && a!=j) {
                        if (!bfs(residual, a, i, j, 1)) {
                            System.out.println("Not k connected 2 nodes " + i + "\t" + j);
                            return false;
                        }
                    }
                }
            }
        }
        System.out.println("Tri connected");
        return true;
    }

    public static int[][] setZero(int[][] temp, int i) {
        for (int j = 0; j < nodes; j++) {
            temp[i][j] = 0;
        }

        return temp;
    }
}