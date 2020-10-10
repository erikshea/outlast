package animals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Animal {
	protected String name;
	protected String color;
	protected String type;
	protected String naturalEnemyType;
	protected double age;
	protected int lifeExpectancy;
	protected double health;			// Current health
	protected double baseMaxHealth;		// Health at birth, max health grows with age.
	protected double energy;		// Current energy
	protected double baseMaxEnergy;	// Energy at birth, max energy grows with age.
	protected double foodPercentage;
	
	protected double excrementPercentage;

	protected double baseMaxSize;	// max attainable size. size depends on age 
	
	protected boolean hasMask;
	protected String hairColor;
	
	/**
	 * 	Only constructor, attributes set in GUI
	 */
	public Animal() {
		this.reset();
	}


	/**
	 * Default values for all parameter
	 */
	public void reset() {
		this.age = 0;
		this.foodPercentage = 100;
		this.excrementPercentage = 0;
		this.hasMask = false;
		this.setRandomName();
	}

	/**
	 * Size, depends on age
	 * @return
	 */
	public double getSize() //TODO: add growth bonus from food
	{
		double growthRange = this.baseMaxSize-this.getBaseSize();
		
		return this.getMaturationRatio()*growthRange + this.getBaseSize();
	}
	
	/**
	 * Calculates current size as a percentage of max size.
	 * @return size percentage.
	 */
	public double getSizePercentage()
	{
		return 100*this.getSize()/this.baseMaxSize;
	}
	
	/**
	 * Is animal alive?
	 * @return true if alive
	 */
	public boolean isAlive()
	{
		return !(this.getHealth() <= 0);
	}
	


	/**
	 * Sets an animal as dead.
	 */
	public void makeDie(boolean a) {
		this.setHealth(0);
	}

	
	/**
	 * mask action: Removes mask if on, and vise versa
	 */
	public void toggleMask() {
		this.hasMask = !this.hasMask;
	}

	/**
	 * Create a new Cat inheriting traits from both the caller, and a partner
	 * @param partner
	 * @return
	 */
	public Animal reproduceWith(Animal partner) {
		try {
			Animal baby = this.getClass().getConstructor().newInstance(); // need to create instance with class constructor, so subclasses don't generate generic animal
			baby.reset();
			
			return this.getClass().cast(baby); // cast baby to subclass
		} catch (Exception E)
		{
			return null;
		}

		//baby.setColor( this.getRandomParent(partner).getColor() ); //todo: mix color
	}


	/**
	 * For reproduction purposes, return a random parent
	 * @param partner another cat with which to reproduce
	 * @return either the caller cat, or its partner
	 */
	protected Animal getRandomParent(Animal partner) {
		
		Animal returnedParent = new Animal();
		
		if ((int) (2 * Math.random()) == 0) {
			returnedParent = this;
		} else
		{
			returnedParent = partner;
		}
		
		return returnedParent;
	}
	
	
	/*
	 * Change health by ammount (negative or positive)
	 */
	public void changeHealthBy(double ammount)
	{
		this.setHealth(this.getHealth() + ammount);
	}
	
	/*
	 * Change energy by ammount (negative or positive)
	 */
	public void changeEnergyBy(int ammount)
	{
		this.setEnergy(this.getEnergy() + ammount);
	}
	

	
	/*
	 * Change age by ammount (negative or positive)
	 */
	public void changeAgeBy(double ammount)
	{
		this.setAge(this.getAge() + ammount);
	}
	
	
	/**
	 *  Find out age at which animal stops growing
	 * @return
	 */
	public int getMatureAge()
	{
		return (this.lifeExpectancy / 3);
	}
	
	
	/**
	 * Get the maturation ratio between current age and mature age: a double from 0 (at birth) to 1 (has stopped growing)
	 * @return
	 */
	public double getMaturationRatio()
	{
		double growthRatio = this.age  / this.getMatureAge();
		
		if (growthRatio>1)
		{
			growthRatio = 1;
		}
		return growthRatio;
	}
	
	/**
	 * Get the size ratio between current size and mature size
	 * @return
	 */
	public double getGrowthRatio()
	{
		return this.getSize()/this.baseMaxSize;
	}
	
	/**
	 * Maximum health at current stage of growth
	 * @return
	 */
	public double getMaxHealth()
	{
		return this.baseMaxHealth * this.getGrowthRatio();
	}
	
	
	/**
	 * Maximum health at current stage of growth
	 * @return
	 */
	public double getMaxEnergy()
	{
		return this.baseMaxEnergy * this.getGrowthRatio();
	}
	
	
	/**
	 * Size at birth
	 * @return
	 */
	public  double getBaseSize()
	{
		return this.baseMaxSize/4;
	}


	/**
	 * Sets health, if above max health resets to max health
	 * @param h
	 */
	public void setHealth(double h) {
		if (h <= this.getMaxHealth()) {
			this.health = h;
		} else {
			this.health = this.getMaxHealth();
		}
	}
	
	/**
	 * Sets age, if above life expectancy, die
	 * @param newAge
	 */
	protected void setAge(double newAge) {
		double timeElapsed = newAge - this.age;
		
		this.changeExcrementPercentageBy((timeElapsed)*15);
		
		double oldMaxHealth = this.getMaxHealth();
		double oldMaxEnergy = this.getMaxEnergy();
		
		this.age = newAge;
		
		// current health changes by a factor of newMaxHealth/oldMaxHealth
		this.setHealth(this.getMaxHealth()/oldMaxHealth * this.getHealth());
		
		// current energy changes by a factor of newMaxHealth/oldMaxHealth
		this.setEnergy(this.getMaxEnergy()/oldMaxEnergy * this.getEnergy());
		
		if (this.excrementPercentage == 100)
		{
			this.changeHealthBy(timeElapsed);
		}
		

		if (this.lifeExpectancy < this.age)
		{
			this.setHealth(0);
		}
	}

	
	public void setName(String n) {
		this.name = n;
	}

	public String getName() {
		return this.name;
	}


	
	public String getColor() {
		return this.color;
	}

	public void setColor(String c) {
		this.color = c;
	}

	public double getHealth() {
		return this.health;
	}
	

	public double getAge() {
		return this.age;
	}
	
	public String getType() {
		return this.type;
	}
	
	public double getEnergy() {
		return this.energy;
	}

	public void setEnergy(double m) {
		this.energy = m;
	}

	public double getLifeExpectancy() {
		return this.lifeExpectancy;
	}
	
	
	public double getExcrementPercentage() {
		return this.excrementPercentage;
	}
	
	public String getNaturalEnemyType() {
		return this.naturalEnemyType;
	}
	
	/**
	 * Sets current excrement level as a percentage. Negative values are reset to 0.
	 * @param p 
	 */
	public void setExcrementPercentage(double p) {

		if (p < 0) {
			p = 0;
		}
		
		this.excrementPercentage = p;
	}
	
	public void changeExcrementPercentageBy(double p) {
		this.setExcrementPercentage(this.excrementPercentage + p);
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
}
