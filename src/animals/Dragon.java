package animals;

public class Dragon extends Animal {
	public void reset(){
		super.reset();
		this.type = "dragon";
		this.naturalEnemyType = "monkey";
		this.lifeExpectancy = 900;
		this.baseMaxSize = 260;
		this.energy = this.baseMaxEnergy = 90;
		this.health = this.baseMaxHealth = 90;
	}
}
