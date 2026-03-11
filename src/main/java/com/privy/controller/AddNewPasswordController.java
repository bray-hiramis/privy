package com.privy.controller;

import java.util.Optional;

import com.privy.database.DatabaseHandler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddNewPasswordController {
	
	@FXML
    private AnchorPane addNewPasswordContainer;

    @FXML
    private Button btnCloseAddPass;

    @FXML
    private Button btnHideAddPassword;

    @FXML
    private Button btnSavePassword;

    @FXML
    private Button btnShowAddPassword;

    @FXML
    private TextField txtAddUrl;

    @FXML
    private TextField txtAddUrlName;

    @FXML
    private TextField txtAddUsername;

    @FXML
    private PasswordField txtHiddenAddPassword;

    @FXML
    private TextField txtShowAddPassword;
    
    @FXML
    private TextField txtLoginID;

    DatabaseHandler db = new DatabaseHandler();
    
    int currentLoginID;
	
	public void btnSaveNewPassword(ActionEvent event) {
		
		String urlName = txtAddUrlName.getText();
		String url = txtAddUrl.getText();
		String username = txtAddUsername.getText();
		String password = txtHiddenAddPassword.getText();
		int loginID = Integer.parseInt(txtLoginID.getText());
		
		if (urlName.isEmpty() && url.isEmpty() && username.isEmpty() && password.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Missing Fields");
			alert.setHeaderText("All Fields Required");
			alert.setContentText("Click OK to close this pop up.");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.isPresent() && result.get() == ButtonType.OK) {
				return;
			}
		} 
		
		try {
			
			db.addNewPasswordVault(urlName, url, username, password, loginID);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("New Password");
			alert.setHeaderText("New Password Saved");
			alert.setContentText("New password saved! Click OK to close this pop up.");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.isPresent() && result.get() == ButtonType.OK) {
				return;
			}
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
	}
	
	// Setter
	public void setLoginID(int loginID) {
		this.currentLoginID = loginID;
		txtLoginID.setText(String.valueOf(loginID));
	}

}
