package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.PendingBooking;

public class PendingBookingDAO {
	private PooledConnection pooledConnection = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	public PendingBookingDAO() {}
	
	public int insert(PendingBooking pbooking) {
		int result = -1;
		try {
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			
			String query = "INSERT INTO PendingBooking VALUES(?,?,?,?)";
			ps = connection.prepareStatement(query);
			ps.setString(1, pbooking.getId());
			ps.setString(2, pbooking.getTimestamp());
			ps.setInt(3, pbooking.getIdFlight());
			ps.setString(4, pbooking.getIdCustomer());
			
			result = ps.executeUpdate();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}finally {
			try {
				if (ps != null)
					ps.close();
				if (pooledConnection != null)
					ConnectionPoolFactory.getInstance().releaseConnection(pooledConnection);
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
		return result;
	}
	
	public int delete(String primaryKey) {
		int result = 0;
		try {
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			
			String query = "DELETE FROM PendingBooking WHERE" +
					" idPendingBooking='" + primaryKey + "';";
			ps = connection.prepareStatement(query);
			result = ps.executeUpdate();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}finally {
			try {
				if (ps != null)
					ps.close();
				if (pooledConnection != null)
					ConnectionPoolFactory.getInstance().releaseConnection(pooledConnection);
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
		return result;
	}
	
	public PendingBooking findByPK(String primarykey) {
		PendingBooking pbooking = null;
		
		try {
			String query = "SELECT * FROM PendingBooking WHERE idPendingBooking='" + primarykey + "';";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs.next()) {
				pbooking = new PendingBooking();
				pbooking.setId(rs.getString(1));
				pbooking.setTimestamp(rs.getString(2));
				pbooking.setIdFlight(rs.getInt(3));
				pbooking.setIdCustomer(rs.getString(4));
			}			
			
		}catch(SQLException sqle) {
			sqle.printStackTrace();
		}finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (pooledConnection != null)
					ConnectionPoolFactory.getInstance().releaseConnection(pooledConnection);
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
		return pbooking;
	}
	
	public List<PendingBooking> findAll() {
		List<PendingBooking> pendingBookings = new ArrayList<PendingBooking>();
		
		try {
			String query = "SELECT * FROM PendingBooking;";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			
			PendingBooking pbooking = null;
			while (rs.next()) {
				pbooking = new PendingBooking();
				pbooking.setId(rs.getString(1));
				pbooking.setTimestamp(rs.getString(2));
				pbooking.setIdCustomer(rs.getString(3));
				pbooking.setIdCustomer(rs.getString(4));
				
				pendingBookings.add(pbooking);
			}			
			
		}catch(SQLException sqle) {
			sqle.printStackTrace();
		}finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (pooledConnection != null)
					ConnectionPoolFactory.getInstance().releaseConnection(pooledConnection);
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
		return pendingBookings;
	}
	
	public List<PendingBooking> findByFlightIDOrderedByTimeStamp(int flightID) {
		List<PendingBooking> pendingBookings = new ArrayList<PendingBooking>();
		
		try {
			String query = "SELECT * FROM PendingBooking " +
						   "WHERE idFlight ='" + flightID + 
						   "' ORDER BY timestamp;";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			
			PendingBooking pbooking = null;
			while (rs.next()) {
				pbooking = new PendingBooking();
				pbooking.setId(rs.getString(1));
				pbooking.setTimestamp(rs.getString(2));
				pbooking.setIdCustomer(rs.getString(3));
				pbooking.setIdCustomer(rs.getString(4));
				
				pendingBookings.add(pbooking);
			}			
			
		}catch(SQLException sqle) {
			sqle.printStackTrace();
		}finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (pooledConnection != null)
					ConnectionPoolFactory.getInstance().releaseConnection(pooledConnection);
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
		return pendingBookings;
	}
	
	public List<Integer> getFlightIDs() {
		List<Integer> flightIDs = new ArrayList<Integer>();
		
		try {
			String query = "SELECT idFlight FROM PendingBooking" +
					" GROUP BY idFlight;";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				flightIDs.add(rs.getInt(1));
			}			
			
		}catch(SQLException sqle) {
			sqle.printStackTrace();
		}finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (pooledConnection != null)
					ConnectionPoolFactory.getInstance().releaseConnection(pooledConnection);
			} catch(SQLException sqle){
				sqle.printStackTrace();
			}
		}
		return flightIDs;
	}

}
