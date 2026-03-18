package com.privy.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.privy.database.DatabaseHandler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ChangeMasterPasswordController implements Initializable {
	
	@FXML
    private Button btnChangePassword;

	@FXML
	private PasswordField txtCurrentPassword;
	
	@FXML
	private PasswordField txtNewPassword;
	
    @FXML
    private PasswordField txtConfirmNewPassword;
    
    @FXML
    private TextField txtLoginID;

    
    @FXML
    private Label lblError;
	
	DatabaseHandler db = new DatabaseHandler();
	
	int loginID;
	
	public void initialize(URL location, ResourceBundle resources) {
		
		
		
	}
	
	public void changMasterPassword(ActionEvent event) {
		
		String currentPassword = txtCurrentPassword.getText();
		String newPassword = txtNewPassword.getText();
		String confirmPassword = txtConfirmNewPassword.getText();
		int id = Integer.parseInt(txtLoginID.getText());
		
		if (currentPassword.isEmpty() && newPassword.isEmpty() && confirmPassword.isEmpty()) {
			
			lblError.setVisible(true);
			lblError.setText("All Fields Required!");
			return;
			
		}
		
		if (!newPassword.equals(confirmPassword)) {
			
			lblError.setVisible(true);
			lblError.setText("New Password and Confirm New Password do not match!");
			return;
			
		}
		
		if (newPassword.length() < 8) {
			
			lblError.setVisible(true);
			lblError.setText("Password must be at least 8 Characters long.");
			return;
			
		}
		
		try {
			
			db.updatePassword(newPassword, currentPassword, loginID);
			lblError.setVisible(true);
			lblError.setText("Master Password Updated!");
			lblError.setStyle("-fx-text-fill: #005000;");
			
		} catch (Exception e) {
			lblError.setVisible(true);
			lblError.setText(e.getMessage());
		}
		
	}

	
	// Setters
	public void setLoginID(int id) {
		this.loginID = id;
		txtLoginID.setText(String.valueOf(id));
	}
	
}
