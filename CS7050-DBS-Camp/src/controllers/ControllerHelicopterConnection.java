package controllers;

import javax.swing.JButton;

import ui.CampUI;

import net.ConnectionHandler;

public class ControllerHelicopterConnection extends Thread {

	private ConnectionHandler con = null;
	private CampUI ui;
	
	
	
	public ControllerHelicopterConnection(CampUI ui) {
		super("ControllerHelicopterConnection");
		con = new ConnectionHandler();
		this.ui = ui;
	}
	
	public void run() {
		String helicopterIP = ui.getTextHeliIP().getText();
		String hPort = ui.getTextHeliPort().getText();
		JButton bHeliConnection = ui.getButtonHeliConnection();
		
		try {
			if (helicopterIP.isEmpty())
				ui.writeNotification("ERROR: Provide a helicopter IP");
			else if (hPort.isEmpty())
				ui.writeNotification("ERROR: Provide a helicopter port");
			else {
				bHeliConnection.setText("Connecting..");
				bHeliConnection.setEnabled(false);
		    	
		    	int helicopterPort = Integer.parseInt(hPort);
	
				int result = con.checkConnection(helicopterIP, helicopterPort); 
				
		    	if (result == 1) {
		        	ui.writeNotification("Connected to helicopter[" + helicopterIP +
		        			":" + helicopterPort + "]");
		        	// set new connection info
		        	ConnectionHandler.serverAddress = helicopterIP;
		        	ConnectionHandler.serverPort = helicopterPort;
		    	}
		    	else {
		    		ui.writeNotification("Error: Helicopter[" + helicopterIP +
		        			":" + helicopterPort + "] is not in the camp");
		    	}
			}
		}catch(Exception e) {
    		ui.writeNotification("Error: " + e.getMessage());
		}
    	
		bHeliConnection.setEnabled(true);
		bHeliConnection.setText("Connect");

	}
}
