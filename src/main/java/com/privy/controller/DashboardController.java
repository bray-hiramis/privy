						package com.privy.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.privy.database.DatabaseHandler;
import com.privy.model.Vault;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    private Button btnHidePassword;

    @FXML
    private Button btnShowPassword;
    
    @FXML
    private AnchorPane innerPasswordContainer;

    
    // stores the current user id to filter the table view for their saved passwords
    private int currentUserID;
    
    private DatabaseHandler db = new DatabaseHandler();
    
    public void setUserID(int id) {
		this.currentUserID = id;
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
				txtURLName.setText(newSelection.getUrlName());
				txtURL.setText(newSelection.getUrl());
				txtUserName.setText(newSelection.getUserName());
				txtPassword.setText(newSelection.getPassword());
			}
			
		});
		
		// For show and hide password
		txtShowPassword.textProperty().bindBidirectional(txtPassword.textProperty());
		txtPassword.textProperty().bindBidirectional(txtShowPassword.textProperty());
		
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
	
	// Getters
	public void getMenuUsername(String name) {
		menuUsername.setText(name);
	}
	
}
