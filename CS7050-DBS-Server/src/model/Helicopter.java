package model;

public class Helicopter {

	private int id;
	private int seatsNO;
	private int companyID;
	
	public Helicopter() {
		
	}
	
	public Helicopter(int helicopterID, int seatsNO, int companyID) {
		super();
		this.id = helicopterID;
		this.seatsNO = seatsNO;
		this.companyID = companyID;
	}

	public int getID() {
		return id;
	}

	public void setID(int helicopterID) {
		this.id = helicopterID;
	}

	public int getSeatsNO() {
		return seatsNO;
	}

	public void setSeatsNO(int seatsNO) {
		this.seatsNO = seatsNO;
	}

	public int getCompanyID() {
		return companyID;
	}

	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}
	
}
