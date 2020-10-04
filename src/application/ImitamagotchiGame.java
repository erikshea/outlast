package application;
import animals.*;
import regions.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;


public class ImitamagotchiGame extends Application {
	@Override
	public void start(Stage primaryStage) {
		
		Cat c = new Cat();
		
		AnimalRegion<Cat> catRegion = new AnimalRegion<Cat>(c);
		
		
		
		try {
			
			//FXMLLoader.load(getClass().getResource("../regions/AnimalRegion.fxml"));
			//BorderPane root = new BorderPane();
			//Scene scene = new Scene(root,400,400);
			VBox  vMainBox  = new VBox(1);

			//vMainBox.getChildren().add(FXMLLoader.load(getClass().getResource("../regions/AnimalRegion.fxml")));
			vMainBox.getChildren().add(catRegion.getNode());
			
			
			
			Scene scene = new Scene(vMainBox,1600,900);
			
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Imitamagotchi");
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		

	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	/*public HBox addTamaBox() {
	    HBox hbox = new HBox();
	    hbox.setPadding(new Insets(15, 12, 15, 12));
	    hbox.setSpacing(10);
	    hbox.setStyle("-fx-background-color: #336699;");
	    
	    hbox.getChildren().add(this.makeActionButton("Eat",35));;
	    hbox.getChildren().add(this.makeActionButton("Rest",40));
	    hbox.getChildren().add(this.makeActionButton("Bathroom",70));
	    hbox.getChildren().add(this.makeActionButton("Dye Hair",65));
	    hbox.getChildren().add(this.makeActionButton("Exercise",75));
	    hbox.getChildren().add(this.makeActionButton("Remove Mask",95));
	    hbox.getChildren().add(this.makeActionButton("Wash Self",70));
	    hbox.getChildren().add(this.makeActionButton("Smoke",65));
	   
	    
	    hbox.getChildren().add(this.makeActionButton("See Friends",80));// TODO: dropdown
	    hbox.getChildren().add(this.makeActionButton("Reproduce",80));// TODO: dropdown
	    return hbox;
	}
	
	public Button makeActionButton(String name,int width) {
		Button button = new Button(name);
		button.setPrefSize(width, 40);
		
		return button;
	} */
	
}

