package animals;

public class TryStuff {
	public static void main(String[] args) {
		System.out.println(makeProgressBar(7));
	}
	
	
	public static String makeProgressBar(int i)
	{
		return "██████████".substring(0,i) + "▒▒▒▒▒▒▒▒▒▒".substring(0,10-i);
	}

}
