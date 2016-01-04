/**
 * Created by Anish Ramasekar on 11/20/15.
 */

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class problemThree {

    public static int vertices = 0;
    public static double[][] cost = new double[1000][1000];
    public static double[][] capacity = new double[1000][1000];
    public static double[] demand = new double[1000];
    public static double[] incoming =new double[1000];
    public static double[] outgoing=new double[1000];

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Need 3 arguments : [src file] [demand file] [name of cplex output file]");
        }

        String fileName = args[0];
        String line;
        int first, second;
        String[] values;
        Double temp;

        long start = System.currentTimeMillis();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            vertices = Integer.parseInt(br.readLine());

            while ((line = br.readLine()) != null) {
                values = line.split(" ");
                first = Integer.parseInt(values[0]);
                second = Integer.parseInt(values[1]);
                //System.out.println(first);TODO
                cost[first - 1][second - 1] = Double.parseDouble(values[2]);
                capacity[first - 1][second - 1] = Double.parseDouble((values[3]));
            }
            br.close();

            String fileName1 = args[1];

            br = new BufferedReader(new FileReader(fileName1));

            vertices = Integer.parseInt(br.readLine());
            while ((line = br.readLine()) != null) {
                values = line.split(" ");
                first = Integer.parseInt(values[0]);
                second = Integer.parseInt(values[1]);
                //System.out.println(first);TODO
                temp = Double.parseDouble(values[2]);
                demand[first - 1] += temp;
                demand[second - 1] -= temp;
            }
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //CPLEX part of the program
        try {
            // define new model
            IloCplex cplex = new IloCplex();
            // variables
            IloNumVar[][] x = new IloNumVar[vertices][vertices];
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (i == j)
                        x[i][j] = cplex.numVar(0, Double.MAX_VALUE);
                    else if (capacity[i][j] == 0)
                        x[i][j] = cplex.numVar(0, Double.MAX_VALUE);
                    else
                        x[i][j] = cplex.numVar(0, capacity[i][j]);

                    //System.out.print(x[i][j] + "\t");//TODO
                }
            }

            // expressions
            IloLinearNumExpr[] constraints = new IloLinearNumExpr[vertices];
            //System.out.println("read everything and declared array");//TODO

            for (int i = 0; i < vertices; i++) {
                constraints[i] = cplex.linearNumExpr();
            }

            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (i != j && cost[i][j] != 0) {
                        if (demand[i] <= 0)
                            constraints[i].addTerm(-1.0, x[i][j]);
                        else
                            constraints[i].addTerm(1.0, x[i][j]);
                    }
                }

                for (int k = 0; k < vertices; k++) {
                    if (i != k && cost[k][i] != 0) {
                        if (demand[i] <= 0)
                            constraints[i].addTerm(1.0, x[k][i]);
                        else
                            constraints[i].addTerm(-1.0, x[k][i]);
                    }
                }
            }

            IloLinearNumExpr objective = cplex.linearNumExpr();

            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (i != j) {
                        if (cost[i][j] != 0) {
                            objective.addTerm(cost[i][j], x[i][j]);
                        }
                    }
                }
            }

            //System.out.println(objective);//TODO

            // define objective
            cplex.addMinimize(objective);

            //constraints equations
            for (int i = 0; i < vertices; i++) {
                if (constraints[i] != null) {
                    //System.out.println(i);//TODO
                    if (demand[i] > 0)
                        cplex.addLe(constraints[i], demand[i]);
                    else if (demand[i] < 0)
                        cplex.addEq(constraints[i], -demand[i]);
                    else
                        cplex.addEq(constraints[i], 0.0);
                }
            }

            cplex.setParam(IloCplex.Param.Simplex.Display, 0);
            String out=args[2];
            cplex.exportModel(out);

            // solve model
            if (cplex.solve()) {
                System.out.println("\nCPLEX Output");
                System.out.println("obj = " + cplex.getObjValue());
            } else {
                System.out.println("problem not solved");
            }

            for(int i=0;i<vertices;i++)
            {
                for(int j=0;j<vertices;j++)
                {
                    if(cost[i][j]!=0 && i!=j && cplex.getValue((x[i][j]))!=0) {
                        System.out.println("Flow from Node " + (i+1) + " to Node " + (j+1) + " is " + cplex.getValue(x[i][j]));
                        outgoing[i+1]+=cplex.getValue(x[i][j]);
                        incoming[j+1]+=cplex.getValue(x[i][j]);
                    }
                }
            }

            System.out.println("\nOutgoing flow");
            for(int i=1;i<=vertices;i++)
            {
                if(outgoing[i]!=0)
                    System.out.println("Flow out of Node " + i + " is " + outgoing[i]);
            }

            System.out.println("\nIncoming flow");
            for(int i=1;i<=vertices;i++)
            {
                if(incoming[i]!=0)
                    System.out.println("Flow into Node " + i + " is " + incoming[i]);
            }

            System.out.println("\nTotal network flow");
            System.out.println("Src" + "\t\t" + "Dest" + "\t\t" + "Flow");
            double count=0;
            do {
                for (int i = 0; i < vertices; i++) {
                    for (int j = 0; j < vertices; j++) {
                        if (cost[i][j] != 0 && i!=j && cplex.getValue(x[i][j]) == count)
                            System.out.println((i + 1) + "\t\t" + (j + 1) + "\t\t" + cplex.getValue(x[i][j]));
                    }
                }
                count++;
            }while(count!=300);

            cplex.end();
        } catch (IloException exc) {
            exc.printStackTrace();
        }

        long stop = System.currentTimeMillis();

        System.out.println("\nProgram Ended");
        System.out.println("Simulation Time: " + (stop - start) + "ms");
    }
}