import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class simulationEngine1 {
	
	static packet packets[] = new packet[5000];
	static Random randomGenerator1 = new Random(1234);
	static Random randomGenerator2 = new Random(2000);
	static Queue<Integer> serviceQueue = new LinkedList<>();
	static double[] delay = new double[5000];
	static double[] avgWaitingTime = new double[5000];
	static double[] avgServiceTime = new double[5000];
	static double[] avgSystemTime = new double[5000];
	static double[] avgPacketsQueue = new double[5000];

	static double arr;// arrival time for packets
	static double servicedTime = 999999;
	static int counter = 0;
	static int packetToBeServiced=0;

	static double meanArrival;
	static double meanService = 0.016;
	static double MC;// main clock
	private static Scanner in;

	public static void main(String args[]) {
		arr=0;
		
		in = new Scanner(System.in);
		System.out.println("Enter the mean inter-arrival rate ranging from 16 to 25 ");
		meanArrival=in.nextDouble()/1000;

		while (MC <= 10) {
			double min1 = minTime(arr, servicedTime);
			if (min1 == arr) {
				if (MC > min1) {
					MC = MC;
				} else {
					MC = min1;
				}
				// System.out.println("Arrival");
				arrivalStage();
			} else if (min1 == servicedTime) {
				if (MC > min1) {
					MC = MC;
				} else {
					MC = min1;
				}
				// System.out.println("Service");
				serviceStage();
			}
		}

		// updating the values of wait time for each packet
		packets[1].waittime = 0.0;// first packet encounters an empty queue
		for (int i = 2; i <= packetToBeServiced; i++) {
			if (packets[i].starttime > packets[i - 1].endtime) {
				packets[i].waittime = 0.0;

			} else {
				packets[i].waittime = packets[i - 1].endtime
						- packets[i].starttime;
			}
		}

		double systemtime = 0, waittime = 0, servicetime = 0, queueSize = 0, time = 0;
		for (int i = 1; i <= packetToBeServiced; i++) {
			delay[i] = packets[i].endtime - packets[i].starttime;
			systemtime += delay[i];
			waittime += packets[i].waittime;
			servicetime += packets[i].servicetime;
			queueSize += (packets[i].starttime * packets[i].queueLength);
			time += packets[i].starttime;

			avgSystemTime[i] = (systemtime / i);
			avgWaitingTime[i] = (waittime / i);
			avgServiceTime[i] = (servicetime / i);
			avgPacketsQueue[i] = (queueSize / time);
		}
		
		double totQueue=0;
		for(int i=1;i<=packetToBeServiced;i++)
		{
			totQueue+=packets[i].queueLength;
		}
		
		System.out.println("Simulation Model: ");

		System.out.println("Total Departures: " + counter);
		System.out.println("Simulation Time: " + MC + " s");
		System.out.println("Average waiting time: " + (waittime / counter)* 1000 + " ms");
		System.out.println("Average system time: " + (systemtime / counter)* 1000 + " ms");
		System.out.println("Average Number of Customers: " + (totQueue/counter));
		
		manualCalc();
	}

	public static double minTime(double a, double b) {
		double result = Math.min(a, b);
		return result;
	}

	// packets that are added into the queue
	public static class packet {

		int id;
		double starttime;
		double endtime;
		double waittime;
		double servicetime;
		int queueLength;

		public packet(int id) {
			this.id = id;
			this.waittime = 0;
			this.servicetime = 0;
			this.queueLength = 0;
		}
	}

	public static void arrivalStage() {

		if (MC <= 10) {
			counter++;
			packets[counter] = new packet(counter);
			packets[counter].starttime = arr;
			packets[counter].queueLength = serviceQueue.size();

			if (serviceQueue.isEmpty()) {
				serviceQueue.add(counter);

				double randomDouble = randomGenerator2.nextDouble();
				double log = Math.log(randomDouble);
				servicedTime = MC + (meanService * log * (-1));

				packets[counter].servicetime = (meanService * log * (-1));

			} else {
				// add and leave..no need to calculate the service time
				serviceQueue.add(counter);
			}

			// calculation for next arrival
			double randomDouble = randomGenerator1.nextDouble();
			double log = Math.log(randomDouble);
			arr += (meanArrival * log * (-1));
		} else {
			arr = 999999;
		}
	}

	public static void serviceStage() {

		packetToBeServiced = serviceQueue.poll();

		if (serviceQueue.isEmpty()) {
			servicedTime = 999999;
		} else {
			double randomDouble = randomGenerator2.nextDouble();
			double log = Math.log(randomDouble);
			servicedTime = MC + (meanService * log * (-1));
		}
		packets[packetToBeServiced].endtime = MC;
	}
	
	public static void manualCalc()
	{
		System.out.println("\nManual Calculations:");
		double arrivalRate=1/meanArrival;
		double serviceRate=1/meanService;
		
		double systemLoad=arrivalRate/serviceRate;
		double avgTime=1/(serviceRate*(1-systemLoad));
		
		double avgCust=arrivalRate*avgTime;
		
		System.out.println("Avg time spent in system: " + avgTime*1000 + " ms");
		System.out.println("Avg number of customers: " + avgCust);
	}
}