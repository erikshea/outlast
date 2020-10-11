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

	@FXML private Label animalNameInfo,animalTypeInfo,animalAge,animalSize;	// Animal info labels in .fxml
    @FXML private ProgressBar healthBar, energyBar;	// stat bars in .fxml
    @FXML private ProgressIndicator ageIndicator, excrementIndicator;
    
    @FXML private ImageView animalPortrait;	// Animal portrait in .fxml
    @FXML private ContextMenu reproduceMenu, seeFriendMenu;
    @FXML private Slider colorSlider;
    
    private MainWindowControl mainController;
    
    SimpleStringProperty ageNumberUpdater, sizeNumberUpdater;
    SimpleDoubleProperty ageIndicatorUpdater,healthIndicatorUpdater,energyIndicatorUpdater,excrementIndicatorUpdater;
    
    /**
     * Set up view elements
     */
    @FXML private void initialize() {
    	this.setUpUpdaters();

    	
    	this.getStyleClass().add(animal.getType());	// for css, make animal type the style class for the root nod

        // Name and type here because it won't change
    	animalNameInfo.setText(animal.getName());
    	animalTypeInfo.setText("the " + TextUtils.capitalize(animal.getType()));
    	
        Image portrait = new Image("file:@../../resources/images/species/" + animal.getType() + ".png");
    	animalPortrait.setImage(portrait);
    	animalPortrait.setPreserveRatio(true);	// preserve ratio when resizing
    
    	this.changePortraitColor(this.animal.getColor());
    	
    	this.setUpActionButtons();
    	
    	this.refreshGui(0);	// Initialize GUI element values by refreshing with no duration change
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
    	// css fetch all action buttons, including nested
    	Set<Node> actionButtons = this.lookupAll(".animal-action-bar Button");
    	
    	
    	for (Node n:actionButtons )
    	{
    		Button actionButton = (Button) n;	// Cast to Button 
    		String actionName = actionButton.getStyleClass().get(1);	// action name is button style class defined in .fxml 
    		
    		actionButton.getTooltip().setShowDelay(Duration.millis(300));
    		actionButton.setText(null);	// no label
    		// need file: when setting background image with setStyle
    		String newButtonStyle 	= "-fx-background-image: url(\"file:../../resources/images/actions/" + animal.getType() + "/" + actionName + ".png\")"
    								+ actionButton.getStyle();
    		actionButton.setStyle(newButtonStyle);
    		actionButton.setOnMouseEntered( e-> { actionButton.setOpacity(0.3); }); // reduces opacity on hover in
    		
    		actionButton.setOnMouseExited( e-> { actionButton.setOpacity(1); }); // restores opacity on hover out
    		
    		actionButton.setOnMousePressed( e-> { this.executeAction(actionName, actionButton); }
    		);

    	}
    }
    
    
    public T getAnimal()
    {
    	return this.animal;
    }
	
	void refreshGui(double duration)
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
    	
    	this.animalPortrait.setFitHeight(100*this.animal.getGrowthRatio());
	}

	
	public void setUpUpdaters() {
    	this.healthIndicatorUpdater = new SimpleDoubleProperty(); 	// full at initialization
    	this.healthBar.progressProperty().bind(healthIndicatorUpdater);
    	
    	this.energyIndicatorUpdater = new SimpleDoubleProperty();	// full at initialization
    	this.energyBar.progressProperty().bind(energyIndicatorUpdater);
    	
    	this.ageNumberUpdater = new SimpleStringProperty();
    	this.animalAge.textProperty().bind(this.ageNumberUpdater);

    	this.ageIndicatorUpdater = new SimpleDoubleProperty(); // starts full
    	this.ageIndicator.progressProperty().bind(ageIndicatorUpdater);
    	
    	this.excrementIndicatorUpdater = new SimpleDoubleProperty();
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
	
	
	protected void showSeeFriendMenu(Node originator)
	{
		List<AnimalControl<T>> animalRegions = this.mainController.getAnimalsRegion().getAnimalRegions();
		
		this.seeFriendMenu.getItems().clear();	// Clear existing menu
		
		for (AnimalControl<T> animalRegion : animalRegions)	// Loop trough animals to find potential friends
		{
			if (animalRegion.getAnimal().getType() != this.animal.getNaturalEnemyType()) {	// No menu item for natural enemies
				MenuItem animalMenuItem = this.getMenuItemForAnimal(animalRegion); 	// redeclare at each loop for event's local method
				
				animalMenuItem.setOnAction( e-> {
					this.animal.changeEnergyBy(5);					//	Both get an energy boost.
					animalRegion.getAnimal().changeEnergyBy(5);		//
			    });
				
				this.seeFriendMenu.getItems().add(animalMenuItem);
			}
				
		}
		
		this.seeFriendMenu.show(originator, Side.BOTTOM, 0, 0);
	}
	
	protected void showReproduceMenu(Node originator)	// TODO: sexual maturity
	{
		this.reproduceMenu.getItems().clear();	// Clear existing menu
		
		// fetch all animals of same type as
		Set<Node> potentialMates = this.mainController.getAnimalsRegion().lookupAll("." + this.getAnimal().getType());
		
		for ( Node n : potentialMates )	// Loop through potential mates
		{
			@SuppressWarnings("unchecked")	// Only AnimalControl nodes present in children
			AnimalControl<T> potentialMate = (AnimalControl<T>) n;	// cast to access subclass

			if (!this.equals(potentialMate)) {	// can't reproduce with self!
				MenuItem animalMenuItem = this.getMenuItemForAnimal(potentialMate); 	// redeclare at each loop for event's local method
				

				animalMenuItem.setOnAction( e-> {
					if (	this.mainController.getAnimalsRegion().getChildren().size()		// 
						== 	this.mainController.getAnimalsRegion().getMaxPopulation()) {	//	if Region is already full
						this.mainController.getConsole().printLine("No free population slot."); 
					} else // Can reproduce
					{
						T baby = this.animal.reproduceWith(potentialMate.getAnimal());	// Baby same subclass as current instance
						
						this.mainController.getAnimalsRegion().addAnimalControl(baby);
					}
			    });
				
				this.reproduceMenu.getItems().add(animalMenuItem);	// Add to potential mates menu
			}
			
		}
		
		
		this.reproduceMenu.show(originator, Side.BOTTOM, 0, 0);
	}
	
	protected MenuItem getMenuItemForAnimal(AnimalControl<T> ac)
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