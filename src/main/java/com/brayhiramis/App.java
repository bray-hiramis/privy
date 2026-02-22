package com.brayhiramis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/login_form.fxml"));
		String css = getClass().getResource("/css/style.css").toExternalForm();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(css);
		stage.setTitle("Privy | Password Manager");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
