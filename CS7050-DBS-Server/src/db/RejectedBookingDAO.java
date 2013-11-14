package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.RejectedBooking;

public class RejectedBookingDAO {
	private PooledConnection pooledConnection = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	public RejectedBookingDAO() {}
	
	public int insert(RejectedBooking rejectedBooking) {
		int result = 0;
		try {
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			
			String query = "INSERT INTO RejectedBooking VALUES(?)";
			ps = connection.prepareStatement(query);
			ps.setString(1, rejectedBooking.getId());

			
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
			
			String query = "DELETE FROM RejectedBooking WHERE" +
					" idRejectedBooking='" + primaryKey + "';";
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
	
	public RejectedBooking findByPK(String primarykey) {
		RejectedBooking rejectedBooking = null;
		
		try {
			String query = "SELECT * FROM RejectedBooking WHERE idRejectedBooking='" + primarykey + "';";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs.next()) {
				rejectedBooking = new RejectedBooking();
				rejectedBooking.setId(rs.getString(1));
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
		return rejectedBooking;
	}
	
	public List<RejectedBooking> findAll() {
		List<RejectedBooking> rejectedBookings = new ArrayList<RejectedBooking>();
		
		try {
			String query = "SELECT * FROM RejectedBooking;";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			
			RejectedBooking rejectedBooking = null;
			while (rs.next()) {
				rejectedBooking = new RejectedBooking();
				rejectedBooking.setId(rs.getString(1));
				
				rejectedBookings.add(rejectedBooking);
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
		return rejectedBookings;
	}

}
