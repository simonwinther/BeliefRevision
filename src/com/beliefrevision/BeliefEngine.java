package com.beliefrevision;

import java.util.ArrayList;
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

    private boolean isCNF(String sentence) {
        Pattern pattern = Pattern.compile("([!]?[a-zA-Z](\\s?[|]\\s?([!]?[a-zA-Z]))+)|(([!]?[a-zA-Z]\\s?)|([(]([!]?[a-zA-Z](\\s?[|]\\s?[!]?[a-zA-Z])+)[)]\\s?))([&]\\s?(([!]?[a-zA-Z]\\s?)|([(]([!]?[a-zA-Z](\\s?[|]\\s?[!]?[a-zA-Z])+)[)]\\s?)))*");
        Matcher matcher = pattern.matcher(sentence);
        return matcher.matches();
    }

    /**************************ONLY WORKS FOR 1 OR 2 CLAUSES WITH '&' IN BETWEEN********************************/
    public CNFSentence negateThesis(CNFSentence sentence){
        String DNFForm;
        String negatedThesisCNF = "";

        if(isCNF(sentence.cnfSentence)){
            if(sentence.clauses.size() == 1) {
                for (Literal l : sentence.clauses.get(0).literals) {
                    if (l.symbol.charAt(0) == '!') {
                        negatedThesisCNF = negatedThesisCNF.concat(Character.toString(l.symbol.charAt(1)).concat("&"));
                    } else {
                        negatedThesisCNF = negatedThesisCNF.concat("!").concat(l.symbol).concat("&");
                    }
                }
                negatedThesisCNF = negatedThesisCNF.substring(0, negatedThesisCNF.length() - 1);
            } else {

                // DeMorgans
                System.out.println("DNF form: "+(DNFForm = DeMorgan(sentence)));

                // Distribution
                negatedThesisCNF = distribution(DNFForm);
            }
        }
        return new CNFSentence(negatedThesisCNF);
    }

    private String DeMorgan(CNFSentence sentence) {
        String negClause;
        String DNFForm= "";

        //DeMorgans and double negation
        for (Clause clause : sentence.clauses) {
            if (clause.clause.charAt(0) != '(') {
                if(clause.clause.charAt(0) == '!') {
                    DNFForm = DNFForm.concat(clause.clause.substring(1)).concat("|");
                } else {
                    StringBuilder tmpSplit = new StringBuilder(clause.clause);
                    tmpSplit.insert(0, "!");
                    DNFForm = DNFForm.concat(tmpSplit.toString()).concat("|");
                }
            } else {
                negClause = clause.clause.replaceAll("\\|", "&!");
                negClause = negClause.replaceAll("\\(", "(!");
                negClause = negClause.replaceAll("!!", "");
                DNFForm = DNFForm.concat(negClause).concat("|");
            }
        }
        DNFForm = DNFForm.substring(0, DNFForm.length() - 1);

        return DNFForm;
    }

    private String distribution(String DNFForm) {
        ArrayList<String> clauses = new ArrayList<>();
        String[] split1 = DNFForm.split("\\|");
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
        for(int i = 0; i < split.length-1; i++) { // Goes through all clauses
            for(int j = 0; j < split[i].length; j++){ // Goes through all literals of each clause
                for(int o = 0; o < split[i+1].length; o++) { //
                    if (split[i][j] != null && split[i+1][o] != null) {
                        newClauses.add(split[i][j].concat("|").concat(split[i+1][o]));
                    }
                }
            }
        }

        String negatedThesisCNF = "";
        for(String s : newClauses){
            negatedThesisCNF = negatedThesisCNF.concat("(").concat(s).concat(")&");
        }
        negatedThesisCNF = negatedThesisCNF.substring(0, negatedThesisCNF.length() - 1);

        return negatedThesisCNF;
    }
}
