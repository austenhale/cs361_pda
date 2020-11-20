package fa.nfa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class NFAState extends fa.State{
	
	private HashMap<Character, Set<NFAState>> delta;
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
		delta = new HashMap<Character, Set<NFAState>>();
		
	}
	
	public boolean isFinal() {
		return isFinal;
	}


	public void addTransition(char onSymb, Set<NFAState> to) {
		delta.put(onSymb, to);
		
	}
	
	public void addTransition(char onSymb, NFAState to) {
		Set<NFAState> setOfOne = new LinkedHashSet<NFAState>();
		setOfOne.add(to);
		delta.put(onSymb, setOfOne);
	}


	public Object getTo(char symb) {
		Set<NFAState> ret = delta.get(symb);
		if (ret==null) {
			System.err.println("ERROR: NFAState.getTo(char symb) returns null on " + symb + " from " + name);
			 System.exit(2);
		}
		return delta.get(symb);
	}

	
	public boolean hasTransition(char c) {
		if (delta.containsKey(c)) {
			return true;
		}
		return false;
	}


	public Set<NFAState> getTransition(char c) {
		return delta.get(c);
	}


	




	
}
