package com.beliefrevision;

import java.util.ArrayList;

public class Clause {
    String clause;
    ArrayList<Literal> literals = new ArrayList<Literal>();

    public Clause(String literal) {
        this.clause = literal;
        parseLiteral();
    }

    public void parseLiteral() {
        String[] arrOfStr = clause.split("[|]+");

        for (String a : arrOfStr) {
            Literal literal = new Literal(a);
            literals.add(literal);
            System.out.println("Literal: " + a);
        }
    }
    
    public ArrayList<Literal> getLiterals(){
    	return literals;
    }
        
}
