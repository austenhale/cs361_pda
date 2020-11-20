package fa.nfa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import fa.State;
import fa.dfa.DFA;
import fa.dfa.DFAState;

public class NFA implements NFAInterface{
	
	private Set<NFAState> states;
	private NFAState start;
	private Set<Character> language;
	private Set<NFAState> visitedStates; 
	
	public NFA() {
		states = new LinkedHashSet<NFAState>();
		language = new LinkedHashSet<Character>();
		visitedStates =  new LinkedHashSet<NFAState>();
	}

	@Override
	public void addStartState(String name) {
		NFAState s = checkIfExists(name);
		if (s == null) {
			s = new NFAState(name);
			states.add(s);
		}else {
			System.out.println("WARNING: A state with name " + name + " already exists in the NFA");
		}
		start = s;
		
	}

	

	@Override
	public void addState(String name) {
		NFAState s = checkIfExists(name);
		if (s == null) {
			s = new NFAState(name);
			states.add(s);
		}else {
			System.out.println("WARNING: A state with name " + name + " already exists in the NFA");
		}
		
	}

	@Override
	public void addFinalState(String name) {
		NFAState s = checkIfExists(name);
		if (s == null) {
			s = new NFAState(name, true);
			states.add(s);
		}else{
			System.out.println("WARNING: A state with name " + name + " already exists in the NFA");
		}
		
	}

	@Override
	public void addTransition(String fromState, char onSymb, String toState) {
		NFAState from = checkIfExists(fromState);
		NFAState to = checkIfExists(toState);
		if (from == null) {
			System.err.println("ERROR: No NFA state exists with name " + fromState);
			System.exit(2);
		}else if (to == null) {
			System.err.println("ERROR: NO NFA state exists with name " + toState);
			System.exit(2);
		}
		from.addTransition(onSymb, to);
		
		if (!language.contains(onSymb) && onSymb != 'e') {
			language.add(onSymb);
		}
		
	}

	@Override
	public Set<? extends State> getStates() {
		return states;
	}

	@Override
	public Set<? extends State> getFinalStates() {
		Set<NFAState> ret = new LinkedHashSet<NFAState>();
		for (NFAState s : states) {
			if (s.isFinal()) {
				ret.add(s);
			}
		}
		return ret;
	}

	@Override
	public State getStartState() {
		return start;
	}

	@Override
	public Set<Character> getABC() {
		return language;
	}

	
	private NFAState checkIfExists(String name) {
		NFAState ret = null;
		for (NFAState s : states) {
			if (s.getName().equals(name)) {
				ret = s;
				break;
			}
		}
		return ret;
	}
	
	@Override
	public String toString(){

		String s = "Q = { ";
		String fStates = "F = { ";
		for(NFAState state : states){
			s += state.toString();
			s +=" ";
			if(state.isFinal()){
				fStates +=state.toString();
				fStates += " ";
			}
		}
		s += "}\n";
		fStates += "}\n";
		s += "Sigma = { ";
		for(char c : language){
			s += c + " ";
		}
		s += "}\n";
		//create transition table
		s += "delta =\n"+String.format("%10s", "");;
		for(char c : language){
			s += String.format("%10s", c);
		}
		s+="\n";
		for(NFAState state : states){
			s += String.format("%10s",state.toString());
			for(char c : language){
				s += String.format("%10s", state.getTo(c).toString());
			}
			s+="\n";
		}
		//start state
		s += "q0 = " + start + "\n";
		s += fStates;
		return s;
	}
	
	//each DFA state corresponds to set of NFA states
			//getDfa method should track whether a DFA state with label(name) 
				//corresponding to the string representation of the NFA states
			//getDFA must use a breadth-first search
				//loop iterating over a queue; an element of a queue is a set of NFA states
				//example BFS:
				//boolean visited[] = new boolean[V];
				//LinkedList<integer> queue = new LinkedList<Integer>();
				//visted[s] = true; //s is an int
				//queue.add(s);
				//while(queue.size() != 0){
				//	s = queue.poll();
				//	Iterator<Integer> i = adj[s].listIterator();
				//	while (i.hasNext()){
						//int n = i.next();
						//if (!visited[n]){
						//visited[n] = true;
						//queue.add(n);}}
			
			//pseudocode for NFA to DFA
			//Perform closure on current state set
			//For each input symbol do the GOTO operation on closure set
				//If the state set you get from GOTO is not empty
					//Do a closure of the state set
					//If it is a new set of states:
						//add a transition between the state sets on the input
						//repeat the entire operation on this new set
					//else
						//add a transition between the state sets on the input

