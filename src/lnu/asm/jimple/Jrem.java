package lnu.asm.jimple;

public class Jrem extends AbstractBinaryOp{
	
	public Jrem(Value left, Value right) {
		super(left, right);
	}
	
	public static Jrem v(Value left, Value right){
		return new Jrem(left, right);
	}

	
	@Override
	public String getSymbol() {
		return " % ";
	}

}
