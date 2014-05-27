package lnu.asm.jimple;

public class Jadd extends AbstractBinaryOp{

	public Jadd(Value left, Value right) {
		super(left, right);
	}
	
	public static Jadd v(Value left, Value right){
		return new Jadd(left, right);
	}

	
	@Override
	public String getSymbol() {
		return "  +  ";
	}


}
