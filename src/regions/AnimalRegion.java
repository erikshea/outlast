package regions;

import animals.*;
import javafx.fxml.FXMLLoader;
/*import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;*/
import javafx.scene.Node;

public class AnimalRegion<T extends Animal> {
	T animal = null;
	
	public AnimalRegion(T a)
	{
		animal=a;
	}
	
	public Node getNode()
	{
		try {
			return FXMLLoader.load(getClass().getResource("AnimalRegion.fxml"));
		} catch (Exception e)
		{
			
		}
		
		return null;
	}
	

}
