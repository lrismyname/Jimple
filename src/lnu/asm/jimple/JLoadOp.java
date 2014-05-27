package lnu.asm.jimple;

public class JLoadOp extends AbstractSingleOp{
	
	public JLoadOp(Value v){
		super(v);
	}
	
	public static JLoadOp v(Value v){
		return new JLoadOp(v);
	}

}
