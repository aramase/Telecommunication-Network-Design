/**
 * Created by Anish Ramasekar on 11/20/15.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class problemTwo {

    public static int vertices = 0;
    public static double[][] cost = new double[1000][1000];
    public static double[][] capacity = new double[1000][1000];
    public static double[][] oldCost = new double[1000][1000];
    public static double[][] oldCapacity = new double[1000][1000];
    public static double[] demand = new double[1000];
    public static double[][] incoming = new double[1000][1000];
    public static double[][] outgoing = new double[1000][1000];
    public static double[][] totFlow=new double[1000][1000];
    public static int[] parent = new int[1000];
    public static int[] pi = new int[1000];
    static double[] distance = new double[1000];//distance is cost
    static Queue<Integer> sourceQueue = new LinkedList<>();
    static Queue<Integer> sinkQueue = new LinkedList<>();
    static Stack<Integer> path = new Stack<>();
    static double delta = 0;
    static LinkedList<Double> bottleNeck = new LinkedList<>();
    static double minCost = 0;

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Need 2 arguments : [src file] [demand file]");
        }

        String fileName = args[0];
        String line;
        int first, second;
        String[] values;
        Double temp;

        long start = System.currentTimeMillis();

        try {
            FileReader fp = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fp);

            vertices = Integer.parseInt(br.readLine());

            while ((line = br.readLine()) != null) {
                values = line.split(" ");
                first = Integer.parseInt(values[0]);
                second = Integer.parseInt(values[1]);
                //System.out.println(first);TODO
                cost[first][second] = Double.parseDouble(values[2]);
                capacity[first][second] = Double.parseDouble((values[3]));
                oldCost[first][second] = Double.parseDouble(values[2]);
                oldCapacity[first][second] = Double.parseDouble((values[3]));
            }
            br.close();

            fileName = args[1];

            fp = new FileReader(fileName);
            br = new BufferedReader(fp);

            vertices = Integer.parseInt(br.readLine());
            while ((line = br.readLine()) != null) {
                values = line.split(" ");
                first = Integer.parseInt(values[0]);
                second = Integer.parseInt(values[1]);
                //System.out.println(first);TODO
                temp = Double.parseDouble(values[2]);
                demand[first] += temp;
                demand[second] -= temp;
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshList();
        ssp();
        printResults();
        calcCost(capacity);

        long stop = System.currentTimeMillis();
        System.out.println("\nProgram Ended");
        System.out.println("Simulation Time: " + (stop - start) + "ms");
    }

    public static void djikstras(double[][] matrix, double[][] cap, int node) {
        for (int i = 1; i <= vertices; i++) {
            //System.out.print(demand[i] + "\t");//TODO
        }

        //System.out.println();//TODO
        Boolean[] visited = new Boolean[vertices + 1];
        Arrays.fill(parent, 0);

        for (int i = 1; i <= vertices; i++) {
            distance[i] = Double.MAX_VALUE;
            visited[i] = false;
        }

        distance[node] = 0;

        for (int count = 1; count <= vertices; count++) {
            double min = Double.MAX_VALUE;
            int min_index = -1;
            for (int v = 1; v <= vertices; v++) {
                if (visited[v] == false && distance[v] <= min) {
                    min = distance[v];
                    min_index = v;
                }
            }

            int u = min_index;
            visited[u] = true;

            for (int v = 1; v <= vertices; v++) {
                if (!visited[v] && (matrix[u][v] != 0 || cap[u][v] != 0) && distance[u] != Double.MAX_VALUE && distance[u] + matrix[u][v] < distance[v]) {
                    distance[v] = distance[u] + matrix[u][v];
                    parent[v] = u;
                }
            }
        }

        //System.out.println("Finished Djikstras");//TODO

        for (int i = 1; i <= vertices; i++) {
            //System.out.print(parent[i] + "\t");//TODO
        }

        //System.out.println();//TODO
    }

    public static void ssp() {
        do {
            //demand and supplies inside array - demand. This is my e.
            //System.out.println();//TODO
            int source = sourceQueue.poll();
            int sink = sinkQueue.poll();
            //System.out.println("source" + source + "sink" + sink);//TODO
            djikstras(cost, capacity, source);

            for (int i = 1; i <= vertices; i++) {
                //System.out.print(distance[i] + "\t");//TODO
            }
            //System.out.println();//TODO
            //System.out.println("value of pi");

            for (int i = 1; i <= vertices; i++) {
                if (i != source) {
                    pi[i] += distance[i];
                }
                //System.out.print(pi[i]+"\t");//TODO
            }
            //System.out.println();

            //System.out.println("finished updating value of pi");//TODO

            //now need to update cost values
            // 1. Need to get the path
            getPath(source, sink);

            //System.out.println("finished getting path");//TODO

            // 2. Need to re-initialize the cost values to 0
            double totCost = 0;//this is the cost along the shortest path
            for (int i = path.size() - 1; i >= 1; i--) {
                //System.out.print(path.get(i)+"\t");//TODO
                totCost += cost[path.get(i)][path.get(i - 1)];
                cost[path.get(i)][path.get(i - 1)] = 0;
                //cost[path.get(i-1)][path.get(i)] = 0;
                bottleNeck.offer(capacity[path.get(i)][path.get(i - 1)]);
            }

            updateCosts(cost);
            resetPi(pi);

            //System.out.println("total cost " + totCost);//TODO

            bottleNeck.offer(demand[source]);
            bottleNeck.offer(-demand[sink]);

            // 3. Need to augment the flow now

            Collections.sort(bottleNeck);
            delta = bottleNeck.removeFirst();
            bottleNeck.clear();

            //System.out.println("delta " + delta);//TODO

            minCost += totCost * delta;

            demand[source] -= delta;
            demand[sink] += delta;

            //need to update capacity along the path now
            for (int i = path.size() - 1; i >= 1; i--) {
                capacity[path.get(i - 1)][path.get(i)] += delta;
                capacity[path.get(i)][path.get(i - 1)] -= delta;
                incoming[path.get(i - 1)][path.get(i)] += delta;
                outgoing[path.get(i)][path.get(i - 1)] += delta;
                totFlow[path.get(i)][path.get(i-1)]+=delta;
                //System.out.println(path.get(i));//TODO
            }

            sourceQueue.clear();
            sinkQueue.clear();

            //System.out.println("new cost");//TODO
            for(int i=1;i<=vertices;i++)
            {
                for(int j=1;j<=vertices;j++)
                {
                    //System.out.print(cost[i][j]+"\t");//TODO
                }

                //System.out.println();//TODO
            }

            //System.out.println("New Cap");//TODO
            //calcCost(capacity);//TODO


            refreshList();

            //System.out.println("minCost " + minCost);//TODO
        } while (!sourceQueue.isEmpty());
        return;
    }

    //to keep updating the sets of source and sink
    public static void refreshList() {
        for (int i = 1; i <= vertices; i++) {
            if (demand[i] > 0)
                sourceQueue.add(i);
            else if (demand[i] < 0)
                sinkQueue.add(i);
        }

        return;
    }

    public static void getPath(int src, int dest) {
        path.clear();
        //System.out.println("Coming into get path");//TODO
        int temp = -1;
        int element = dest;
        path.push(element);
        if (parent[element] != src) {
            while (temp != src) {
                //System.out.println("temp" + temp);//TODO
                path.push(parent[element]);
                element = parent[element];
                temp = parent[element];
            }

            path.push(src);
        } else {
            path.push(src);
        }
        return;
    }

    public static void printResults() {

        double maxOut = 0, maxIn = 0;
        int maxNode = 0, maxInNode = 0;
        for (int i = 1; i <= vertices; i++) {
            double outSum = 0, inSum = 0;
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

        System.out.println("Max flow out is " + maxOut + " and is out of node " + maxNode);
        System.out.println("Max flow in is " + maxIn + " and is going into node " + maxInNode);

        Scanner in = new Scanner(System.in);
        System.out.println("Enter 1 to find flow out of Node ");
        System.out.println("Enter 2 to find flow into Node ");
        System.out.println("Enter 3 to end the program ");
        System.out.println("Enter 4 to print results for 20 node network - final task ");

        int choice = in.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Enter the Node num (out flow) ");
                int node = in.nextInt();

                double totOut=0;
                for (int i = 1; i <= vertices; i++) {
                    if(oldCost[node][i]!=0)
                        System.out.println("Flow out of Node " + node + " to node " + i + " = " + totFlow[node][i]);
                    totOut+=totFlow[node][i];
                }

                System.out.println("Total outgoing flow from Node " + node + " : " + totOut);
                break;

            case 2:
                System.out.println("Enter the Node num (in flow) ");
                node = in.nextInt();

                double totIn=0;
                for (int i = 1; i <= vertices; i++) {
                    if(oldCost[i][node]!=0)
                        System.out.println("Flow into Node " + node + " from node " + i + " = " + totFlow[i][node]);
                    totIn+=totFlow[i][node];
                }

                System.out.println("Total incoming flow into Node " + node + " : " + totIn);
                break;

            case 4:
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

            default:
                break;
        }

        in.close();
    }

    public static void updateCosts(double[][] temp)
    {
        for(int i=1;i<=vertices;i++)
        {
            for(int j=1;j<=vertices;j++)
            {
                if(temp[i][j]>0)
                {
                    if(pi[i]-pi[j]<=0)
                        temp[i][j]+=(pi[i]-pi[j]);
                }
            }
        }
    }

    public static void resetPi(int[] temp)
    {
        for(int i=1;i<=vertices;i++)
        {
            temp[i]=0;
        }
    }

    public static void calcCost(double[][] newCap)
    {
        double cost=0;
        for(int i=1;i<=vertices;i++)
        {
            for(int j=1;j<=vertices;j++)
            {
                if(oldCapacity[i][j]!=0)
                {
                    double diff=oldCapacity[i][j]-newCap[i][j];
                    cost+=oldCost[i][j]*(diff>=0?diff:0);
                }
            }
        }

        System.out.println("COST: " + cost);
    }
}
