package com.beliefrevision;

import java.util.ArrayList;

public class Literal {
    String symbol;

    public Literal(Literal l){
        this.symbol = l.symbol;
    }

    public Literal(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
    	return symbol;
    }
}
