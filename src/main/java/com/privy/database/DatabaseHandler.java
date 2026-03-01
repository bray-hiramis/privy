package com.privy.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.privy.model.SecurityQuestions;
import com.privy.model.User;
import com.privy.model.Vault;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseHandler {

// DB Path
private static final String databaseURL= "jdbc:sqlite:privy.db";
	
	// DB connection
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(databaseURL);
	}
	
	// Login logic
	public User checkLogin(String username, String password) {
		String findUser = "SELECT id, username FROM users WHERE username = ? AND password = ?";
		
		try (
				Connection conn = getConnection();
				PreparedStatement statement = conn.prepareStatement(findUser);
				) {
			statement.setString(1, username);
			statement.setString(2, password);
			ResultSet rs = statement.executeQuery();
			
			if (rs.next()) {
				return new User(rs.getInt("id"), rs.getString("username"));
			}
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return null;
	}
	
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
				 System.out.println(vaultRow);
				 observableList.add(vaultRow);
			 }
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return observableList;
		
	}
	
	// To get security questions
	public ObservableList<SecurityQuestions> fetchSecurityQuestions() {
		
		ObservableList<SecurityQuestions> comboList = FXCollections.observableArrayList();
		
		String secQuestion = "SELECT * FROM security_questions";
		
		try (
				Connection conn = getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(secQuestion);
				){
			
			while (rs.next()) {				
				String questions = rs.getString("security_question");
				System.out.println(questions);
				SecurityQuestions sq = new SecurityQuestions(questions);
				
				comboList.add(sq);
			}
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return comboList;
	}
	
}
