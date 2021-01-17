package com.erikshea.outlast.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Sets up and controls the application's main menu
 */
public class MenuBarControl extends MenuBar {
	
    @FXML private MenuItem newGame, quit;		// menu items in .fxml
    private MainWindowControl mainController;	// for access to other controllers
	
	public void initialize()
	{
		// "New game" menu item
		this.newGame.setOnAction( e -> { this.mainController.newGame(); } ); 
		
		// "Quit" menu item
        this.quit.setOnAction( e -> { System.exit(0); } );
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
