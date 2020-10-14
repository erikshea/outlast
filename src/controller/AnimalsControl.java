package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import animals.Animal;
import animals.Cat;
import animals.Dog;
import animals.Dragon;
import animals.Monkey;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * Sets up, populates and controls interactions between a group of animal regions.
 */
public class AnimalsControl extends VBox{
    private MainWindowControl mainController;	// for access to other controllers
    private final int maxPopulation = 8;		// max simultaneous animals
    
    /**
     * Loads .fxml as root
     */
    public AnimalsControl()
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("animalsControl.fxml"));
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
			rand = (int)(1 + 100*Math.random());
			
			if (rand >= 85 )
			{
				this.addAnimalControl(new Dragon());	// 15% chance
			} else if (rand >= 60) {
				this.addAnimalControl(new Monkey());	// 25% chance
			} else if (rand >= 30 ) {
				this.addAnimalControl(new Dog());		// 30% chance
			} else {
				this.addAnimalControl(new Cat());		// 30% chance
			}
		}
	}

	/**
	 * Adds an animal region to the current animals region
	 * @param a Animal subclass
	 */
	public <T extends Animal> void addAnimalControl(T a) {
		AnimalControl<T> ac = new AnimalControl<>(a);
		
		ac.setMainController(this.mainController); // for access to other controllers
    	
		this.mainController.getConsole().printLine(a.getName() + " the " + a.getType() + " is born.");
		
		this.getChildren().add(ac);
	}

	/**
	 * Sets a reference to the main controller (for console, and to communicate with other controllers)
	 * @param c main window controller
	 */
	void setMainController(MainWindowControl c)
	{
		this.mainController = c;
	}
	
	/**
	 * Increases the age of all animals in the region
	 * @param years
	 */
	public<T extends Animal> void increaseAges(double years)
	{
		//ObservableList<Node> nodes = this.getChildren();
		List<AnimalControl<T>> animalRegions = this.getAnimalRegions();
		
		for (AnimalControl<T> animalRegion:animalRegions)
		{
			animalRegion.getAnimal().changeAgeBy(years);
			
	    	// Remove dead animals here and not by listening to isAlive property to avoid concurrencyException
	    	// TODO : get around with copy of state in CopyOnWriteArrayList?
			if(!animalRegion.getAnimal().isAlive()) {
				this.getChildren().remove(animalRegion);
			}
		}
	}
	
	public<T extends Animal> List<AnimalControl<T>> getAnimalRegions()
	{
		List<AnimalControl<T>> animalRegions = new ArrayList<>();
		ObservableList<Node> nodes = this.getChildren();

		for (int i=0;i<nodes.size();i++) {
			@SuppressWarnings("unchecked")
			// Have to redeclare temp at every iteration to be able to suppress warnings. TODO: find way around
			AnimalControl<T> temp = (AnimalControl<T>) nodes.get(i);	
			animalRegions.add(temp );
		}
		
		return animalRegions;
	}
	
	/**
	 * get max population allowed allowed at once
	 * @return max population
	 */
	public int getMaxPopulation() {
		return this.maxPopulation;
	}

	
}
