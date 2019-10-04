/* CRITTERS Stalker.java
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


/**
 * Stalker is a critter that chooses a target at birth and then stalks it till death of either critter. Upon target dying,
 * the Stalker chooses a new target.
 */

package assignment5;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Stalker extends Critter{
    private Critter stalkee;
    private int direction;

    public Stalker(){
        chooseCritter();
        if (stalkee != null){
            direction = findDirection();
        }
    }


    @Override
    public void doTimeStep() {
        if (stalkee == null){
            chooseCritter();
        }
        if (stalkee != null){
            if (!stalkee.isAlive()){
                chooseCritter();
            }
            direction = findDirection();
            walk(direction);
        } else {
            walk(getRandomInt(7));
        }
    }

    @Override
    public boolean fight(String opponent) {
        return false;
    }

    @Override
    public String toString() { return "S"; }

    private void chooseCritter(){
        ArrayList<Critter> populationList = new ArrayList<>(world.getPopulation());
        int size = populationList.size();
        if (size > 0){
            stalkee = populationList.get(getRandomInt(populationList.size()));
        } else {
            stalkee = null;
        }

    }

    private int findDirection(){
        int dx = stalkee.getX_coord() - getX_coord();
        int dy = stalkee.getY_coord() - getY_coord();

        if (dx == 0 && dy == 0) return -1;

        if (dx > 0 && dy == 0) return 0;
        if (dx > 0 && dy < 0) return 1;
        if (dx == 0 && dy < 0) return 2;
        if (dx < 0 && dy < 0) return 3;
        if (dx < 0 && dy == 0) return 4;
        if (dx < 0 && dy > 0) return 5;
        if (dx == 0 && dy > 0) return 6;
        else return 7;
    }


	@Override
	public CritterShape viewShape() {
		return CritterShape.STAR;
	}

	@Override
    public javafx.scene.paint.Color viewColor() {
        return Color.ORANGE;
    }

    @Override
    public javafx.scene.paint.Color viewOutlineColor() {
        return viewColor();
    }
}
