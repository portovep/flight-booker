package model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class BufferResource implements Serializable {

	private List<Request> buffer;

	public List<Request> getBuffer() {
		return buffer;
	}

	public void setBuffer(List<Request> buffer) {
		this.buffer = buffer;
	}
	
}
