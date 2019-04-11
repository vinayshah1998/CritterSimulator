package assignment5;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.layout.GridPane;
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

    static GridPane gridPane;
    
    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name,
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) {    	
        // launch(args)
    }
    
    @Override
    public void init() {

    }
    
    @Override
    public void start(Stage stage) throws Exception {
    	gridPane = new GridPane();
    	
    	
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
						Critter.displayWorld();
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
    		}
    		System.out.print("critters> ");
    	}	
    }
}
