package controllers;

import db.FlightDAO;


public class FlightController {
	
	public boolean existFlight(int flightID) {
		
		FlightDAO flightDAO = new FlightDAO();							
		if (flightDAO.findByPK(flightID) != null)
			return true;
		
		return false;
	}
}
