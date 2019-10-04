package assignment5;
/* CRITTERS Wizard.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Vinay Shah
 * vss452
 * 16205
 * Vignesh Ravi
 * vgr325
 * 16225
 * Slip days used: <0>
 * Spring 2019
 */



/* Wizard is a critter that will 'teleport' all critters it 
 * encounters (same location) to a random position on the board
 */

import javafx.scene.paint.Color;

public class Wizard extends Critter {
	
	@Override
	/**
	 * Returns String representation of Wizard
	 */
	public String toString() { return "W"; }
	
	private int dir;

	public Wizard() {
		dir = Critter.getRandomInt(8);
	}

	/**
	 * Simulates the fight step between Wizard and opponent
	 * @param opponent
	 * @return
	 */
	public boolean fight(String opponent) {	
		if(opponent.equals("@")) {
			return true;
		}
		else {
			setX_coord(Critter.getRandomInt(Params.WORLD_HEIGHT));
			setY_coord(Critter.getRandomInt(Params.WORLD_WIDTH));
			return false; 	
		}
	}
	
	@Override
	/**
	 * Simulates the timestep for the Wizard
	 */
	public void doTimeStep() { 
		walk(dir); 
		
		if(getEnergy() > Params.MIN_REPRODUCE_ENERGY) {
			Wizard child = new Wizard();
			reproduce(child, Critter.getRandomInt(8));
		}
		
		dir = Critter.getRandomInt(8);
	}


	@Override
	public CritterShape viewShape() {
		return CritterShape.CIRCLE;
	}

	@Override
	public javafx.scene.paint.Color viewColor() {
		return Color.RED;
	}

	@Override
	public javafx.scene.paint.Color viewOutlineColor() {
		return viewColor();
	}
}
