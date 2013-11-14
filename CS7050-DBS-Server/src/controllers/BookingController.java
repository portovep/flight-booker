package controllers;

import java.util.List;

import db.BookingDAO;
import db.CustomerDAO;
import db.PendingBookingDAO;
import db.RejectedBookingDAO;

import model.Booking;
import model.Customer;
import model.PendingBooking;
import model.RejectedBooking;
import model.StatusResource;

public class BookingController {

	public int confirmPendingBooking(String bookingID){
		int result = -1;
		
		PendingBookingDAO pbDAO = new PendingBookingDAO();
		BookingDAO bDAO = new BookingDAO();
		PendingBooking pendingBooking = pbDAO.findByPK(bookingID);
		if (pendingBooking != null){
			// copy info from pending booking to confirmed booking
			Booking booking = new Booking(pendingBooking);
			// adding confirmed booking
			if (bDAO.insert(booking) == 1) {
				// deleting pending booking
				result = pbDAO.delete(bookingID);
			}
		}	
		return result;
	}
	
	public int confirmPendingBookingWithAlternativeFlight(String bookingID,
			int alternativeFlightID){
		int result = -1;
		
		
		PendingBookingDAO pbDAO = new PendingBookingDAO();
		BookingDAO bDAO = new BookingDAO();
		PendingBooking pendingBooking = pbDAO.findByPK(bookingID);
		if (pendingBooking != null){
			// changing the flight to implement load-share
			pendingBooking.setIdFlight(alternativeFlightID);
			
			// copy info from pending booking to confirmed booking
			Booking booking = new Booking(pendingBooking);
			// adding confirmed booking
			if (bDAO.insert(booking) == 1) {
				// deleting pending booking
				result = pbDAO.delete(bookingID);
			}
		}	
		return result;
	}
	
	public int rejectPendingBooking(String bookingID){
		int result = -1;
		
		PendingBookingDAO pbDAO = new PendingBookingDAO();
		RejectedBookingDAO rbDAO = new RejectedBookingDAO();
		PendingBooking pendingBooking = pbDAO.findByPK(bookingID);
		if (pendingBooking != null){
			// copy info from pending booking to rejected booking
			RejectedBooking rejectedBooking = new RejectedBooking(pendingBooking);
			// adding rejected booking
			if (rbDAO.insert(rejectedBooking) == 1) {
				// deleting pending booking
				result = pbDAO.delete(bookingID);
			}
		}	
		return result;
	}
	
	public int addPendingBooking(String bookingReference, Long timeStamp, int flightID,
			String customerID, String CustomerName){
		int result = -1;
		
		
		// check if exist flight
		FlightController fController = new FlightController();
		if (!fController.existFlight(flightID))
			return 0;
		
		// TRANSACTION NEEDED
		CustomerDAO cDAO = new CustomerDAO();
		if (cDAO.findByPK(customerID) == null) {
			// adding new customer			
			Customer customer = new Customer();
			customer.setId(customerID);
			customer.setName(CustomerName);

			result = cDAO.insert(customer);			
		}
		
		// adding pending booking
		PendingBooking pendingBooking = new PendingBooking();
		pendingBooking.setId(bookingReference);
		pendingBooking.setTimestamp(Long.toString(timeStamp));
		pendingBooking.setIdFlight(flightID);
		pendingBooking.setIdCustomer(customerID);
		
		PendingBookingDAO pbDAO = new PendingBookingDAO();
		result = pbDAO.insert(pendingBooking); 
		
		return result;
	}
	
	public int cancelBooking(String bookingReference){
		int result = -1;
		
		// is a pending booking?
		PendingBookingDAO pbDAO = new PendingBookingDAO();
		result = pbDAO.delete(bookingReference);
		if (result < 1) {
			// is a confirmed booking?
			BookingDAO bDAO = new BookingDAO();
			result = bDAO.delete(bookingReference);
			if (result < 1) {
				// is a rejected booking?
				RejectedBookingDAO rbDAO = new RejectedBookingDAO();
				result = rbDAO.delete(bookingReference);
			}
		}
		return result;
	}
	
	public int checkBookingStatus(String bookingReference) {
		int result = -1;
		
		BookingDAO bDAO = new BookingDAO();
		// is confirmed
		if (bDAO.findByPK(bookingReference) != null)
			result = StatusResource.CONFIRMED;
		else {
			// is a pending booking?
			PendingBookingDAO pbDAO = new PendingBookingDAO();
			if (pbDAO.findByPK(bookingReference) != null)
				result = StatusResource.PENDING;
			else {
				// is a rejected booking?
				RejectedBookingDAO rbDAO = new RejectedBookingDAO();
				if (rbDAO.findByPK(bookingReference) != null) {
					result = StatusResource.REJECTED;
				}
				else
					// booking reference not found
					result = 0;
			}
		}	
		return result;
	}
	
	public List<Booking> getBookings() {
		List<Booking> bookings = null;
		
		BookingDAO bDAO = new BookingDAO();
		bookings = bDAO.findAll();
		
		return bookings;
	}
	
	public List<PendingBooking> getPendingBookings() {
		List<PendingBooking> pendingBookings = null;
		
		PendingBookingDAO pbDAO = new PendingBookingDAO();
		pendingBookings = pbDAO.findAll();
		
		return pendingBookings;
	}
	
	public List<RejectedBooking> getRejectedBookings() {
		List<RejectedBooking> rejectedBookings = null;
		
		RejectedBookingDAO rbDAO = new RejectedBookingDAO();
		rejectedBookings = rbDAO.findAll();
		
		return rejectedBookings;
	}

}
