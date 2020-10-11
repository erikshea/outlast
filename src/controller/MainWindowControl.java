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
	@FXML private AnimalsControl animalsRegion;
	@FXML private MenuBarControl mainMenuBar;
	@FXML private MiniConsole console;
	
	private double currentGameYearsElapsed;
	
	Timeline gameTimeline;
	double gameTimeElapsed;
	
	public void initialize() {
		this.createTimeline(0.1); // will refresh every 0.1s
		
		this.mainMenuBar.setMainController(this);	// TODO: decouple controllers with mediator
		this.animalsRegion.setMainController(this);	//
		
		this.newGame();
	}

	public void newGame() {
		this.gameTimeline.stop();					// Stop time line, in case it was already started
		this.currentGameYearsElapsed = 0;
		this.console.clear(); 						// Clear console
		this.animalsRegion.getChildren().clear();	// Clear animals
		this.animalsRegion.spawnRandomAnimals(8);	// Create 8 new animals
		this.gameTimeline.play();					// Restart time line					
	}
	
	public MainWindowControl(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainWindowControl.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
	public void createTimeline(double refreshInterval) {
		this.gameTimeline = new Timeline(
			    new KeyFrame(Duration.seconds(refreshInterval), e -> {
			    	double gameYearsInterval = refreshInterval/4; // 2 seconds real time = 1 year in-game
			    	this.getAnimalsRegion().increaseAges(gameYearsInterval);	
			    	this.currentGameYearsElapsed += gameYearsInterval;
			    	
			    	// Remove dead animals here for thread concurrency
			    	this.getAnimalsRegion().clearDeadAnimals();	// TODO : handle dead animals with signal in AnimalRegion
					
			    	if (this.getAnimalsRegion().getChildren().size() == 0)	// If no more animals
			    	{
			    		this.getConsole().printLine( "Game over. Your population has survived " + (int)this.currentGameYearsElapsed + " years." );
			    		this.gameTimeline.stop();
			    	}
			    })
			);
		
		this.gameTimeline.setCycleCount(Animation.INDEFINITE);	// repeats until stopped
	}
	
	public MiniConsole getConsole()
	{
		return this.console;
	}

	
	public AnimalsControl getAnimalsRegion()
	{
		return this.animalsRegion;
	}
}
