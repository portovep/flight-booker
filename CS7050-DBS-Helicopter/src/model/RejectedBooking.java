package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class RejectedBooking implements Serializable {

	private String id;
	
	public RejectedBooking() {}
	
	public RejectedBooking(PendingBooking pb) {
		this.id = pb.getId();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
