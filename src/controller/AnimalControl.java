package controller;

import java.io.IOException;
import animals.*;
import util.TextUtils;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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
import java.util.HashMap;

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
    
 //  @FXML ComboBox seeFriendMenu;
    
    @FXML private ImageView animalPortrait;	// Animal portrait in .fxml
    
    private MainWindowControl mainController;
    
    SimpleStringProperty ageNumberUpdater, sizeNumberUpdater;
    SimpleDoubleProperty ageIndicatorUpdater,healthIndicatorUpdater,energyIndicatorUpdater,excrementIndicatorUpdater;

    HashMap<String,ContextMenu> socialActionMenus = new HashMap<>(); 	// TODO: move to fxml
    
    /**
     * Set up view elements
     */
    @FXML private void initialize() {
    	this.setUpUpdaters();
    	
    	this.animal.setEnergy(this.animal.getMaxEnergy()/2);	// Energy at 50%
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
    	
    	this.setUpSocialActionMenus();
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
     * @param actionName action name
     */
    public void executeAction(String actionName, Node originator)
    {
    	switch (actionName)
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
				this.animal.setExcrementPercentage(0);
				break;
			
			case "seeFriend":
				this.showSeeFriendMenu(originator);
				break;

			case "reproduce":
				this.showReproduceMenu(originator);
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
    	// css fetch all action ImageViews, including nested
    	Set<Node> actionImageViewNodes = this.lookupAll(".animal-action-bar ImageView");
    	;
    	
    	
    	for (Node actionImageViewNode:actionImageViewNodes )
    	{
    		ImageView actionImageView = (ImageView) actionImageViewNode;	// Cast node to ImageView 
    		String actionName = actionImageView.getStyleClass().get(1);		// action name (class) defined in .fxml 
    		
    		// set image path 
    		Image actionIcon = new Image("file:@../../resources/images/actions/" + animal.getType() + "/" + actionImageView.getStyleClass().get(1) + ".png");
    		actionImageView.setPickOnBounds(true);	// entire image bounding box should be clickable, not just visible parts
    		actionImageView.setImage(actionIcon);		
    		
    		actionImageView.setOnMouseEntered( e-> { actionImageView.setOpacity(0.3); }); // reduces opacity on hover in
    		
    		actionImageView.setOnMouseExited( e-> { actionImageView.setOpacity(1); }); // restores opacity on hover out
    		
    		
    		actionImageView.setOnMousePressed( e-> {							// on action icon click
    		    	String action = actionName;
    		    	this.executeAction(action, actionImageView);
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
		
		int age = (int) (Math.floor(this.animal.getAge()));
		this.ageNumberUpdater.setValue(String.valueOf(age));
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
	
	/**
	 * Sets a reference to the main controller (for console, and to communicate with other controllers)
	 * @param c main window controller
	 */
	void setMainController(MainWindowControl c)
	{
		this.mainController = c;
	}
	
	void setUpSocialActionMenus()
	{
		Set<Node> socialActionImageViewNodes = this.lookupAll(".animal-action-bar .socialAction");

		for( Node actionImageView : socialActionImageViewNodes )
		{
			ContextMenu actionMenu = new ContextMenu();
			
			// add to hashmap with action name as key.
			this.socialActionMenus.put(actionImageView.getStyleClass().get(1), actionMenu );
		}
	}
	
	
	protected void showSeeFriendMenu(Node originator)
	{
		List<AnimalControl<Animal>> animalRegions = this.mainController.getAnimalsRegion().getAnimalRegions();
		
		ContextMenu currentContextMenu = this.socialActionMenus.get("seeFriend");	// TODO : do without explicit action name
		currentContextMenu.getItems().clear();	// Clear existing menu
		
		for (AnimalControl<Animal> animalRegion : animalRegions)	// Loop trough animals to find potential friends
		{
			if (animalRegion.getAnimal().getType() != this.animal.getNaturalEnemyType()) {	// No menu item for natural enemies
				MenuItem animalMenuItem = this.getMenuItemForAnimal(animalRegion); 	// redeclare at each loop for event's local method
				
				animalMenuItem.setOnAction( e-> {
					this.animal.changeEnergyBy(5);					//	Both get an energy boost.
					animalRegion.getAnimal().changeEnergyBy(5);		//
			    });
				
				currentContextMenu.getItems().add(animalMenuItem);
			}
				
		}
		
		currentContextMenu.show(originator, Side.BOTTOM, 0, 0);
	}
	
	protected void showReproduceMenu(Node originator)
	{
		ContextMenu currentContextMenu = this.socialActionMenus.get("reproduce");	// TODO : do without explicit action name
		currentContextMenu.getItems().clear();	// Clear existing menu
		
		
		// fetch all animals of same type as
		Set<Node> potentialMates = this.mainController.getAnimalsRegion().lookupAll("." + this.getAnimal().getType());
		
		for ( Node n : potentialMates )	// Loop through potential mates
		{
			@SuppressWarnings("unchecked")
			AnimalControl<Animal> potentialMate = (AnimalControl<Animal>) n;	// cast to access animal subclass

			if (!this.equals(potentialMate)) {	// can't reproduce with self!
				MenuItem animalMenuItem = this.getMenuItemForAnimal(potentialMate); 	// redeclare at each loop for event's local method
				

				animalMenuItem.setOnAction( e-> {
					if (this.mainController.getAnimalsRegion().getChildren().size() == this.mainController.getAnimalsRegion().getMaxPopulation()) {

						// Can't reproduce if population at max. TODO: grey out button with signal
						this.mainController.getConsole().printLine("No free population slot.");
					} else
					{
						// all in one call, to not have to cast the baby to current animal subclass
						this.mainController.getAnimalsRegion().addAnimalControl(this.animal.reproduceWith(potentialMate.getAnimal()));
					}
			    });
				
				currentContextMenu.getItems().add(animalMenuItem);	// Add to potential mates menu
			}
			
		}
		
		
		currentContextMenu.show(originator, Side.BOTTOM, 0, 0);
	}
	
	protected MenuItem getMenuItemForAnimal(AnimalControl<Animal> ac)
	{
		MenuItem animalMenuItem = new MenuItem(ac.getAnimal().getName());
		
		if (ac.getAnimal().getType() == "monkey")
		{
			animalMenuItem.setStyle("-fx-font-weight: bold; -fx-text-fill: #0067a8");	// TODO: get rid of stupid hack 
		} else
		{
			animalMenuItem.setStyle("-fx-font-weight: bold; -fx-text-fill: #"	// TODO: general style properties to css
									+ ac.animalNameInfo.getTextFill().toString().substring(2,8));	// use same text color as name
		}

		return animalMenuItem;
	}
	
}