package com.erikshea.outlast.animals;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.util.Pair;

public class TryStuff {
	public static void main(String[] args) {
		//System.out.println(makeProgressBar(7));
		
		
		String text="gfdjfdgkjl dfgjkhfdkfdghjk fdgkllhhjkl<span class='test'>fjdksd kljsklj</span> sqsdsqd <span class='tes3t'>fjdksd kljsklj</span> sqsdsq";
		
		parseSpans(text);
	}
	
	
	public static String makeProgressBar(int i)
	{
		return "██████████".substring(0,i) + "▒▒▒▒▒▒▒▒▒▒".substring(0,10-i);
	}
	
	
	
    public static void parseSpans(String text) {
        String spanPattern = "([^<]*)(?:<span class='([^']+)'>([^<]+)<\\/span>(.*))?";
        
    	Pattern p = Pattern.compile(spanPattern);
    	Matcher m = p.matcher(text);
    	
    	List<Pair<String,String>> elements = new ArrayList<>();
    	
    	while (m.find()) {
    		if (m.group(1).length()>0) {
    			elements.add(new Pair<>("", m.group(1)));
    		}
    		if (m.group(2) != null) {
        		if (m.group(3).length()>0)
        		{
        			elements.add(new Pair<>(m.group(2), m.group(3)));
        		}
        		m = p.matcher(m.group(4));
    		}
    	}
    }
    
    
}
