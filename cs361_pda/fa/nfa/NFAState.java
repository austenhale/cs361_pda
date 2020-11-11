package fa.nfa;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class NFAState extends fa.State implements Set<NFAState>{
	
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


	public void addTransition(char onSymb, NFAState to) {
		delta.put(onSymb, to);
		
	}


	public Object getTo(char symb) {
		Set<NFAState> ret = delta.get(symb);
		if (ret==null) {
			System.err.println("ERROR: NFAState.getTo(char symb) returns null on " + symb + " from " + name);
			 System.exit(2);
		}
		return delta.get(symb);
	}


	@Override
	public boolean add(NFAState e) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean addAll(Collection<? extends NFAState> c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean contains(Object o) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Iterator<NFAState> iterator() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean remove(Object o) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Auto-generated method stub
		return null;
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
