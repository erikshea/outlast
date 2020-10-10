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
    private int maxPopulation;
    
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
		this.maxPopulation = 8;
		
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
	
	void increaseAges(double years)
	{
		ObservableList<Node> nodes = this.getChildren();
		
		for (Node n:nodes)
		{
			@SuppressWarnings("unchecked")
			AnimalControl<Animal> animalRegion = (AnimalControl<Animal>) n;
			
			animalRegion.refreshStats(years);
		}
	}
	
	void clearDeadAnimals()
	{
		List<AnimalControl<Animal>> animalRegions = getAnimalRegions();
		List<AnimalControl<Animal>> deadAnimalRegions = new ArrayList<>();
		
		
		for (AnimalControl<Animal> a:animalRegions)
		{
			if(!a.getAnimal().isAlive()) {
				deadAnimalRegions.add(a);
				this.mainController.getConsole().printLine(a.getAnimal().getName() + " the " + a.getAnimal().getType() + " has died.");
			}
		}
		this.getChildren().removeAll(deadAnimalRegions);
	}
	

	
	
	List<AnimalControl<Animal>> getAnimalRegions()
	{
		List<AnimalControl<Animal>> animalRegions = new ArrayList<>();
		ObservableList<Node> nodes = this.getChildren();

		for (int i=0;i<nodes.size();i++) {
			@SuppressWarnings("unchecked")
			// Have to redeclare temp at every iteration to suppress warnings
			AnimalControl<Animal> temp = (AnimalControl<Animal>) nodes.get(i);	
			animalRegions.add(temp );
		}
		
		return animalRegions;
	}
	
	public int getMaxPopulation() {
		return this.maxPopulation;
	}
}
