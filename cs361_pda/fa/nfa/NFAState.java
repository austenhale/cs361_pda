package fa.nfa;

import java.util.HashMap;

public class NFAState extends fa.State{
	
	private HashMap<Character, NFAState> delta;
	private boolean isFinal;
	

	public NFAState(String name) {
		initDefault(name);
		isFinal = false;
	}

	
	public NFAState(String name, boolean b) {
		initDefault(name);
		this.isFinal = b;
	}
	
	private void initDefault(String name) {
		this.name = name;
		delta = new HashMap<Character, NFAState>();
		
	}
	
	public boolean isFinal() {
		return isFinal;
	}


	public void addTransition(char onSymb, NFAState toState) {
		delta.put(onSymb, toState);
		
	}


	public Object getTo(char symb) {
		NFAState ret = delta.get(symb);
		if (ret==null) {
			System.err.println("ERROR: NFAState.getTo(char symb) returns null on " + symb + " from " + name);
			 System.exit(2);
		}
		return delta.get(symb);
	}

}
