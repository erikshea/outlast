package regions;
import animals.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class AnimalRegion<T extends Animal> {
	T animal = null;
	
	public AnimalRegion(T a)
	{
		this.animal = a;
	    HBox hbox = new HBox();
	    hbox.setPadding(new Insets(15, 12, 15, 12));
	    hbox.setSpacing(10);
	    hbox.setStyle("-fx-background-color: #336699;");
	    
	}
	
	/*
	
	private void makeActionButton(String actionName,int width) {

        ImageView img = new ImageView("../../resources/images/actions/" + actionName + ".png");
        img.setPickOnBounds(true); // allows clicking on transparent areas
		return button;
	}*/

}
