package controller;

import java.io.IOException;
import animals.*;
import util.TextUtils;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
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
	
    @FXML private Label animalNameInfo,animalTypeInfo,animalAge,animalSize;	// Animal info labels in .fxml
    @FXML private ProgressBar healthBar, energyBar;	// stat bars in .fxml
    @FXML private ProgressIndicator ageIndicator, excrementIndicator;
    
    @FXML private ImageView animalPortrait;	// Animal portrait in .fxml
    
    private MainWindowControl mainController;
    
    SimpleStringProperty ageNumberUpdater, sizeNumberUpdater;
    SimpleDoubleProperty ageIndicatorUpdater,healthIndicatorUpdater,energyIndicatorUpdater,excrementIndicatorUpdater;
    
    /**
     * Set up view elements
     */
    @FXML private void initialize() {
    	
        this.animal.setName(this.getRandomName());	// Set random name
       
    	this.setUpUpdaters();
    	
    	this.animal.setEnergy(this.animal.getMaxEnergy()/2);
        this.animal.setHealth((2*Math.random()+3)/5 *this.animal.getMaxHealth()); // random health between 60% and 100%
    	this.refreshStats(0);	// reset stats by refreshing with no duration change

        // Set name and label in view
    	animalNameInfo.setText(animal.getName());
    	animalTypeInfo.setText("the " + TextUtils.capitalize(animal.getType()));
    	
    	this.getStyleClass().add(animal.getType());	// make animal type the style class for the root node (for css)
    	
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
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("animalControl.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        
    }
    
    /**
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
    	
        colorAdjust.setHue(value); 
        
        this.animalPortrait.setEffect(colorAdjust);
    }
    
    /**
     * Sets up action logic
     * @param action action name
     */
    public void executeAction(String action)
    {
    	switch (action)
    	{
	    	case "color":
	    		this.changePortraitColor(2*Math.random()-1);
	    		break;
	    		
	    	case "eat":
	    		this.animal.changeHealthBy(5);
	    		break;
	    	
			case "smoke":
				this.animal.changeHealthBy(-5);
				this.animal.changeEnergyBy(5);
				break;
				
			case "mask":
				this.animal.toggleMask();
				this.animal.changeHealthBy(5);
				this.animal.changeEnergyBy(-5);
				break;
				
			case "exercise":
				this.animal.changeEnergyBy(-5);
				break;
				
			case "bathroom":
				this.animal.changeExcrementPercentageBy(-5);
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
    	// css fetch all action ImageViews
    	Set<Node> actionImageViewNodes = this.lookupAll(".animal-action-bar ImageView");

    	for (Node actionImageViewNode:actionImageViewNodes )
    	{
    		ImageView actionImageView = (ImageView) actionImageViewNode;	// Cast node to ImageView 
    		
    		// image path depends on animal type, and action style class defined in .fxml
    		Image actionIcon = new Image("file:@../../resources/images/actions/" + animal.getType() + "/" + actionImageView.getStyleClass().get(1) + ".png");
    		actionImageView.setPickOnBounds(true);	// entire image bounding box should be clickable, not just visible parts
    		actionImageView.setImage(actionIcon);		
    		
    		actionImageView.setOnMouseEntered( e-> { actionImageView.setOpacity(0.3); }); // lambda event restores opacity on hover in
    		
    		actionImageView.setOnMouseExited( e-> { actionImageView.setOpacity(1); }); // lambda event restores opacity on hover out
    		
    		
    		actionImageView.setOnMousePressed( e-> {							// lambda event handles click on action icons
    		    	String action = actionImageView.getStyleClass().get(1);	// action name (class) defined in .fxml 
    		    	this.executeAction(action);
    		}
    		);
    		
    	}
    }
    
    
    public T getAnimal()
    {
    	return this.animal;
    }
	
	void refreshStats(double duration)
	{
		this.animal.changeAgeBy(duration);
		
		String ageString = (String.valueOf((int) (Math.floor(this.animal.getAge()))));
		this.ageNumberUpdater.setValue(ageString);
		this.ageIndicatorUpdater.setValue( 1 - this.animal.getAge()/this.animal.getLifeExpectancy());
		this.excrementIndicatorUpdater.setValue( this.animal.getExcrementPercentage()/100);

    	this.healthBar.setMaxWidth( this.animal.getMaxHealth()  * 4);
    	this.healthIndicatorUpdater.setValue(this.animal.getHealth()/this.animal.getMaxHealth());
    	
    	this.energyBar.setMaxWidth(this.animal.getMaxEnergy()  * 4);
    	this.energyIndicatorUpdater.setValue(this.animal.getEnergy()/this.animal.getMaxEnergy());
    	
    	this.sizeNumberUpdater.setValue( String.valueOf((int)(this.animal.getSize()))  + " cm" );
	}

	
	public void setUpUpdaters() {
    	this.healthIndicatorUpdater = new SimpleDoubleProperty(1); 	// full at initialization
    	this.healthBar.progressProperty().bind(healthIndicatorUpdater);
    	
    	this.energyIndicatorUpdater = new SimpleDoubleProperty(1);	// full at initialization
    	this.energyBar.progressProperty().bind(energyIndicatorUpdater);
    	
    	this.ageNumberUpdater = new SimpleStringProperty();
    	this.animalAge.textProperty().bind(this.ageNumberUpdater);

    	this.ageIndicatorUpdater = new SimpleDoubleProperty(1); // starts full
    	this.ageIndicator.progressProperty().bind(ageIndicatorUpdater);
    	
    	this.excrementIndicatorUpdater = new SimpleDoubleProperty(0);
    	this.excrementIndicator.progressProperty().bind( excrementIndicatorUpdater );

    	this.sizeNumberUpdater = new SimpleStringProperty();
    	this.animalSize.textProperty().bind( sizeNumberUpdater );
	}
	
	public void setMainController(MainWindowControl mc) {
		this.mainController = mc;
	}
	
	public MainWindowControl getMainController() {
		return this.mainController;
	}
	
}