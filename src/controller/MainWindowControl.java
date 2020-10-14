package controller;
import java.io.IOException;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.animation.Timeline;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

/**
 * Sets up main window and spawns menu, animal regions..
 * acts as a bridge between different controllers
 */
public class MainWindowControl extends BorderPane{
	@FXML private AnimalsControl animalsRegion;		// Contains animals
	@FXML private MenuBarControl mainMenuBar;		// Top menu
	@FXML private MiniConsole console;				// Displays game messages
	private final double refreshInterval = 0.05;	// How often to refresh game state
	
	private double currentGameYearsElapsed;	// keep track of in-game years, for end game message
	
	Timeline gameTimeline;	// TimeLine instance to age animals every refreshInterval
	
	
	/*
	 * set up game elements (those not in the .fxml) 
	 * */
	public void initialize() {
		this.createTimeline(this.refreshInterval); 	// Will increase animal ages every refreshInterval
		
		this.mainMenuBar.setMainController(this);	// Pass controllers a reference to current instance, to act 
													// as an intermediary. TODO: decouple controllers with mediator
		this.animalsRegion.setMainController(this);
		this.newGame();
	}

	/*
	 * set up logic for a new game
	 */
	public void newGame() {
		this.gameTimeline.stop();					// Stop time line, in case we were in the middle of a game
		this.currentGameYearsElapsed = 0;
		this.console.clear(); 						
		this.animalsRegion.getChildren().clear();	// Clear animals
		this.animalsRegion.spawnRandomAnimals(this.animalsRegion.getMaxPopulation());	// Fill with random animals
		this.gameTimeline.play();					// Restart time line					
	}
	
	public MainWindowControl(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindowControl.fxml"));	// load .fxml 
        loader.setRoot(this);																	// as root
        loader.setController(this); // register as its controller 
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
	/*
	 * Keeps track of in-game time
	 */
	public void createTimeline(double refreshInterval) {
		this.gameTimeline = new Timeline( new KeyFrame(Duration.seconds(refreshInterval), e -> {
		    	double gameYearsInterval = refreshInterval/3; // 3 seconds real time = 1 year in-game
		    	this.currentGameYearsElapsed += gameYearsInterval;			// add to in-game years
		    	
		    	this.getAnimalsRegion().increaseAges(gameYearsInterval);	// increase animal ages

		    	if (this.getAnimalsRegion().getChildren().size() == 0)	// If no more animals
		    	{
		    		this.getConsole().printLine( "Game over. Your population has survived " + (int)this.currentGameYearsElapsed + " years." );
		    		this.gameTimeline.stop();	// Stop timeline.
		    	}
		    })
		);
		
		this.gameTimeline.setCycleCount(Animation.INDEFINITE);	// repeats until stopped
	}
	
	/*
	 * Reference to console controller, for in-game messages
	 */
	public MiniConsole getConsole()
	{
		return this.console;
	}

	/*
	 * Reference to the region that contains the animals
	 */
	public AnimalsControl getAnimalsRegion()
	{
		return this.animalsRegion;
	}
}
