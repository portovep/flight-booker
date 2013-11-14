package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Booking implements Serializable{

	private String id;
	private String timestamp;
	private int idFlight;
	private String idCustomer;
	
	public Booking(){};
	
	public Booking(String id, String timestamp, int idFlight, String idCustomer) {
		this.id = id;
		this.timestamp = timestamp;
		this.idFlight = idFlight;
		this.idCustomer = idCustomer;
	}
	
	public Booking(PendingBooking pb) {
		this.id = pb.getId();
		this.timestamp = pb.getTimestamp();
		this.idFlight = pb.getIdFlight();
		this.idCustomer = pb.getIdCustomer();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public int getIdFlight() {
		return idFlight;
	}
	public void setIdFlight(int idFlight) {
		this.idFlight = idFlight;
	}
	public String getIdCustomer() {
		return idCustomer;
	}
	public void setIdCustomer(String idCustomer) {
		this.idCustomer = idCustomer;
	}

}
