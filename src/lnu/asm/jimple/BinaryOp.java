package lnu.asm.jimple;

public interface BinaryOp extends Operation{
	
    public void setLeftValue(Value left);
	
	public void setRightValue(Value right);
	
	public Value getLeftValue();
	
	public Value getRightValue();
	
	public String getSymbol();

}
