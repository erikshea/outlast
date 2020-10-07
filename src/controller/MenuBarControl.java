package controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Sets up and controls the application's menu bar
 */
public class MenuBarControl extends MenuBar {
	
    @FXML private MenuItem refreshAnimals,exit,test;
    @FXML private MainWindowControl mainRegion;
	
	public void initialize()
	{
		MenuBarControl me = this;

		// "Refresh Animals" menu item
		this.refreshAnimals.setOnAction(new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	// delete them all
            	me.mainRegion.getAnimalsRegion().getChildren().clear();
            	// add new ones
            	me.mainRegion.getAnimalsRegion().spawnRandomAnimals(8);
            } 
        }); 
        
        
    
        this.exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });
        
        this.test.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	Node test = me.mainRegion.getAnimalsRegion().getChildren().get(0);
            	me.mainRegion.getAnimalsRegion().getChildren().remove(test);
            }
        });
	}
	
    /**
     * Loads .fxml as root
     * @param a animal subclass
     */
	public MenuBarControl()
	{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menubar_control.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}

	/**
	 * Sets mainPane to a reference to the main window pane (to communicate with other controllers)
	 * @param p main window pane
	 */
	public void setMainRegion(MainWindowControl p)
	{
		this.mainRegion = p;
	}
}
