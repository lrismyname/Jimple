package lnu.asm.jimple;

import org.objectweb.asm.Type;

public class Jsub extends AbstractBinaryOp{
	
	public Jsub(Value left, Value right) {
		super(left, right);
	}
	
	public static Jsub v(Value left, Value right){
		return new Jsub(left, right);
	}

	
	@Override
	public String getSymbol() {
		return "  -  ";
	}
	

}
