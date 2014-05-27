package lnu.asm.jimple;

import org.objectweb.asm.Label;



public interface IfStmt extends Unit{
	public Operation getCondition();
	public void setCondition(Operation condition);
    public Label getLable();
}     
