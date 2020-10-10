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
	
    @FXML private MenuItem refreshAnimals,exit;
    private MainWindowControl mainController;
	
	public void initialize()
	{
		MenuBarControl me = this;

		// "Refresh Animals" menu item
		this.refreshAnimals.setOnAction( e -> { 
            	// delete them all
            	me.mainController.newGame();
        }); 
        
        this.exit.setOnAction( e -> { System.exit(0); }  );
	}
	
    /**
     * Loads .fxml as root
     * @param a animal subclass
     */
	public MenuBarControl()
	{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menubarControl.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}

	/**
	 * Sets a reference to the main controller (for console, and to communicate with other controllers)
	 * @param c main window controller
	 */
	void setMainController(MainWindowControl c)
	{
		this.mainController = c;
	}
}
