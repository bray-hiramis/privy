package com.privy.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import com.privy.database.DatabaseHandler;
import com.privy.helper.Navigation;
import com.privy.model.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable{
	
	@FXML
    private Button btnHidePassword;

    @FXML
    private Button btnShowPassword;
	
	@FXML
    private PasswordField txtPassword;
	
	@FXML
    private TextField txtShowPassword;

    @FXML
    private TextField txtUsername;
    
    @FXML
    private Label lblError;

	DatabaseHandler db = new DatabaseHandler();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		txtShowPassword.textProperty().bindBidirectional(txtPassword.textProperty());
		txtPassword.textProperty().bindBidirectional(txtShowPassword.textProperty());
		
	}
	
	public void login(ActionEvent event) {
		String username = txtUsername.getText().trim();
		String password = txtPassword.getText();
		
		User loggedInUser = db.checkLogin(username, password);
		
		
		if (loggedInUser != null) {
			System.out.println("You are now login!");
			int currentId = loggedInUser.getId();
			
			try {				
				Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
				FXMLLoader loader = Navigation.getLoader(stage, "/fxml/dashboard.fxml", "Privy - Vault");
				DashboardController dashboard = new DashboardController();
				dashboard = loader.getController();
				dashboard.setUserID(currentId);
				dashboard.getMenuUsername("Welcome " + username);
				stage.setMinWidth(1280);
				stage.setMinHeight(720);
				stage.setResizable(true);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		} else 
			lblError.setVisible(true);
	}
	
	public void createAccount(ActionEvent event) throws IOException  {		
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Navigation.navigateTo(stage, "/fxml/register.fxml", "Privy - Create Account");
		stage.setResizable(false);
	}
	
	public void forgotAccountForm(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Navigation.navigateTo(stage, "/fxml/forgot_account.fxml", "Privy - Reset Password");
		stage.setResizable(true);
	}
	
	public void showPassword(ActionEvent event) {
		
		txtPassword.setVisible(false);
		btnShowPassword.setVisible(false);
		
		txtShowPassword.setVisible(true);
		btnHidePassword.setVisible(true);
		
	}
	
public void hidePassword(ActionEvent event) {
		
		txtPassword.setVisible(true);
		btnShowPassword.setVisible(true);
		
		txtShowPassword.setVisible(false);
		btnHidePassword.setVisible(false);
		
	}

}
