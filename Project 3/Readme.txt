Pre-requisites:
1. Super User Access in Linux OS
2. jdk 7u21 installed.

If running on eos, Linux Lab Machine (Realm RHEnterprise 6), install jdk using the following commands:

eos$add jdk
eos$add jdk7u21

install cplex using the following commands:
eos$add ilog

--------------------------------------------------------------------------------------------------------------------------------------
programOne.java - Question 1 

STEP 1: Check the pre-requisites. Make sure that jdk 7 or higher is installed.

STEP 2: Compile the program by running the command "javac problemOne.java"

STEP 3: Run the program by using the command "java problemOne adjacency_10-a.txt demands_10.txt" java problemOne [src file] [demand file].

STEP 4: The program generates an output file(output_10-a.txt) containing the feasibility detail for flows given in demands_10.txt and also prints the required statistics. 

--------------------------------------------------------------------------------------------------------------------------------------
programTwo.java - Question 2 

STEP 1: Check the pre-requisites. Make sure that jdk 7 or higher is installed.

STEP 2: Compile the program by running the command "javac problemTwo.java"

STEP 3: Run the program by using the command "java problemTwo adjacency_10-a.txt demands_10.txt" java problemOne [src file] [demand file].

STEP 4: The program prints the required statistics and also provides the options to view incoming and outgoing flows for specific nodes. 

--------------------------------------------------------------------------------------------------------------------------------------
programThree.java - Question 3 (CPLEX) 

STEP 1: Check the pre-requisites. Make sure that jdk 7 or higher is installed and ilog is added using the "add ilog" command.

STEP 2: Compile the program by running the command 
eos$javac -cp "/ncsu/ilog/cplex/lib/cplex/jar" problemThree.java

STEP 3: Run the program by using the command 
eos$java -cp /ncsu/ilog/cplex/lib/cplex.jar:. -Djava.library.path=/ncsu/ilog/cplex/bin/x86-64_sles10_4.1 problemThree adjacency_10-a.txt demands_10.txt aramase_10a.lp

STEP 4: The program prints the required statistics for all nodes in the network.

--------------------------------------------------------------------------------------------------------------------------------------
Method 2 to run CPLEX program using the lp model file generated from java code

STEP 1: The java program also generates a cplex model file (aramase_10a.lp) [Name of file provided as cmd line arg in java code].

STEP 2: Type cplex in the command line.

STEP 3: Load the cplex model using the command "read aramase_10a.lp"

STEP 4: The model is now loaded and can be run using the command "opt"

STEP 5: The output is displayed and can also be written into a file for future reference using the command "write result.sol"









