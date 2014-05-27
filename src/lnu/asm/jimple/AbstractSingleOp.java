package lnu.asm.jimple;

import org.objectweb.asm.Type;

public abstract class AbstractSingleOp implements SingleOp {
	protected Type tp;
	protected Value value;
	
	public AbstractSingleOp(Value v){
		value=v;
		tp=value.getType();
	}

	
	public Value getValue(){
		return value;
	}
	
	public Type getType() {
		return tp;
	}
	
	public String toString(){
		return value.toString();
	}
}
