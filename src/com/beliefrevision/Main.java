package com.beliefrevision;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	static Util util = new Util();

    public static void main(String[] args) {
    	menu();
    }

    public static void menu() {

        BeliefEngine be = new BeliefEngine();

    	@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

		while(true) {
			Pattern pattern = Pattern.compile("([!]?[a-zA-Z](\\s?[|]\\s?([!]?[a-zA-Z]))+)|(([!]?[a-zA-Z]\\s?)|([(]([!]?[a-zA-Z](\\s?[|]\\s?[!]?[a-zA-Z])+)[)]\\s?))([&]\\s?(([!]?[a-zA-Z]\\s?)|([(]([!]?[a-zA-Z](\\s?[|]\\s?[!]?[a-zA-Z])+)[)]\\s?)))*");
			String test = in.nextLine();
			Matcher matcher = pattern.matcher(test);
			if (matcher.matches()) {
				System.out.println("It matches!");
			} else {
				System.out.println("It doesn't match..");
			}
		}
   	/*
    	for(;;) {
	    	System.out.println("1. Add to belief base");
	    	System.out.println("2. Check formula for consistency");
	    	System.out.println("3. Show belief base");
	    	System.out.println("4. Quit");




	    	//Read input for choice
	    	int choice = in.nextInt();
	    	in.nextLine();



	    	switch (choice) {
			case 1:
				be.addToBeliefBase("something from michael");
				break;
				
			case 2:
				
				break;
				
			case 3:
				
				break;
	
			case 4:
				System.out.println("Belief engine shutdown");
				System.exit(0);
				break;

			default:
				break;
			}
    	}
    */
    }
}
