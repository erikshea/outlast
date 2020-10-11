package animals;

public class Dragon extends Animal {
	public void reset(){
		this.type = "dragon";
		this.naturalEnemyType = "monkey";
		this.lifeExpectancy = 90;
		this.baseMaxSize = 260;
		this.energy = this.baseMaxEnergy = 90;
		this.health = this.baseMaxHealth = 90;
		super.reset();
	}
}
