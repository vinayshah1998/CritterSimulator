package assignment5;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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



		critterGrid.setGridLinesVisible(true);
//		critterGrid.setHgap(8);
//		critterGrid.setVgap(8);
		critterGrid.setPadding(new Insets(5));

		Label create = new Label("Create critter");
		Label step = new Label("Number of timesteps");
		Label stats = new Label("Statistics of critters");
		Label seed = new Label("Random seed number");

		//Textbox to enter number of steps
		TextField stepBox = new TextField();
		stepBox.setPromptText("number of timesteps");
		stepBox.setOnAction(e -> {
			try{
				int stepNum = Integer.parseInt(stepBox.getText());
				if (stepNum <= 0){
					throw new NumberFormatException();
				}
				for (int i = 0; i < stepNum; i++){
					Critter.worldTimeStep();
				}
				System.out.println("Step number successfully set to: " + stepNum);
			}catch(NumberFormatException ex){
				Alert invalidStepNumber = new Alert(Alert.AlertType.ERROR);
				invalidStepNumber.setTitle("Error Dialog");
				invalidStepNumber.setHeaderText("You typed in an invalid step number!!");
				invalidStepNumber.setContentText("Enter in a valid non-negative integer");

				invalidStepNumber.showAndWait();
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

		//Clear button
		Button clear = new Button("Clear");
		clear.setOnAction(e -> {
			System.out.println("Clearing world!");
			Critter.clearWorld();
		});

		//Quit button
		Button quit = new Button("Quit");
		quit.setOnAction(e -> {
			System.out.println("Ta-ta!");
			stage.close();
		});

		optionsPane.setSpacing(8);
		optionsPane.getChildren().addAll(create, step, stepBox, stats, seed, seedBox, clear, quit);

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
		stage.setScene(new Scene(mainPane, 500, 500));

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
