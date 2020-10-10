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
    
    public void printLine(String line)
    {
    	ObservableList<Node> consoleLines = FXCollections.observableArrayList(this.consolePane.getChildren());
    	TextFlow consoleLine = (TextFlow) consoleLines.get(0);	// Topmost line will get overwritten
    	Text lineText = new Text(line);
    	
    	consoleLine.getChildren().clear();						// Clear old line text
    	consoleLine.getChildren().add(lineText);
    	
    	Collections.rotate(consoleLines, -1);					// Each line shifts one position back, rotating the top one to the bottom
    	this.consolePane.getChildren().setAll(consoleLines);	// Clear all old lines, replace with new ones
    }
    

    
    public void clear()
    {
    	for (Node line: this.consolePane.getChildren()) {
    		((TextFlow) line).getChildren().clear();	// Cast to TextFlow to access children
    	}
    }
	
}
