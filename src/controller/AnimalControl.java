package controller;

import java.io.IOException;
import animals.*;
import util.TextUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 * Sets up and controls a single animal region: portraits, clickables, stats...
 *
 * @param <T> Animal subclass on which to build region
 */
public class AnimalControl<T extends Animal> extends HBox {
	private T animal;	// Animal subclass: Cat, Dog, etc...
	
    @FXML private Label animalNameInfo,animalTypeInfo;	// Animal info labels in .fxml
    
    @FXML private ImageView animalPortrait;	// Animal portrait in .fxml
    
    /**
     * Set up view elements
     */
    @FXML private void initialize() {
        this.animal.setName(this.getRandomName());	// Set random name
    	
        // Set name and label in view
    	animalNameInfo.setText(animal.getName());
    	animalTypeInfo.setText("the " + TextUtils.capitalize(animal.getType()));
    	
    	this.getStyleClass().add(animal.getType());	// Make root item have animal name as root c
    	

        Image portrait = new Image("file:@../../resources/images/species/" + animal.getType() + ".png");
        
    	animalPortrait.setImage(portrait);

    	// Randomize initial portrait color
    	this.changePortraitColor(2*Math.random()-1);

    	this.setUpActionButtons();
    }

    /**
     * Loads .fxml as root, sets current animal.
     * @param a animal subclass
     */
    public AnimalControl(T a) {
        this.animal = a;
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("animal_control.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
       
    }
    
    /*
     * Reads a file consisting of a list of names, returns a random one
     */
    private String getRandomName()
    {
    	List<String> names = new ArrayList<String>();
    	
    	 try {
    	      File animalNamesFile = new File("@../../resources/data/animal_names.txt");
    	      Scanner nameReader = new Scanner(animalNamesFile);
    	      while (nameReader.hasNextLine()) {
    	        String name = nameReader.nextLine();
    	        names.add(name);
    	      }
    	      nameReader.close();
    	    } catch (FileNotFoundException e) {
    	      System.out.println("Read error.");
    	      e.printStackTrace();
    	    }
    	
    	int nameIndex = (int) (names.size() * Math.random());
    	
    	return names.get(nameIndex);
    }
    
    /**
     * Changes animal portrait color to a different hue
     * @param value hue between -1 and 1
     */
    private void changePortraitColor(double value)
    {
    	ColorAdjust colorAdjust = new ColorAdjust();
    	
        colorAdjust.setSaturation(1);
        colorAdjust.setHue(value); 
        
        this.animalPortrait.setEffect(colorAdjust);
    }
    
    /**
     * Sets up action logic
     * @param action action name
     */
    public void executeAction(String action)
    {
    	System.out.println(action);
    	
    	switch (action)
    	{
    	case "color":
    		this.changePortraitColor(2*Math.random()-1);
    		break;
    	}
    }
    
    /**
     * Loops through all actions and sets up
	 * -icons
 	 * -hover out effect
 	 * -method to call on click, passing action name
     */
    private void setUpActionButtons()
    {

    	AnimalControl<T> me = this;
    	
    	// css fetch all action ImageViews
    	Set<Node> actionImageViews = this.lookupAll(".animal-action-bar ImageView");

    	for (Node actionImageView:actionImageViews )
    	{
    		ImageView v = (ImageView) actionImageView;	// Cast node to ImageView 
    		
    		// Set icon
    		Image actionIcon = new Image("file:@../../resources/images/actions/normal/" + animal.getType() + "/" + v.getStyleClass().get(1) + ".png");
    		v.setPickOnBounds(true);	// entire image bounding box should be clickable, not just visible parts
    		v.setImage(actionIcon);
    		
    		v.setOnMouseEntered(new EventHandler<MouseEvent>() {
    		    @Override
    		    public void handle(MouseEvent t) {
    		    	v.setOpacity(0.3);								// Hover in opacity effect
    		    }
    		});
    		
    		v.setOnMouseExited(new EventHandler<MouseEvent>() {	
    		    @Override
    		    public void handle(MouseEvent t) {
    		    	v.setOpacity(1);								// Restore opacity on hover out
    		    }
    		});
    		
    		v.setOnMousePressed(new EventHandler<MouseEvent>() {
    		    @Override
    		    public void handle(MouseEvent t) {
    		    	
    		    	String action = v.getStyleClass().get(1).toString();	// action name (class) defined in .fxml 
    		    	me.executeAction(action);
    		    }
    		});
    		
    	}
    }
    
    
    
    
}