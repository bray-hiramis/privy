package com.privy.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.privy.database.DatabaseHandler;
import com.privy.model.Vault;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class DashboardController implements Initializable{

	@FXML
    private TableView<Vault> tablePassword;

    @FXML
    private TableColumn<Vault, String> url;

    @FXML
    private TableColumn<Vault, String> urlName;

    @FXML
    private TableColumn<Vault, String> userNameCol;
    
    @FXML
    private MenuButton menuUsername;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private TextField txtShowPassword;

    @FXML
    private TextField txtSearchPassword;

    @FXML
    private TextField txtURL;

    @FXML
    private TextField txtURLName;

    @FXML
    private TextField txtUserName;
    
    @FXML
    private TextField txtPasswordID;
    
    @FXML
    private TextField txtLoginID;
    
    @FXML
    private Button btnHidePassword;

    @FXML
    private Button btnShowPassword;
    
    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnEdit;
    
    @FXML
    private Button btnSave;
    
    @FXML
    private Button btnUpdate;
    
    @FXML
    private Button btnCancel;
    
    @FXML
    private Label lblError;
    
    @FXML
    private AnchorPane innerPasswordContainer;
    
    private Node originalContent;

    
    // stores the current user id to filter the table view for their saved passwords
    private int currentUserID;
    
    private DatabaseHandler db = new DatabaseHandler();
    
    public void setUserID(int id) {
		this.currentUserID = id;
		txtLoginID.setText(String.valueOf(id));
		refreshTable();
	}
    
    public void refreshTable() {
		ObservableList<Vault> dataList = db.fetchDBToTable(this.currentUserID);
		tablePassword.setItems(dataList);		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Maps for the tableView
		urlName.setCellValueFactory(new PropertyValueFactory<>("urlName"));
		url.setCellValueFactory(new PropertyValueFactory<>("url"));
		userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
		
		// Listens to a click when user selects a row in the table
		tablePassword.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			
			if (newSelection != null) {
				txtPasswordID.setText(String.valueOf(newSelection.getId()));
				txtURLName.setText(newSelection.getUrlName());
				txtURL.setText(newSelection.getUrl());
				txtUserName.setText(newSelection.getUserName());
				txtPassword.setText(newSelection.getPassword());
			}
			
		});
		
		// For show and hide password
		txtShowPassword.textProperty().bindBidirectional(txtPassword.textProperty());
		txtPassword.textProperty().bindBidirectional(txtShowPassword.textProperty());
		
		originalContent = innerPasswordContainer.getChildren().get(0);
		
		txtPasswordID.setText(String.valueOf(0));
		
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
	
	public void btnEdit(ActionEvent event) {
		
		int idString = Integer.parseInt(txtPasswordID.getText());
		if (idString == 0) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Privy | Update Password");
			alert.setHeaderText("All Fields Required!");
			alert.setContentText("Please select on the table the password you want to update.");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.isPresent() && result.get() == ButtonType.OK) {
				return;
			}
		} else {
			lblError.setVisible(true);
			lblError.setText("You can now edit the fields above.");
			lblError.setStyle("-fx-text-fill: #005000");
			btnUpdate.setVisible(true);
			txtURLName.setEditable(true);
			txtURL.setEditable(true);
			txtUserName.setEditable(true);
			txtPassword.setEditable(true);
			txtShowPassword.setEditable(true);
			
			btnEdit.setVisible(false);
		}
		
		
	}
	
	public void btnUpdate(ActionEvent event) {
		
		int id = Integer.parseInt(txtPasswordID.getText());
		String urlName = txtURLName.getText();
		String url = txtURL.getText();
		String username = txtUserName.getText();
		String password = txtPassword.getText();
		
		try {			
			db.dashboardUpdatePassword(urlName, url, username, password, id, currentUserID);
			refreshTable();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Privy | Password Updated");
			alert.setHeaderText("Password Updated!");
			alert.setContentText("Password Updated! Press OK to close this pop up.");
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.isPresent() && result.get() == ButtonType.OK) {				
				
				btnUpdate.setVisible(false);
				
				txtURLName.setEditable(false);
				txtURL.setEditable(false);
				txtUserName.setEditable(false);
				txtPassword.setEditable(false);
				txtShowPassword.setEditable(false);
				
				btnEdit.setVisible(true);
				lblError.setVisible(false);
			}
			
		} catch (Exception e) {
			lblError.setText(e.getMessage());
			lblError.setStyle("-fx-text-fill: red;");
			lblError.setVisible(true);
		}
		
	}
	
	public void showAddPassword(ActionEvent event) throws IOException {
	
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/add_password.fxml"));
		Parent showPasswordForm = loader.load();
		AddNewPasswordController addPass = loader.getController();
		addPass.setLoginID(this.currentUserID);
		
		innerPasswordContainer.getChildren().clear();
		innerPasswordContainer.getChildren().add(showPasswordForm);
		
		btnAdd.setVisible(false);
		btnCancel.setVisible(true);
		disableInputs();
		
		
		
	}
	
	public void closeAddPassword(ActionEvent event) throws IOException {
		
		innerPasswordContainer.getChildren().clear();
		innerPasswordContainer.getChildren().add(originalContent);
		
		btnAdd.setVisible(true);
		btnCancel.setVisible(false);
		enableInputs();
		
	}
	
	// helper
	public void disableInputs() {
		
		txtSearchPassword.setEditable(false);
		txtURLName.setEditable(false);
		txtURL.setEditable(false);
		txtUserName.setEditable(false);
		txtPassword.setEditable(false);
		txtShowPassword.setEditable(false);
		
		txtURLName.clear();;
		txtURL.clear();;
		txtUserName.clear();;
		txtPassword.clear();
		
		btnEdit.setDisable(true);
		btnEdit.setVisible(true);
		btnUpdate.setVisible(false);
		btnDelete.setDisable(true);
		
		
	}
	
	public void enableInputs() {
		
		txtSearchPassword.setEditable(true);
		txtURLName.setEditable(true);
		txtURL.setEditable(true);
		txtUserName.setEditable(true);
		txtPassword.setEditable(true);
		txtShowPassword.setEditable(true);
		
		btnEdit.setDisable(false);
		btnDelete.setDisable(false);
		
	}
	
	// Getters
	public void getMenuUsername(String name) {
		menuUsername.setText(name);
	}
	
}
