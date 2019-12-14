package db.mysql;

import java.util.ArrayList;
import java.util.List;

import db.DBConnection;
import entity.Image;
import entity.Image.ImageBuilder;

import java.sql.*;

public class MySQLConnection implements DBConnection {
	
	private Connection conn;
	
	public MySQLConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(MySQLDBUtil.URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean saveImage(Image image) {
		if (conn == null) {
			return false;
		}
		try {
			
			String sql = "INSERT IGNORE INTO images (filename, camera_id, url, capture_time, mlResult, comments)"
					+ "VALUES ( ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, image.getFilename());
			stmt.setString(2, image.getCameraID());
			stmt.setString(3, image.getUrl());
			stmt.setString(4, image.getCaptureTime());
			stmt.setString(5, image.getMLResult());
			stmt.setString(6, image.getComment());
			stmt.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Image> searchImages(String camera_id, String startTime) {
		List<Image> images = new ArrayList<>();
		if (conn == null) {
			return images;
		}
		try {
			String sql = "SELECT * FROM images WHERE camera_id = ? AND capture_time >= ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, camera_id);
			stmt.setString(2, startTime);
			
			ResultSet rs = stmt.executeQuery();
			ImageBuilder builder = new ImageBuilder();
			while (rs.next()) {
				builder.setFilename(rs.getString("filename"));
				builder.setCamera_id(rs.getString("camera_id"));
				builder.setCapture_time(rs.getString("capture_time"));
				builder.setMLResult(rs.getString("mlResult"));
				builder.setUrl(rs.getString("url"));
				builder.setComment(rs.getString("comments"));
				images.add(builder.build());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return images;
	}

	@Override
	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
