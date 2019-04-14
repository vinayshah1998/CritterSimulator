/*
 * CRITTERS Critter.java
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
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/*
 * See the PDF for descriptions of the methods and fields in this
 * class.
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter {

    /* START --- NEW FOR PROJECT 5 */
    public enum CritterShape {
        CIRCLE,
        SQUARE,
        TRIANGLE,
        DIAMOND,
        STAR
    }

    /* the default color is white, which I hope makes critters invisible by default
     * If you change the background color of your View component, then update the default
     * color to be the same as you background
     *
     * critters must override at least one of the following three methods, it is not
     * proper for critters to remain invisible in the view
     *
     * If a critter only overrides the outline color, then it will look like a non-filled
     * shape, at least, that's the intent. You can edit these default methods however you
     * need to, but please preserve that intent as you implement them.
     */
    public javafx.scene.paint.Color viewColor() {
        return javafx.scene.paint.Color.WHITE;
    }

    public javafx.scene.paint.Color viewOutlineColor() {
        return viewColor();
    }

    public javafx.scene.paint.Color viewFillColor() {
        return viewColor();
    }

    public abstract CritterShape viewShape();

    protected final String look(int direction, boolean steps) {
    	// steps = false (1)
    	// steps = true  (2)
    	// direction: 0 - 7
    	
    	setEnergy(getEnergy() - Params.LOOK_ENERGY_COST);
    	
    	int stepSize = 1;
    	if(steps) {
    		stepSize = 2;
    	}

        int x_coord = getX_coord();
        int y_coord = getY_coord();

    	if (!encounters){
            x_coord = getPrevx_coord();
            y_coord = getPrevy_coord();
        }

		switch(direction) {
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

    	if(!encounters){
            return world.getGrid()[y_coord][x_coord];
        }

    	for (Critter crit : world.getPopulation()){
    	    if((crit.getX_coord() == x_coord) && (crit.getY_coord() == y_coord)){
    	        return crit.toString();
            }
        }
    	return null;
    }

    public static String runStats(List<Critter> critters) {
        // TODO Implement this method
        return null;
    }


    public static void displayWorld(Object pane) throws Exception{
        // TODO Implement this method
    	for(int i=0; i<Params.WORLD_HEIGHT; i++) {
    		for(int j=0; j<Params.WORLD_WIDTH; j++) {
    			if(world.getGrid()[i][j] != null){
    				Shape s = new Circle(Main.FACTOR/2);
    				((GridPane) pane).add(s, j, i);
    			}
    		}
    	}
    }

	/* END --- NEW FOR PROJECT 5
			rest is unchanged from Project 4 */

    private int energy = 0;

    private int prevx_coord;
    private int prevy_coord;

    private int x_coord;
    private int y_coord;

    private boolean moved = true;
    private static int timestep = 0;
    private boolean alive;
    private boolean encounters;

//    private static List<Critter> population = new ArrayList<Critter>();
//    private static List<Critter> babies = new ArrayList<Critter>();

    public static CritterWorld world = new CritterWorld();
    
    // Initialize world/grid that will hold all alive critters
    public static void initializeWorld(){
        world = new CritterWorld();
    }
    
    /* Gets the package name.  This assumes that Critter and its
     * subclasses are all in the same package. */
    private static String myPackage;

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    private static Random rand = new Random();

    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    public static void setSeed(long new_seed) {
        rand = new Random(new_seed);
    }
    
    /** 
     *  initialize private variables
     */
    protected void initializeCritter() {
    	energy = Params.START_ENERGY;
    	x_coord = getRandomInt(Params.WORLD_WIDTH);
    	y_coord = getRandomInt(Params.WORLD_HEIGHT);
        prevx_coord = x_coord;
        prevy_coord = y_coord;
    	alive = true;
    	moved = false;
    }

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the qualified name of a concrete
     * subclass of Critter, if not, an InvalidCritterException must be
     * thrown.
     *
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void createCritter(String critter_class_name)
            throws InvalidCritterException {
        // TODO: Complete this method
    	String newCritClassName = myPackage + "." + critter_class_name; 
    	try {
    		Object critter = Class.forName(newCritClassName).newInstance();
    		if (!(critter instanceof Critter)){
    		    throw new InvalidCritterException(newCritClassName);
            }
            ((Critter) critter).initializeCritter();
    		world.addCritterToCollection((Critter)critter);
    	}catch(ClassNotFoundException | InstantiationException | IllegalAccessException | NoClassDefFoundError ex) {
    		throw new InvalidCritterException(newCritClassName);
    	}
    }
    
    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the qualified name of a concrete subclass of Critter, if not,
     * an InvalidCritterException must be thrown.
     *
     * @param critter_class_name
     * @throws InvalidCritterException
     */
    public static void createBabies(String critter_class_name) throws InvalidCritterException {
    	String newCritClassName = myPackage + "." + critter_class_name; 
    	try {
    		Class<?> critter = Class.forName(newCritClassName); 
    		
        	switch(critter_class_name) {
        	case "Clover":
        		Clover clover = (Clover) critter.newInstance(); 
                clover.initializeCritter();
                world.getBabies().add(clover);
        		break;
        	default:
        		break;
        	}	
    	}catch(ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
    		throw new InvalidCritterException(newCritClassName);
    	} 	   	
    }
    

    /**
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.
     *                           Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException
     */
    public static List<Critter> getInstances(String critter_class_name)
            throws InvalidCritterException {
        String newCritClassName = myPackage + "." + critter_class_name;
        List<Critter> instances = new ArrayList<>();
        try {
            Class<?> critter = Class.forName(newCritClassName);
            for (Critter crit : world.getPopulation()){
                if (critter.isInstance(crit)){
                    instances.add(crit);
                }
            }
        }catch(ClassNotFoundException ex) {
            throw new InvalidCritterException(newCritClassName);
        }
        return instances;
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
    	world.clear();
    }

    /**
     * 1) increment timestep
     * 2) reset moved field for all critters to false
     * 3) doTimeStep for all critters in the population
     * 4) evaluate encounters and remove dead critters
     * 5) subtract rest energy from all critters
     * 6) refresh the clover population and add them to babies
     * 7) update the grid
     * 8) add all babies to population
     */
    public static void worldTimeStep() {
        //increment timestep
        timestep++;

        //reset critter's moved fields to false
        world.resetMovement();

        //do time step method for all critters in population
    	for(Critter crit : world.getPopulation()) {
    		crit.doTimeStep();
    	}
        
    	world.doEncounters();
    	world.removeDeadCritters();
    	
        for(Critter crit : world.getPopulation()) {
    		//crit.subEnergy(Params.REST_ENERGY_COST);
    		crit.setEnergy(crit.energy - Params.REST_ENERGY_COST);
    	}
        
        for(int i=0; i<Params.REFRESH_CLOVER_COUNT; i++) {
        	try {
				createBabies("Clover");
			} catch (InvalidCritterException e) {}
        }
        
        world.updateGrid();
        
        //then add babies to main population
        world.getPopulation().addAll(world.getBabies());
        //then clear babies collection
        world.getBabies().clear();
    }
    
    /**
     * helper function for displayWorld()
     */
    private static void printTopBottom() {
    	System.out.print("+");
    	for(int i=0; i<Params.WORLD_WIDTH; i++)
    		System.out.print("-");
    	System.out.print("+");
    	System.out.println();
    }
    
    
    /**
     * prints boarder and grid to consol
     */
    public static void displayWorld() {
        world.updateGrid();
    	printTopBottom();
    	for(int i=0; i<Params.WORLD_HEIGHT; i++) {
    		System.out.print("|");
    		world.printGridLine(i);
    		System.out.print("|");
    		System.out.println();
    	}
    	printTopBottom();
    }

    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    /* a one-character long string that visually depicts your critter
     * in the ASCII interface */
    public String toString() {
        return "";
    }
    
    
//    /**
//     * subtract energy from critter
//     * @param energy_cost
//     */
//    protected void subEnergy(int energy_cost) {
//    	energy -= energy_cost;
//    }
    
    
    /**
     * returns energy of critter
     * @return
     */
    protected int getEnergy() {
        return energy;
    }
    
    /**
     * sets the critter's x coordinate
     * 
     * @param x
     */
	protected void setX_coord(int x) {
		x_coord = x;
	}
	
	
	/**
	 * sets the critter's y coordinate
	 * 
	 * @param y
	 */
	protected void setY_coord(int y) {
		y_coord = y;
	}

    //TODO: Javadocs???
    public int getPrevx_coord() {
        return prevx_coord;
    }

    public void setPrevx_coord(int prevx_coord) {
        this.prevx_coord = prevx_coord;
    }

    public int getPrevy_coord() {
        return prevy_coord;
    }

    public void setPrevy_coord(int prevy_coord) {
        this.prevy_coord = prevy_coord;
    }


    /**
     * sets new energy level for critter
     * @param new_energy
     */
    protected void setEnergy(int new_energy) {
        energy = new_energy;
    }

    
    /**
     * returns the critter's x coordinate 
     * @return
     */
    protected int getX_coord() {
        return x_coord;
    }

    
    /**
     * returns the critter's y coordinate
     * @return
     */
    protected int getY_coord() {
        return y_coord;
    }

    
    /**
     * sets move field of critter
     * @param moved
     */
    public void setMoved(boolean moved) {
        this.moved = moved;
    }
    
    
    /**
     * checks if critter is alive
     * @return
     */
    protected boolean isAlive() {
    	if (getEnergy() <= 0){
    	    alive = false;
        }
        return alive;
    }

    
    /**
     * sets alive field of critter
     * @param life
     */
    protected void setAlive(boolean life){
        alive = life;
    }
    
    
    /**
     * sets encounter field of critter
     * @param e
     */
    protected void setEncounters(boolean e) {
    	encounters = e;
    }

    /**
     * checks to see if adjacent cells for walk or run are empty
     * 
     * @param crit		critter object
     * @param dir		direction (0-7) critter wants to move in
     * @param stepSize	how many steps critter wants to move (1 for walk; 2 for run)
     * @return			boolean
     */
    protected boolean adjacentEmpty(Critter crit, int dir, int stepSize) {
    	return world.isAdjacentEmpty(crit, dir, stepSize);
    }
    
    /**
     * critter uses energy to move one step in a direction on the grid
     * 
     * @param direction	direction (0-7) critter wants to move in
     */
    protected final void walk(int direction) {
    	move(direction, 1);
    	if(moved) {
    		setEnergy(getEnergy() - Params.WALK_ENERGY_COST);
    	}  	
    }

    /**
     * critter uses energy to move two steps in a direction on the grid
     * 
     * @param direction	direction (0-7) critter wants to move in
     */
    protected final void run(int direction) {
    	move(direction, 2);
        if(moved)
            setEnergy(getEnergy() - Params.RUN_ENERGY_COST);

    }

    /**
     * if critter has not moved, it will move in the desired direction.
     * if critter has moved already (from doTimeStep() or fight()) critter will not be able to move
     * but will be penalized for trying to move.
     * if critter is in an encounter (encounters) and the desired cell the critter wants to move to
     * is not empty, the critter will not move and will be penalized for trying to move. 
     * 
     * @param direction direction (0-7) critter wants to move in
     * @param stepSize	how many steps critter wants to move (1 for walk; 2 for run)
     */
    protected final void move(int direction, int stepSize){
        
    	if(encounters && !moved) {
    		if(!world.isAdjacentEmpty(this, direction, stepSize)) {
    			if(stepSize == 1) {
    				setEnergy(getEnergy() - Params.WALK_ENERGY_COST);
    			}
    			else {
    				setEnergy(getEnergy() - Params.RUN_ENERGY_COST);
    			}
    			return;
    		}
    	}
    	
    	
    	if (!moved){
    	    prevx_coord = x_coord;
    	    prevy_coord = y_coord;
            switch(direction) {
                //(row, column)
                //( y ,   x   )
                case 0: //(0,1)
                    x_coord=(x_coord+stepSize) % Params.WORLD_WIDTH;
                    //y_coord = (y_coord+0) % Params.WORLD_HEIGHT;
                    moved = true;
                    break;
                case 1: //(-1,1)
                    x_coord = (x_coord+stepSize) % Params.WORLD_WIDTH;
                    y_coord = (y_coord-stepSize) % Params.WORLD_HEIGHT;
                    if(y_coord < 0) { y_coord += Params.WORLD_HEIGHT; }
                    moved = true;
                    break;
                case 2: //(-1,0)
                    //x_coord = (x_coord-0) % Params.WORLD_WIDTH;
                    y_coord=(y_coord-stepSize) % Params.WORLD_HEIGHT;
                    if(y_coord < 0) { y_coord += Params.WORLD_HEIGHT; }
                    moved = true;
                    break;
                case 3: //(-1,-1)
                    x_coord = (x_coord-stepSize) % Params.WORLD_WIDTH;
                    y_coord = (y_coord-stepSize) % Params.WORLD_HEIGHT;
                    if(x_coord < 0) { x_coord += Params.WORLD_WIDTH; }
                    if(y_coord < 0) { y_coord += Params.WORLD_HEIGHT;  }
                    moved = true;
                    break;
                case 4: //(0,-1)
                    x_coord=(x_coord-stepSize) % Params.WORLD_WIDTH;
                    //y_coord = (y_coord+0) % Params.WORLD_HEIGHT;
                    if(x_coord < 0) { x_coord += Params.WORLD_WIDTH;  }
                    moved = true;
                    break;
                case 5: //(1,-1)
                    x_coord = (x_coord-stepSize) % Params.WORLD_WIDTH;
                    y_coord = (y_coord+stepSize) % Params.WORLD_HEIGHT;
                    if(x_coord < 0) { x_coord += Params.WORLD_WIDTH;  }
                    moved = true;
                    break;
                case 6: //(1,0)
                    //x_coord = (x_coord+0) % Params.WORLD_WIDTH;
                    y_coord=(y_coord+stepSize) % Params.WORLD_HEIGHT;
                    moved = true;
                    break;
                case 7: //(1,1)
                    x_coord = (x_coord+stepSize) % Params.WORLD_WIDTH;
                    y_coord = (y_coord+stepSize) % Params.WORLD_HEIGHT;
                    moved = true;
                    break;
                default:
                    break;
            }
        }
    }
    
    
    /**
     * if critter has sufficient energy, it will initialize its offspring and 
     * add it to babies
     * 
     * @param offspring	new critter created by the parent critter
     * @param direction	direction adjacent to parent critter that the offspring will be placed in
     */
    protected final void reproduce(Critter offspring, int direction) {
        if ((energy < Params.MIN_REPRODUCE_ENERGY) || !alive){
            return;
        }
        offspring.energy = energy/2;
        energy = (int)Math.ceil(energy/2);
        offspring.x_coord = x_coord;
        offspring.y_coord = y_coord;
        offspring.walk(direction);
        world.getBabies().add(offspring);
    }

    /**
     * The TestCritter class allows some critters to "cheat". If you
     * want to create tests of your Critter model, you can create
     * subclasses of this class and then use the setter functions
     * contained here.
     * <p>
     * NOTE: you must make sure that the setter functions work with
     * your implementation of Critter. That means, if you're recording
     * the positions of your critters using some sort of external grid
     * or some other data structure in addition to the x_coord and
     * y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {

        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        /**
         * This method getPopulation has to be modified by you if you
         * are not using the population ArrayList that has been
         * provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         */
        protected static ArrayList<Critter> getPopulation() {
            return new ArrayList<Critter>(world.getPopulation());
        }

        /**
         * This method getBabies has to be modified by you if you are
         * not using the babies ArrayList that has been provided in
         * the starter code.  In any case, it has to be implemented
         * for grading tests to work.  Babies should be added to the
         * general population at either the beginning OR the end of
         * every timestep.
         */
        protected static List<Critter> getBabies() {
            return new ArrayList<Critter>(world.getBabies());
        }
    }
}
