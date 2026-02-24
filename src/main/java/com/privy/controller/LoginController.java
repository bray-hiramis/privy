package com.privy.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import com.privy.database.DatabaseHandler;
import com.privy.model.User;
import com.privy.model.Vault;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable{
	
	@FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUsername;

	DatabaseHandler db = new DatabaseHandler();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			db.getConnection();
			System.out.println("Database connected: " + db.getConnection());
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}
	
	public void login(ActionEvent event) {
		String username = txtUsername.getText();
		String password = txtPassword.getText();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
		DashboardController getUser = new DashboardController();
		Parent root;
		Scene scene;
		Stage stage;
		
		User loggedInUser = db.checkLogin(username, password);
		
		
		if (loggedInUser != null) {
			System.out.println("You are now login!");
			int currentId = loggedInUser.getId();
			
			try {				
				root = loader.load();
				scene = new Scene(root);
				stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
				
				getUser = loader.getController();
				getUser.setUserID(currentId);
				getUser.getMenuUsername("Welcome " + username);  
				
				stage.setScene(scene);
				stage.setTitle("Welcome to Privy");
				stage.setMinWidth(1280);
				stage.setMinHeight(720);
				stage.setResizable(true);
				stage.centerOnScreen();
				stage.show();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		} else 
			System.out.println("Invalid Login!");
	}

}
