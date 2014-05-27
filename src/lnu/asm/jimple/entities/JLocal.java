package lnu.asm.jimple.entities;

import java.util.List;

import lnu.asm.jimple.Local;
import lnu.asm.jimple.ValueBox;

import org.objectweb.asm.Type;

public class JLocal implements Local{
	String name;
	Type type;
	int index; // index!=10
	
	public JLocal(String n, Type t){
		name = n.intern();
        type = t;
	}
	public static JLocal v(String n, Type t){
		return new JLocal(n,t);
	}
	

	@Override
	public List<ValueBox> getUseBoxes() {
		return null;
	}
	
	public int getIndex() {
		return index;
	}

	@Override
	public Type getType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name=name;
		
	}

	@Override
	public void setType(Type t) {
		type=t;
		
	}
	
	public void setIndex(int i) {
		index=i;
		
	}
	
	public String toString(){
		return name;
	}

}
