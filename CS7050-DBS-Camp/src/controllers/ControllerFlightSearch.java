package controllers;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JList;

import ui.CampUI;

import model.Flight;
import model.Request;
import model.Response;
import net.ConnectionHandler;

public class ControllerFlightSearch extends Thread {

	private ConnectionHandler con = null;
	private CampUI ui;
	
	
	public ControllerFlightSearch(CampUI userInterface) {
		super("ControllerFlightSearch");
		con = new ConnectionHandler();
		ui = userInterface;
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		
		JButton bSearch = ui.getButtonSearch();
    	bSearch.setText("Searching..");
    	bSearch.setEnabled(false);
		
    	try{
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
				else if (response == null || response.getStatus() == Response.ERROR){
		        	ui.writeNotification("ERROR: A problem occurred retrieving flights' information from the helicopter");
		    	}
		    	else if (response.getStatus() == Response.No_INFO)
		    		ui.writeNotification("No flights information");
		    	else if (response.getStatus() == Response.OK) {
		    		ui.writeNotification("New flights found");
		    		JList flightList = ui.getFlightList();
		    		flights = (ArrayList<Flight>)response.getResource();
		    		
		    		String[] flightsAvailable2 = new String[flights.size()+1];
		    		Flight flight = null;
		    		String flightsAvailable = " ID"+
		    				"    |   Comp" +
		    				"    |   ArrivT" +
		    				"    |   DepartT" +
		    				"    |   Date" +
		    				"    |   From" +
		    				"    |   To";
		    		flightsAvailable2[0] = flightsAvailable;
		    		for (int i = 0; i < flights.size(); i++) {
		    			flight = flights.get(i);
			    		flightsAvailable = " " + flight.getId() +
			    				"    |   " +flight.getCompanyId() +
			    				"    |   " +flight.getArrivalTime() +
			    				"    |   " +flight.getDepartureTime() +
			    				"    |   " +flight.getDate() +
			    				"    |   " +flight.getFrom() +
			    				"    |   " +flight.getTo();
			    		 flightsAvailable2[i+1] = flightsAvailable;
		    		}
		    		flightList.setListData(flightsAvailable2);  	
		    	}
			}
			else {
				ui.writeNotification("ERROR: Imposible to stablish a connection with the helicopter");
			}
		}catch (Exception e) {
			ui.writeNotification("ERROR: " + e.getMessage());
		}
    	
    	bSearch.setEnabled(true);
    	bSearch.setText("Search flights");

	}
	

}
