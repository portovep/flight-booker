package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Flight;

public class FlightDAO {

	private PooledConnection pooledConnection = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	public FlightDAO() {}
	
	public Flight findByPK(int primarykey) {
		Flight flight = null;
		
		try {
			String query = "SELECT * FROM Flight WHERE idFlight='" + primarykey + "';";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs.next()) {
				flight = new Flight();
				flight.setId(rs.getInt(1));
				flight.setCompanyId(rs.getInt(2));
				flight.setHelicopterId(rs.getInt(3));
				flight.setDepartureTime(rs.getString(4));
				flight.setArrivalTime(rs.getString(5));
				flight.setDate(rs.getString(6));
				flight.setFrom(rs.getString(7));
				flight.setTo(rs.getString(8));
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
		return flight;
	}
	
	public ArrayList<Flight> findAll() {
		ArrayList<Flight> flights = new ArrayList<Flight>();
		
		try {
			String query = "SELECT * FROM Flight;";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			
			Flight flight = null;
			while (rs.next()) {
				flight = new Flight();
				flight.setId(rs.getInt(1));
				flight.setCompanyId(rs.getInt(2));
				flight.setHelicopterId(rs.getInt(3));
				flight.setDepartureTime(rs.getString(4));
				flight.setArrivalTime(rs.getString(5));
				flight.setDate(rs.getString(6));
				flight.setFrom(rs.getString(7));
				flight.setTo(rs.getString(8));
				
				flights.add(flight);
			}			
			
		}catch(SQLException sqle) {
			flights = null;
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
		return flights;
	}
	
	public ArrayList<Flight> findBySameTime(String date, String departureTime,
			String from, int currentFlightID) {
		ArrayList<Flight> flights = new ArrayList<Flight>();
		
		try {
			String query = "SELECT * FROM Flight WHERE date='" +
					date + "' AND departureTime='" +
					departureTime + "' AND idFlight!='" + currentFlightID + "' AND fromPlace='" +
					from + "';";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			
			Flight flight = null;
			while (rs.next()) {
				flight = new Flight();
				flight.setId(rs.getInt(1));
				flight.setCompanyId(rs.getInt(2));
				flight.setHelicopterId(rs.getInt(3));
				flight.setDepartureTime(rs.getString(4));
				flight.setArrivalTime(rs.getString(5));
				flight.setDate(rs.getString(6));
				flight.setFrom(rs.getString(7));
				flight.setTo(rs.getString(8));
				
				flights.add(flight);
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
		return flights;
	}
	
}
