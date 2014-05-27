package lnu.asm.jimple;

import java.util.List;

import org.objectweb.asm.Type;

public abstract class AbstractStaticInvoke  extends AbstractInvoke{

	protected Type type;
	
	public AbstractStaticInvoke(JMethodRef methRef, List<Value> args) {
		super(methRef, args);
		type=methRef.getType();
	}


	
	
	public Type getType(){
		return type;
	}

}
