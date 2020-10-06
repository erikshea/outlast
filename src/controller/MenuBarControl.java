package controller;

import java.io.IOException;
import java.util.Set;
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
	
    @FXML private MenuItem refreshAnimals,exit;
    @FXML private MainWindowControl mainPane;
	
	public void initialize()
	{
		MenuBarControl me = this;

		// "Refresh Animals" menu item
		this.refreshAnimals.setOnAction(new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	// fetch all animal panes from main window reference
            	Set<Node> animals = me.mainPane.getAnimalsPane().lookupAll("AnimalControl");

            	// delete them all
            	me.mainPane.getAnimalsPane().getChildren().removeAll(animals);
            	// add new ones
            	me.mainPane.getAnimalsPane().spawnRandomAnimals(8);
            } 
        }); 
        
        
    
        this.exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
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
	public void setMainPane(MainWindowControl p)
	{
		this.mainPane = p;
	}
}
