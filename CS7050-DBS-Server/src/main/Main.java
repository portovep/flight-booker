package main;


import controllers.BookingManager;

import net.ConnectionManager;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			try {
				int port = Integer.parseInt(args[0]);
				// set server port
				ConnectionManager.server_port_number = port;
			}catch(NumberFormatException nfe) {
				System.out.println("The server port has to be a number");
				System.exit(-1);
			}
		}
		if (args.length > 1) {
			try {
				int bookingProcessingInterval = Integer.parseInt(args[1]);
				// set server port
				BookingManager.TIME_INTERVAL = bookingProcessingInterval;
				System.out.println("TIME_INTERVAL=" + bookingProcessingInterval);
			}catch(NumberFormatException nfe) {
				System.out.println("The time interval has to be a number");
				System.exit(-1);
			}
		}
		 Thread thread = new BookingManager();
		 thread.start();
		 new ConnectionManager().startListening(); 
	}

}