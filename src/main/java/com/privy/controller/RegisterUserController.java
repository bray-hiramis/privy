package com.privy.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.privy.database.DatabaseHandler;
import com.privy.model.SecurityQuestions;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class RegisterUserController implements Initializable {

    @FXML
    private ComboBox<SecurityQuestions> cmbSecurityQuestions;

    @FXML
    private TextField txtAnswer;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private TextField txtUserName;
    
    private DatabaseHandler db = new DatabaseHandler();
    
    //Controller Class
    public void getSecQuestions() {		
    	ObservableList<SecurityQuestions> secQuestions = db.fetchSecurityQuestions();
    	cmbSecurityQuestions.setItems(secQuestions);
	}
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	cmbSecurityQuestions.setConverter(new StringConverter<SecurityQuestions>() {
			
			@Override
			public String toString(SecurityQuestions object) {
				// TODO Auto-generated method stub
				if (object != null) {
					return object.getSecurityQuestions();
				} else {
					return "";
				}
			}
			
			@Override
			public SecurityQuestions fromString(String string) {
				return null;
			}
		});
    	
    	getSecQuestions();
    	
    }

    public void createAccount(ActionEvent event) {
    	
    }

}
