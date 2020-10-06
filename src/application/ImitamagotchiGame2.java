package application;

import java.util.Set;

import animals.*;
import controller.AnimalControl;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ImitamagotchiGame extends Application {
	@Override
	public void start(Stage primaryStage) {

		try {

			VBox vMainBox = new VBox();
			Button b = new Button("Refresh");
			
	        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() { 
	            public void handle(ActionEvent e) 
	            { 
	            	Set<Node> animals = (Set<Node>) vMainBox.lookupAll("AnimalControl");
	            	System.out.println(animals.size());

	            		vMainBox.getChildren().removeAll(animals);
	            		ImitamagotchiGame.spawnRandomAnimals(vMainBox, 8);
	            } 
	        }; 
	        
	        b.setOnAction(event); 

			vMainBox.getChildren().add(b);
			ImitamagotchiGame.spawnRandomAnimals(vMainBox, 8);

			Scene scene = new Scene(vMainBox, 880, 826);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Imitamagotchi");

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void spawnRandomAnimals(Pane p, int ammount)
	{
		int rand;
		for(int i = 0;i<ammount;i++)
		{
			 rand = (int)(1 + 4*Math.random());
			
			switch (rand)
			{
			case 1:
				ImitamagotchiGame.addAnimalControl(p, new Cat());
				break;
			case 2:
				ImitamagotchiGame.addAnimalControl(p, new Dog());
				break;
			case 3:
				ImitamagotchiGame.addAnimalControl(p, new Monkey());
				break;
			case 4:
				ImitamagotchiGame.addAnimalControl(p, new Dragon());
				break;
			}
		}
	}

	private static <T extends Animal> void addAnimalControl(Pane p, T a) {
		p.getChildren().add(new AnimalControl<Animal>(a));
	}

	public static void main(String[] args) {
		launch(args);
	}
	

}
