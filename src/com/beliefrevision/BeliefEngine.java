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
		clauses.addAll(getClausesFromCNFSentence(getNegatedSentence(alpha)));
		
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
					// resolvents.addAll(plResolve(ci, cj);
					
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

    private boolean isCNF(String sentence) {
        Pattern pattern = Pattern.compile("([!]?[a-zA-Z](\\s?[|]\\s?([!]?[a-zA-Z]))+)|(([!]?[a-zA-Z]\\s?)|([(]([!]?[a-zA-Z](\\s?[|]\\s?[!]?[a-zA-Z])+)[)]\\s?))([&]\\s?(([!]?[a-zA-Z]\\s?)|([(]([!]?[a-zA-Z](\\s?[|]\\s?[!]?[a-zA-Z])+)[)]\\s?)))*");
        Matcher matcher = pattern.matcher(sentence);
        return matcher.matches();
    }
    
}
