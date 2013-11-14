package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectionPoolFactory{
	
	public static final String DRIVER_NAME = "org.gjt.mm.mysql.Driver" ;
		
	// DB information
    public static final String HOST = "localhost";
    public static final int PORT = 3306;
    public static final String DB_NAME = "cs7050dbs";
    public static final String DB_USER_NAME = "dbs";
    public static final String DB_USER_PSWD = "dbs";
    
    public static final String DB_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME; 
    
    // Pool configuration
    public static final int INIT_CONNECTIONS = 15;
    
    private static ArrayList<PooledConnection> connections = null;

    
    private static final ConnectionPoolFactory connectionPool = new ConnectionPoolFactory();
    
    private ConnectionPoolFactory() {
		try {
			Class.forName(DRIVER_NAME);
			
			// Init connection pool
			connections = new ArrayList<PooledConnection>();
			
			// creating initial connections
			PooledConnection connection = null;
			for (int i=0; i<INIT_CONNECTIONS; i++) {
				connection = getNewPooledConnection();
		    	connections.add(connection);
			}
			
		} catch (java.lang.ClassNotFoundException e) {
			System.err.println("MySQL JDBC Driver not found..");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}	
    }
    
    public static ConnectionPoolFactory getInstance() {
    	return connectionPool;
    }
    
    public synchronized PooledConnection getConnection() throws SQLException {
    	
    	PooledConnection availableConnection = null;
    	for (PooledConnection connection : connections ) {
    		if (!connection.isInUse())
    			availableConnection = connection;
    			break;
    	}
    	if (availableConnection == null) {
    		// all connections in use
    			// create a extraordinary connection
    		availableConnection = getNewPooledConnection();
    		availableConnection.setExtra(true);
    	}
    	availableConnection.setInUse(true);
    	return availableConnection;
    }
    
    public synchronized void releaseConnection(PooledConnection connection) throws SQLException{
    	if (connection.isExtra()) {
    		// if it was a extraordinary connection
    			// close the connection
    		connection.getConnection().close();
    	}
    	else {
    		// release the connection resource
			connection.setInUse(false);
    	}
    }
    
    private PooledConnection getNewPooledConnection() throws SQLException {
    	Connection connection = null;
    	connection = DriverManager.getConnection(DB_URL, DB_USER_NAME , DB_USER_PSWD);
    	PooledConnection pc = new PooledConnection(connection);
    	return pc;
    }
    
}
