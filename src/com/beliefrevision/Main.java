package com.beliefrevision;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {



    public static void main(String[] args) {
    	menu();
    }

    public static void menu() {

        BeliefEngine be = new BeliefEngine();
        Util util = new Util();


    	@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
        System.out.println(be.negateThesis());
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
				be.addToBeliefBase(util.getRandomLine());
				System.out.println(be.beliefBase.size());
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
    }
}
