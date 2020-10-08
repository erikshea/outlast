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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

/**
 * 
 * Sets up, populates and and controls interactions between a group of animal regions.
 *
 */
public class AnimalsControl extends VBox{
    @FXML private MainWindowControl mainRegion;
    
    private int maxAnimalRegions=8;
    
    /**
     * Spawn initial animals
     */
    public void initialize()
    {
    	this.spawnRandomAnimals(this.maxAnimalRegions);
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
	void setMainRegion(MainWindowControl p)
	{
		this.mainRegion = p;
	}
	
	void increaseAges(double refreshInterval)
	{
		double duration = refreshInterval/5;
		
		ObservableList<Node> nodes = this.getChildren();
		
		for (Node n:nodes)
		{
			@SuppressWarnings("unchecked")
			AnimalControl<Animal> a = (AnimalControl<Animal>) n;
			
			a.refreshStats(duration);
		}
	}
	
	
	void repopulateTo(int newAmmount)
	{
		int numberToMake = newAmmount - this.getChildren().size();
		
		this.spawnRandomAnimals(numberToMake);
	}
	
	List<AnimalControl<Animal>> getDeadAnimals()
	{
		List<AnimalControl<Animal>> animalRegions = getAnimalRegions();
		List<AnimalControl<Animal>> deadAnimalRegions = new ArrayList<>();
		
		
		for (AnimalControl<Animal> a:animalRegions)
		{
			System.out.println(a.getAnimal().getHealth());
			
			if(!a.getAnimal().isAlive()) {
				deadAnimalRegions.add(a);
			}
		}
		
		return deadAnimalRegions;
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
	
	public void setMaxAnimalRegions(int ammount)
	{
		this.maxAnimalRegions = ammount;
	}

	public int getMaxAnimalRegions()
	{
		return this.maxAnimalRegions;
	}
}
