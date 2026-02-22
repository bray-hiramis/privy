package com.privy.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import com.privy.database.DatabaseHandler;

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
		String username = txtUsername.getText().trim();
		String password = txtPassword.getText();
		
		if (db.checkLogin(username, password)) {
			System.out.println("You are now login!");
			
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
				Parent root = loader.load();
				Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.setTitle("Welcome to Privy");
				stage.show();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		} else 
			System.out.println("Invalid Login!");
	}

}
