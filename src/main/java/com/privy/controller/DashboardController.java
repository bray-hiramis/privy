package com.privy.controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.privy.database.DatabaseHandler;
import com.privy.model.Vault;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    
    private int currentUserID;
    
    private DatabaseHandler db = new DatabaseHandler();
    
    public void setUserID(int id) {
		this.currentUserID = id;
		refreshTable();
	}
    
    public void refreshTable() {
    	System.out.println("the id is: " + currentUserID);
		ObservableList<Vault> dataList = db.fetchDBToTable(this.currentUserID);
		System.out.println(dataList.size());
		tablePassword.setItems(dataList);		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		try {
			db.getConnection();
			System.out.println("DB Connected!");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		// Maps for the tableView
		urlName.setCellValueFactory(new PropertyValueFactory<>("urlName"));
		url.setCellValueFactory(new PropertyValueFactory<>("url"));
		userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
		
		
	}
	
	// Getters
	public void getMenuUsername(String name) {
		menuUsername.setText(name);
	}
	
}
