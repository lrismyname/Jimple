package lnu.asm.jimple;

public class JCondition extends AbstractBinaryOp{
	String symbol;

	public JCondition(Value left, Value right) {
		super(left, right);
	}
	
	public static JCondition v(Value left, Value right){
		return new JCondition(left, right);
	}
	
	
	public void setSymbol(String sb){
		symbol=sb;
	}
	@Override
	public String getSymbol() {
		return symbol;
	}

}
