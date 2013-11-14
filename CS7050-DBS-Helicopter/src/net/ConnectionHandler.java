package net;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import model.Request;
import model.Response;

public class ConnectionHandler {

	public static String serverAddress = "localhost";
	public static int serverPort = 2012;
	private Socket clientSocket = null;
	
	public ConnectionHandler() {
		
	}
	
	public int connect() {
		int result = -1;
		try {
			System.out.println("Connecting to the server...");
			
			clientSocket = new Socket(serverAddress, serverPort);			

			System.out.println("Connected to the server");
			
			result = 1;
			
		} catch (IOException e) {
			System.out.println("Error: Could not establish a connection to " + serverAddress + " on port " + serverPort);
		}
		return result;
	}
	
	public Response sendRequest(Request request) {
		Response response = null;
		try {
			ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

			
			System.out.println("Requesting");
			
			out.writeObject(request);
			out.flush();
			
			response = (Response) in.readObject();
			
			out.close();
			in.close();
			System.out.println("Response received!");
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error sending request & getting response");
			closeConnection();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		return response;		
		
	}
	
	public void closeConnection() {
		try {
			if (clientSocket != null) {
				clientSocket.close();
				clientSocket = null;
			}
		} catch (IOException e) {
			System.out.println("Error: Could not close connection");
		}
	}
		

}
