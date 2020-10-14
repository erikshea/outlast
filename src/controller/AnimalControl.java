package controller;

import java.io.IOException;
import animals.*;
import util.TextUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.util.List;
import java.util.Set;

/**
 * 
 * Sets up and controls a single animal region: portraits, clickables, stats...
 *
 * @param <T> Animal subclass on which to build region
 */
public class AnimalControl<T extends Animal> extends HBox {
	private T animal;	// Animal subclass: Cat, Dog, etc...

	@FXML private Label animalNameInfo,animalTypeInfo,animalAge,animalSize;	// Animal info labels 
    @FXML private ProgressBar healthBar, energyBar;	// stat bars
    @FXML private ProgressIndicator ageIndicator, excrementIndicator;	// stat indicators (circular)
    @FXML private StackPane animalProfile;		// Contains portrait and overlays (masks...)
    
    @FXML private ImageView animalPortrait, animalMask;		// picture of animal and corresponding mask
    @FXML private ContextMenu reproduceMenu, seeFriendMenu;	// context menu for social actions
    @FXML private Slider colorSlider;						// slider for color adjustment
    
    private MainWindowControl mainController;				// for access to other controllers
    
    /**
     * Set up gui elements
     */
    @FXML private void initialize() {
    	this.getStyleClass().add(animal.getType());	// for css, make animal type the style class for root

        // Name and type here because it won't change
    	this.animalNameInfo.setText(animal.getName());
    	this.animalTypeInfo.setText("the " + TextUtils.capitalize(animal.getType()));
    	
    	// load portrait
        Image portrait = new Image("file:@../../resources/images/species/" + animal.getType() + ".png");
    	this.animalPortrait.setImage(portrait);
    	this.animalPortrait.setPreserveRatio(true);	// preserve ratio when resizing
    	this.animalPortrait.setFitHeight(1); // Start tiny so that the GUI doesn't show max size on initization.
    	
    	// load corresponding mask
    	Image mask = new Image("file:@../../resources/images/species/" + animal.getType() + "_mask.png");
    	this.animalMask.setImage(mask);
    	this.animalMask.setPreserveRatio(true);	// preserve ratio when resizing
    	this.animalMask.setVisible(false);
    	
    	
    	this.setUpActionButtons();

    	// apply animal color property
    	this.applyPortraitColor();
    	
    	this.colorSlider.valueProperty().addListener( (o, oldHue, newHue) -> {
    		if (-0.1 > newHue.doubleValue() || newHue.doubleValue() > 0.12) {	// Can't change color to ginger
        		this.animal.setColor(newHue.doubleValue());	// If valid, set animal property to slider value
        		this.applyPortraitColor();	
    		}
        });

    	this.addGuiListeners();	// Bind GUI elements to animal properties
    }

    /**
     * Loads .fxml as root, sets current animal.
     * @param an animal subclass
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
    private void applyPortraitColor()
    {
    	ColorAdjust colorAdjust = new ColorAdjust();
    	
        colorAdjust.setHue(this.animal.getColor()); // Hue variation from base portrait
        											// is defined by animal's color property
        this.animalPortrait.setEffect(colorAdjust);
    }
    
    /**
     * Executes action logic depending on action name parameter
     * @param actionName action name
     * @param originator clicked node, in case a context menu needs to know where to appear
     */
    
