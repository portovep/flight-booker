package net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import controllers.StorageSystem;

import model.CheckingResource;
import model.Flight;
import model.Request;
import model.Response;
import model.StatusResource;



public class RequestHandler extends Thread {

	private Socket clientSocket = null;
	
	public RequestHandler (Socket socket) {
		super("RequestHandlerThread");
		clientSocket = socket;
	}
	
	public void run() {
		try {			
			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
			
			String clientName = clientSocket.getRemoteSocketAddress().toString();
			System.out.println("New request from client: " + clientName);
			
			Request request = null;
			do {
				request = (Request) in.readObject();
				// Looking for actions
				if (request != null && 
						request.getAction().equals(Request.ACTION_SEARCH)){				
					List<Flight> flights = null;
					flights = StorageSystem.getInstance().getFlights();
					
					Response response = new Response();
					response.setToAction(Request.ACTION_SEARCH);
					if (flights == null) {
						response.setStatus(Response.ERROR);
					}
					else if(flights.isEmpty()) {
						response.setStatus(Response.No_INFO);
					}
					else {
						response.setResource(flights);
						response.setStatus(Response.OK);
					}
					
					out.writeObject(response);
					out.flush();
					break;
				}
				
				
				else if(request != null && 
						request.getAction().equals(Request.ACTION_BOOK)){
					
					// adding request to the request buffer
					int result = StorageSystem.getInstance().addNewRequest(request);
					
					Response response = new Response();
					response.setToAction(Request.ACTION_BOOK);
					if (result > 0)
						response.setStatus(Response.OK);
					else
						response.setStatus(Response.ERROR);
	
					out.writeObject(response);
					out.flush();
					break;
				}
				
				else if(request != null && 
						request.getAction().equals(Request.ACTION_CHECK)){
					
					CheckingResource checkingResource = (CheckingResource) request.getResource();
					String bookingReference = checkingResource.getBookingReference();
							
					int result = -1;
					result = StorageSystem.getInstance().checkBookingState(bookingReference);
					
					Response response = new Response();
					response.setToAction(Request.ACTION_CHECK);
					if (result > 0) {
						response.setStatus(Response.OK);
						StatusResource sResource = new StatusResource();
						sResource.setStatus(result);
						response.setResource(sResource);
					}
					else if (result == 0)
						response.setStatus(Response.No_INFO);
					else
						response.setStatus(Response.ERROR);
	
					out.writeObject(response);
					out.flush();
					break;
				}
				
				
				else if(request != null && 
						request.getAction().equals(Request.ACTION_CANCEL)){
					
					// adding request to the request buffer
					int result = StorageSystem.getInstance().addNewRequest(request);
					
					Response response = new Response();
					response.setToAction(Request.ACTION_CANCEL);
					if (result > 0)
						response.setStatus(Response.OK);
					else
						response.setStatus(Response.ERROR);
	
					out.writeObject(response);
					out.flush();
					break;
				}
						
			} while (request != null && !request.getAction().isEmpty());
		} catch (IOException e) {
			System.out.println("Client " + clientSocket.getRemoteSocketAddress() +
					" diconnected!");
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} finally{
			try {
				if (clientSocket != null) {
					clientSocket.close();
					clientSocket = null;
				}
			} catch (IOException e) {
				System.out.println("Error: Could not release resources");
			}
		}
	}
}
