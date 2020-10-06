package controller;
import java.io.IOException;

import animals.Animal;
import animals.Cat;
import animals.Dog;
import animals.Dragon;
import animals.Monkey;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;



public class MainWindowControl extends BorderPane{
	@FXML private AnimalsControl animalsPane;
	@FXML private MenuBarControl mainMenuBar;
	
	public void initialize() {

		this.mainMenuBar.setMainPane(this);
		this.spawnRandomAnimals(8);
	}
	
	
	public MainWindowControl()
	{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_window_control.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
	
	
	public void spawnRandomAnimals(int ammount)
	{
		Pane animalsPane = (Pane) this.getCenter();
		
		int rand;
		for(int i = 0;i<ammount;i++)
		{
			 rand = (int)(1 + 4*Math.random());
			
			switch (rand)
			{
			case 1:
				this.addAnimalControl(animalsPane, new Cat());
				break;
			case 2:
				this.addAnimalControl(animalsPane, new Dog());
				break;
			case 3:
				this.addAnimalControl(animalsPane, new Monkey());
				break;
			case 4:
				this.addAnimalControl(animalsPane, new Dragon());
				break;
			}
		}
	}

	private <T extends Animal> void addAnimalControl(Pane p, T a) {
		Pane animalsPane = (Pane) this.getCenter();
		animalsPane.getChildren().add(new AnimalControl<Animal>(a));
	}

	
	public AnimalsControl getAnimalsPane()
	{
		return this.animalsPane;
	}
	
}
