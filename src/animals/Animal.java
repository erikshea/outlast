package animals;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Animal {
	protected String name;
	protected String color;
	protected String type;
	protected double age;
	protected int lifeExpectancy;
	protected double health;			// Current health
	protected double baseMaxHealth;		// Health at birth, maxHealth depends on size
	protected int moodPercentage;		// Current mood as a percentage

	protected double food;
	protected double baseMaxFoodLevel;	// Food level at birth, maxFoodLevel depends on age
	
	protected int excrementPercentage;

	//protected double baseSize; 	// size at birth, maxSize depends on age (TODO: food)
	protected double baseMaxSize;			
	
	protected boolean hasMask;
	protected String hairColor;
	
	
	public Animal() {
		this.reset();
	}

	public Animal(String name) {
		this.reset();
		this.name = name;
	}

	public Animal(String name, String color, int age) {
		this.reset();
		this.name = name;
		this.color = color;
		this.age = age;
	}

	/**
	 * Default values for all parameter
	 */
	public void reset() {
		this.age = 0;
		
		this.baseMaxSize = 30;

		this.health = this.baseMaxHealth = 50;
		
		this.baseMaxFoodLevel = 70;
		
		this.moodPercentage = 100;
		this.lifeExpectancy = 20;
		this.excrementPercentage = 0;
		this.hasMask = false;
		this.color = "tabby";
	}


	public double getSize() //TODO: add growth bonus from food
	{
		double growthRange = this.baseMaxSize-this.getBaseSize();
		
		return this.getMaturationRatio()*growthRange + this.getBaseSize();
	}
	
	
	public double getSizePercentage()
	{
		return 100*this.getSize()/this.baseMaxSize;
	}
	
	public boolean isAlive()
	{
		return this.getHealth() == 0;
	}
	



	public void setBowelStatus(int bStatus) {
		this.excrementPercentage = bStatus;

		if (this.excrementPercentage < 0) {
			this.excrementPercentage = 0;
		}

	}

	public void show() {
		System.out.println("Chat nommé " + this.name + " de couleur " + this.color + " et d'age " + this.age);
	}

	public void die() {
		// this.health=0;
		this.setHealth(0);
	}


	public void smoke() {
		this.setHealth(this.health - 1);
		this.setFood(this.food + 1);
	}

	public void jumpInJoy() {
		if (this.isAlive()) {
			System.out.println("Hooray!");
		}
	}

	public void toggleMask() {
		this.hasMask = !this.hasMask;
	}

	/**
	 * Eats food
	 * @param quantity quantity
	 */
	public void eat(int quantity) {
		this.food += quantity;
	}

	/**
	 * Ages one year
	 */
	public void hasBirthday() {
		this.changeAgeBy(1);
	}

	/**
	 * Empty bowels
	 */
	public void goToBathroom() {
		this.setBowelStatus(0);
	}
	
	/**
	 * Play a sport
	 */
	public void playSports() {
		this.changeHealthBy(1);
	}

	/**
	 * Create a new Cat inheriting traits from both the caller, and a partnet
	 * @param partner
	 * @return
	 */
	public Animal reproduce(Animal partner) {
		Animal newCat = new Animal();

		newCat.reset();

		newCat.setColor( this.getRandomParent(partner).getColor() );

		return newCat;
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
	
	protected void changeHungerBy(int ammount)
	{
		this.setFood(this.food + ammount);
	}
	
	protected void changeHealthBy(int ammount)
	{
		this.setHealth(this.getHealth() + ammount);
	}
	
	protected void changeAgeBy(int ammount)
	{
		this.setAge(this.getAge() + ammount);
	}
	

	
	public void setHairColor(String hc)
	{
		if ( ! (hc == "ginger") )
		{
			this.hairColor = hc;
		}
	}
	
	public void wash()
	{
		
	}
	
	public int getMatureAge()
	{
		return (this.lifeExpectancy / 3);
	}
	
	public double getMaturationRatio()
	{
		double growthRatio = this.age  / this.getMatureAge();
		
		if (growthRatio>1)
		{
			growthRatio = 1;
		}
		return growthRatio;
	}
	
	public double getGrowthRatio()
	{
		return this.getSize()/this.baseMaxSize;
	}
	
	public double getMaxHealth()
	{
		return this.baseMaxHealth * this.getGrowthRatio();
	}
	
	public double getMaxFoodLevel()
	{
		return this.baseMaxFoodLevel + (this.baseMaxFoodLevel * this.getMaturationRatio());
	}
	
	public  double getBaseSize()
	{
		return this.baseMaxSize/4;
	}

	public void increaseAge( double duration ) {
		double healthIncreaseRatio = (this.getAge() + duration)/this.getAge();	

		this.setHealth(healthIncreaseRatio * this.getHealth());
		
		this.setAge(this.age + duration);
	}

	public void setPostActionParameters()
	{
		this.increaseAge( 0.1 );
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

	public double getFood() {
		return this.food;
	}

	public void setFood(double h) {
		this.food = h;
	}

	public double getHealth() {
		return this.health;
	}
	
	public void setHealth(double h) {
		if (h <= this.getMaxHealth())
		{
			this.health = h;
		}
	}
	
	public double getAge() {
		return this.age;
	}
	
	public String getType() {
		return this.type;
	}
	
	public void setAge(double a) {
		if (this.lifeExpectancy < a)
		{
			this.setHealth(0);
		}
		
		this.age = a;
	}
	

	public int getExcrementPercentage() {
		return this.excrementPercentage;
	}
	
	
	public void doActionBathrooom()
	{
		
	}
	
	public void doActionColor()
	{
		
	}
	
	public void doActionEat()
	{
		
	}
	
	public void doActionExercise()
	{
		
	}
	
	public void doActionMask()
	{
		
	}
	

	
	public void doActionSeeFriend()
	{
		
	}
	
	public void doSocialActionReproduce()
	{
		
	}
	
	public void doSocialActionSeeFriend()
	{
		
	}
	/*
	public List<List<Method>> getActionMethods()
	{
		List<Method> publicMethods = new ArrayList<Method>(), normal = new ArrayList<Method>(), social = new ArrayList<Method>();
		
		for (Class<?> currentClass = this.getClass(); currentClass != null; currentClass = currentClass.getSuperclass()) {
		  for (Method method : currentClass.getDeclaredMethods()) {
			
			if ( Modifier.isPublic(method.getModifiers()) ) {
				publicMethods.add(method);
			}
		  }
		}
		
		for (Method m: publicMethods)
		{
			if (m.getName().startsWith("doAction")){
				normal.add(m);
			};
			if (m.getName().startsWith("doSocialAction")){
				social.add(m);
			};
		}
		
		
		List<List<Method>> result = new ArrayList<List<Method>>();
		result.add(normal);
		result.add(social);
		
		return result;
	}*/

}
