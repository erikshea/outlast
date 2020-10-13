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
 * 
 * Sets up, populates and and controls interactions between a group of animal regions.
 *
 */
public class AnimalsControl extends VBox{
    private MainWindowControl mainController;
    private final int maxPopulation = 8;
    
    /**
     * Spawn initial animals
     */
    public void initialize()
	{
  
    }
    
    
    /**
     * Loads .fxml as root
     * @param a animal subclass
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
				this.addAnimalControl(new Dragon());
			} else if (rand >= 60) {
				this.addAnimalControl(new Monkey());
			} else if (rand >= 30 ) {
				this.addAnimalControl(new Dog());
			} else {
				this.addAnimalControl(new Cat());
			}
		}
	}

	/**
	 * Adds an animal region to the current animals region
	 * @param <T> Animal subtype
	 * @param a Animal subtype
	 */
	public <T extends Animal> void addAnimalControl(T a) {
		AnimalControl<T> ac = new AnimalControl<>(a);
		
		ac.setMainController(this.mainController);
    	
		this.mainController.getConsole().printLine(a.getName() + " the " + util.TextUtils.capitalize(a.getType()) + " is born.");
		
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
	
	public<T extends Animal> void increaseAges(double years)
	{
		ObservableList<Node> nodes = this.getChildren();
		
		for (Node n:nodes)
		{
			@SuppressWarnings("unchecked")
			AnimalControl<T> animalRegion = (AnimalControl<T>) n;

			animalRegion.getAnimal().changeAgeBy(years);
		}
    	
    	// Remove dead animals here for thread concurrency
    	this.clearDeadAnimals();	// TODO : handle dead animals with signal (copy of state in CopyOnWriteArrayList to avoid concurrencyexception?)
	}
	
	public<T extends Animal> List<AnimalControl<T>> getAnimalRegions()
	{
		List<AnimalControl<T>> animalRegions = new ArrayList<>();
		ObservableList<Node> nodes = this.getChildren();

		for (int i=0;i<nodes.size();i++) {
			@SuppressWarnings("unchecked")	// Only AnimalControl nodes in children
			// Have to redeclare temp at every iteration to suppress warnings
			AnimalControl<T> temp = (AnimalControl<T>) nodes.get(i);	
			animalRegions.add(temp );
		}
		
		return animalRegions;
	}
	
	public int getMaxPopulation() {
		return this.maxPopulation;
	}
	
	
	public<T extends Animal> void clearDeadAnimals()
	{
		List<AnimalControl<T>> animalRegions = this.getAnimalRegions();
		List<AnimalControl<T>> deadAnimalRegions = new ArrayList<>();
		
		
		for (AnimalControl<T> a:animalRegions)
		{
			if(!a.getAnimal().isAlive()) {
				deadAnimalRegions.add(a);
			}
		}
		this.getChildren().removeAll(deadAnimalRegions);
	}
	
}
