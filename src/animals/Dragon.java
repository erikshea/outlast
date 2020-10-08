package animals;

public class Dragon extends Animal {
	public void reset(){
		super.reset();
		this.type = "dragon";
		this.lifeExpectancy = 900;
		this.baseMaxSize = 260;
		this.health = this.baseMaxHealth = 90;
	}
}
