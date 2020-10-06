package application;

import controller.MainWindowControl;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class ImitamagochiMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// Main window is handled by its own controller
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
