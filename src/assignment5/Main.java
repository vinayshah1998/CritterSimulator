package assignment5;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import org.reflections.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class Main extends Application {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console


    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name,
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) {    	
    	launch(args);
    }

    
    private static GridPane gridPane;
    public static int FACTOR = 20;
    public final int STAGE_WIDTH  = FACTOR * Params.WORLD_WIDTH;
    public final int STAGE_HEIGHT = FACTOR * Params.WORLD_HEIGHT;
    
    @Override
    public void start(Stage stage) throws Exception {
		GridPane critterGrid = new GridPane();
		BorderPane mainPane = new BorderPane();
		VBox optionsPane = new VBox();



//		critterGrid.setGridLinesVisible(true);
		critterGrid.setHgap(8);
		critterGrid.setVgap(8);
		critterGrid.setPadding(new Insets(5));

		Label create = new Label("Create critter");
		Label step = new Label("Number of timesteps");
		Label stats = new Label("Statistics of critters");
		Label seed = new Label("Random seed number");
		Label animateSpeed = new Label("Animation speed");

		//Drop-down menu to choose a critter to create
		Reflections reflections = new Reflections(myPackage);
		Set<Class<? extends Critter>> allClasses = reflections.getSubTypesOf(Critter.class);
		allClasses.removeIf(e -> e.toString().equals("class assignment5.Critter$TestCritter"));

		ObservableList<String> options = FXCollections.observableArrayList();

		for (Class<? extends Critter> crit : allClasses){
			options.add(crit.getName().split("\\.")[1]);
		}

		Collections.sort(options);
		
		
		ChoiceBox<String> critterStats = new ChoiceBox<String>(options);
		//set default value
		critterStats.setValue(options.get(0));
		
		//Textbox to display stats of critter
		TextField critterStatsBox = new TextField();
		critterStatsBox.setPromptText("critter stats");
		critterStats.setOnAction(e -> {
			try {
				// Create list of instances of Critter selected
				List<Critter> instances = new ArrayList<Critter>();
				instances = Critter.getInstances(critterStats.getValue());		
				
				// Output string of stats on text field
				critterStatsBox.setText(Critter.runStats(instances));
				
				System.out.println("Ran stats for critter " + critterStats.getValue());
			}catch(SecurityException | IllegalArgumentException | InvalidCritterException e2){
				System.out.println("OOps! Don't know what happened here");
				e2.printStackTrace();
			}
		});
		
		
		
		ChoiceBox<String> critterList = new ChoiceBox<String>(options);

		//set default value
		critterList.setValue(options.get(0));

		//Textbox to enter number of critters
		TextField numCrittersBox = new TextField();
		numCrittersBox.setPromptText("number of critters");
		numCrittersBox.setOnAction(e -> {
			try{
				int critterNum = Integer.parseInt(numCrittersBox.getText());
				if (critterNum <= 0){
					throw new NumberFormatException();
				}
				for (int i = 0; i < critterNum; i++){
					Critter.createCritter(critterList.getValue());
				}
				Critter.displayWorld(critterGrid);
				System.out.println(critterNum + " " + critterList.getValue() + "s successfully created");
				
				// UPDATE RUNSTATS:
				// Create list of instances of Critter selected
				List<Critter> instances = new ArrayList<Critter>();
				instances = Critter.getInstances(critterStats.getValue());		
				
				// Output string of stats on text field
				critterStatsBox.setText(Critter.runStats(instances));				
				System.out.println("Ran stats for critter " + critterStats.getValue());
			}catch(NumberFormatException ex){
				Alert invalidCritterNumber = new Alert(Alert.AlertType.ERROR);
				invalidCritterNumber.setTitle("Error Dialog");
				invalidCritterNumber.setHeaderText("You typed in an invalid step number!!");
				invalidCritterNumber.setContentText("Enter in a valid non-negative integer");

				invalidCritterNumber.showAndWait();
			} catch (Exception e1) {
				System.out.println("OOps! Don't know what happened here");
				e1.printStackTrace();
			}
		});

		//Textbox to enter seed number
		TextField seedBox = new TextField();
		seedBox.setPromptText("seed number");
		seedBox.setOnAction(e -> {
			try{
				int seedNum = Integer.parseInt(seedBox.getText());
				if (seedNum < 0){
					throw new NumberFormatException();
				}
				Critter.setSeed(seedNum);
				System.out.println("Seed successfully set to: " + seedNum);
			}catch(NumberFormatException ex){
				Alert invalidSeed = new Alert(Alert.AlertType.ERROR);
				invalidSeed.setTitle("Error Dialog");
				invalidSeed.setHeaderText("You typed in an invalid seed number!!");
				invalidSeed.setContentText("Enter in a valid non-negative integer");

				invalidSeed.showAndWait();
			}
		});

		//Animation stuff
		TextField animateSpeedBox = new TextField();
		animateSpeedBox.setPromptText("animation speed");
//		Button stopAnimation = new Button("Stop");

		TextField stepBox = new TextField();

		AnimationTimer timer = new AnimationTimer() {
			int numSteps = 0;
			@Override
			public void handle(long now) {
				animateSpeedBox.setDisable(true);
				numCrittersBox.setDisable(true);
				stepBox.setDisable(true);
				seedBox.setDisable(true);

				int speed = 1;
				try {
					speed = Integer.valueOf(animateSpeedBox.getText().trim());
				} catch (Exception e) {
				}

				if(numSteps == Integer.valueOf(stepBox.getText().trim())){
					try {
						Critter.displayWorld(critterGrid);
					} catch (Exception e) {
						e.printStackTrace();
					}
					numSteps = 0;
					animateSpeedBox.setDisable(false);
					numCrittersBox.setDisable(false);
					stepBox.setDisable(false);
					seedBox.setDisable(false);
					stop();
				} else if (numSteps % speed == 0) {
					try {
						Critter.displayWorld(critterGrid);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				Critter.worldTimeStep();
//				if (statsOn) {
//					btnRunStats.fire();
//				}
				numSteps++;
			}
		};

		//Textbox to enter number of steps
		stepBox.setPromptText("number of timesteps");
		stepBox.setOnAction(e -> {
			try{
				int stepNum = Integer.parseInt(stepBox.getText());
				if (stepNum <= 0){
					throw new NumberFormatException();
				}
//				for (int i = 0; i < stepNum; i++){
//					Critter.worldTimeStep();
//				}
				timer.start();
				Critter.displayWorld(critterGrid);
				System.out.println("Step number successfully set to: " + stepNum);
			}catch(NumberFormatException ex){
				Alert invalidStepNumber = new Alert(Alert.AlertType.ERROR);
				invalidStepNumber.setTitle("Error Dialog");
				invalidStepNumber.setHeaderText("You typed in an invalid step number!!");
				invalidStepNumber.setContentText("Enter in a valid non-negative integer");

				invalidStepNumber.showAndWait();
			} catch (Exception e1) {
				System.out.println("OOps! Don't know what happened here");
				e1.printStackTrace();
			}
		});

//		stopAnimation.setOnAction(e -> {
//				timer.stop();
//		});



		//Clear button
		Button clear = new Button("Clear");
		clear.setOnAction(e -> {
			System.out.println("Clearing world!");
			Critter.clearWorld();
			try {
				Critter.displayWorld(critterGrid);
			} catch (Exception e1) {
				System.out.println("OOps! Don't know what happened here");
				e1.printStackTrace();
			}
		});

		//Quit button
		Button quit = new Button("Quit");
		quit.setOnAction(e -> {
			System.out.println("Ta-ta!");
			stage.close();
		});

		optionsPane.setSpacing(8);
		optionsPane.getChildren().addAll(create, critterList, numCrittersBox, step, stepBox, stats, seed, seedBox, animateSpeed, animateSpeedBox, clear, quit);

		mainPane.setCenter(critterGrid);
		mainPane.setLeft(optionsPane);

//		for(int i=0; i<Params.WORLD_WIDTH; i++) {
//			for(int j=0; j<Params.WORLD_HEIGHT; j++) {
//			Shape s = new Rectangle(FACTOR,FACTOR);
//			s.setFill(null);
//			s.setStroke(Color.BLACK);
//			gridPane.add(s, i, j);
////		}
//	}
		Critter.displayWorld(critterGrid);



		stage.setTitle("Critters");
		stage.setScene(new Scene(mainPane, 8*STAGE_WIDTH, 8*STAGE_HEIGHT));

        stage.show();





//    	kb = new Scanner(System.in); // use keyboard and console
//    	
//    	Critter.initializeWorld();
//    	Critter.worldTimeStep();
//
//    	gridPane = new GridPane();
//
//		for(int i=0; i<Params.WORLD_WIDTH; i++) {
//			for(int j=0; j<Params.WORLD_HEIGHT; j++) {
//				Shape s = new Rectangle(FACTOR,FACTOR);
//				s.setFill(null);
//				s.setStroke(Color.BLACK);
//				gridPane.add(s, i, j);
//			}
//		}
//
//		Critter.displayWorld(gridPane);
//
//    	stage.setScene(new Scene(gridPane, STAGE_WIDTH+FACTOR, STAGE_HEIGHT+FACTOR));
//    	stage.show();
    	
//    	commandInterpreter(kb);
//    	System.out.flush();
    }

    
    private static void commandInterpreter (Scanner kb) {
    	//TODO: create[class_name]
		Critter.initializeWorld();
    	System.out.print("critters> ");
    	while(kb.hasNext()) {
    		String nextline = kb.nextLine();
    		String[] tokens = nextline.split(" ");
    		
    		try {
    			switch(tokens[0]) {
        		case "quit":
        			if(tokens.length == 1) {
        				return;
        			}
        			else {
        				System.out.println("error processing: " + nextline);	
        			}
        			break;
        		
        		case "show":
        			if (tokens.length == 1){
						Critter.displayWorld(gridPane);
					} else {
						System.out.println("error processing: " + nextline);
					}
        			break;
        		
        		case "step":   	
        			if (tokens.length == 1){
                		Critter.worldTimeStep();
                	}
        			else if(tokens.length == 2) {
                		int count = Integer.parseInt(tokens[1]);
                		
                		if(count<0) {
                			System.out.println("error processing: " + nextline);
                			break;
                		}
                		
                		for(int i=0; i<count; i++) {
                			Critter.worldTimeStep();
                		}
                	}
        			else {
                		System.out.println("error processing: " + nextline); 
                	}
                	
                	break;
        		
        		case "seed":
        			if(tokens.length == 2) {
    					int seed = Integer.parseInt(tokens[1]);
						if(seed<0) {
							System.out.println("error processing: " + nextline);
							break;
						}
    					Critter.setSeed(seed);
        			}
        			else {
        				System.out.println("error processing: " + nextline); 
        			}
        			break;
                	
        		case "create":   			
        			// Critter string name
        			String c = null;
        			// number of critters to create
        			int n = 1;
					c = tokens[1];
					if (tokens[1].equals("Critter")) {
    					System.out.println("error processing: " + nextline);
    					break;
    				}
					if(tokens.length < 4) {
						if (tokens.length == 3){
							n = Integer.parseInt(tokens[2]);
							if(n<0) {
								System.out.println("error processing: " + nextline);
								break;
							}
						}
						try {
							for(int i=0; i<n; i++) {
								Critter.createCritter(c);
							}
						} catch (InvalidCritterException e){
							System.out.println("error processing: " + nextline);
						}
					} else {
						System.out.println("error processing: " + nextline);
					}
					break;
				
        		case "stats":
        			if(tokens.length == 2) {
        				if (tokens[1].equals("Critter")) {
        					System.out.println("error processing: " + nextline);
        					break;
        				}
        				List<Critter> instances = new ArrayList<Critter>();
        				instances = Critter.getInstances(tokens[1]);
        				
        				String critter_name = myPackage + "." + tokens[1];
        				Class<?> critter = Class.forName(critter_name);
        				
        				
        				Method m = critter.getMethod("runStats", List.class);
        				m.invoke(critter, instances);
        			}
        			else {
        				System.out.println("error processing: " + nextline);
        			}
        			break;
        			
        		case "clear":
        			if(tokens.length == 1) {
        				Critter.clearWorld();	
        			}
        			else {
        				System.out.println("error processing: " + nextline);
        			}
        			break;
        			
        		case "poop":
        			System.out.println("   |   ");
        			System.out.println("  ( )  ");
        			System.out.println(" (. .) ");
        			System.out.println("(__^__)");
        			break;
        		
        		default:
        			System.out.println("invalid command: " + nextline);
        		}
    		}
    		catch(InvalidCritterException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException a) {
    			System.out.println("error processing: " + nextline);
    		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		System.out.print("critters> ");
    	}	
    }
}
