package com.privy.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {

// DB Path
private static final String databaseURL= "jdbc:sqlite:privy.db";
	
	// DB connection
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(databaseURL);
	}
	
	// Login logic
	public boolean checkLogin(String username, String password) {
		String findUser = "SELECT * FROM users WHERE username = ? AND password = ?";
		
		try (
				Connection conn = getConnection();
				PreparedStatement statement = conn.prepareStatement(findUser);
				) {
			statement.setString(1, username);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();
			boolean isLogin = rs.next();
			return isLogin;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
	
}
