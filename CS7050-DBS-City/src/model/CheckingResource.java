package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CheckingResource implements Serializable{

	private String bookingReference;

	public String getBookingReference() {
		return bookingReference;
	}

	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
	}
	
}
