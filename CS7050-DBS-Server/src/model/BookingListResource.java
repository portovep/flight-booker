package model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class BookingListResource implements Serializable {

	private List<Booking> bookings;
	private List<PendingBooking> pendingBookings;
	private List<RejectedBooking> rejectedBookings;
	
	public BookingListResource() {
		bookings = null;
		pendingBookings = null;
		rejectedBookings = null;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public List<PendingBooking> getPendingBookings() {
		return pendingBookings;
	}

	public void setPendingBookings(List<PendingBooking> pendingBookings) {
		this.pendingBookings = pendingBookings;
	}

	public List<RejectedBooking> getRejectedBookings() {
		return rejectedBookings;
	}

	public void setRejectedBookings(List<RejectedBooking> rejectedBookings) {
		this.rejectedBookings = rejectedBookings;
	}
	
	
}
