package animals;

public class Animal {
	protected String name;
	protected String color;
	protected String type;
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
	 * smoke action: health increases, appetite decreases
	 */
	public void doActionSmoke() {
		this.setHealth(this.health - 1);	
		this.setFoodPercentage(this.foodPercentage + 1);
	}
	
	/**
	 * mask action: Removes mask if on, and vise versa
	 */
	public void toggleMask() {
		this.hasMask = !this.hasMask;
	}

	
	/**
	 * eat action: add to food
	 * @param quantity quantity
	 */
	public void doActionEat(double quantity) {
		this.foodPercentage += quantity;
	}


	/**
	 * bathroom action: empty bowels
	 */
	public void doActionBathroom() {
		this.setExcrementPercentage(0);
	}
	
	/**
	 * sport action: increase health
	 */
	public void doActionPlaySports() {
		this.changeHealthBy(1);
	}

	/**
	 * Create a new Cat inheriting traits from both the caller, and a partner
	 * @param partner
	 * @return
	 */
	public Animal doActionReproduce(Animal partner) {
		Animal newCat = new Animal();

		newCat.reset();

		newCat.setColor( this.getRandomParent(partner).getColor() ); //todo: mix color

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
	
	/*
	 * Change hunger by ammount (negative or positive)
	 */
	protected void changeHungerBy(int ammount)
	{
		this.setFoodPercentage(this.foodPercentage + ammount);
	}
	
	/*
	 * Change health by ammount (negative or positive)
	 */
	public void changeHealthBy(int ammount)
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
		this.changeExcrementPercentageBy((newAge - this.age)*15);
		
		double oldMaxHealth = this.getMaxHealth();
		double oldMaxEnergy = this.getMaxEnergy();
		
		this.age = newAge;
		
		// current health changes by a factor of newMaxHealth/oldMaxHealth
		this.setHealth(this.getMaxHealth()/oldMaxHealth * this.getHealth());
		
		// current energy changes by a factor of newMaxHealth/oldMaxHealth
		this.setEnergy(this.getMaxEnergy()/oldMaxEnergy * this.getEnergy());
		

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

	public double getFoodPercentage() {
		return this.foodPercentage;
	}

	public void setFoodPercentage(double h) {
		this.foodPercentage = h;
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
	
	/**
	 * Sets current excrement level as a percentage. Negative values are reset to 0.
	 * @param p 
	 */
	public void setExcrementPercentage(double p) {

		if (p < 0) {
			p = 0;
		}
		
		if (p>=100)
		{
			this.setHealth(0);
		}
		this.excrementPercentage = p;
	}
	
	public void changeExcrementPercentageBy(double p) {
		this.setExcrementPercentage(this.excrementPercentage + p);
	}
}
