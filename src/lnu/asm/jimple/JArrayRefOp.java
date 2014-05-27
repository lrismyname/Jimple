package lnu.asm.jimple;

public class JArrayRefOp extends AbstractSingleOp{
	JArrayRef array;
	
	public JArrayRefOp(Value v){
		super(v);
		array=(JArrayRef)v;
	}
	
	public static JArrayRefOp v(Value v){	
		return new JArrayRefOp(v);
	}
	
	public void setBase(Local l){
		array.setBase(l);
	}

}
