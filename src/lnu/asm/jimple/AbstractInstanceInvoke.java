package lnu.asm.jimple;

import java.util.List;

import org.objectweb.asm.Type;

public abstract class AbstractInstanceInvoke extends AbstractInvoke{
	protected Type type;
	protected Value base;
	
	public AbstractInstanceInvoke(Value base,JMethodRef methRef, List<Value> args) {
		super(methRef, args);
		this.base=base;
		type=methRef.getType();
	}
	
	public Type getType(){
		return type;
	}

}
