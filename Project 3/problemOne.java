/**
 * Created by Anish Ramasekar on 11/15/15.
 */

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class problemOne {

    public static int vertices = 0;
    public static double[][] cost = new double[1000][1000];
    public static int[][] capacity = new int[1000][1000];
    public static int[][] demand = new int[1000][1000];
    public static int[][] incoming = new int[1000][1000];
    public static int[][] outgoing = new int[1000][1000];
    public static double[][] oldCost = new double[1000][1000];
    public static double[][] oldCapacity = new double[1000][1000];
    public static int feasible = 0, infeasible = 0;
    public static int[][] totFlow=new int[1000][1000];

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Need 2 arguments : [src file] [demand file]");
        }

        String fileName = args[0];
        String line;
        int first, second;
        String[] values;

        long start = System.currentTimeMillis();

        try {
            FileReader fp = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fp);

            vertices = Integer.parseInt(br.readLine());

            while ((line = br.readLine()) != null) {
                values = line.split(" ");
                first = Integer.parseInt(values[0]);
                second = Integer.parseInt(values[1]);
                //System.out.println(first);//TODO
                cost[first][second] = Double.parseDouble(values[2]);
                capacity[first][second] = Integer.parseInt((values[3]));
                oldCost[first][second] = Double.parseDouble(values[2]);
                oldCapacity[first][second] = Integer.parseInt((values[3]));
            }
            br.close();

            fileName = args[1];
            String fileName1 = "output_"+args[0];

            fp = new FileReader(fileName);
            br = new BufferedReader(fp);

            FileWriter out = new FileWriter(fileName1);
            BufferedWriter br1 = new BufferedWriter(out);
            String output;

            vertices = Integer.parseInt(br.readLine());

            int result;
            while ((line = br.readLine()) != null) {
                values = line.split(" ");
                first = Integer.parseInt(values[0]);
                second = Integer.parseInt(values[1]);
                //System.out.println(first);//TODO
                demand[first][second] = Integer.parseInt(values[2]);
                result = maxFlow(capacity, first, second, demand[first][second]);

                if (result == 1) {
                    output = first + " " + second + " " + "\tFeasible\n";
                    feasible++;
                } else {
                    output = first + " " + second + " " + "\tNot Feasible\n";
                    infeasible++;
                }

                br1.write(output);
            }
            br1.close();
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        printResults();//TODO

        long stop = System.currentTimeMillis();
        System.out.println("Program Ended");
        System.out.println("Simulation Time: " + (stop - start) + "ms");
    }

    public static boolean bfs(int rGraph[][], int s, int t, int parent[]) {
        boolean[] visited = new boolean[vertices + 1];
        Arrays.fill(visited, false);

        Queue<Integer> q = new LinkedList<>();
        q.add(s);
        visited[s] = true;
        parent[s] = -1;

        while (!q.isEmpty()) {
            int u = (int) q.peek();
            q.poll();

            for (int v = 1; v <= vertices; v++) {
                if (visited[v] == false && rGraph[u][v] > 0) {
                    q.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        return (visited[t] == true);
    }

    public static int maxFlow(int capacity[][], int s, int t, int flowCap) {
        //System.out.println("Coming into maxFlow" + s + " " + t );//TODO
        int u, v;

        int[] parent = new int[vertices + 1];
        int[][] rGraph = new int[vertices + 1][vertices + 1];
        int[][] temp = new int[vertices + 1][vertices + 1];
        int[][] tempIn = new int[vertices + 1][vertices + 1];
        int[][] tempOut = new int[vertices + 1][vertices + 1];
        int[][] temptot=new int[vertices+1][vertices+1];

        //System.out.println();//TODO

        for (u = 1; u <= vertices; u++) {
            for (v = 1; v <= vertices; v++) {
                rGraph[u][v] = capacity[u][v];
                temp[u][v] = capacity[u][v];
                tempIn[u][v] = incoming[u][v];
                tempOut[u][v] = outgoing[u][v];
                temptot[u][v]=totFlow[u][v];
                //System.out.print(capacity[u][v]+"\t");//TODO
            }

            //System.out.println();//TODO
        }

        //int maximumFlow=0;//TODO

        while (bfs(rGraph, s, t, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                pathFlow = Math.min(pathFlow, rGraph[u][v]);
                //System.out.println("pathFlow" + pathFlow + " u and v " + u + " " + v);//TODO
            }

            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] -= pathFlow;
                rGraph[v][u] += pathFlow;

                if (flowCap != 0) {
                    temp[u][v] -= Math.min(flowCap, pathFlow);
                    temp[v][u] += Math.min(flowCap, pathFlow);
                    tempIn[v][u] += Math.min(flowCap, pathFlow);
                    tempOut[u][v] += Math.min(flowCap, pathFlow);
                    temptot[u][v]+=Math.min(flowCap,pathFlow);
                }
            }

            //System.out.println(Math.min(flowCap,pathFlow));//TODO

            flowCap -= Math.min(flowCap, pathFlow);
            if (flowCap == 0) {
                replaceMatrix(temp, tempIn, tempOut,temptot);
                return 1;
            }
            //maximumFlow+=pathFlow;//TODO
            //System.out.println("maximum flow: " + maximumFlow);//TODO
        }
        return -1;
    }

    public static void printResults() {

        int maxOut = 0, maxIn = 0;
        int maxNode = 0, maxInNode = 0;
        for (int i = 1; i <= vertices; i++) {
            int outSum = 0, inSum = 0;
            for (int j = 1; j <= vertices; j++) {
                outSum += outgoing[i][j];
                inSum += incoming[i][j];
            }

            maxOut = Math.max(maxOut, outSum);
            maxIn = Math.max(maxIn, inSum);
            if (maxOut == outSum) {
                maxNode = i;
            }

            if (maxIn == inSum) {
                maxInNode = i;
            }
        }

        System.out.println("Max flow out is " + maxOut + " and is out of node " + maxNode);//TODO
        System.out.println("Max flow in is " + maxIn + " and is going into node " + maxInNode);//TODO
        System.out.println("Total feasible flows: " + feasible);
        System.out.println("Total infeasible flows: " + infeasible);

        if (infeasible == 0)
            System.out.println("Given flow is FEASIBLE");
        else
            System.out.println("Given flow is INFEASIBLE");

        Scanner in = new Scanner(System.in);
        System.out.println("Enter 1 to find flow out of Node ");
        System.out.println("Enter 2 to find flow into Node ");
        System.out.println("Enter 3 to end the program ");
        System.out.println("Enter 4 to print results for 20 node network - final task ");
        int choice = in.nextInt();

        if (choice == 1) {
            System.out.println("Enter the Node num (out flow) ");
            int node = in.nextInt();

            double totOut=0;
            for (int i = 1; i <= vertices; i++) {
                if(oldCost[node][i]!=0)
                    System.out.println("Flow out of Node " + node + " to node " + i + " = " + outgoing[node][i]);
                totOut+=outgoing[node][i];
            }

            System.out.println("Total outgoing flow from Node " + node + " : " + totOut);


        } else if (choice == 2) {
            int node;
            System.out.println("Enter the Node num (in flow) ");
            node = in.nextInt();

            double totIn=0;
            for (int i = 1; i <= vertices; i++) {
                if(oldCost[i][node]!=0)
                    System.out.println("Flow into Node " + node + " from node " + i + " = " + incoming[i][node]);
                totIn+=incoming[i][node];
            }

            System.out.println("Total incoming flow into Node " + node + " : " + totIn);

        } else if (choice == 4) {
            System.out.println("\nTotal network flow");
            System.out.println("Src" + "\t\t" + "Dest" + "\t\t" + "Flow");
            int count=0;

            do {
                for (int i = 1; i <= vertices; i++) {
                    for (int j = 1; j <= vertices; j++) {
                        if (totFlow[i][j] == count && oldCost[i][j]!=0)
                            System.out.println(i + "\t\t" + j + "\t\t" + totFlow[i][j]);
                    }
                }

                count++;
            }while(count!=300);
        }
    }

    public static void replaceMatrix(int[][] temp, int[][] tempIn, int[][] tempOut,int[][] tempTot) {
        for (int i = 1; i <= vertices; i++) {
            for (int j = 1; j <= vertices; j++) {
                capacity[i][j] = temp[i][j];
                incoming[i][j] = tempIn[i][j];
                outgoing[i][j] = tempOut[i][j];
                totFlow[i][j]=tempTot[i][j];
            }
        }
    }

    //to get the maximum flow between nodes
    public static int getmaxFlow(int capacity[][], int s, int t) {
        //System.out.println("Coming into maxFlow" + s + " " + t );
        int u, v;
        int[][] rGraph = new int[vertices][vertices];

        for (u = 0; u < vertices; u++) {
            for (v = 0; v < vertices; v++) {
                rGraph[u][v] = capacity[u][v];
            }
        }

        int[] parent = new int[vertices];
        int maximumFlow = 0;

        while (bfs(rGraph, s, t, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                pathFlow = Math.min(pathFlow, rGraph[u][v]);
            }

            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] -= pathFlow;
                rGraph[v][u] += pathFlow;
            }

            maximumFlow += pathFlow;
        }
        return maximumFlow;
    }
}