package controller;

import java.io.IOException;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MiniConsole extends AnchorPane {
	@FXML VBox consolePane;
	
	public void initialize() {
		
	}
	
	public int getLineHeight() {

		return this.consolePane.getChildren().size();
	}

	public void setLineHeight (int h) {
		for (int i=0;i<h;i++)
		{
			TextFlow t = new TextFlow();
			t.setMinWidth(Region.USE_PREF_SIZE); // Disable wrapping
			
			this.consolePane.getChildren().add(t);
		}
	}
	
    public MiniConsole() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("miniConsole.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    
    public void printLn(String line)
    {
    	ObservableList<Node> workingCollection = FXCollections.observableArrayList(this.consolePane.getChildren());
    	TextFlow t = (TextFlow) workingCollection.get(workingCollection.size()-1);
    	Text lineText = new Text(line);
    	
    	t.getChildren().clear();
    	t.getChildren().add(lineText);
    	
    	Collections.rotate(workingCollection, 1);
    	this.consolePane.getChildren().setAll(workingCollection);
    }
	
}
