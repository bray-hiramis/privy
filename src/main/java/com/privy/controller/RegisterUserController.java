package com.privy.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.privy.database.DatabaseHandler;
import com.privy.model.NewUser;
import com.privy.model.SecurityQuestions;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
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
    
    @FXML
    private TextField txtQuestionId;
    
    @FXML
    private Label lblError;
    
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
    	
		cmbSecurityQuestions.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		
			if (newSelection != null) {
				txtQuestionId.setText(String.valueOf(newSelection.getID()));
			} else {
				txtQuestionId.clear();
			}
			
		});
    }

    public void createAccount(ActionEvent event) throws Exception {
    	String userName = txtUserName.getText();
    	String password = txtPassword.getText();
    	String email = txtEmail.getText();
    	String questionStr = txtQuestionId.getText();
    	String answer = txtAnswer.getText();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
    	Parent root;
    	Scene scene;
    	Stage stage;
    	
    	
    	if (userName.isEmpty() || password.isEmpty() || email.isEmpty() || questionStr.isEmpty() || answer.isEmpty()) {
    		lblError.setVisible(true);
    		lblError.setText("All fields required!");
    		return;
    	}
    	
    	int questionId;
    	try {
			questionId = Integer.parseInt(questionStr);
			
		} catch (NumberFormatException e) {
			lblError.setVisible(true);
			lblError.setText("Please select a valid question.");
			return;
		}
    	
    	if (!isValidEmail(email)) {
    		lblError.setVisible(true);
    		lblError.setText("Invalid email format. Acceptable email format: E.g., johndoe@emailprovider.com");
    		return;
    	}
    	
    	try {			    		
    		NewUser newUser = db.addNewUser(userName, password, email, questionId, answer);
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("New account created!");
			alert.setHeaderText("Welcome " + newUser.getUserName());
			alert.setContentText("Welcome to Privy! Your Offline Password Manager.");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.isPresent() && result.get() == ButtonType.OK) {
				lblError.setVisible(false);
				root = loader.load();
				scene = new Scene(root);
				stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.centerOnScreen();
				stage.show();
				return;
    			}
		} catch (Exception e) {
			lblError.setText(e.getMessage()); 
	        lblError.setVisible(true);
		}
    	
    }
    
}
