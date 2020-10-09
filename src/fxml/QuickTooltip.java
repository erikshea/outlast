package fxml;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import javafx.scene.Node;


public final class QuickTooltip extends Region {
	
	public QuickTooltip()
	{
		//this.tt = new Tooltip("dfdsf");
		//Tooltip.install(this.getParent(), tt);
	}
	
	
	public void setText(String t)
	{
		//tt.setText(t);
	}
	
	public void setDelay(int ms)
	{
		//tt.setStyle("-fx-show-delay: "+ms+"ms;");
	}
	
}

