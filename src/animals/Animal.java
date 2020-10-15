package animals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Represents an Animal. Parameters update as age increases. Superclass for other animal types.
 * @author Erik Shea
 *
 */
public class Animal {
	protected String name;
	protected double color;	// Color, as a hue variation from base picture (-1 to 1)
	protected String type;
	protected String naturalEnemyType;	// Natural enemy
	protected int lifeExpectancy;
	protected SimpleDoubleProperty health,maxHealth,age,maxEnergy,energy,size,excrementPercentage;	
	protected SimpleBooleanProperty alive, hasMask;
	protected String potentialCauseOfDeath;
	
	protected double baseMaxHealth;		// Health at birth, max health grows with age.
	protected double baseMaxEnergy;	// Energy at birth, max energy grows with age.
	protected double maxSize;	// max attainable size. size depends on age 
	
	/**
	 * 	Initialize shared observable properties
	 */
	public Animal() {
		this.health = new SimpleDoubleProperty();
		this.energy = new SimpleDoubleProperty();
		this.maxHealth = new SimpleDoubleProperty();
		this.maxEnergy = new SimpleDoubleProperty();
		this.age = new SimpleDoubleProperty(0);
		this.size = new SimpleDoubleProperty();
		this.excrementPercentage = new SimpleDoubleProperty(0);
		this.alive = new SimpleBooleanProperty(true);
		this.hasMask = new SimpleBooleanProperty(false);
		this.reset();
	}

	/**
	 * Initialize shared properties that depend on subclass-specific properties
	 */
	public void reset() {
		this.setUpListeners();
		this.setRandomName();
		this.setColor(2*Math.random()-1);
		this.maxHealth.setValue(this.baseMaxHealth);
		this.maxEnergy.setValue(this.baseMaxEnergy);
    	this.energy.setValue(this.maxEnergy.get()/2);	// energy at 50%
        this.health.setValue((2*Math.random()+3)/5 * this.maxHealth.get()); // random health between 80% and 100%
	}

	/**
	 * Use listeners to automatically update inter-dependent properties.
	 */
	public void setUpListeners()
	{    	
		// age-dependent properties
    	this.age.addListener((arg, oldAge, newAge) -> {
    		// calculate time elapsed since last age update
    		double timeElapsed = newAge.doubleValue() - oldAge.doubleValue();
    		this.changeExcrementPercentageBy((timeElapsed)*5); // Excrement goes up
    		
    		// How much animal grows between birth and death
    		double growthRange = this.maxSize-this.getBaseSize();
    		
    		// How far along is animal to full maturity
    		double maturationRatio = newAge.doubleValue()  / this.getMatureAge();
    		if(maturationRatio>1) {
    			maturationRatio=1;
    		}
    		
    		// Size depends on both of the above
    		this.size.setValue(maturationRatio*growthRange + this.getBaseSize());
    		
    		if (this.excrementPercentage.get() >= 100) {
    			this.potentialCauseOfDeath = "bowels";	// In case it kills animal
    			this.changeHealthBy(-timeElapsed*15);	// Lose health if bowels impacted
    		}
    		
    		// if age exceeds life expectancy, set to dead
    		if (this.lifeExpectancy < newAge.doubleValue() ) {	
    			this.potentialCauseOfDeath = "age";
    			this.alive.set(false);
    		}
    	});
    	
    	// if health dips below 0, set to dead
    	this.health.addListener((arg, oldHealth, newHealth) -> {
    		if ( newHealth.doubleValue() <= 0 ) {
    			this.alive.set(false);
    		}
    	});
    	
    	// maxHealth and maxEnergy increase with size.
    	this.size.addListener((arg, oldSize, newSize) -> {
    		double growthRatio = newSize.doubleValue()/this.maxSize;
    		
    		this.maxHealth.setValue(this.baseMaxHealth * growthRatio);

    		this.maxEnergy.setValue(this.baseMaxEnergy * growthRatio);
    	});
    	

		// when maxHealth changes, current health changes by the same ratio
    	this.maxHealth.addListener((arg, oldMaxHealth, newMaxHealth) -> {
    		this.health.setValue( this.health.get() * newMaxHealth.doubleValue()/oldMaxHealth.doubleValue() );
    	});

		// when maxEnergy changes, current energy changes by the same ratio
    	this.maxEnergy.addListener((arg, oldMaxEnergy, newMaxEnergy) -> {
    		this.energy.setValue( this.energy.get() * newMaxEnergy.doubleValue()/oldMaxEnergy.doubleValue() );
    	});
		
	}

	
	/**
	 * mask action: Removes mask if on, and vise versa
	 */
	public void toggleMask() {
		this.hasMask.set(!this.hasMask.get());
	}

	/**
	 * Create a new Cat inheriting traits from both the caller, and a partner
	 * @param partner
	 * @return baby Animal
	 */
	public<T extends Animal> T reproduceWith(T partner) {
		try {
			@SuppressWarnings("unchecked")
		 	T baby = (T)this.getClass().getConstructor().newInstance(); // use newInstance to always return animal of correct subclass
			
			baby.combineTraits(this, partner);
			
			return baby;
		} catch (Exception E)
		{
			return null;
		}
	}
	
