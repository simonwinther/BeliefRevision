package com.beliefrevision;

import java.util.ArrayList;

public class CNFSentence {

    ArrayList<Clauses> clauses = new ArrayList<Clauses>();
    String cnfSentence;

    public CNFSentence(String cnfSentence) {
        this.cnfSentence = cnfSentence;
        parseClauses();
    }

    public void parseClauses() {
        String[] arrOfStr = cnfSentence.split("[&]+");

        for (String a : arrOfStr) {
            Clauses clause = new Clauses(a);
            clauses.add(clause);
            System.out.println("Clause: " + a);
        }
    }
}
