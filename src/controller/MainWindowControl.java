package controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import animals.Animal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
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
	Timeline mainTimeline;
	
	
	public void initialize() {

		this.mainMenuBar.setMainRegion(this);
		this.createTimeline();
	}
	
	public MainWindowControl(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_window_control.fxml"));
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
		MainWindowControl me = this;
		
		double interval = 0.2;
		
		this.mainTimeline = new Timeline(
			    new KeyFrame(Duration.seconds(interval), e -> {
			    	me.getAnimalsRegion().increaseAges(interval);
					
			    	// Need to removeAll separately for thread concurrency
					me.getAnimalsRegion().getChildren().removeAll(this.getAnimalsRegion().getDeadAnimals());
					
					
					me.getAnimalsRegion().repopulateTo(this.getAnimalsRegion().getMaxAnimalRegions());
			    })
			);
		this.mainTimeline.setCycleCount(Animation.INDEFINITE);
		this.mainTimeline.play();
	}
	
}