	public DFA getDFA() {
		DFA dfa = new DFA();
		
		Set<NFAState> startState = eClosure(this.start);
		
		String startStateName = getStatesName(startState); 
		
		dfa.addStartState(startStateName);
		
		Queue<DFAState> queue = new LinkedList<DFAState>();
		Set<String> statesFound = new LinkedHashSet<String>();
		statesFound.add(startStateName);
		queue.add(new DFAState(startStateName));
	
		// Add transitions 
		//addDFAStates(dfa, queue, statesFound); //Do we want to do another method or just do it all in here? --I think we can start it here, then move it later if we want
		
		while (!queue.isEmpty()) {
			DFAState currState = queue.remove();
			String[] NFAStates = getStatesFromName(currState.getName());
			
			for (char c : language) { //for every character in the language
				boolean finalState = false; //boolean tracker for when the final state is current
				LinkedHashSet<String> DFAState = new LinkedHashSet<String>(); //set of all the DFA's states
				
				for (int i = 0; i < NFAStates.length; i++) { //iterate through all states of current state (in case of a state such as [a,b])
					if (NFAStates[i].equals("")) continue;
					
					Set<NFAState> toStates = getToState(get(NFAStates[i]), c);
					if (toStates == null) {
						DFAState.add("{}");
					} else {
						for (NFAState state : toStates) {
							if (state.isFinal())
								finalState = true;
							DFAState.add(state.getName());
							Set<NFAState> eClosureStates = eClosure(state);
							if (eClosureStates == null) {
								DFAState.add("{}");
							} else {
								for (NFAState s : eClosureStates) {
									DFAState.add(s.getName());
									if (state.isFinal())
										finalState = true;
								}
							}
						}
					} //end else statement for if toStates wasn't null
				} //end for loop to go through all states
				String stateName = getStatesNameString(DFAState);
				if (!statesFound.contains(stateName)) {
					statesFound.add(stateName);
					if (finalState) {
						dfa.addFinalState(stateName);
					}
					else {
						dfa.addState(stateName);
					}
					queue.add(new DFAState(stateName)); //adding new state to queue
				}
				
				dfa.addTransition(currState.getName(), c, stateName);
			} //end for loop to go through all characters
			
			
		}
		
		return dfa;
	}
	
	private String[] getStatesFromName(String stateName) {
		String minusBrackets = stateName.substring(1, stateName.length() - 1); 
		String[] splitString = minusBrackets.split(","); 
		return splitString; 
	}

	private String getStatesNameString(LinkedHashSet<String> dFAState) {
		String retVal = "{"; 
		ArrayList<String> chars = new ArrayList<String>(); 
		for (String s : dFAState) {
			if (!s.equals("{}")) {
				if(!chars.contains(s)) chars.add(s); 
			}
		}
		Collections.sort(chars, String.CASE_INSENSITIVE_ORDER); 
		for (String string : chars) {
			retVal += string; 
			retVal += ","; 
		}
		if (!retVal.equals("{")) {
			retVal = retVal.substring(0, retVal.length()-1); 
		}
		retVal += "}"; 
		return retVal; 
	}

	/**
	 * Gets the names of a state, given a set of NFAStates. For example if the
	 * NFA State is called [a,b], it will return a string containing {a, b}
	 * @param state
	 * @return
	 */
	private String getStatesName(Set<NFAState> state) {
		String retVal = "{";
		ArrayList<String> stateNames = new ArrayList<String>(); //list for storing all the names of the states
		for (NFAState s: state) { //iterate through passed in states in the set
			if (!stateNames.contains(s.getName())) {
				stateNames.add(s.getName());
			}
		}
		//I don't think we need to sort through the list, [a,b] or [b,a] is fine
		for (String str : stateNames) {
			retVal += str + ",";
		}
		
		retVal = retVal.substring(0, retVal.length()-1);
		retVal += "}";
		return retVal;
	}

	public Set<NFAState> eClosure(NFAState s){
		visitedStates.add(s); //add the state to the visited list
		if (s.hasTransition('e')) { //if there's an empty transition
			Set<NFAState> nextState = s.getTransition('e'); //get a set of all possible transitions
			for (NFAState next : nextState) { //for every state that is reachable
				eClosure(next); //repeat the process
			}
			
		}
		
		return visitedStates; //return the full list after recursion ends
	}

	
	/**
	 * This will get an element from the set.
	 * @param stateToGet
	 * @param set
	 * @return a copy of the State from the set, null if state is not in set
	 */
	private NFAState get(NFAState stateToGet, Set<NFAState> set) {
		for (NFAState state : set) {
			if (state.toString().equals((stateToGet.toString()))) {
				return state;
			}
		}
		return null;
	}
	
	/**
	 * This will get an element from the set, given an index.
	 * @param i - index
	 * @param set
	 * @return a copy of the State from the set at the index, null if state is not in set
	 */
	private NFAState get(int i, Set<NFAState> set) {
		int n = 0;
		for (NFAState state : set) {
			n++;
			if (n == i) {
					return state;
			}
		
		}
		return null;
	}
	
	/**
	 * This will get an NFAState from a set, given a name. Returns null if not found.
	 * @param name
	 * @return a NFAState, or null
	 */
	private NFAState get(String name) {
		for (NFAState s: states) {
			if (s.getName().equals(name))
				return s;
		}
		return null;
	}

	@Override
	public Set<NFAState> getToState(NFAState from, char onSymb) {
		return from.getTransition(onSymb);
	}
}
