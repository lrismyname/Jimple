package lnu.asm.jimple;


public class Jmul extends AbstractBinaryOp{
	
	
	public Jmul(Value left, Value right) {
		super(left, right);
	}
	
	public static Jmul v(Value left, Value right){
		return new Jmul(left, right);
	}

	
	@Override
	public String getSymbol() {
		return "  *  ";
	}


	

}
