package com.beliefrevision;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BeliefEngine {
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

    private boolean isCNF(String sentence) {
        Pattern pattern = Pattern.compile("([!]?[a-zA-Z](\\s?[|]\\s?([!]?[a-zA-Z]))+)|(([!]?[a-zA-Z]\\s?)|([(]([!]?[a-zA-Z](\\s?[|]\\s?[!]?[a-zA-Z])+)[)]\\s?))([&]\\s?(([!]?[a-zA-Z]\\s?)|([(]([!]?[a-zA-Z](\\s?[|]\\s?[!]?[a-zA-Z])+)[)]\\s?)))*");
        Matcher matcher = pattern.matcher(sentence);
        return matcher.matches();
    }

    public String negateThesis(){
        String test = "(A|!B)&C";
        StringBuilder tmp = new StringBuilder(test);
        ArrayList<String> clauses = new ArrayList<>();
        ArrayList<String> newClauses = new ArrayList<>();
        String negClause;
        String DeMorgans = "";
        String moreDeMorgans = "";

        /*
        !((A|!B)&C)
        !(A|!B)|!C
        (!A&B)|!C
        (!A|!C)&(B|!C)
         */

        if(isCNF(test)){

            //Negate
            tmp.insert(0,"!(");
            tmp.insert(tmp.length(),")");

            //DeMorgans
            String[] split = test.split("&");

            for (String s : split){
                StringBuilder tmpSplit = new StringBuilder(s);
                //tmpSplit.insert(0,"!"); //for DeMorgans string
                clauses.add(tmpSplit.toString());
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

            // distribution

            clauses.clear();
            String[] split1 = moreDeMorgans.split("\\|");
            String[] split2;
            String[] split3;
            String tmpS;
            //System.out.println(split1.length);
            for (String s : split1) {
                tmpS = s.replaceAll("\\(", "");
                tmpS = tmpS.replaceAll("\\)", "");
                clauses.add(tmpS);
            }

            for (int i = 0; i < clauses.size(); i++) {
                split2 = clauses.get(i).split("&");
                for (int j = 1; j < clauses.size(); j++) {
                    split3 = clauses.get(i).split("&");
                    for (int o = 0; o < split2.length; o++) {
                        for (int u = 0; u < split2.length; u++) {
                            newClauses.add(split2[o].concat("|").concat(split3[u]));
                        }
                    }
                }
            }
            newClauses = removeDuplicates(newClauses);
            for (String s : newClauses) {
                System.out.println(s);
            }
        }
        return moreDeMorgans;
    }

    public static <String> ArrayList<String> removeDuplicates(ArrayList<String> list)
    {

        // Create a new ArrayList
        ArrayList<String> newList = new ArrayList<String>();

        // Traverse through the first list
        for (String element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }
}
