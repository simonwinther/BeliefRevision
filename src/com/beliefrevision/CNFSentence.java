package com.beliefrevision;

import java.util.ArrayList;

public class CNFSentence {

    ArrayList<Clause> clauses = new ArrayList<Clause>();
    String cnfSentence;

    public CNFSentence(String cnfSentence) {
        this.cnfSentence = cnfSentence;
        parseClauses();
    }

    public void parseClauses() {
        String[] arrOfStr = cnfSentence.split("[&]+");

        for (String a : arrOfStr) {
            a = a.replaceAll("[()]", "");
            Clause clause = new Clause(a);
            clauses.add(clause);
//            System.out.println("Clause: " + a);
        }
    }
    
    public String getCNFSentence() {
    	return cnfSentence;
    }
    
    public ArrayList<Clause> getClauses(){
    	return clauses;
    }
}
