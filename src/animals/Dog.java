package animals;

public class Dog extends Animal {
	public void reset(){
		super.reset();
		this.type = "dog";
		this.lifeExpectancy = 12;
		this.baseMaxSize = 70;
		this.energy = this.baseMaxEnergy = 30;
		this.health = this.baseMaxHealth = 75;
	}
}
