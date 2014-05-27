package lnu.asm.jimple;

import org.objectweb.asm.Type;

public class Jdiv extends AbstractBinaryOp{
	
	public Jdiv(Value left, Value right) {
		super(left, right);
	}
	
	public static Jdiv v(Value left, Value right){
		return new Jdiv(left, right);
	}

	
	@Override
	public String getSymbol() {
		return "  /  ";
	}

	

}
