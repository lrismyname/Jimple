package lnu.asm.jimple;

import org.objectweb.asm.Type;



public abstract class AbstractBinaryOp implements BinaryOp{
	protected Value leftValue;
	protected Value rightValue;
	protected Type tp;
	
	public AbstractBinaryOp(Value left,Value right){
		leftValue=left;
		rightValue=right;
		tp=left.getType();
		// bite , short, int
		/*if(left.getType()==right.getType())
			tp=left.getType();
		else{
			tp=null;
			throw new RuntimeException("Type does not match error");
		}*/
			
	}
	
    public void setLeftValue(Value left){
    	leftValue=left;
    }
	
	public void setRightValue(Value right){
		rightValue=right;
	}
	
	public Value getLeftValue(){
		return leftValue;
	}
	
	public Value getRightValue(){
		return rightValue;
	}
	
	
	public String toString()
    {  
        String leftOp = leftValue.toString(), rightOp = rightValue.toString();

        return leftOp + getSymbol() + rightOp;
    }
	
	public Type getType() {
		
		return tp;
	}
}
