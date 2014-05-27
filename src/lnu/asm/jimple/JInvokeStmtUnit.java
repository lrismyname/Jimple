package lnu.asm.jimple;

public class JInvokeStmtUnit implements InvokeStmt{
	Operation invokeOp;
	
	public JInvokeStmtUnit(Operation op){
		invokeOp=op;
	}
	public static JInvokeStmtUnit v(Operation op){
		return new JInvokeStmtUnit(op);
	}

	@Override
	public Operation getInvokeOp() {
		return invokeOp;
	}
	
	public String toString(){
		return invokeOp.toString();
	}

}
