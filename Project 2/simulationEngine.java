/**
 * Created by Anish Ramasekar on 10/18/2015.
 */

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class simulationEngine {
	static packet packets[] = new packet[5000];
	static Random randomGenerator1 = new Random(11245);
	static Random randomGenerator2 = new Random(21245);
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

	static double meanArrival = 0.02;
	static double meanService = 0.016;
	static double MC;// main clock
	private static Scanner userInput;
	static boolean submenuvar=true;

	public static void main(String args[]) {
		arr=0;

		System.out.println("Enter 1 for case 1 ");
		System.out.println("Enter 2 for case 2 ");
		System.out.println("Enter 3 for case 3 ");
		userInput = new Scanner(System.in);
		int flag = Integer.parseInt(userInput.next());
		if(flag==1 || flag==2 || flag==3 ){

			while (MC <= 10) {
				double min1 = minTime(arr, servicedTime);
				// System.out.println("min:" + min1);
				if (min1 == arr) {
					if (MC > min1) {
						MC = MC;
					} else {
						MC = min1;
					}
					// System.out.println("Arrival");
					arrivalStage(flag);
				} else if (min1 == servicedTime) {
					if (MC > min1) {
						MC = MC;
					} else {
						MC = min1;
					}
					// System.out.println("Service");
					serviceStage(flag);
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

			//System.out.println(counter);
			NumberFormat formatter = new DecimalFormat("#0.0000000000000000");
			for(int i=1;i<=counter;i++){
				//System.out.println(packets[i].starttime + "\t" + packets[i].endtime + "\t" + delay[i]);
			}

			// printing the values for on going average
			//System.out.println("AVG WAITING TIME(ms)" + "\t" + "AVG SERVICE TIME(ms)" + "\t" + "AVG SYSTEM TIME(ms)" + "\t" + "AVERAGE PACKETS" + "\t     " + "PACKETS");
			for (int j = 1; j <= packetToBeServiced; j++) {
				//System.out.println(formatter.format(avgWaitingTime[j] * 1000) + "\t" + formatter.format(avgServiceTime[j] * 1000) + "\t" + formatter.format(avgSystemTime[j] * 1000) + "\t" + formatter.format(avgPacketsQueue[j]) + "\t" + packets[j].queueLength);
			}

			System.out.println("\nTotal Departures: " + packetToBeServiced);
			System.out.println("Average system time: " + (systemtime / packetToBeServiced)* 1000 + " ms");
			System.out.println("Simulation Time: " + MC + " s");
			System.out.println("Average waiting time: " + (waittime / packetToBeServiced)* 1000 + " ms");
			System.out.println("Average service time: " + (servicetime / packetToBeServiced)* 1000 + " ms");
			System.out.println("Average Packets in queue: " + avgPacketsQueue[packetToBeServiced] + "\n");
		}
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

	public static void arrivalStage(int caseNum) {

		switch(caseNum){
		case 1:
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
			break;

		case 2:
			if (MC <= 10) {
				//System.out.println(serviceQueue.size() + "\t" + counter);
				//implement check as buffer size is limited to 20
				if(serviceQueue.size()<20){
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
				}

				// calculation for next arrival
				double randomDouble = randomGenerator1.nextDouble();
				double log = Math.log(randomDouble);
				arr += (meanArrival * log * (-1));
			} else {
				arr = 999999;
			}
			break;

		case 3:
			if (MC <= 10) {
				counter++;
				packets[counter] = new packet(counter);
				packets[counter].starttime = arr;
				packets[counter].queueLength = serviceQueue.size();

				if (serviceQueue.isEmpty()) {
					serviceQueue.add(counter);
					servicedTime = MC + meanService;
					packets[counter].servicetime = meanService;

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
			break;
		}
	}

	public static void serviceStage(int caseNum) {

		switch(caseNum){
		case 1:
		case 2:
			packetToBeServiced = serviceQueue.poll();

			if (serviceQueue.isEmpty()) {
				servicedTime = 999999;
			} else {
				double randomDouble = randomGenerator2.nextDouble();
				double log = Math.log(randomDouble);
				servicedTime = MC + (meanService * log * (-1));
				packets[packetToBeServiced + 1].servicetime = (meanService* log * (-1));
			}
			packets[packetToBeServiced].endtime = MC;
			break;

		case 3:
			packetToBeServiced = serviceQueue.poll();

			if (serviceQueue.isEmpty()) {
				servicedTime = 999999;
			} else {
				servicedTime = MC + meanService;
				packets[packetToBeServiced + 1].servicetime = meanService;
			}
			packets[packetToBeServiced].endtime = MC;
			break;
		}
	}
}