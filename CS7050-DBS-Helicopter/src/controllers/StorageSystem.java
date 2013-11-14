package controllers;

import java.util.ArrayList;
import java.util.List;

import model.Booking;
import model.Flight;
import model.PendingBooking;
import model.RejectedBooking;
import model.Request;
import model.StatusResource;

public class StorageSystem {
	
	private static final int BUFFER_SIZE = 200;
	
	private static final StorageSystem storageSystem = new StorageSystem();
	
	// last copy of the flights database
	private List<Flight> flights;
	
	// last copy of bookings
	private List<Booking> bookings;
	private List<PendingBooking> pendingBookings;
	private List<RejectedBooking> rejectedBookings;
	
	// information timestamp
	private Long informationTimeStamp;
	
	// request buffer
	private List<Request> pendingRequest;
	
	private StorageSystem() {
		flights = new ArrayList<Flight>();
		pendingRequest = new ArrayList<Request>(BUFFER_SIZE);
		
		bookings = null;
		pendingBookings = null;
		rejectedBookings = null;
	}
	
	public static StorageSystem getInstance() {
		return storageSystem;
	}
	
	public synchronized List<Flight> getFlights() {
		return flights;
	}
	
	public synchronized void saveFlightInformation(List<Flight> flights) {
		this.flights = flights;
	}
	
	public synchronized List<Request> getRequestBuffer() {
		return pendingRequest;
	}
	
	public synchronized void clearRequestBuffer() {
		pendingRequest.clear();
	}
	
	public synchronized int addNewRequest(Request request) {
		int result = 0;
		
		if (pendingRequest.size() < BUFFER_SIZE && 
				!pendingRequest.contains(request)) {
			pendingRequest.add(request);
			result = 1;
		}
		
		return result;
	}
	
	public synchronized int checkBookingState(String bookingReference) {
		// is confirmed?
		if (bookings != null) {
			for (Booking booking : bookings) {
				if (booking.getId().equals(bookingReference))
					return StatusResource.CONFIRMED;
			}
		}
		// is pending?
		if (pendingBookings != null) {
			for (PendingBooking booking : pendingBookings) {
				if (booking.getId().equals(bookingReference))
					return StatusResource.PENDING;
			} 
		}
		// is rejected?
		if (rejectedBookings != null) {
			for (RejectedBooking booking : rejectedBookings) {
				if (booking.getId().equals(bookingReference))
					return StatusResource.REJECTED;
			}
		}
		return 0;
	}
	
	public synchronized void setBookings(List<Booking> Bookings) {
		this.bookings = Bookings;

	}
	
	public synchronized void setPendingBookings(List<PendingBooking> pendingBookings) {
		this.pendingBookings = pendingBookings;
	}
	
	public synchronized void setRejectedBookings(List<RejectedBooking> rejectedBookings) {
		this.rejectedBookings = rejectedBookings;
	}
	
	public synchronized void setNewInformationTimeStamp() {
		this.informationTimeStamp = System.currentTimeMillis();
	}
	
	public synchronized long getInformationTimeStamp() {
		return informationTimeStamp;
	}
	
	public String getInformationTimeStampFormatted(){
		String result = "No db info";
		Long milliseconds = null;
		if (informationTimeStamp != null) {
			synchronized(informationTimeStamp) {
				milliseconds = informationTimeStamp;
			}

			int seconds = (int) (milliseconds / 1000) % 60 ;
			int minutes = (int) ((milliseconds / (1000*60)) % 60);
			int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
			result = hours + ":" + minutes + ":" + seconds;
		}
		return result;
	}
}
