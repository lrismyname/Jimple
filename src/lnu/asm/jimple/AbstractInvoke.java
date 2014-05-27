package lnu.asm.jimple;

import java.util.List;
import lnu.asm.jimple.entities.JMethod;

public abstract class AbstractInvoke implements Invoke{
	
	protected JMethodRef methodRef;
	protected List<Value> argValues;
	
	
	public AbstractInvoke(JMethodRef methRef, List<Value> args){
		methodRef=methRef;
		argValues=args;
	}
	
    public void setMethodRef(JMethodRef mref){
    	methodRef=mref;
    }
	
	public void setArgs(int index, Value arg){
		argValues.set(index, arg);
	}
	
	public Value getArg(int index){
		return argValues.get(index);
	}
	
	public JMethod getMethod(){
		return methodRef.getMethod();
	}

}
