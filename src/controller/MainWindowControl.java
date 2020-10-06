package controller;
import java.io.IOException;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;


/**
 * Sets up main window and spawns menu, animal regions..
 * acts as a bridge between different controllers
 */
public class MainWindowControl extends BorderPane{
	@FXML private AnimalsControl animalsPane;
	@FXML private MenuBarControl mainMenuBar;
	
	public void initialize() {

		this.mainMenuBar.setMainPane(this);
	}
	
	public MainWindowControl()
	{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_window_control.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
	
	public AnimalsControl getAnimalsPane()
	{
		return this.animalsPane;
	}
	
}
