package animals;
import java.lang.reflect.Method;
import java.util.List;

public class TryStuff {
	public static void main(String[] args) {
		

    	//Set<Node> animals = me.mainPane.getAnimalsPane().lookupAll("AnimalControl");
		
		System.out.println(makeProgressBar(7));
	}
	
	
	public static String makeProgressBar(int i)
	{
		return "██████████".substring(0,i) + "▒▒▒▒▒▒▒▒▒▒".substring(0,10-i);
	}

}
