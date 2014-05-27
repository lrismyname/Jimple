package lnu.asm.jimple;

public class JNewRefOp extends AbstractSingleOp{
	
	JNewRef newRef;
	
	public JNewRefOp(Value v){
		super(v);
		newRef=(JNewRef)v;
	}
	
	public static JNewRefOp v(Value v){	
		return new JNewRefOp(v);
	}
	
	public JNewRef getNewRef(){
		return newRef;
	}


}
