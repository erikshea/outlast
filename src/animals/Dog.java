package animals;

public class Dog extends Animal {
	public void reset(){
		this.type = "dog";
		this.naturalEnemyType = "cat";
		this.lifeExpectancy = 12;
		this.baseMaxSize = 70;
		this.energy = this.baseMaxEnergy = 30;
		this.health = this.baseMaxHealth = 75;
		super.reset();
	}
}
