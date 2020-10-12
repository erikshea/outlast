package animals;

public class Monkey extends Animal {
	public void reset(){
		this.type = "monkey";
		this.naturalEnemyType = "dragon";
		this.lifeExpectancy = 30;
		this.maxSize = 60;
		this.baseMaxHealth = 65;
		this.baseMaxEnergy = 40;
		super.reset();
	}
}
