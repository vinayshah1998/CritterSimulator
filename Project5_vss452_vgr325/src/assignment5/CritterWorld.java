/* CRITTERS CritterWorld.java
 * EE422C Project 5 submission by
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

package assignment5;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CritterWorld {
	private String[][] grid;
	private Set<Critter> population;
	private Set<Critter> babies;
	
	
	public CritterWorld() {
		grid = new String[Params.WORLD_HEIGHT][Params.WORLD_WIDTH];
		population = new HashSet<Critter>();
		babies = new HashSet<Critter>();
	}


	/**
	 * Adds critter to collection
	 * @param crit
	 */
	public void addCritterToCollection(Critter crit) {
		population.add(crit);
	}

	/**
	 * Getter for critter population
	 * @return population
	 */
	public Set<Critter> getPopulation() {
		return population;
	}

	/**
	 * Getter for babies population
	 * @return babies
	 */
	public Set<Critter> getBabies() {
		return babies;
	}

	/**
	 * Outputs the grid to the console
	 * @param i
	 */
	public void printGridLine(int i) {
		for(int j=0; j<Params.WORLD_WIDTH; j++) {
			if(grid[i][j] == null)
				System.out.print(" ");
			else
				System.out.print(grid[i][j].toString());
		}	
	}


	/**
	 * Getter for grid
	 * @return grid
	 */
	public String[][] getGrid() {
		return grid;
	}

	/**
	 * Finds if the adjacent place in the grid is empty with the specified step size
	 * @param crit
	 * @param dir
	 * @param stepSize
	 * @return
	 */
	protected boolean isAdjacentEmpty(Critter crit, int dir, int stepSize){
		int x_coord = crit.getX_coord();
		int y_coord = crit.getY_coord();
		switch(dir) {
			case 0: //(0,1)
	            x_coord=(x_coord+stepSize) % Params.WORLD_WIDTH;
	            //y_coord = (y_coord+0) % Params.WORLD_HEIGHT;
	            break;
	        case 1: //(-1,1)
	            x_coord = (x_coord+stepSize) % Params.WORLD_WIDTH;
	            y_coord = (y_coord-stepSize) % Params.WORLD_HEIGHT;
	            if(y_coord < 0) { y_coord += Params.WORLD_HEIGHT; }
	            break;
	        case 2: //(-1,0)
	            //x_coord = (x_coord-0) % Params.WORLD_WIDTH;
	            y_coord=(y_coord-stepSize) % Params.WORLD_HEIGHT;
	            if(y_coord < 0) { y_coord += Params.WORLD_HEIGHT; }
	            break;
	        case 3: //(-1,-1)
	            x_coord = (x_coord-stepSize) % Params.WORLD_WIDTH;
	            y_coord = (y_coord-stepSize) % Params.WORLD_HEIGHT;
	            if(x_coord < 0) { x_coord += Params.WORLD_WIDTH; }
	            if(y_coord < 0) { y_coord += Params.WORLD_HEIGHT;  }
	            break;
	        case 4: //(0,-1)
	            x_coord=(x_coord-stepSize) % Params.WORLD_WIDTH;
	            //y_coord = (y_coord+0) % Params.WORLD_HEIGHT;
	            if(x_coord < 0) { x_coord += Params.WORLD_WIDTH;  }
	            break;
	        case 5: //(1,-1)
	            x_coord = (x_coord-stepSize) % Params.WORLD_WIDTH;
	            y_coord = (y_coord+stepSize) % Params.WORLD_HEIGHT;
	            if(x_coord < 0) { x_coord += Params.WORLD_WIDTH;  }
	            break;
	        case 6: //(1,0)
	            //x_coord = (x_coord+0) % Params.WORLD_WIDTH;
	            y_coord=(y_coord+stepSize) % Params.WORLD_HEIGHT;
	            break;
	        case 7: //(1,1)
	            x_coord = (x_coord+stepSize) % Params.WORLD_WIDTH;
	            y_coord = (y_coord+stepSize) % Params.WORLD_HEIGHT;
	            break;
	        default:
	            break;
		}
		
		return isXYEmpty(crit, x_coord, y_coord);
	}

	/**
	 * Checks if a specific x,y spot on the grid is empty
	 * @param crit
	 * @param x_coord
	 * @param y_coord
	 * @return
	 */
	protected boolean isXYEmpty(Critter crit, int x_coord, int y_coord){
		if (grid[y_coord][x_coord] == null){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Updates the critters on the grid
	 */
	public void updateGrid() {
		this.clearGrid();
		if(!population.isEmpty()) {
			for(Critter c : population){
				//prints alive critters only
				if (c.isAlive()){
					grid[c.getY_coord()][c.getX_coord()] = c.toString();
				}
			}
		}
	}

	/**
	 * Clears the grid
	 */
	public void clearGrid() {
		grid = new String[Params.WORLD_HEIGHT][Params.WORLD_WIDTH];
	}

	/**
	 * Clears the CritterWorld
	 */
	public void clear() {
		population.clear();
		babies.clear();
		clearGrid();
	}

	/**
	 * Removes dead critters from the population set
	 */
	public void removeDeadCritters(){
		population.removeIf((Critter crit) -> !crit.isAlive());
	}

	/**
	 * Resets the moved fields to false for all critters in the population
	 */
	public void resetMovement(){
		for (Critter crit : population){
			crit.setMoved(false);
		}
	}

	/**
	 * Simulates encounters for all the Critters in the population
	 */
	public void doEncounters(){
		ArrayList<Critter> populationArray = new ArrayList<>(population);
		for (int i = 0; i < populationArray.size(); i++) {
			if (populationArray.get(i).isAlive()){
				populationArray.get(i).setEncounters(true);
				for (int j = 0; j < populationArray.size(); j++){
					if (populationArray.get(j).isAlive()){
						populationArray.get(j).setEncounters(true);
						if (i == j){
							continue;
						} else if ((populationArray.get(i).getX_coord()) != (populationArray.get(j).getX_coord())){
							continue;
						} else if ((populationArray.get(i).getY_coord()) != (populationArray.get(j).getY_coord())){
							continue;
						} else {
							boolean A_fight = populationArray.get(i).fight(populationArray.get(j).toString());
							boolean B_fight = populationArray.get(j).fight(populationArray.get(i).toString());
							Critter A = populationArray.get(i);
							Critter B = populationArray.get(j);
							int A_roll = 0;
							int B_roll = 0;
							if ((A.getX_coord() == B.getX_coord()) && (A.getY_coord() == B.getY_coord())) {
								if (A_fight && B_fight) {
									if (A.isAlive() && B.isAlive()) {
										A_roll = Critter.getRandomInt(A.getEnergy());
										B_roll = Critter.getRandomInt(B.getEnergy());
										if (A_roll >= B_roll) {
											B.setAlive(false);
											//A.addEnergy(B.getEnergy() / 2);
											A.setEnergy(A.getEnergy() + B.getEnergy() / 2);
										} else {
											A.setAlive(false);
											//B.addEnergy(A.getEnergy() / 2);
											B.setEnergy(B.getEnergy() + A.getEnergy() / 2);
										}
									}
								} else if (!A_fight && !B_fight) {
									B.setAlive(false);
									//A.addEnergy(B.getEnergy() / 2);
									A.setEnergy(A.getEnergy() + B.getEnergy() / 2);
								} else if (A_fight) {
									B.setAlive(false);
									//A.addEnergy(B.getEnergy() / 2);
									A.setEnergy(A.getEnergy() + B.getEnergy() / 2);
								} else {
									A.setAlive(false);
									//B.addEnergy(A.getEnergy() / 2);
									B.setEnergy(B.getEnergy() + A.getEnergy() / 2);
								}
							}

						}
						populationArray.get(j).setEncounters(false);
					}
				}
				populationArray.get(i).setEncounters(false);
			}
		}
	}
}
