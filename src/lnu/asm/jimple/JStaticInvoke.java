package lnu.asm.jimple;

import java.util.List;

public class JStaticInvoke extends AbstractStaticInvoke{

	public JStaticInvoke(JMethodRef methRef, List<Value> args) {
		super(methRef, args);
	}
	
	public static JStaticInvoke v(JMethodRef methRef, List<Value> args){
		return new JStaticInvoke(methRef,args);
	}
	
	
	public String toString(){
        StringBuffer buffer = new StringBuffer();

        buffer.append("Staticinvoke" + " " + methodRef.getClass().getName() +
        		"."+ methodRef.getSignature() + "(");

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
