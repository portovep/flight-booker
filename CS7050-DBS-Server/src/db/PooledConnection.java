package db;

import java.sql.Connection;

public class PooledConnection {
	
	private Connection connection;
	private boolean inUse;
	private boolean extra;
	
	public PooledConnection(Connection connection){
		this.connection = connection;
		inUse = false;
		extra = false;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public boolean isInUse() {
		return inUse;
	}

	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}

	public boolean isExtra() {
		return extra;
	}

	public void setExtra(boolean extra) {
		this.extra = extra;
	}

}
