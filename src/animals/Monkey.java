package animals;

public class Monkey extends Animal {
	public void reset(){
		super.reset();
		this.type = "monkey";
		this.lifeExpectancy = 30;
		this.baseMaxSize = 60;
		this.health = this.baseMaxHealth = 65;
		this.energy = this.baseMaxEnergy = 40;
	}
}
