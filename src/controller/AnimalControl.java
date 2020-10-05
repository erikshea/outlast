package controller;

import java.io.IOException;
import animals.*;
import util.*;

import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBox;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Set;

public class AnimalControl<T extends Animal> extends HBox {
	private T animal;
	private ImageView originalPortrait;
	
	
    @FXML
    private Label animalNameInfo,animalTypeInfo;
    
    @FXML
    private ImageView animalPortrait;
    
    @FXML
    private void initialize() {
    	animalNameInfo.setText(animal.getName());
    	animalTypeInfo.setText("the " + TextUtils.capitalize(animal.getType()));
    	this.getStyleClass().add(animal.getType());
    	

        Image portrait = new Image("file:@../../resources/images/species/" + animal.getType() + ".png");
        
        //ImageView originalPortrait = new ImageView();
    	//originalPortrait.setImage(portrait);
    	animalPortrait.setImage(portrait);

    	this.changePortraitColor(2*Math.random()-1);
    	
    	
    	Set<Node> actionImageViews = (Set<Node>) this.lookupAll(".animal-action-bar ImageView");
    	
    	for (Node actionImageView:actionImageViews )
    	{
    		ImageView v = (ImageView) actionImageView;
    		
    		Image actionIcon = new Image("file:@../../resources/images/actions/normal/" + animal.getType() + "/" + v.getStyleClass().get(1) + ".png");
    		v.setPickOnBounds(true);
    		v.setImage(actionIcon);
    		
    		v.setOnMouseEntered(new EventHandler<MouseEvent>() {
    		    @Override
    		    public void handle(MouseEvent t) {
    		    	v.setOpacity(0.3);
    		    }
    		});
    		
    		v.setOnMouseExited(new EventHandler<MouseEvent>() {
    		    @Override
    		    public void handle(MouseEvent t) {
    		    	v.setOpacity(1);
    		    }
    		});
    		
    		v.setOnMousePressed(new EventHandler<MouseEvent>() {
    		    @Override
    		    public void handle(MouseEvent t) {
    		    	
    		    	String action = v.getStyleClass().get(1).toString();
    		    	Node temp = v;
    		    	while ( !(temp instanceof AnimalControl) ) {
        		    		temp=temp.getParent();
    		    	}

    		    	AnimalControl c = (AnimalControl) temp;
    		    	c.executeAction(action);
    		    }
    		});
    		
    	}
    	
    }
    
    @FXML private TextField textField;

    public AnimalControl(T a) {
        this.animal = a;
        a.setName(this.getRandomName());
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("animal_control.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        //loader.getNamespace().put("animal_type", animal.getType());
        
        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
       
    }
    
    
    private String getRandomName()
    {
    	List<String> names = new ArrayList<String>();
    	
    	 try {
    	      File animalNamesFile = new File("@../../resources/data/animal_names.txt");
    	      Scanner nameReader = new Scanner(animalNamesFile);
    	      while (nameReader.hasNextLine()) {
    	        String name = nameReader.nextLine();
    	        names.add(name);
    	      }
    	      nameReader.close();
    	    } catch (FileNotFoundException e) {
    	      System.out.println("An error occurred.");
    	      e.printStackTrace();
    	    }
    	
    	int nameIndex = (int) (names.size() * Math.random());
    	
    	return names.get(nameIndex);
    }
    
    
    private void changePortraitColor(double value)
    {
    	
    	//this.animalPortrait.setImage(this.originalPortrait.getImage() );;
    	
    	ColorAdjust colorAdjust = new ColorAdjust();
    	
    	// set saturation to 1, otherwise hue won't have an effect
        colorAdjust.setSaturation(1);
        colorAdjust.setHue(value); 
        
        this.animalPortrait.setEffect(colorAdjust);
        
        //this.animalNameInfo.setEffect(colorAdjust);
        
    }
    
    public void executeAction(String action)
    {
    	System.out.println(action);
    	switch (action)
    	{
    	case "color":
    		this.changePortraitColor(2*Math.random()-1);
    		break;
    	}
    }
    
    
    
    
    
    
}