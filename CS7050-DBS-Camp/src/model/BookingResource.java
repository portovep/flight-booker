package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BookingResource implements Serializable {

	private int flightID;
	private String bookingReference;
	private String userID;
	private String userName;
	private Long timeStamp;
	private BookingResource returnFlight;
	
	public BookingResource() {
		timeStamp = System.currentTimeMillis();
		returnFlight = null;
	}
	
	public int getFlightID() {
		return flightID;
	}
	public void setFlightID(int flightID) {
		this.flightID = flightID;
	}
	public String getBookingReference() {
		return bookingReference;
	}
	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public BookingResource getReturnFlight() {
		return returnFlight;
	}

	public void setReturnFlight(BookingResource returnFlight) {
		this.returnFlight = returnFlight;
	}
	
}
