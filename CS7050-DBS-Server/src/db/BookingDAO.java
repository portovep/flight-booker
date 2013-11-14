package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Booking;

public class BookingDAO {
	private PooledConnection pooledConnection = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	public BookingDAO() {}
	
	public int insert(Booking booking) {
		int result = 0;
		try {
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			
			String query = "INSERT INTO Booking VALUES(?,?,?,?)";
			ps = connection.prepareStatement(query);
			ps.setString(1, booking.getId());
			ps.setString(2, booking.getTimestamp());
			ps.setInt(3, booking.getIdFlight());
			ps.setString(4, booking.getIdCustomer());

			
			result = ps.executeUpdate();
		} catch(SQLException sqle) {
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
		return result;
	}
	
	public int delete(String primaryKey) {
		int result = 0;
		try {
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			
			String query = "DELETE FROM Booking WHERE" +
					" idBooking='" + primaryKey + "';";
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
	
	public Booking findByPK(String primarykey) {
		Booking booking = null;
		
		try {
			String query = "SELECT * FROM Booking WHERE idBooking='" + primarykey + "';";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs.next()) {
				booking = new Booking();
				booking.setId(rs.getString(1));
				booking.setTimestamp(rs.getString(2));
				booking.setIdCustomer(rs.getString(3));
				booking.setIdCustomer(rs.getString(4));
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
		return booking;
	}
	
	public List<Booking> findAll() {
		List<Booking> bookings = new ArrayList<Booking>();
		
		try {
			String query = "SELECT * FROM Booking;";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			
			Booking booking = null;
			while (rs.next()) {
				booking = new Booking();
				booking.setId(rs.getString(1));
				booking.setTimestamp(rs.getString(2));
				booking.setIdCustomer(rs.getString(3));
				booking.setIdCustomer(rs.getString(4));
				
				bookings.add(booking);
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
		return bookings;
	}
	
	public List<Booking> findBookingsByFlight(int flightID) {
		List<Booking> bookings = new ArrayList<Booking>();
		
		try {
			String query = "SELECT * FROM Booking WHERE idFlight='" + flightID + "';";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			
			Booking booking = null;
			while (rs.next()) {
				booking = new Booking();
				booking.setId(rs.getString(1));
				booking.setTimestamp(rs.getString(2));
				booking.setIdCustomer(rs.getString(3));
				booking.setIdCustomer(rs.getString(4));
				
				bookings.add(booking);
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
		return bookings;
	}

}
