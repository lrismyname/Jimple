package lnu.asm.jimple;

import lnu.asm.jimple.entities.JMethod;

public interface Invoke extends Operation{
	
    public void setMethodRef(JMethodRef mref);
	
	public void setArgs(int index, Value arg);
	
	public Value getArg(int index);
	
	public JMethod getMethod();
	

}
