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
	
	Timeline mainTimeline;
	
	
	public void initialize() {
		this.mainMenuBar.setMainController(this);	// TODO: decouple controllers with mediator pattern
		this.animalsRegion.setMainController(this);	//
		this.animalsRegion.repopulateTo(8);	// TODO: move to AnimalsControl when controller mediator done
		
		this.createTimeline();
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
	
	
	public AnimalsControl getAnimalsRegion()
	{
		return this.animalsRegion;
	}
	
	
	public void createTimeline() {
		double refreshInterval = 0.1;
		
		this.mainTimeline = new Timeline(
			    new KeyFrame(Duration.seconds(refreshInterval), e -> {	// lambda handler allows access to "this"
			    	this.getAnimalsRegion().increaseAges(refreshInterval);
					
			    	// Need to remove all separately for thread concurrency
			    	this.getAnimalsRegion().clearDeadAnimals();
					
					// replace dead animals
			    	this.getAnimalsRegion().repopulateTo(this.getAnimalsRegion().getMaxAnimalRegions());
			    })
			);
		this.mainTimeline.setCycleCount(Animation.INDEFINITE);
		this.mainTimeline.play();
	}
	
	public MiniConsole getConsole()
	{
		return this.console;
	}
	
}
