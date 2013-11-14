package controllers;

import ui.HeliUI;
import net.ConnectionManager;

public class StateController {

	public static final int LOCATION_AIR = 0;
	public static final int LOCATION_CITY = 1;
	public static final int LOCATION_CAMP = 2;

	
	private int currentLocation;
	private ConnectionManager connectionManager;
	
	private HeliUI ui;
	
	public StateController (HeliUI heliUI) {
		ui = heliUI;
		currentLocation = LOCATION_AIR;
	}
	
	public int getCurrentLocation() {
		return currentLocation;
	}
	
	public void changeLocation(int newLocation) {
		if (newLocation != currentLocation) {
			if (currentLocation == LOCATION_CAMP) {
				// shutdown helicopter server
				stopHelicopterServer();
				ui.writeNotification("Closing connections from the camp...");
			}
			
			if (newLocation == LOCATION_CAMP) {
				// starting helicopter server
				startHelicopterServer();
				ui.writeNotification("Helicopter is currently at the camp");
				String dbTimeStamp = StorageSystem.getInstance().getInformationTimeStampFormatted();
				ui.writeNotification("Timestamp of database information: " + dbTimeStamp);
				ui.writeNotification("Waiting connections from the camp...");
			}
			else if (newLocation == LOCATION_CITY) {
				ui.writeNotification("Helicopter is currently at the city");
				ui.writeNotification("Ready to send information to the city");
			}
			currentLocation = newLocation;
		}

	}
	
	public void sendInformationToCity() {
		if (currentLocation == LOCATION_CITY) 
			new StorageController(ui).start();
	}
	
	public void startHelicopterServer() {
		connectionManager = new ConnectionManager();
		connectionManager.start();
	}
	
	public void stopHelicopterServer() {
		ConnectionManager.stopListening();
	}
	
}