	/**
	 * For reproduction purposes, mix traits from 2 animals
	 * @param parent1 a parent
	 * @param parent2 the other parent
	 */
	public<T extends Animal>  void combineTraits(T parent1, T parent2)
	{
		this.color = (parent1.getColor() + parent2.getColor())/2;
	}
	
	
	
	/**
	 * Change health by ammount (negative or positive)
	 */
	public void changeHealthBy(double ammount)
	{
		this.health.setValue(this.health.get() + ammount);
	}
	
	/**
	 * Change health by ammount, with a reason for the change (to keep track of cause of death).
	 * @param ammount value to change by, positive or negative
	 */
	public void changeHealthBy(double ammount,String reason)
	{
		this.health.setValue(this.health.get() + ammount);
		this.potentialCauseOfDeath = reason;
	}
	
	
	/**
	 * Change energy by ammount 
	 * @param ammount value to change by, positive or negative
	 */
	public void changeEnergyBy(int ammount)
	{
		this.energy.setValue(this.energy.get() + ammount);
	}
	

	
	/*
	 * Change age by ammount 
	 * @param ammount value to change by, positive or negative
	 */
	public void changeAgeBy(double ammount)
	{
		this.age.setValue(this.age.get() + ammount);
	}
	
	
	/**
	 * @return age at which animal stops growing
	 */
	public int getMatureAge()
	{
		return (this.lifeExpectancy / 5);
	}

	/**
	 * @return Size at birth
	 */
	public  double getBaseSize()
	{
		return this.maxSize/4;
	}


	/**
	 * Sets health, if above max health resets to max health
	 * @param h
	 */
	public void setHealth(double h) {
		if (h <= this.maxHealth.get()) {
			this.health.setValue(h);
		} else {
			this.health.setValue(this.maxHealth.get());
		}
	}
	
	
	
	/**
	 * Sets current excrement level as a percentage. Negative values are reset to 0.
	 * @param p value to change by, positive or negative
	 */
	public void setExcrementPercentage(double p) {
		if (p < 0) {
			p = 0;
		}
		
		this.excrementPercentage.setValue(p);
	}
	/**
	 * Changes excrement percentage by a value
	 * @param p value to change by, positive or negative
	 */
	public void changeExcrementPercentageBy(double p) {
		this.excrementPercentage.setValue(this.excrementPercentage.get() + p);
	}
	
    /**
     * Reads a file consisting of a list of names, sets name a random one
     */
    private void setRandomName()
    {
    	List<String> names = new ArrayList<String>();
    	
    	 try {
    	      File animalNamesFile = new File("@../../resources/data/animal_names.txt");
    	      Scanner nameReader = new Scanner(animalNamesFile);
    	      while (nameReader.hasNextLine()) {
    	        String name = nameReader.nextLine();
    	        names.add(name);
    	      }
    	      nameReader.close();
    	    } catch (FileNotFoundException e) {
    	      System.out.println("Read error.");
    	      e.printStackTrace();
    	    }
    	
    	int nameIndex = (int) (names.size() * Math.random());
    	
    	this.name = names.get(nameIndex);
    }

	// Generic getters + setters
    
    public void setName(String n) {
		this.name = n;
	}

	public String getName() {
		return this.name;
	}

	
	public double getColor() {
		return this.color;
	}

	public void setColor(double c) {
		this.color = c;
	}
	
	public double getMaxSize() {
		return this.maxSize;
	}
    
    public String getPotentialCauseOfDeath()
    {
    	return this.potentialCauseOfDeath;
    }
    
	public double getLifeExpectancy() {
		return this.lifeExpectancy;
	}
	
	public String getNaturalEnemyType() {
		return this.naturalEnemyType;
	}
	
	public String getType() {
		return this.type;
	}
	
    
    // Getters for observable properties. Always return final to protect against modification
    
	public final SimpleBooleanProperty getIsAliveProperty() {
		return this.alive;
	}
	
	public final SimpleDoubleProperty getSizeProperty() {
		return this.size;
	}
	
	public final SimpleDoubleProperty getHealthProperty() {
		return this.health;
	}

	public final SimpleDoubleProperty getMaxHealthProperty() {
		return this.maxHealth;
	}

	public final SimpleDoubleProperty getMaxEnergyProperty() {
		return this.maxEnergy;
	}

	public final SimpleDoubleProperty getAgeProperty() {
		return this.age;
	}
	public final SimpleDoubleProperty getEnergyProperty() {
		return this.energy;
	}

	public final SimpleDoubleProperty getExcrementPercentageProperty() {
		return this.excrementPercentage;
	}
	
	public final SimpleBooleanProperty getHasMaskProperty() {
		return this.hasMask;
	}
	
	// Value getters for observable properties
	
    public boolean isAlive()
    {
    	return this.alive.get();
    }
    
	public double getSize() {
		return this.size.get();
	}
	
	public double getHealth() {
		return this.health.get();
	}

	public double getMaxHealth() {
		return this.maxHealth.get();
	}

	public double getMaxEnergy() {
		return this.maxEnergy.get();
	}

	public double getAge() {
		return this.age.get();
	}
	public double getEnergy() {
		return this.energy.get();
	}

	public double getExcrementPercentage() {
		return this.excrementPercentage.get();
	}
	
}
