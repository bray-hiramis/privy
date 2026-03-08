package com.privy.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.privy.database.DatabaseHandler;
import com.privy.helper.Navigation;
import com.privy.helper.SecurityQuestions;
import com.privy.model.User;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ForgotAccountController implements Initializable {
	
    @FXML
    private Button btnHidePassword;

    @FXML
    private Button btnResetPassword;

    @FXML
    private ImageView btnShowPassword;

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
		
		// Closing forgot account form
		Platform.runLater(() -> {
			Stage stage = (Stage) mainContainer.getScene().getWindow();
			stage.setOnCloseRequest(e -> {
				e.consume();
				Navigation.navigateTo(stage, "/fxml/login.fxml", "Privy | Password Manager");
				stage.setResizable(false);
			});
		});
		
	}

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
	
}
