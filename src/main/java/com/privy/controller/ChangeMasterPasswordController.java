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
    private Button btnHide1;

    @FXML
    private Button btnHide2;

    @FXML
    private Button btnHide3;

    @FXML
    private Button btnShow1;

    @FXML
    private Button btnShow2;

    @FXML
    private Button btnShow3;

	@FXML
	private PasswordField txtCurrentPassword;
	
	@FXML
	private PasswordField txtNewPassword;
	
    @FXML
    private PasswordField txtConfirmNewPassword;
    
    @FXML
    private TextField txtShowCurrentPassword;
    
    @FXML
    private TextField txtShowNewPassword;
    
    @FXML
    private TextField txtShowConfirmNewPassword;
    
    @FXML
    private TextField txtLoginID;

    
    @FXML
    private Label lblError;
	
	DatabaseHandler db = new DatabaseHandler();
	
	int loginID;
	
	public void initialize(URL location, ResourceBundle resources) {
		
		txtCurrentPassword.textProperty().bindBidirectional(txtShowCurrentPassword.textProperty());
		txtShowCurrentPassword.textProperty().bindBidirectional(txtCurrentPassword.textProperty());
		
		txtNewPassword.textProperty().bindBidirectional(txtShowNewPassword.textProperty());
		txtShowNewPassword.textProperty().bindBidirectional(txtNewPassword.textProperty());
		
		txtConfirmNewPassword.textProperty().bindBidirectional(txtShowConfirmNewPassword.textProperty());
		txtShowConfirmNewPassword.textProperty().bindBidirectional(txtConfirmNewPassword.textProperty());
		
	}
	
	public void showCurrentPassword(ActionEvent event) {
		
		txtShowCurrentPassword.setVisible(true);
		btnHide1.setVisible(true);
		
		txtCurrentPassword.setVisible(false);
		btnShow1.setVisible(false);
		
	}
	
	public void hideCurrentPassword(ActionEvent event) {
		
		txtShowCurrentPassword.setVisible(false);
		btnHide1.setVisible(false);
		
		txtCurrentPassword.setVisible(true);
		btnShow1.setVisible(true);
		
	}
	
	public void showNewPassword(ActionEvent event) {
		
		txtShowNewPassword.setVisible(true);
		btnHide2.setVisible(true);
		
		txtNewPassword.setVisible(false);
		btnShow2.setVisible(false);
		
	}
	
	public void hideNewPassword(ActionEvent event) {
		
		txtShowNewPassword.setVisible(false);
		btnHide2.setVisible(false);
		
		txtNewPassword.setVisible(true);
		btnShow2.setVisible(true);
		
	}
	
	public void showConfirmPassword(ActionEvent event) {
		
		txtShowConfirmNewPassword.setVisible(true);
		btnHide3.setVisible(true);
		
		txtConfirmNewPassword.setVisible(false);
		btnShow3.setVisible(false);
		
	}
	
	public void hideConfirmPassword(ActionEvent event) {
		
		txtShowConfirmNewPassword.setVisible(false);
		btnHide3.setVisible(false);
		
		txtConfirmNewPassword.setVisible(true);
		btnShow3.setVisible(true);
		
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
