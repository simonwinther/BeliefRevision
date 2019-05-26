package com.beliefrevision;

import java.util.ArrayList;
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

    public void PLResolve(Clause c1, Clause c2) {
        boolean found = false;
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
                        c1.clause.replaceAll(obj1.symbol,"");
                        c2.clause.replaceAll(obj2.symbol,"");
                        String res = c1.clause + c2.clause;
                        System.out.println("result " + res);
                        found = false;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    private boolean isCNF(String sentence) {
        Pattern pattern = Pattern.compile("([!]?[a-zA-Z](\\s?[|]\\s?([!]?[a-zA-Z]))+)|(([!]?[a-zA-Z]\\s?)|([(]([!]?[a-zA-Z](\\s?[|]\\s?[!]?[a-zA-Z])+)[)]\\s?))([&]\\s?(([!]?[a-zA-Z]\\s?)|([(]([!]?[a-zA-Z](\\s?[|]\\s?[!]?[a-zA-Z])+)[)]\\s?)))*");
        Matcher matcher = pattern.matcher(sentence);
        return matcher.matches();
    }

}
