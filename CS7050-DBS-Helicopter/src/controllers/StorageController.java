package controllers;


import java.util.ArrayList;
import java.util.List;


import ui.HeliUI;

import net.ConnectionHandler;

import model.BookingListResource;
import model.BufferResource;
import model.Flight;
import model.Request;
import model.Response;

public class StorageController extends Thread{
	
	private ConnectionHandler con = null;
	private HeliUI ui = null;
	
	public StorageController (HeliUI ui) {
		this.ui = ui;
		con = new ConnectionHandler();
	}
	
	public void run(){
		boolean connectionE = getFlights();
		if(!connectionE) {
			sendRequestBuffer();
			getBookingInfo();
		}
	}
	
	private void sendRequestBuffer() {	
		// get pending requests
		BufferResource bufferResource = new BufferResource();
		List<Request> bufferedRequests = StorageSystem.getInstance().getRequestBuffer();
		
		if (bufferedRequests == null || bufferedRequests.isEmpty()) {
			ui.writeNotification("No pending request from the camp");
		}
		else {
			bufferResource.setBuffer(bufferedRequests);
			
			Request request = new Request(Request.ACTION_SEND_BUFFER);
			request.setResource(bufferResource);
			
			
			int result = con.connect();
			if (result == 1) {
				Response response = null;
				response = con.sendRequest(request);
				con.closeConnection();
				
				if (response == null) {
					ui.writeNotification("ERROR: A problem occurred sending buffer request to the server");
				}
				else if (response.getStatus() == Response.ERROR){
		        	ui.writeNotification("ERROR: A problem occurred processing requests int the server");
		    	}
		    	else if (response.getStatus() == Response.No_INFO)
		    		ui.writeNotification("ERROR:  No camp request received by the city server");
		    	else if (response.getStatus() == Response.OK) {
		    		
		    		ui.writeNotification(bufferedRequests.size() + 
		    				" camp requests processed by the city server!");
		    		// change information timestamp
		    		StorageSystem.getInstance().setNewInformationTimeStamp();
		    		// reseting request buffer
		    		StorageSystem.getInstance().clearRequestBuffer();
		    	}
			}
			else {
				ui.writeNotification("ERROR: Imposible to stablish a connection with the server");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean getFlights(){
		boolean connectionE = false;
 		Request request = new Request(Request.ACTION_SEARCH);
	
		ArrayList<Flight> flights = null;
		Response response = null;
		
		int result = con.connect();
		if (result == 1) {
			response = con.sendRequest(request);
			con.closeConnection();
			
	
			if (response == null) {
				ui.writeNotification("ERROR: A problem occurred requesting flights information");
			}
			else if (response.getStatus() == Response.ERROR){
	        	ui.writeNotification("ERROR: A problem occurred retrieving flights' information in the server");
	    	}
	    	else if (response.getStatus() == Response.No_INFO)
	    		ui.writeNotification("ERROR: INo flights information");
	    	else if (response.getStatus() == Response.OK) {
	    		flights = (ArrayList<Flight>)response.getResource();
	    		
	    		StorageSystem.getInstance().saveFlightInformation(flights);
	    		
	    		// change information timestamp
	    		StorageSystem.getInstance().setNewInformationTimeStamp();

	    		String dbTimeStamp = StorageSystem.getInstance().getInformationTimeStampFormatted();
	    		ui.writeNotification("Flights database updated correctly at " + dbTimeStamp);
	    	}
		}
		else {
			ui.writeNotification("ERROR: Imposible to stablish a connection with the server");
			connectionE = true;
		}
		
		return connectionE;
	}
	
	public void getBookingInfo() {
		Request request = new Request(Request.ACTION_GET_BOOKINGS);
		
		Response response = null;
		int result = con.connect();
		if (result == 1) {
			response = con.sendRequest(request);
			con.closeConnection();
			
			if (response == null) {
				ui.writeNotification("ERROR: A problem occurred requesting booking information");
			}
			else if (response.getStatus() == Response.ERROR){
	        	ui.writeNotification("ERROR: A problem occurred retrieving booking information in the server");
	    	}
	    	else if (response.getStatus() == Response.No_INFO)
	    		ui.writeNotification("ERROR: No booking information");
	    	else if (response.getStatus() == Response.OK) {
	    		BookingListResource blResource = (BookingListResource)response.getResource();
	    		
	    		if (blResource != null) {
	    			StorageSystem.getInstance().setBookings(blResource.getBookings());
	    			StorageSystem.getInstance().setPendingBookings(blResource.getPendingBookings());
	    			StorageSystem.getInstance().setRejectedBookings(blResource.getRejectedBookings());
	    			ui.writeNotification("Booking information updated correctly");
	    		}
	    		else
	    			ui.writeNotification("ERROR: A problem occurred saving booking information");
	    	}
		}
		else {
			ui.writeNotification("ERROR: Imposible to stablish a connection with the server");
		}
	}

}
