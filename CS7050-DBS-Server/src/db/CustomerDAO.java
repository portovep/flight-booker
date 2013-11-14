package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Customer;

public class CustomerDAO {

	private PooledConnection pooledConnection = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	public CustomerDAO() {}
	
	public int insert(Customer customer) {
		int result = 0;
		try {
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			
			String query = "INSERT INTO Customer VALUES(?,?)";
			ps = connection.prepareStatement(query);
			ps.setString(1, customer.getId());
			ps.setString(2, customer.getName());
			
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
	
	public Customer findByPK(String primarykey) {
		Customer customer = null;
		
		try {
			String query = "SELECT * FROM Customer WHERE idCustomer='" + primarykey + "';";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs.next()) {
				customer = new Customer();
				customer.setId(rs.getString(1));
				customer.setName(rs.getString(2));
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
		return customer;
	}
}
