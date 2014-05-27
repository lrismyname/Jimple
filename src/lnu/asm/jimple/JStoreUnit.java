package lnu.asm.jimple;

public class JStoreUnit extends AbstractAssignStmt{
	
	public JStoreUnit(Local local, Operation op){
		super(local,op);
	}
	
	public static JStoreUnit v(Local local, Operation op){
		return new JStoreUnit(local,op);
	}
	

}
