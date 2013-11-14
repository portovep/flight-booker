package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager {

	public static int server_port_number = 2012;
	
	public ConnectionManager() {
		
	}

	public void startListening() {
		// creating server instance
		ServerSocket serverSocket = null;			
		try {
			serverSocket = new ServerSocket(server_port_number);
			System.out.println("Server is running on port " + server_port_number);
		} catch (IOException e) {
			System.out.println("Error: Coul not setup the server on port " + server_port_number);
			System.exit(-1);
		}
		
		Socket clientSocket = null;
		try {
			System.out.println("Waiting for connections...");
			while(true){
				clientSocket = serverSocket.accept();

				Thread thread = new RequestHandler(clientSocket);
				thread.start();
			}
		} catch (IOException e) {
			System.out.println("Error: Could not accept connection on port " + server_port_number);
			System.exit(-1);
		} finally{
			try {
				if (serverSocket != null) {
					serverSocket.close();
					serverSocket = null;
				}
			} catch (IOException e) {
				System.out.println("Error: Could not release resources");
			}
		}
	}
	

}
