package com.erikshea.outlast.animals;

public class Dragon extends Animal {
	
	// Subclass-specific property values
	public void reset(){
		this.type = "dragon";
		this.naturalEnemyType = "monkey";
		this.lifeExpectancy = 90;
		this.maxSize = 260;
		this.baseMaxEnergy = 90;
		this.baseMaxHealth = 90;
		super.reset();
	}
}
