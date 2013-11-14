package model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Response implements Serializable {
	
	public static final int OK = 1;
	public static final int No_INFO = 2;
	public static final int ERROR = 3;
	
	private String toAction;
	private int status;
	private Object resource;
	public String getToAction() {
		return toAction;
	}
	public void setToAction(String toAction) {
		this.toAction = toAction;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Object getResource() {
		return resource;
	}
	public void setResource(Object resource) {
		this.resource = resource;
	}	

}
