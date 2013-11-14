package net;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import controllers.BookingController;

import db.FlightDAO;

import model.Booking;
import model.BookingListResource;
import model.BookingResource;
import model.BufferResource;
import model.CancellationResource;
import model.CheckingResource;
import model.Flight;
import model.PendingBooking;
import model.RejectedBooking;
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
			String clientName = clientSocket.getRemoteSocketAddress().toString();
			System.out.println("New request from client: " + clientName);
			
			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
			
			Request request = null;
			do {
				request = (Request) in.readObject();
				// Looking for commands
				if (request != null && 
						request.getAction().equals(Request.ACTION_SEARCH)){
					FlightDAO flightDAO = new FlightDAO();					
					ArrayList<Flight> flights = new ArrayList<Flight>();
					flights = flightDAO.findAll();
		
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
					
					int result = -1;				
					result = processBooking(request);
					
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
						request.getAction().equals(Request.ACTION_CANCEL)){
					
					int result = -1;
					result = processCancellation(request);
					
					
					Response response = new Response();
					response.setToAction(Request.ACTION_CANCEL);
					if (result > 0)
						response.setStatus(Response.OK);						
					else if (result == 0)
						response.setStatus(Response.No_INFO);
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
					
					
					BookingController bController = new BookingController();
					int result = -1;
					result = bController.checkBookingStatus(bookingReference);
					
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
				
				// HELICOPTER INTERACTION
				else if(request != null && 
						request.getAction().equals(Request.ACTION_SEND_BUFFER)){
					
					BufferResource bufferResource = (BufferResource) request.getResource();
					
					// getting request from the camp					//ClientManager.getInstance().removeClient(clientAddr);
					List<Request> campRequests = bufferResource.getBuffer();
					
					int result = 0;
					
					if (campRequests != null && !campRequests.isEmpty()) {
						result = 1;
						// handling each request
						for (Request campRequest : campRequests) {
							String action = campRequest.getAction();
							
							if (action.equals(Request.ACTION_BOOK)) {
								processBooking(campRequest);
							}
							else if (action.equals(Request.ACTION_CANCEL)) {
								processCancellation(campRequest);
							}
						}
					}
					
					
					Response response = new Response();
					response.setToAction(Request.ACTION_SEND_BUFFER);
					if (result > 0) {
						response.setStatus(Response.OK);
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
						request.getAction().equals(Request.ACTION_GET_BOOKINGS)){
				
					BookingListResource blResource = new BookingListResource();
					
					BookingController bController = new BookingController();
					int result = 0;
					
					// getting confirmed bookings from db
					List<Booking> bookings = bController.getBookings();
					if (bookings != null && !bookings.isEmpty()) {
						result++;
						blResource.setBookings(bookings);
					}
					
					// getting pending bookings from db
					List<PendingBooking> pbookings = bController.getPendingBookings();
					if (pbookings != null && !pbookings.isEmpty()) {
						result++;
						blResource.setPendingBookings(pbookings);
					}
					
					// getting rejected bookings from db
					List<RejectedBooking> rbookings = bController.getRejectedBookings();
					if (rbookings != null && !rbookings.isEmpty()) {
						result++;
						blResource.setRejectedBookings(rbookings);
					}
					
					Response response = new Response();
					response.setToAction(Request.ACTION_GET_BOOKINGS);
					if (result > 0) {
						response.setStatus(Response.OK);
						response.setResource(blResource);
					}
					else if (result == 0)
						response.setStatus(Response.No_INFO);
					else
						response.setStatus(Response.ERROR);
	
					out.writeObject(response);
					out.flush();
					break;
				}
				
			} while (request != null && !request.getAction().isEmpty());
			
		} catch (IOException e) {
			System.out.println("Client " + clientSocket.getRemoteSocketAddress() +
					" disconnected");
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
	
	private int processBooking(Request request) {
		int result = -1;
		BookingResource bResource = (BookingResource) request.getResource();
		String bookingReference = bResource.getBookingReference();
		int flightID = bResource.getFlightID();
		String customerID = bResource.getUserID();
		String customerName = bResource.getUserName();
		Long timeStamp = bResource.getTimeStamp();
		
		
		BookingController bController = new BookingController();
		result = bController.addPendingBooking(bookingReference, timeStamp,
				flightID, customerID, customerName);
		
		// return flight
		BookingResource returnBoResource = bResource.getReturnFlight();
		if (returnBoResource != null) {
			bookingReference = returnBoResource.getBookingReference();
			System.out.println("Return " + bookingReference);
			flightID = returnBoResource.getFlightID();
			customerID = returnBoResource.getUserID();
			customerName = returnBoResource.getUserName();
			timeStamp = returnBoResource.getTimeStamp();
			
			result = bController.addPendingBooking(bookingReference, timeStamp,
					flightID, customerID, customerName);
		}
		
		return result;
	}
	
	private int processCancellation(Request request) {
		int result = -1;
		CancellationResource cResource = (CancellationResource) request.getResource();
		String bookingReference = cResource.getBookingReference();	
		
		BookingController bController = new BookingController();		
		result = bController.cancelBooking(bookingReference);
		
		return result;
	}
}
