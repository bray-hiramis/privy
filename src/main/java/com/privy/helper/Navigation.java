package com.privy.helper;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigation {

	public static void navigateTo(Stage stage, String fxmlPath, String title) {
		try {
			FXMLLoader loader = new FXMLLoader(Navigation.class.getResource(fxmlPath));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle(title);
			stage.centerOnScreen();
			stage.setOnCloseRequest(null);
			stage.show();
		} catch (Exception e) {
			System.err.println("Navigation Error: Could not load " + fxmlPath);
            e.printStackTrace();
		}
	}
	
	public static FXMLLoader getLoader(Stage stage, String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(Navigation.class.getResource(fxmlPath));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.centerOnScreen();
        stage.setOnCloseRequest(null);
        stage.show();
        
        return loader;
    }
	
}
