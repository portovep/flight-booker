package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CancellationResource implements Serializable{

	private String bookingReference;

	public String getBookingReference() {
		return bookingReference;
	}

	public void setBookingReference(String bookingReference) {
		this.bookingReference = bookingReference;
	}
	
}
