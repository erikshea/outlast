package animals;

public class Cat extends Animal {

	public void reset(){
		super.reset();
		this.type = "cat";
		this.lifeExpectancy = 20;
		this.baseMaxSize = 30;
		this.energy = this.baseMaxEnergy = 70;
		this.health = this.baseMaxHealth = 50;
	}
	
}
