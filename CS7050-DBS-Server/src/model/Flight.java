package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Flight implements Serializable {
	
	private int id;
	private int companyId;
	private int helicopterId;
	private String departureTime;
	private String arrivalTime;
	private String date;
	private String from;
	private String to;
	
	public Flight(){}

	public Flight(int id, int companyId, int helicopterId, String departureTime,
			String arrivalTime, String date, String from, String to) {

		this.id = id;
		this.companyId = companyId;
		this.helicopterId = helicopterId;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.date = date;
		this.from = from;
		this.to = to;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public int getHelicopterId() {
		return helicopterId;
	}

	public void setHelicopterId(int helicopterId) {
		this.helicopterId = helicopterId;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
}
