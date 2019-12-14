package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import entity.Image;
import entity.Image.ImageBuilder;

public class MySQLTest {

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
			
			// query test
//			List<Image> images = new ArrayList<>();
//			String camera_id = "0001";
//			String startTime = "2019-11-27 16: 00: 00";
//			String sql = "SELECT * FROM images WHERE camera_id = ? AND capture_time >= ?";
//			PreparedStatement stmt = conn.prepareStatement(sql);
//			stmt.setString(1, camera_id);
//			stmt.setString(2, startTime);
//			System.out.println(stmt);
//			ResultSet rs = stmt.executeQuery();
//			ImageBuilder builder = new ImageBuilder();
//			while (rs.next()) {
//				builder.setFilename(rs.getString("filename"));
//				builder.setCamera_id(rs.getString("camera_id"));
//				builder.setCapture_time(rs.getString("capture_time"));
//				builder.setMLResult(rs.getString("mlResult"));
//				builder.setUrl(rs.getString("url"));
//				builder.setComment(rs.getString("comments"));
//				images.add(builder.build());
//			}
//			System.out.println(images.size());
			
			// save test
			ImageBuilder builder = new ImageBuilder();
			builder.setFilename("Insert test filename");
			builder.setCamera_id("0001");
			builder.setCapture_time("2019-11-28 10:58:42");
			builder.setMLResult("Insert result");
			builder.setUrl("urlurl");
			builder.setComment("Insert test");
			Image image = builder.build();
			
			String sql = "INSERT IGNORE INTO images (filename, camera_id, url, capture_time, mlResult, comments)"
					+ "VALUES ( ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, image.getFilename());
			stmt.setString(2, image.getCameraID());
			stmt.setString(3, image.getUrl());
			stmt.setString(4, image.getCaptureTime());
			stmt.setString(5, image.getMLResult());
			stmt.setString(6, image.getComment());
			System.out.println(stmt.execute());
			
			System.out.println("Import is done successfully.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
