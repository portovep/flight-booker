package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Helicopter;

public class HelicopterDAO {

	private PooledConnection pooledConnection = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	public HelicopterDAO() {}
	
	public Helicopter findByPK(int primarykey) {
		Helicopter helicopter = null;
		
		try {
			String query = "SELECT * FROM Helicopter WHERE idHelicopter='" + primarykey + "';";
			pooledConnection = ConnectionPoolFactory.getInstance().getConnection();
			Connection connection = pooledConnection.getConnection();
			ps = connection.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs.next()) {
				helicopter = new Helicopter();
				helicopter.setID(rs.getInt(1));
				helicopter.setSeatsNO(rs.getInt(2));
				helicopter.setCompanyID(rs.getInt(3));
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
		return helicopter;
	}
}
