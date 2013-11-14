package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class StatusResource implements Serializable{

	public static final int CONFIRMED = 1;
	public static final int PENDING = 2;
	public static final int REJECTED = 3;
	
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
