package lnu.asm.jimple;

public abstract class AbstractAssignStmt implements Unit{
	
	Local local;
	Operation operation;
	public AbstractAssignStmt(Local local, Operation op){
		this.local=local;
		operation=op;
		local.setType(op.getType());
	}
	
    public String toString(){
		return local.getName()+"  =  "+operation.toString();
	}

}
