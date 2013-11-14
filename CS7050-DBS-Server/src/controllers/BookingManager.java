package controllers;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.Booking;
import model.Flight;
import model.Helicopter;
import model.PendingBooking;

import db.BookingDAO;
import db.FlightDAO;
import db.HelicopterDAO;
import db.PendingBookingDAO;


@SuppressWarnings("rawtypes")
public class BookingManager extends Thread{
	
	// pending bookings will be processed each INTERVAL seconds
	public static int TIME_INTERVAL = 60; 
	
	// % of seats occupied to apply load-share between companies
	private static final double LOADSHARE_PERCENTAGE = 50.0;
	
	private BookingController bookingController;
	private BookingDAO bDAO;
	private PendingBookingDAO pbDAO;
	
	public BookingManager(){
		super("BookingManagerThread");
		bookingController = new BookingController();
		bDAO = new BookingDAO();
		pbDAO = new PendingBookingDAO();
	}
	
	public void run(){
		while(true) {
			try {
				Thread.sleep(TIME_INTERVAL * 1000);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
			
			System.out.println(getTimeFormatted() + "Processing pending bookings...");
			// processing pending booking table
			processPendingBookings();
			System.out.println(getTimeFormatted() + "Pending bookings processed...");
			
		}
	}
	
	private String getTimeFormatted() {
        GregorianCalendar currentTime = new GregorianCalendar();
        int hour, min, secs, mill;

        hour = currentTime.get(GregorianCalendar.HOUR_OF_DAY);
        min = currentTime.get(GregorianCalendar.MINUTE);
        secs = currentTime.get(GregorianCalendar.SECOND);
        mill = currentTime.get(GregorianCalendar.MILLISECOND);
        
        return hour + ":" + min + ":" + secs + ":" + mill + " - ";
	}
	
	private void processPendingBookings() {
		List<Integer> flightsIDs = null;
		
		// getting all the flights involved
		flightsIDs = pbDAO.getFlightIDs();
		if (flightsIDs != null && !flightsIDs.isEmpty()) {
			for (Integer flightID : flightsIDs) {
				
				HashMap<Integer, Integer> availableFlights = null;
				
				FlightDAO fDAO = new FlightDAO();
				Flight flight = fDAO.findByPK(flightID);

				// getting pending bookings for each flight
					// ordered by timestamp
				List<PendingBooking> pendingBookings = null;
				pendingBookings = pbDAO.findByFlightIDOrderedByTimeStamp(flightID);
				
				
				// calculating current seats available
				int originalFlightSeatsAvailable = -1;
				int totalSeats = 0;
				int totalBookings = 0;
				
				// getting the total number of seats
				if (flight != null) {
					int helicopterID = flight.getHelicopterId();
					
					HelicopterDAO hDAO = new HelicopterDAO();
					Helicopter helicopter = hDAO.findByPK(helicopterID);
					if (helicopter != null) {
						totalSeats = helicopter.getSeatsNO();
						
						// getting the total number of seats booked
						List<Booking> bookings = bDAO.findBookingsByFlight(flightID);
						
						totalBookings = bookings.size();
						System.out.println("Total bookings:" + totalBookings);
						System.out.println("Total seats:" + totalSeats);
						// calculate seats available
						originalFlightSeatsAvailable = totalSeats - totalBookings;
					}
				}
				
				// LOAD-SHARING between companies if passengers > 50%
				double occupationPercentage = 100 - (originalFlightSeatsAvailable * 100) / totalSeats;
				System.out.println("Percentage = " + occupationPercentage);
				
				
				if (occupationPercentage > LOADSHARE_PERCENTAGE) {
					System.out.println("Looking for alternative flights...");
					// looking for flight at the same time
					String flightDate = flight.getDate();
					String flightDepartureTime = flight.getDepartureTime();
					String from = flight.getFrom();
					
					List<Flight> aFlightsList = null;
					aFlightsList = fDAO.findBySameTime(flightDate,
							flightDepartureTime, from, flightID);
					
					if (!aFlightsList.isEmpty()) {
						System.out.println("Alternative flights found!");
						availableFlights = new HashMap<Integer, Integer>();
						// doing loadshare...
						for (Flight aFlight : aFlightsList) {
							int aFlightID = aFlight.getId();
							int afSeatsAv = getSeatsAvailable(aFlightID);
							
							System.out.println(aFlightID + ":" + afSeatsAv);
							
							if (afSeatsAv > 0)
								availableFlights.put(aFlightID, afSeatsAv);						
						}
					}
				}
				if (availableFlights !=null && !availableFlights.isEmpty()) {
					// LOADSHARE
					// adding original flight to the list
					availableFlights.put(flightID, originalFlightSeatsAvailable);
					System.out.println("Applying load-share");
					// confirming or rejecting
					for(PendingBooking pb : pendingBookings){
						int targetFlightID = getMoreSuitableFlight(availableFlights);
						System.out.println("Alternative flight: " + targetFlightID);
						
						int currentSeatsNo = availableFlights.get(targetFlightID);
						System.out.println("Alternative flight seats no: " + currentSeatsNo);
						if (currentSeatsNo > 0) {
							System.out.println("Booking reallocated");
							int bResult = bookingController.confirmPendingBookingWithAlternativeFlight(pb.getId(),
									targetFlightID);
							if (bResult == 1) 
								// decrease number of seats in alternative flight							
								availableFlights.put(targetFlightID, currentSeatsNo - 1);
						}
						else {
							bookingController.rejectPendingBooking(pb.getId());
						}
					}
				}
				else {
					System.out.println("Processing booking without applying loadshare");
					// NO LOADSHARE		
					// confirming or rejecting
					for(PendingBooking pb : pendingBookings){
						if (originalFlightSeatsAvailable > 0) {
							bookingController.confirmPendingBooking(pb.getId());
							originalFlightSeatsAvailable--;
						}
						else {
							bookingController.rejectPendingBooking(pb.getId());
						}
					}
				}
			}
		}
		
	}
	
	private int getMoreSuitableFlight(HashMap<Integer, Integer> availableFlight) {
		int flightID = -1;
		int maxSeatsAvailable = 0;
		
		Iterator it = availableFlight.entrySet().iterator();
		Map.Entry pairs = null;
		
		while(it.hasNext()) {
			pairs = (Map.Entry)it.next();
			int seatsNo = (Integer) pairs.getValue();
			if (seatsNo > maxSeatsAvailable) {
				maxSeatsAvailable = seatsNo;
				flightID = (Integer )pairs.getKey();
			}
		}
		
		return flightID;
	}
	
	public int getSeatsAvailable(int flightID){
		int result = -1;
		int totalSeats = 0;
		int totalBookings = 0;
		
		// getting the total number of seats
		FlightDAO fDAO = new FlightDAO();
		Flight flight = fDAO.findByPK(flightID); 
		if (flight != null) {
			int helicopterID = flight.getHelicopterId();
			
			HelicopterDAO hDAO = new HelicopterDAO();
			Helicopter helicopter = hDAO.findByPK(helicopterID);
			if (helicopter != null) {
				totalSeats = helicopter.getSeatsNO();
				
				// getting the total number of seats booked
				List<Booking> bookings = bDAO.findBookingsByFlight(flightID);
				
				totalBookings = bookings.size();
				
				// calculate seats available
				result = totalSeats - totalBookings;
			}
		}
		return result;
	}
	
}
