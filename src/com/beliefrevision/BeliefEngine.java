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

    //TODO: plResolution has not yet been tested
	public boolean plResolution(ArrayList<Object> beliefBase, CNFSentence alpha) {
		ArrayList<Clause> clauses = new ArrayList<Clause>();
		ArrayList<Clause> _new = new ArrayList<Clause>();
		ArrayList<Clause> resolvents = new ArrayList<Clause>();
		
		clauses.addAll(getClausesFromBB(beliefBase));
		clauses.addAll(getClausesFromCNFSentence(negateThesis(alpha)));
		
		System.out.println("Negated heis: " + negateThesis(new CNFSentence("(!C|B)&A")).cnfSentence);
		
		//loop do
		for(;;) {
			
			// for each pair of clauses ci, cj in clauses do
			for (Clause ci: clauses) {
				for (Clause cj: clauses) {
					if (ci == cj) {
						continue;
					}
					
					// resolvents <- PL-Resolve(ci, cj)
					// Add method: plResolve
					resolvents.addAll(plResolve(ci, cj));
					
					// if resolvents contains the empty clause then return true
					for (Clause tmp: resolvents) {
						if (tmp.clause.isEmpty()) {
							return true;
						}
					}
					
					// new <- new U resolvents
					_new = unifyArrayLists(_new, resolvents);
				}
			}
			
			// if new is equivalent to clauses or clauses already has all existing clauses in new then return false
			if(array1ContentMatchArray2(_new, clauses)) {
				return false;
			}
			
			// clauses <- clauses U new
			clauses = unifyArrayLists(clauses, _new);
		}
	}

	private ArrayList<Clause> unifyArrayLists(ArrayList<Clause> arr1, ArrayList<Clause> arr2) {
		boolean clauseExist = false;
		
		for (Clause tmp: arr2) {
			for (Clause tmp2: arr1) {
				if(tmp.clause.equals(tmp2.clause)) {
					clauseExist = true;
					break;
				}
			}
			if (!clauseExist) {
				arr1.add(tmp);
			}
			clauseExist = false;
		}
		return arr1;
	}
	
	public boolean array1ContentMatchArray2(ArrayList<Clause> arr1, ArrayList<Clause> arr2) {
	    ArrayList<Clause> work = new ArrayList<Clause>(arr2);
	    
	    // as long as elements from arr1 can be removed from arr2, arr2 must contain the same elements as arr1.
	    for (Clause element : arr1) {
	        if (!work.remove(element)) {
	            return false;
	        }
	    }
	    return true;
	}
    
    public CNFSentence getNegatedSentence(CNFSentence cnf) {
    	return cnf;
    }
    
    public ArrayList<Clause> getClausesFromCNFSentence(CNFSentence cnf){
    	ArrayList<Clause> clauses = new ArrayList<Clause>();
    		for(Clause tmp: cnf.getClauses()) {
				clauses.add(tmp);
			}
    	return clauses;
    }
    
    public ArrayList<Clause> getClausesFromBB(ArrayList<Object> beliefBase){
    	ArrayList<Clause> clauses = new ArrayList<Clause>();
    	for(Object obj: beliefBase) {
    		if(obj.getClass() == CNFSentence.class) {
    			cnf = (CNFSentence) obj;
    			for(Clause tmp: cnf.getClauses()) {
    				clauses.add(tmp);
    			}
    		}
    	}
    	return clauses;
    }
      
    public ArrayList<Clause> plResolve(Clause c1, Clause c2) {
        ArrayList<Clause> resArr = new ArrayList<>();

        boolean found = false;

        Clause tmpc1 = new Clause(c1);
        Clause tmpc2 = new Clause(c2);

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
                        tmpc1.clause = tmpc1.clause.replace(obj1.symbol,"");
                        tmpc2.clause = tmpc2.clause.replace(obj2.symbol,"");

                        StringBuilder res = new StringBuilder(tmpc1.clause + "|" + tmpc2.clause);
                        String result = "";

                        if(res.charAt(0) == '|') {
                            res.setCharAt(0, ' ');
                        }

                        if(res.charAt(res.length()-1) == '|') {
                            res.setCharAt(res.length()-1, ' ');
                        }
                        result =  res.toString();

                        result = result.replaceAll(" ", "");
                        result = result.replaceAll("\\|\\|", "|");

                        System.out.println("result " + result);

                        resArr.add(new Clause(result));
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
        String DNFForm= "";
        String DNFClause;

        //DeMorgans and double negation
        for (Clause clause : sentence.clauses) {
            DNFClause = "";
            DNFClause = DNFClause.concat("(");
            for (Literal l : clause.literals) {
                if (l.symbol.charAt(0) == '!') {
                    DNFClause = DNFClause.concat(Character.toString(l.symbol.charAt(1))).concat("&");
                } else {
                    DNFClause = DNFClause.concat("!").concat(l.symbol).concat("&");
                }
            }
            DNFClause = DNFClause.substring(0, DNFClause.length() - 1);
            DNFForm = DNFForm.concat(DNFClause).concat(")").concat("|");
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
    
    public void testPL() {
    	CNFSentence cnf = new CNFSentence("(!c|b)&a");
    	plResolution(beliefBase, cnf);
    }
}