    public void executeAction(String actionName, Node originator)
    {
    	switch (actionName)
    	{
	    	case "eat":
	    		this.animal.changeHealthBy(5);
	    		this.animal.changeExcrementPercentageBy(5);
	    		break;
	    	
			case "smoke":
				this.animal.changeHealthBy(-5,"smoke"); // pass potential cause of death
				this.animal.changeEnergyBy(5);
				break;
				
			case "mask":
				this.animal.toggleMask();
				break;
				
			case "exercise":
				this.animal.changeEnergyBy(-5);
	    		this.animal.changeHealthBy(5);
				
				break;
				
			case "bathroom":
				if (this.animal.getEnergy() > 20)	// Can only go to bathroom if sufficient energy
				{
					this.animal.changeEnergyBy(-20);
					this.animal.setExcrementPercentage(0);
				}
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
    	// css fetch all action buttons, including nested
    	Set<Node> actionButtons = this.lookupAll(".animal-action-bar Button");
    	
    	for (Node n:actionButtons )
    	{
    		Button actionButton = (Button) n;	// Cast to Button 
    		String actionName = actionButton.getStyleClass().get(1);	// action name is button's style class as defined in .fxml 
    		
    		actionButton.getTooltip().setShowDelay(Duration.millis(300));
    		actionButton.setText(null);	// no label
    		// need file: in front of path for setStyle to apply background image
    		String newButtonStyle 	= "-fx-background-image: url(\"file:../../resources/images/actions/" + animal.getType() + "/" + actionName + ".png\")"
    								+ actionButton.getStyle();
    		actionButton.setStyle(newButtonStyle);
    		actionButton.setOnMouseEntered( e-> { actionButton.setOpacity(0.3); }); // reduce opacity on hover in
    		
    		actionButton.setOnMouseExited( e-> { actionButton.setOpacity(1); }); // restore opacity on hover out
    		
    		// execute action on click
    		actionButton.setOnMousePressed( e-> { this.executeAction(actionName, actionButton); }
    		);
    	}
    }
    
    /**
     * Allow access to animal sublclass
     * @return animal subclass T
     */
    public T getAnimal()
    {
    	return this.animal;
    }
	
	
	/*
	 *  Bind GUI elements to observable animal properties.
	 */
	public void addGuiListeners() {
		// size-dependent elements 
		this.animal.getSizeProperty().addListener((arg, oldSize, newSize) -> {
			this.animalSize.setText(String.valueOf((int)(newSize.doubleValue()))  + " cm" );	// Height label
			
			double portraitScale = 100*newSize.doubleValue()/this.animal.getMaxSize();	
			this.animalPortrait.setFitHeight(portraitScale);	// Scale portrait with size
			this.animalMask.setFitHeight(portraitScale);		// as well as mask overlay
    	});
		
		// age-dependent elements
		this.animal.getAgeProperty().addListener((arg, oldAge, newAge) -> {
			int age = (int) (Math.floor(newAge.doubleValue()));
			this.animalAge.setText(String.valueOf(age));		// age label
			
			// age indicator starts full, and reaches 0 at end of life
			this.ageIndicator.progressProperty().setValue( 1 - newAge.doubleValue()/this.animal.getLifeExpectancy());
    	});
		
		// health-dependent elements
    	this.animal.getHealthProperty().addListener((arg, oldHealth, newHealth) -> {
    		// portion of health bar filled
    		this.healthBar.progressProperty().setValue(newHealth.doubleValue()/this.animal.getMaxHealth());
    	});

		// max health-dependent elements
    	this.animal.getMaxHealthProperty().addListener((arg, oldMaxHealth, newMaxHealth) -> {
    		this.healthBar.setMaxWidth( newMaxHealth.doubleValue()  * 4); // health bar width depends on max health
    	});
    	
    	// energy-dependent elements
    	this.animal.getEnergyProperty().addListener((arg, oldEnergy, newEnergy) -> {
    		// portion of energy bar filled
    		this.energyBar.progressProperty().setValue(newEnergy.doubleValue()/this.animal.getMaxEnergy());
    	});

		// max energy-dependent elements
    	this.animal.getMaxEnergyProperty().addListener((arg, oldMaxEnergy, newMaxEnergy) -> {
    		this.energyBar.setMaxWidth( newMaxEnergy.doubleValue()  * 4); // energy bar width depends on max energy
    	});

		// excrement percentage-dependent elements
    	this.animal.getExcrementPercentageProperty().addListener((arg, oldExcrementPercentage, newExcrementPercentage) -> {
    		// circular indicator of excrement ammount
    		this.excrementIndicator.progressProperty().setValue((this.animal.getExcrementPercentage()/100));
    	});

		// if mask state changes
    	this.animal.getHasMaskProperty().addListener((arg, oldMaskState, newMaskState) -> {
    		// toggle mask
    		this.animalMask.setVisible(newMaskState);
    	});
    	
    	// if animal has died
    	this.animal.getIsAliveProperty().addListener((arg, oldAliveState, newAliveState) -> {
    		if (!newAliveState)
    		{
				String deathMessage = this.animal.getName() + " the " + this.animal.getType() + " has died of ";
				
				switch(this.animal.getPotentialCauseOfDeath())
				{
					case "smoke" : 
						deathMessage += "lung cancer.";
						break;
					case "bowels" :
						deathMessage += "impacted bowels.";
						break;
					case "age" :
						deathMessage += "old age.";
						break;
					default :
						deathMessage += "unknown causes.";
						break;
				}
				
				this.mainController.getConsole().printLine(deathMessage);
    		}
    	});
    	
	}
	
	/**
	 * Sets a reference to the main controller (for console, and to communicate with other controllers)
	 * @param c main window controller
	 */
	void setMainController(MainWindowControl c)
	{
		this.mainController = c;
	}
	
	/**
	 * Displays potential friends in a context menu
	 * @param originator
	 */
	protected void showSeeFriendMenu(Node originator)
	{
		List<AnimalControl<T>> animalRegions = this.mainController.getAnimalsRegion().getAnimalRegions();
		
		this.seeFriendMenu.getItems().clear();	// Clear existing menu
		
		for (AnimalControl<T> potentialFriend : animalRegions)	// Loop trough animals to find potential friends
		{
			if (potentialFriend.getAnimal().getType() != this.animal.getNaturalEnemyType()) {	// No menu item for natural enemies
				MenuItem animalMenuItem = this.getMenuItemForAnimal(potentialFriend); 	// redeclare at each loop for event's local method
				
				animalMenuItem.setOnAction( e-> {
					if (this.animal.isAlive() && potentialFriend.getAnimal().isAlive()) // Check that neither animal died since menu showed //TODO: hide with signal
					{
						this.animal.changeEnergyBy(5);					//	Both get an energy boost.
						potentialFriend.getAnimal().changeEnergyBy(5);	//
					}
			    });
				
				this.seeFriendMenu.getItems().add(animalMenuItem);	// add to friends menu
			}
				
		}
		
		this.seeFriendMenu.show(originator, Side.BOTTOM, 0, 0);	// show menu below button
	}
	
	protected void showReproduceMenu(Node originator)
	{
		this.reproduceMenu.getItems().clear();	// Clear existing menu
		
		// fetch all animals of same type
		Set<Node> potentialMates = this.mainController.getAnimalsRegion().lookupAll("." + this.getAnimal().getType());
		
		for ( Node n : potentialMates )	// Loop through potential mates
		{
			@SuppressWarnings("unchecked")	// Safe: only AnimalControl nodes in children
			AnimalControl<T> potentialMate = (AnimalControl<T>) n;

			if (!this.equals(potentialMate)) {	// can't reproduce with self
				MenuItem animalMenuItem = this.getMenuItemForAnimal(potentialMate); 	// redeclare at each loop for local event method

				animalMenuItem.setOnAction( e-> {
					if (	this.mainController.getAnimalsRegion().getChildren().size()		// 
						== 	this.mainController.getAnimalsRegion().getMaxPopulation()) {	//	if Region is already full
						this.mainController.getConsole().printLine("No free population slot."); 
					} else if (this.animal.isAlive() && potentialMate.getAnimal().isAlive()) // Check that neither animal died since menu showed //TODO: hide with signal
					{
						T baby = this.animal.reproduceWith(potentialMate.getAnimal());	// Baby same subclass as current instance
						
						this.mainController.getAnimalsRegion().addAnimalControl(baby);	// Add to region
					}
					
			    });
				
				this.reproduceMenu.getItems().add(animalMenuItem);	// Add to potential mates menu
			}
			
		}
		
		
		this.reproduceMenu.show(originator, Side.BOTTOM, 0, 0);	// show menu below button
	}
	
	/**
	 * Create menu item from an animal parameter
	 * @param ac animal controller from which to create menu item
	 * @return styled menu item
	 */
	protected MenuItem getMenuItemForAnimal(AnimalControl<T> ac)
	{
		MenuItem animalMenuItem = new MenuItem(ac.getAnimal().getName());	//  menu label is name
		
		animalMenuItem.setStyle(" -fx-text-fill: #"	// can't add style classes to menu items, need to go through setStyle
				+ ac.animalNameInfo.getTextFill().toString().substring(2,8));	// use same text color as name label next to portrait
		
		return animalMenuItem;
	}

}