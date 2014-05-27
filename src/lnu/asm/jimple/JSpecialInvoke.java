package lnu.asm.jimple;
import java.util.List;


public class JSpecialInvoke extends AbstractInstanceInvoke{

	public JSpecialInvoke(Value base,JMethodRef methRef, List<Value> args) {
		super(base, methRef,args);
	}
	
	public static JSpecialInvoke v(Value base,JMethodRef methRef, List<Value> args){
		return new JSpecialInvoke(base,methRef,args);
	}
	
	public String toString(){
        StringBuffer buffer = new StringBuffer();

        buffer.append("specialinvoke" + " " + base.toString() +
        		".<"+methodRef.getSignature() + "(");

        for(int i = 0; i < argValues.size(); i++)
        {
            if(i != 0)
                buffer.append(", ");

            buffer.append(argValues.get(i).toString());
        }

        buffer.append(")");

        return buffer.toString();
    }
	
}
	

 

	
	
	

