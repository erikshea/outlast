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

public class MenuBarControl extends MenuBar {
	
    @FXML private MenuItem refreshAnimals,exit;
    @FXML private MainWindowControl mainPane;
	
	public void initialize()
	{
		MenuBarControl me = this;

		this.refreshAnimals.setOnAction(new EventHandler<ActionEvent>() { 
            public void handle(ActionEvent e) 
            { 
            	Set<Node> animals = me.mainPane.getAnimalsPane().lookupAll("AnimalControl");
            	System.out.println(animals.size());

            	me.mainPane.getAnimalsPane().getChildren().removeAll(animals);
            	me.mainPane.spawnRandomAnimals(8);
            } 
        }); 
        
        
    
        this.exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });
	}
	
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
	
	public void setMainPane(MainWindowControl p)
	{
		this.mainPane = p;
	}
}
