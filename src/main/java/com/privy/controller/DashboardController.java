						package com.privy.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.privy.database.DatabaseHandler;
import com.privy.model.Vault;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TextField txtSearchPassword;

    @FXML
    private TextField txtURL;

    @FXML
    private TextField txtURLName;

    @FXML
    private TextField txtUserName;
    
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
		
		
		tablePassword.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			
			if (newSelection != null) {
				txtURLName.setText(newSelection.getUrlName());
				txtURL.setText(newSelection.getUrl());
				txtUserName.setText(newSelection.getUserName());
				txtPassword.setText(newSelection.getPassword());
			}
			
		});
		
	}
	
	// Getters
	public void getMenuUsername(String name) {
		menuUsername.setText(name);
	}
	
}
