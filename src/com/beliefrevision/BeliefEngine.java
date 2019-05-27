package com.beliefrevision;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;


public class BeliefEngine {
    private static final int MAX_CLAUSES = 5;
    private static final int MAX_LITERALS = 5;
    ArrayList<Object> beliefBase = new ArrayList<>();
    CNFSentence cnf;

    public BeliefEngine() {

    }

    public void addToBeliefBase(String sentence) {
        if(isCNF(sentence)){
            cnf = new CNFSentence(sentence);
            beliefBase.add(cnf);
        }

    }
    
    public void printBeliefBase() {
    	int beliefBaseLength = beliefBase.size();
    	int printCounter = 1;
    	System.out.print("{");
    	for(Object obj: beliefBase) {
    		if(obj.getClass() == CNFSentence.class) {
    			cnf = (CNFSentence) obj;
    			System.out.print(cnf.getCNFSentence());
    		}
    		if(beliefBaseLength != printCounter) {
    			System.out.print(", ");
    			printCounter++;
    		}
    	}
    	System.out.println("}");
    }

    private boolean isCNF(String sentence) {
        Pattern pattern = Pattern.compile("([!]?[a-zA-Z](\\s?[|]\\s?([!]?[a-zA-Z]))+)|(([!]?[a-zA-Z]\\s?)|([(]([!]?[a-zA-Z](\\s?[|]\\s?[!]?[a-zA-Z])+)[)]\\s?))([&]\\s?(([!]?[a-zA-Z]\\s?)|([(]([!]?[a-zA-Z](\\s?[|]\\s?[!]?[a-zA-Z])+)[)]\\s?)))*");
        Matcher matcher = pattern.matcher(sentence);
        return matcher.matches();
    }
  
    public String negateThesis(String sentence){
        String test = "(A|B)&!C";

        String moreDeMorgans;
        String negatedThesis = "";

        if(isCNF(test)){

            /* Not used for result
            //Negate
            StringBuilder tmp = new StringBuilder(test);
            tmp.insert(0,"!(");
            tmp.insert(tmp.length(),")");
            */

            //DeMorgans
            moreDeMorgans = DeMorgan(test);

            // distribution
            negatedThesis = distribution(moreDeMorgans);
        }
        return negatedThesis;
    }

    private String DeMorgan(String test) {
        ArrayList<String> clauses = new ArrayList<>();
        String negClause;
        String moreDeMorgans = "";

        String[] split = test.split("&");

        for (String s : split){
            //tmpSplit.insert(0,"!"); //for DeMorgans string (needs tmpSplit to be a StringBuilder
            clauses.add(s);
        }

            /*
            for (String clause : clauses) {
                DeMorgans = DeMorgans.concat(clause).concat("|");
            }
            DeMorgans = DeMorgans.substring(0, DeMorgans.length() - 1);
            */

        //More DeMorgans and double negation
        for (String clause : clauses) {
            if (clause.charAt(0) != '(') {
                if(clause.charAt(0) == '!') {
                    moreDeMorgans = moreDeMorgans.concat(clause.substring(1)).concat("|");
                } else {
                    StringBuilder tmpSplit = new StringBuilder(clause);
                    tmpSplit.insert(0, "!");
                    moreDeMorgans = moreDeMorgans.concat(tmpSplit.toString()).concat("|");
                }
            } else {
                negClause = clause.replaceAll("\\|", "&!");
                negClause = negClause.replaceAll("\\(", "(!");
                negClause = negClause.replaceAll("!!", "");
                moreDeMorgans = moreDeMorgans.concat(negClause).concat("|");
            }
        }
        moreDeMorgans = moreDeMorgans.substring(0, moreDeMorgans.length() - 1);

        return moreDeMorgans;
    }

    private String distribution(String moreDeMorgans) {
        ArrayList<String> clauses = new ArrayList<>();
        String[] split1 = moreDeMorgans.split("\\|");
        String tmpS;
        for (String s : split1) {
            tmpS = s.replaceAll("\\(", "");
            tmpS = tmpS.replaceAll("\\)", "");
            clauses.add(tmpS);
        }

        String[][] split = new String[MAX_CLAUSES][MAX_LITERALS];
        for (String clause : clauses) {
            split[clauses.indexOf(clause)] = clause.split("&");
        }


        ArrayList<String> newClauses = new ArrayList<>();
        for(int i = 0; i < split.length-1; i++) {
            for(int j = 0; j < split[i].length; j++){
                for(int o = 0; o < split[i+1].length; o++) {
                    if (split[i][j] != null && split[i+1][o] != null) {
                        //System.out.println("Value of split[" + i + "][" + j + "] = " + split[i][j]);
                        //System.out.println(split[i][j].concat("|").concat(split[i+1][o]));
                        newClauses.add(split[i][j].concat("|").concat(split[i+1][o]));
                    }
                }
            }
        }

        String negatedThesis = "";
        for(String s : newClauses){
            negatedThesis = negatedThesis.concat("(").concat(s).concat(")&");
        }
        negatedThesis = negatedThesis.substring(0, negatedThesis.length() - 1);

        return negatedThesis;
    }
}
