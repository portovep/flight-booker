package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Request implements Serializable {
	
	public static final String ACTION_SEARCH = "Search";
	public static final String ACTION_BOOK = "Book";
	public static final String ACTION_CANCEL = "Cancel";
	public static final String ACTION_CHECK = "Check";
	
	public static final String ACTION_SEND_BUFFER = "SendBuffer";
	public static final String ACTION_GET_BOOKINGS = "GetBookings";

	private String action;
	private Object resource;
	
	public Request(String action) {
		this.action = action;
		resource = null;
	}
	
	public Request(String action, Object resource) {
		super();
		this.action = action;
		this.resource = resource;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Object getResource() {
		return resource;
	}

	public void setResource(Object resource) {
		this.resource = resource;
	}
	
}
