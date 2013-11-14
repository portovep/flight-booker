package controllers;

import java.util.UUID;

import javax.swing.JButton;

import ui.CityUI;

import model.BookingResource;
import model.Request;
import model.Response;
import net.ConnectionHandler;

public class ControllerBookings extends Thread {

	private ConnectionHandler con = null;
	private CityUI ui;
	
	
	public ControllerBookings(CityUI ui) {
		super("ControllerBookings");
		con = new ConnectionHandler();
		
		this.ui = ui;
	}
	
	public void run() {
		String returnFID = "";
		String flightID = ui.getTextFlightID().getText();
		String userID = ui.getTextCustomerID().getText();
		String userName = ui.getTextCustomerName().getText();
		JButton bBooking = ui.getButtonBooking();
		
		// get return flightid
		returnFID = ui.getTextReturnFlightID().getText();

		try {
			if (flightID.isEmpty())
				ui.writeNotification("ERROR: Provide a flightID!");
			else if (userID.isEmpty())
				ui.writeNotification("ERROR: Provide a customerID");
			else if (userName.isEmpty())
				ui.writeNotification("ERROR: Provide a customerName");
			else if (ui.getCheckBReturn().isSelected() && returnFID.isEmpty()) {
				ui.writeNotification("ERROR: Provide a return flightID!");
			}
			else {
				bBooking.setText("Booking..");
				bBooking.setEnabled(false);

		    	String bookingReference = UUID.randomUUID().toString();
		    	String returnBookingReference = "";
		    	
		    	BookingResource bResource = new BookingResource();
		    	bResource.setFlightID(Integer.parseInt(flightID));
		    	bResource.setBookingReference(bookingReference);
		    	bResource.setUserID(userID);
		    	bResource.setUserName(userName);
		    	
		    	// return flight
		    	if (ui.getCheckBReturn().isSelected()){
		    		returnBookingReference = UUID.randomUUID().toString();
			    	BookingResource returnBoResource = new BookingResource();
			    	returnBoResource.setFlightID(Integer.parseInt(returnFID));
			    	returnBoResource.setBookingReference(returnBookingReference);
			    	returnBoResource.setUserID(userID);
			    	returnBoResource.setUserName(userName);
			    	
			    	bResource.setReturnFlight(returnBoResource);
		    	}
		    	
		    	Request request = new Request(Request.ACTION_BOOK);
		    	request.setResource(bResource);
		    	
				Response response = null;
				int result = con.connect(); 
				if (result == 1) {
					response =  con.sendRequest(request);
					con.closeConnection();
					
					if (response == null) {
						ui.writeNotification("ERROR: A problem occurred sending booking request");
					}
			    	if (response.getStatus() == Response.ERROR){
			        	ui.writeNotification("ERROR: A problem occurred registering booking request in the server");
			    	}
			    	else if (response.getStatus() == Response.OK) {
			    		ui.writeNotification("The booking request was registered");
	    				ui.writeNotification("Booking reference: ## " + bookingReference + "  ##");
	    				if (!returnBookingReference.isEmpty())
		    				ui.writeNotification("Return reference: ## " + returnBookingReference + "  ##");

			    	}
				}
				else {
					ui.writeNotification("ERROR: Imposible to stablish a connection with the server");
				}
			}
		}catch (Exception e) {
			ui.writeNotification("ERROR: " + e.getMessage());
		}
    	
    	bBooking.setEnabled(true);
    	bBooking.setText("Book");

	}
}
