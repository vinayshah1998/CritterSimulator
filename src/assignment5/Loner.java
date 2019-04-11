package assignment5;
/* CRITTERS Loner.java
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


/* Likes to avoid everyone and stay alone in life
 */

public class Loner extends Critter {

	@Override
	/**
	 * Returns String representation of Loner
	 */
	public String toString() { return "L"; }
	
	private int dir;
	private int myLimit;
	
	public Loner() {
		dir = getRandomInt(8);
		myLimit = getRandomInt(4);
		//myLimit = 1;
	}

	/**
	 * Finds the number of people surrounding the Loner
	 * @return numCrittersSurrounding
	 */
	private int tooManyPeople() {
		int numCrittersSurrounding = 0;
		for(int iDontLikePeople=0; iDontLikePeople<8; iDontLikePeople++) {
			if(!adjacentEmpty(this, iDontLikePeople, 1)) {
				numCrittersSurrounding++;
			}
		}
		return numCrittersSurrounding;
	}

	/**
	 * Loner does not fight, just returns false
	 * @param not_used
	 * @return false
	 */
	public boolean fight(String not_used) {	return false; } 
	
	@Override
	/**
	 * Simulates timestep for the Loner. Checks to see if there are more people around him than the limit.
	 * If limit is exceeded, the Loner dies.
	 */
	public void doTimeStep() {		
		if(tooManyPeople() < myLimit) {
			while(true) {
				if(adjacentEmpty(this, dir, 1)) {
					walk(dir);
					break;
				}
				dir = (dir+1)%8;
			}
		}
		else if(tooManyPeople() >= 5) {
			setAlive(false);
		}
		else {
			while(true) {
				if(adjacentEmpty(this, dir, 2)) {
					run(dir);
					break;
				}
				dir = (dir+1)%8;
			}
		}
		dir = Critter.getRandomInt(8);
	}

	@Override
	public CritterShape viewShape() {
		// TODO Auto-generated method stub
		return null;
	}
}
