package controller;

import java.io.IOException;

import animals.Animal;
import animals.Cat;
import animals.Dog;
import animals.Dragon;
import animals.Monkey;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

/**
 * 
 * Sets up, populates and and controls interactions between a group of animal regions.
 *
 */
public class AnimalsControl extends VBox{
    @FXML private MainWindowControl mainPane;
    
    /**
     * Spawn initial animals
     */
    public void initialize()
    {
    	this.spawnRandomAnimals(8);
    }
    
    /**
     * Loads .fxml as root
     * @param a animal subclass
     */
    public AnimalsControl()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("animals_control.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    /**
     * Spawn random animals in current animals region
     * @param ammount number to spawn
     */
	public void spawnRandomAnimals(int ammount)
	{
		int rand;
		for(int i = 0;i<ammount;i++)
		{
			rand = (int)(1 + 4*Math.random());
			
			switch (rand){
			case 1:
				this.addAnimalControl(new Cat());
				break;
			case 2:
				this.addAnimalControl(new Dog());
				break;
			case 3:
				this.addAnimalControl(new Monkey());
				break;
			case 4:
				this.addAnimalControl(new Dragon());
				break;
			}
		}
	}

	/**
	 * Adds an animal region to the current animals region
	 * @param <T> Animal subtype
	 * @param a Animal subtype
	 */
	private <T extends Animal> void addAnimalControl(T a) {
		this.getChildren().add(new AnimalControl<Animal>(a));
	}

	/**
	 * Sets mainPane to a reference to the main window pane (to communicate with other controllers)
	 * @param p main window pane
	 */
	void setMainPane(MainWindowControl p)
	{
		this.mainPane = p;
	}
}
