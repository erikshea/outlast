package animals;

public class Monkey extends Animal {
	public void reset(){
		this.type = "monkey";
		this.naturalEnemyType = "dragon";
		this.lifeExpectancy = 30;
		this.baseMaxSize = 60;
		this.health = this.baseMaxHealth = 65;
		this.energy = this.baseMaxEnergy = 40;
		super.reset();
	}
}
