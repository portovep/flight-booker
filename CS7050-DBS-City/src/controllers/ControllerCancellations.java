package controllers;

import javax.swing.JButton;

import ui.CityUI;

import model.CancellationResource;
import model.Request;
import model.Response;
import net.ConnectionHandler;

public class ControllerCancellations extends Thread {

	private ConnectionHandler con = null;
	private CityUI ui;
	
	
	public ControllerCancellations(CityUI userInterface) {
		super("ControllerCancellations");
		con = new ConnectionHandler();
		ui = userInterface;
	}
	
	public void run() {
		JButton bCancellation = ui.getButtonCancellation();
    	bCancellation.setText("Cancelling..");
    	bCancellation.setEnabled(false);
		try {		
	    	String bookingReference = ui.getTextBookingReference().getText();
	    	
	    	if (bookingReference != null && !bookingReference.isEmpty()) {
	    		CancellationResource cResource = new CancellationResource();
	        	cResource.setBookingReference(bookingReference);
	
	        	Request request = new Request(Request.ACTION_CANCEL);
	        	request.setResource(cResource);
	        	
	    		Response response = null;
	    		int result = con.connect();
	    		if (result==1) {
		    		response =  con.sendRequest(request);
		    		con.closeConnection();
		    		if (response == null) {
						ui.writeNotification("ERROR: A problem occurred sending cancellation request");
		    		}
		        	if (response.getStatus() == Response.ERROR){
		            	ui.writeNotification("ERROR: A problem occur cancelling your booking");
		        	}
		        	else if (response.getStatus() == Response.No_INFO) {
		        		ui.writeNotification("The booking reference is invalid!");
		        	}
		        	else if (response.getStatus() == Response.OK) {
		        		ui.writeNotification("Booking canceled sucessful!");
		        	}
	    		}
				else {
					ui.writeNotification("ERROR: Imposible to stablish a connection with the server");
				}
	    	}
	    	else
	    		ui.writeNotification("ERROR: Provide a booking reference!");
	    	
		}catch (Exception e) {
			ui.writeNotification("ERROR: " + e.getMessage());
		}
		
    	bCancellation.setEnabled(true);
    	bCancellation.setText("Cancel a booking");
	}
	

}
