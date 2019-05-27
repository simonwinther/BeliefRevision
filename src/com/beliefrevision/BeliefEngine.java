package com.beliefrevision;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



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

    public ArrayList<Clause> PLResolve(Clause c1, Clause c2) {
        ArrayList<Clause> resArr = new ArrayList<>();

        boolean found = false;

        Clause tmpc1 = new Clause(c1);
        Clause tmpc2 = new Clause(c2);

        ArrayList<Clause> newClauses = new ArrayList<Clause>();
        try {
            for (Literal obj1 : c1.literals) {
                for (Literal obj2 : c2.literals) {
                    if(obj1.symbol.charAt(0) == '!') {
                        if(obj1.symbol.charAt(1) == obj2.symbol.charAt(0)) {
                            System.out.println("obj1 " + obj1.symbol);
                            System.out.println("obj2 " + obj2.symbol);
                            found = true;
                        }
                    } else if (obj2.symbol.charAt(0) == '!') {
                        if(obj2.symbol.charAt(1) == obj1.symbol.charAt(0)) {
                            System.out.println("obj1 " + obj1.symbol);
                            System.out.println("obj2 " + obj2.symbol);
                            found = true;
                        }
                    }

                    if(found) {
                        Set<String> hash_Set = new HashSet<>();
                        StringBuilder res = new StringBuilder();

                        tmpc1.clause = tmpc1.clause.replace(obj1.symbol,"");
                        tmpc2.clause = tmpc2.clause.replace(obj2.symbol,"");

                        for(int i = 0; i < tmpc1.literals.size(); i++) {
                            if(tmpc1.literals.get(i).symbol == obj1.symbol) {
                                tmpc1.literals.remove(i);
                            }
                        }

                        for(int i = 0; i < tmpc2.literals.size(); i++) {
                            if(tmpc2.literals.get(i).symbol == obj2.symbol) {
                                tmpc2.literals.remove(i);
                            }
                        }

                        for (Literal l1 : tmpc1.literals) {
                            hash_Set.add(l1.symbol);
                        }
                        for (Literal l2 : tmpc2.literals) {
                            hash_Set.add(l2.symbol);
                        }

                        int counter = hash_Set.size();

                        for(String s : hash_Set){
                            System.out.println("s "+ s);
                            res.append(s);
                            if(counter > 1) {
                                res.append("|");
                            }
                            counter--;
                        }

                        System.out.println("Sum " +res.toString());
                        resArr.add(new Clause(res.toString()));
                        found = false;
                        tmpc1 = new Clause(c1);
                        tmpc2 = new Clause(c2);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error");
        }
        return resArr;
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
