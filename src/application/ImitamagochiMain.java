package application;

import java.util.Set;

import animals.*;

import controller.AnimalControl;
import controller.MainWindowControl;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ImitamagochiMain extends Application {
	@Override
	public void start(Stage primaryStage) {

		try {
			MainWindowControl mainWindow = new MainWindowControl();
			
			Scene scene = new Scene(mainWindow, 880, 826);

			primaryStage.setTitle("Imitamagotchi");

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public static void main(String[] args) {
		launch(args);
	}
	

}
