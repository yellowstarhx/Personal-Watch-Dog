package db.mysql;

import java.sql.*;

public class MySQLTableCreation {
	// reset db schema.
	public static void main(String[] args) {
		try {
			// this is java.sql.Connection. Not com.mysql.jdbc.Connection
			Connection conn = null;
			
			// Step 1 Connect to MySQL
			try {
				System.out.println("Connecting to " + MySQLDBUtil.URL);
				Class.forName("com.mysql.jdbc.Driver").getConstructor().newInstance();
				conn = DriverManager.getConnection(MySQLDBUtil.URL);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if (conn == null) {
				return;
			}
			
			// Step 2 drop table if exist
			Statement stmt = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS images";
			stmt.executeUpdate(sql);
			sql = "DROP TABLE IF EXISTS cameras";
			stmt.executeUpdate(sql);
			
			// Step 3 create new table
			sql = "CREATE TABLE cameras ("
					+ "camera_id VARCHAR(255) NOT NULL,"
					+ "owner_name VARCHAR(255) NOT NULL,"
					+ "password VARCHAR(255),"
					+ "owner_address VARCHAR(255),"
//					+ "last_watch_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
					+ "PRIMARY KEY (camera_id))";
			stmt.executeUpdate(sql);
			
			sql = "CREATE TABLE images ("
					+ "image_id int(10) NOT NULL AUTO_INCREMENT,"	// be careful overflow
					+ "filename VARCHAR(255) NOT NULL,"
					+ "camera_id VARCHAR(255) NOT NULL,"
					+ "url VARCHAR(255) NOT NULL,"
					+ "capture_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
					+ "mlResult VARCHAR(255),"	// what does ml return ?
					+ "comments VARCHAR(255),"
					+ "PRIMARY KEY (image_id),"
					+ "FOREIGN KEY (camera_id) REFERENCES cameras(camera_id))";
			stmt.executeUpdate(sql);
			
			// Step 4: insert test data
			sql = "INSERT INTO cameras VALUES ("
					+ "'0001', 'Yani Jin', 'jynjynjyn','Somewhere in Los Angeles, CA, 90007')";
			System.out.println("Executing query: " + sql);
			stmt.executeUpdate(sql);
			
			System.out.println("Import is done successfully.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
