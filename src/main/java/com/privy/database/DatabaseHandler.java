package com.privy.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.privy.helper.HashPassword;
import com.privy.helper.SecurityQuestions;
import com.privy.model.NewUser;
import com.privy.model.User;
import com.privy.model.Vault;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseHandler {

// Hash Password model class
private HashPassword hashPassword;

// DB Path
private static final String databaseURL= "jdbc:sqlite:privy.db";
	
	// DB connection
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(databaseURL);
	}
	
	/*
	 * Login Section
	 * 
	 */
	
	// Login logic
	public User checkLogin(String username, String password) {
		String findUser = "SELECT id, username FROM users WHERE username = ? AND password = ?";
		hashPassword = new HashPassword(password);
		String hex = hashPassword.getPassword();
		
		try (
				Connection conn = getConnection();
				PreparedStatement statement = conn.prepareStatement(findUser);
				) {
			statement.setString(1, username);
			statement.setString(2, hex);
			ResultSet rs = statement.executeQuery();
			
			if (rs.next()) {
				return new User(rs.getInt("id"), rs.getString("username"));
			}
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return null;
	}
	
	/*
	 * Dashboard Section
	 * 
	 */
	
	// fetch data to table
	public ObservableList<Vault> fetchDBToTable(int currentID) {
		
		ObservableList<Vault> observableList = FXCollections.observableArrayList();
		String select = "SELECT * FROM vault WHERE login_id = ?";
		
		try (
				Connection conn = getConnection();
				PreparedStatement preparedStatement = conn.prepareStatement(select);
				) {
			
			preparedStatement.setInt(1, currentID);
			ResultSet rs = preparedStatement.executeQuery();
			 while (rs.next()) {
				 int id = rs.getInt("id");
				 String urlName = rs.getString("url_name");
				 String urlString = rs.getString("url");
				 String userName = rs.getString("username");
				 String password = rs.getString("password");
				 int loginId = rs.getInt("login_id");
				 
				 Vault vaultRow = new Vault(id, urlName, urlString, userName, password, loginId);
				 observableList.add(vaultRow);
			 }
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return observableList;
		
	}
	
	public boolean dashboardUpdatePassword(String urlName, String url, String username, String password, int id, int loginID) throws Exception {
		
		String sqlUpdatePassword = "UPDATE vault SET url_name = ?, url = ?, username = ?, password = ? WHERE id = ? AND login_id = ?";
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sqlUpdatePassword);) {
			
			pstmt.setString(1, urlName);
			pstmt.setString(2, url);
			pstmt.setString(3, username);
			pstmt.setString(4, password);
			
			pstmt.setInt(5, id);
			pstmt.setInt(6, loginID);
			
			return pstmt.executeUpdate() == 1;
			
			
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
		
	}
	
	/*
	 * Create New User Account Section
	 * 
	 */
	
	// fetch security questions
	public ObservableList<SecurityQuestions> fetchSecurityQuestions() {
		
		ObservableList<SecurityQuestions> comboList = FXCollections.observableArrayList();
		
		String secQuestion = "SELECT * FROM security_questions";
		
		try (
				Connection conn = getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(secQuestion);
				){
			
			while (rs.next()) {				
				int id = rs.getInt("id");
				String questions = rs.getString("security_question");
				SecurityQuestions sq = new SecurityQuestions(id, questions);
				
				comboList.add(sq);
			}
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return comboList;
	}
	
	// Create new user account
	public NewUser addNewUser(String userName, String password, String email, int secQuestion, String answer) throws Exception {
		
		String insertSQL = "INSERT INTO users (username, password, email, question_id, security_answer) VALUES (?, ?, ?, ?, ?)";
		hashPassword = new HashPassword(password);
		String hex = hashPassword.getPassword();
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(insertSQL);
				) {
			
			pstmt.setString(1, userName);
			pstmt.setString(2, hex);
			pstmt.setString(3, email);
			pstmt.setInt(4, secQuestion);
			pstmt.setString(5, answer);
			int rows = pstmt.executeUpdate();					
			
			if (rows == 1) {
				return new NewUser(userName, hex, email, secQuestion, answer);
			}
			
		} catch (SQLException e) {
	        String sqlState = e.getSQLState();
	        if ("23505".equals(sqlState) || e.getMessage().contains("duplicate") || e.getMessage().contains("UNIQUE")) {
	        	throw new Exception("Username or email already exists.");
	        } else {
	        	throw new Exception("Database error: " + e.getMessage());
	        }
	    }
		
		return null;
		
	}
	
	/*
	 * Forgot Password Section
	 * 
	 */
	
	
	public SecurityQuestions confirmEmail(String email) throws Exception {
		
		String findEmail = "SELECT question_id FROM users WHERE email = ?";
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(findEmail);
				){
			
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt("question_id");
				return new SecurityQuestions(id, null);
			}
			
			throw new Exception("Email does not exist!");
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
	}
	
	public SecurityQuestions questionID(int id) throws Exception {
		
		String findQuestion = "SELECT security_question FROM security_questions WHERE id = ?";
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(findQuestion);
				) {
			
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String sq = rs.getString("security_question");
				return new SecurityQuestions(id, sq);
			}
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return null;
	}
	
	public User getAnswer(String answer, String email) throws SQLException {
		
		String findAnswer = "SELECT * FROM users WHERE security_answer = ? AND email = ?";
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(findAnswer);
				) {
			
			pstmt.setString(1, answer);
			pstmt.setString(2, email);
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				int id = rs.getInt("id");
				String username = rs.getString("username");
				return new User(id, username);
			} 				
			
			throw new SQLException("Incorrect Answer.");
				
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}
	
	public boolean updatePassword(String password, String username) {
		
		String update = "UPDATE users SET password = ? WHERE username = ?";
		
		hashPassword = new HashPassword(password);
		String hex = hashPassword.getPassword();
		
		try (
				Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(update);
				) {
			
			pstmt.setString(1, hex);
			pstmt.setString(2, username);
			
			return pstmt.executeUpdate() == 1;
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}
	
}
