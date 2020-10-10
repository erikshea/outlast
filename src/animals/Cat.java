package animals;

public class Cat extends Animal {

	public void reset(){
		super.reset();
		this.type = "cat";
		this.naturalEnemyType = "dog";
		this.lifeExpectancy = 20;
		this.baseMaxSize = 30;
		this.energy = this.baseMaxEnergy = 70;
		this.health = this.baseMaxHealth = 50;
	}
	
	
	
	
	public void meow()
	{
		System.out.println("meo");
	}
	/*
	 * 
	 * 
	 * 
	public Animal reproduceWith(Animal partner) 
	{
		return (Cat) super.reproduceWith(partner);
	}*/
	
}
