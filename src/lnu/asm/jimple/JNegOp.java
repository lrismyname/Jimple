package lnu.asm.jimple;

import org.objectweb.asm.Type;

public class JNegOp implements Operation{
	
	Operation operation;
	
	public JNegOp(Operation op){
		operation=op;
	}
	
	public static JNegOp v(Operation op){
		return new JNegOp(op);
	}
	
	public String toString(){
		return " Neg("+operation.toString()+")";
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

}
