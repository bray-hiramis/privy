package com.privy.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.privy.database.DatabaseHandler;
import com.privy.helper.Navigation;
import com.privy.helper.SecurityQuestions;
import com.privy.model.User;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ForgotAccountController implements Initializable {
	
    @FXML
    private Button btnHidePassword;
    
    @FXML
    private Button btnShowPassword;

    @FXML
    private Button btnResetPassword;

    @FXML
    private Button btnVerifyAnswer;

    @FXML
    private Button btnVerifyEmail;

    @FXML
    private Label lblSecurityQuestion;
    
    @FXML
    private Label lblFound;

    @FXML
    private StackPane mainContainer;

    @FXML
    private TextField txtAnswer;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtHiddenPassword;

    @FXML
    private TextField txtShowPassword;

    @FXML
    private TextField txtUsername;
    
    @FXML
    private TextField txtQuestionID;
    
    DatabaseHandler db = new DatabaseHandler();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Closing forgot_account form
		Platform.runLater(() -> {
			Stage stage = (Stage) mainContainer.getScene().getWindow();
			stage.setOnCloseRequest(e -> {
				e.consume();
				Navigation.navigateTo(stage, "/fxml/login.fxml", "Privy | Password Manager");
				stage.setResizable(false);
			});
		});
		
		// To capture the password text for "Show and Hide password" buttons
		txtShowPassword.textProperty().bindBidirectional(txtHiddenPassword.textProperty());
		txtHiddenPassword.textProperty().bindBidirectional(txtShowPassword.textProperty());
		
	}

	// For verify email button
	public void verifyEmail(ActionEvent event) {
		String email = txtEmail.getText().trim();
		
		if (email.isEmpty()) {
			lblFound.setText("Please enter your recovery email.");
			lblFound.setVisible(true);
			lblFound.setStyle("-fx-text-fill: red;");
			return;
		}
		
		try {
			// Checks for the email and question id in the database
			SecurityQuestions emailID = db.confirmEmail(email);
			txtQuestionID.setText(String.valueOf(emailID.getID()));
			
			// Capture the question id and populate it to the security questions field (UI)
			SecurityQuestions sq = db.questionID(Integer.parseInt(txtQuestionID.getText()));
			lblSecurityQuestion.setText(sq.getSecurityQuestions());
			
			lblFound.setVisible(false);
			
			txtAnswer.setEditable(true);
			btnVerifyAnswer.setDisable(false);
			
			txtEmail.setEditable(false);
			txtEmail.setStyle("-fx-background-color: #00900080;");
			btnVerifyEmail.setDisable(true);
			
			lblFound.setStyle("-fx-text-fill: #005000;");
			lblFound.setText("Email Found!");
			lblFound.setVisible(true);
		} catch (Exception e) {
			lblFound.setText(e.getMessage());
			lblFound.setStyle("-fx-text-fill: red;");
			lblFound.setVisible(true);
		}
		
	}
	
	// For verify answer button
	public void verifyAnswer(ActionEvent event) {
		
		String answer = txtAnswer.getText().trim();
		String email = txtEmail.getText().trim();
		
		if (answer.isEmpty()) {
			lblFound.setText("Please enter your answer.");
			lblFound.setVisible(true);
			lblFound.setStyle("-fx-text-fill: red;");
			return;
		}
		
		try {
			User user = db.getAnswer(answer, email);
			txtUsername.setText(user.getUserName());
			
			lblFound.setStyle("-fx-text-fill: #005000;");
			lblFound.setText("We found your account!");
			lblFound.setVisible(true);
			
			txtAnswer.setStyle("-fx-background-color: #00900080;");
			txtAnswer.setEditable(false);
			btnVerifyAnswer.setDisable(true);
			
			txtHiddenPassword.setEditable(true);
			txtShowPassword.setEditable(true);
			btnResetPassword.setDisable(false);
		} catch (Exception e) {			
			lblFound.setText(e.getMessage());
			lblFound.setStyle("-fx-text-fill: red;");
			lblFound.setVisible(true);
		}
		
	}
	
	// For reset password button
	public void resetPassword(ActionEvent event) throws Exception {
		
		String password = txtHiddenPassword.getText();
		String username = txtUsername.getText();
		
		if (password.isEmpty()) {
			lblFound.setText("Please enter your new password.");
			lblFound.setVisible(true);
			lblFound.setStyle("-fx-text-fill: red;");
		} else if (password.length() != 8) {
			lblFound.setText("Password must be at least 8 characters long.");
			lblFound.setVisible(true);
			lblFound.setStyle("-fx-text-fill: red;");
		} else {
			db.updatePassword(password, username);
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Password Updated!");
			alert.setHeaderText(username + "'s password is now updated!");
			alert.setContentText("Password updated! Press OK to go back to the login screen.");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.isPresent() && result.get() == ButtonType.OK) {
				Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
				Navigation.navigateTo(stage, "/fxml/login.fxml", "Privy | Password Manager");
				stage.setResizable(false);
				return;
			}
		}
		
	}
	
	public void showPassword(ActionEvent event) {
		
		btnShowPassword.setVisible(false);
		txtHiddenPassword.setVisible(false);
		
		btnHidePassword.setVisible(true);
		txtShowPassword.setVisible(true);
		
	}
	
	public void hidePassword(ActionEvent event) {
		
		btnShowPassword.setVisible(true);
		txtHiddenPassword.setVisible(true);
		
		btnHidePassword.setVisible(false);
		txtShowPassword.setVisible(false);
		
	}
	
}
