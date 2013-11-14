package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


public class ConnectionManager extends Thread {

	public static  int helicopter_port_number = 2014;	
	private static volatile boolean running = false;
	
	private static ServerSocket serverSocket = null;
	
	public ConnectionManager() {}
	
	public void run() {
		startListening();
	}

	public void startListening() {
		
		running = true;
		// creating server instance			
		try {
			serverSocket = new ServerSocket(helicopter_port_number);
			System.out.println("Helicopter listening for connections on port " + helicopter_port_number);
		} catch (IOException e) {
			System.out.println("Error: Coul not setup the server on port " + helicopter_port_number);
			System.exit(-1);
		}
		
		Socket clientSocket = null;
		try {
			System.out.println("Waiting for connections...");
			while(running){
				clientSocket = serverSocket.accept();
				
				// launching new thread to handle communication with the client
				Thread thread = new RequestHandler(clientSocket);
				thread.start();
				
			}
		} catch (SocketException se) {
			if (running == true)
				se.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error: Could not accept connection on port " + helicopter_port_number);
		} finally{
			try {
				if (serverSocket != null) {
					serverSocket.close(); 	
					serverSocket = null;
				}
			} catch (IOException e) {
				System.out.println("Error: Could not release resources");
			}
			System.out.println("Helicopter stoped to listen for connections on port " + helicopter_port_number);
		}
	}
	
	public static void stopListening() {
		running = false;
		try{
			if (serverSocket != null) {
				serverSocket.close(); 	
				serverSocket = null;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	

}
