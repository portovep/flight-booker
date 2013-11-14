package controllers;

import javax.swing.JButton;

import ui.CampUI;

import model.CancellationResource;
import model.Request;
import model.Response;
import net.ConnectionHandler;

public class ControllerCancellations extends Thread {

	private ConnectionHandler con = null;
	private CampUI ui;
	
	
	public ControllerCancellations(CampUI userInterface) {
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
						ui.writeNotification("ERROR: A problem occurred sending cancellation request to the helicopter");
		    		}
		        	if (response.getStatus() == Response.ERROR){
		            	ui.writeNotification("ERROR: The helicopter cannot carry more requests");
		        	}
		        	else if (response.getStatus() == Response.OK) {
		        		ui.writeNotification("The cancellation request was registered!");
		        	}
	    		}
				else {
					ui.writeNotification("ERROR: Imposible to stablish a connection with the helicopter");
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
