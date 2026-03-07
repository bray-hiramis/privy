package com.privy.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.privy.database.DatabaseHandler;
import com.privy.helper.Navigation;
import com.privy.helper.SecurityQuestions;
import com.privy.model.NewUser;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class RegisterUserController implements Initializable {
	
	@FXML
    private AnchorPane createAccountPane;

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
    
    @FXML
    private TextField txtQuestionId;
    
    @FXML
    private Label lblError;
    
    @FXML
    private Button btnHidePassword;

    @FXML
    private Button btnShowPassword;
    
    @FXML
    private TextField txtShowPassword;
    
    private DatabaseHandler db = new DatabaseHandler();
    
    // Populate the combobox with the security questions from the database
    public void getSecQuestions() {		
    	ObservableList<SecurityQuestions> secQuestions = db.fetchSecurityQuestions();
    	cmbSecurityQuestions.setItems(secQuestions);
	}
    
    // Email Regex
    public boolean isValidEmail(String email) {
    	
    	String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    	Pattern pattern = Pattern.compile(regex);
    	
    	if (email == null) {
			return false;
		}
    	
    	Matcher matcher = pattern.matcher(email);
    	// matches() or find() both works to match the regex with email
    	// return matcher.matches();
    	return matcher.find();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
    	// Converts the memory stack to string
    	cmbSecurityQuestions.setConverter(new StringConverter<SecurityQuestions>() {
			
			@Override
			public String toString(SecurityQuestions object) {
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
    	
    	// Calling method for the security questions
    	getSecQuestions();
    	
    	// Capturing the id of the security question (for database purposes)
		cmbSecurityQuestions.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		
			if (newSelection != null) {
				txtQuestionId.setText(String.valueOf(newSelection.getID()));
			} else {
				txtQuestionId.clear();
			}
			
		});
		
		// Capturing text from password field to show password textfield
		txtShowPassword.textProperty().bindBidirectional(txtPassword.textProperty());
		
		// Capturing text from show password textfield to passwordfield
		txtPassword.textProperty().bindBidirectional(txtShowPassword.textProperty());
		
		// Closing register form
		Platform.runLater(() -> {
			Stage stage = (Stage) createAccountPane.getScene().getWindow();
			stage.setOnCloseRequest(e -> {
				e.consume();
				Navigation.navigateTo(stage, "/fxml/login.fxml", "Privy | Password Manager");
			});
		});
		
    }
    
    // Creating new user
    public void createAccount(ActionEvent event) throws Exception {
    	String userName = txtUserName.getText().trim();
    	String password = txtPassword.getText();
    	String showPassword = txtShowPassword.getText();
    	String email = txtEmail.getText().trim();
    	String questionStr = txtQuestionId.getText();
    	String answer = txtAnswer.getText().trim();
    	
    	// Check all fields are filled out
    	if (userName.isEmpty() || password.isEmpty() || email.isEmpty() || questionStr.isEmpty() || answer.isEmpty()) {
    		lblError.setVisible(true);
    		lblError.setText("All fields required!");
    		return;
    	}
    	
    	// Check if they selected a security question
    	int questionId;
    	try {
			questionId = Integer.parseInt(questionStr);
			
		} catch (NumberFormatException e) {
			lblError.setVisible(true);
			lblError.setText("Please select a valid question.");
			return;
		}
    	
    	// Check if email format is valid
    	if (!isValidEmail(email)) {
    		lblError.setVisible(true);
    		lblError.setText("Invalid email format. Acceptable email format: E.g., johndoe@emailprovider.com");
    		return;
    	}
    	
    	// Check if password is a minimum of 8 characters
    	if (password.length() < 8 && showPassword.length() < 8) {
    	    lblError.setVisible(true);
    	    lblError.setText("Password must be at least 8 characters long.");
    	    return;
    	}
    	
    	// Calling insert database method from db.addNewUser()
    	try {			    		
    		NewUser newUser = db.addNewUser(userName, password, email, questionId, answer);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("New account created!");
			alert.setHeaderText("Welcome " + newUser.getUserName());
			alert.setContentText("Welcome to Privy! Your Offline Password Manager.");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.isPresent() && result.get() == ButtonType.OK) {
				lblError.setVisible(false);
				Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
				Navigation.navigateTo(stage, "/fxml/login.fxml", "Privy | Password Manager");
				return;
    			}
		} catch (Exception e) {
			// Check if email or user name exist to avoid duplicate errors
			lblError.setText(e.getMessage()); 
	        lblError.setVisible(true);
		}
    	
    }
    
    // Show password
    public void showPassword(ActionEvent event) {
    	
    	txtPassword.setVisible(false);
    	txtShowPassword.setVisible(true);
    	btnHidePassword.setVisible(true);
    	btnShowPassword.setVisible(false);
    	
    }
    
    // Hide Password
    public void hidePassword(ActionEvent event) {
		
    	txtPassword.setVisible(true);
    	txtShowPassword.setVisible(false);
    	btnHidePassword.setVisible(false);
    	btnShowPassword.setVisible(true);
    	
	}
    
}
