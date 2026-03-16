package com.privy.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.privy.database.DatabaseHandler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ChangeMasterPasswordController implements Initializable {
	
	@FXML
    private Button btnChangePassword;

    @FXML
    private TextField txtConfirmNewPassword;

    @FXML
    private TextField txtCurrentPassword;

    @FXML
    private TextField txtLoginID;

    @FXML
    private TextField txtNewPassword;
	
	DatabaseHandler db = new DatabaseHandler();
	
	int loginID;
	
	public void initialize(URL location, ResourceBundle resources) {
		
		
		
	}

	public void setLoginID(int id) {
		this.loginID = id;
		txtLoginID.setText(String.valueOf(id));
	}
	
}
