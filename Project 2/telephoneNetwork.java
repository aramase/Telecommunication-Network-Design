import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class telephoneNetwork {

	static packet packets[] = new packet[100000];
	static Random randomGenerator1 = new Random(1000);
	static Random randomGenerator2 = new Random(4050);
	static Queue<Integer> serviceQueue = new LinkedList<>();
	static LinkedList<Double> serverTime=new LinkedList<Double>();

	static double arr;// arrival time for packets
	static double servicedTime = 999999;
	static int counter = 0;
	static int packetToBeServiced=0;
	static double temp;

	static double meanArrival;
	static double meanService = 180;
	static double MC;// main clock
	private static Scanner in;

	static int current=0;
	static int blocked=0;
	static int total=0;

	public static void main(String args[]) {

		double start=System.currentTimeMillis();
		arr=0;
		serverTime.offer(servicedTime);
		in = new Scanner(System.in);
		System.out.println("Enter the mean inter-arrival rate ");
		meanArrival=in.nextDouble();

		while (MC <= 18000) {
			temp=serverTime.peek();
			double min1 = minTime(arr, temp);
			if (min1 == arr) {
				if (MC > min1) {
					MC = MC;
				} else {
					MC = min1;
				}
				// System.out.println("Arrival");
				arrivalStage();
			} else if (min1 == temp) {
				if (MC > min1) {
					MC = MC;
				} else {
					MC = min1;
				}
				// System.out.println("Service");
				serviceStage();
			}
		}

		double stop=System.currentTimeMillis();

		System.out.println("Total Calls: " + total);
		System.out.println("Blocked Calls: " + blocked);
		System.out.println("Counter: " + counter);

		System.out.println("Blocking rate: " + (double)(blocked*100)/total);
		System.out.println("Run time: " + (stop-start));
		System.out.println("Simulation time: " + MC);
	}

	public static double minTime(double a, double b) {
		double result = Math.min(a, b);
		return result;
	}

	// packets that are added into the queue
	public static class packet {
		int id;
		public packet(int id) {
			this.id = id;
		}
	}

	public static void arrivalStage() {
		if(MC<=18000){
			if(serviceQueue.size()<=50){
				counter++;
				serviceQueue.add(counter);

				double randomDouble = randomGenerator2.nextDouble();
				double log = Math.log(randomDouble);
				servicedTime = MC + (meanService * log * (-1));
				serverTime.offer(servicedTime);
				Collections.sort(serverTime);
			}
			else
			{
				blocked++;
			}

			// calculation for next arrival
			double randomDouble = randomGenerator1.nextDouble();
			double log = Math.log(randomDouble);
			arr += (meanArrival * log * (-1));
			total++;
		}
		else
		{
			arr=999999;
		}
	}

	public static void serviceStage() {
		serverTime.poll();
		packetToBeServiced = serviceQueue.poll();
	}
}