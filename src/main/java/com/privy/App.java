package com.privy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
		String css = getClass().getResource("/css/style.css").toExternalForm();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(css);
		stage.setTitle("Privy | Password Manager");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/logo/privy-logo.png")));
		stage.setResizable(false);
		stage.setScene(scene);
		stage.centerOnScreen()
;		stage.show();		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
}
