package controllers;

import javax.swing.JButton;

import ui.CampUI;

import model.CheckingResource;
import model.Request;
import model.Response;
import model.StatusResource;
import net.ConnectionHandler;

public class ControllerBookingStatus extends Thread {

	private ConnectionHandler con = null;
	private CampUI ui;
	
	
	public ControllerBookingStatus(CampUI userInterface) {
		super("ControllerBookingStatus");
		con = new ConnectionHandler();
		ui = userInterface;
	}
	
	public void run() {
		
		JButton bChecking = ui.getButtonCheckStatus();
    	bChecking.setText("Checking..");
    	bChecking.setEnabled(false);
		
    	try {
	    	String bookingReference = ui.getTextBookingReference().getText();
	    	
	    	if (bookingReference != null && !bookingReference.isEmpty()) {
	    		CheckingResource checkingResource = new CheckingResource();
	        	checkingResource.setBookingReference(bookingReference);
	
	        	Request request = new Request(Request.ACTION_CHECK);
	        	request.setResource(checkingResource);
	        	
	    		Response response = null;
	    		int result = con.connect(); 
	    		if (result == 1) {
		    		response =  con.sendRequest(request);
		    		con.closeConnection();
		    		
		    		if (response == null) {
		    			ui.writeNotification("A problem occurred sending check request to the helicoper");
		    		}
		    		else if (response.getStatus() == Response.ERROR){
		            	ui.writeNotification("A problem occurred checking booking state in the helicopter");
		        	}
		        	else if (response.getStatus() == Response.No_INFO) {
		        		ui.writeNotification("The helicopter doesn't have information about your booking.");
		        	}
		        	else if (response.getStatus() == Response.OK) {
		        		StatusResource statusResource = (StatusResource) response.getResource();
		        		int status = statusResource.getStatus();
		        		if (status == StatusResource.CONFIRMED)
		        			ui.writeNotification("Booking confirmed!");
		        		else if (status == StatusResource.PENDING)
		        			ui.writeNotification("Booking was not processed yet");
		        		else if (status == StatusResource.REJECTED)
		        			ui.writeNotification("Booking was rejected");
		        		else
		    				ui.writeNotification("Booking was rejected");
		        				
		        	}
	    		}
				else {
					ui.writeNotification("ERROR: Imposible to stablish a connection with the helicopter");
				}
	    	}
	    	else
	    		ui.writeNotification("ERROR: Provide a booking reference!");
    	}catch(Exception e) {
    		ui.writeNotification("ERROR: " + e.getMessage());
    	}
    		
    	bChecking.setEnabled(true);
    	bChecking.setText("Check booking");

	}
	

}
