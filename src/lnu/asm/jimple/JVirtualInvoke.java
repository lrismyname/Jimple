package lnu.asm.jimple;

import java.util.List;

public class JVirtualInvoke extends AbstractInstanceInvoke{
	
	public JVirtualInvoke(Value base,JMethodRef methRef, List<Value> args) {
		super(base, methRef,args);
	}
	
	public static JVirtualInvoke v(Value base,JMethodRef methRef, List<Value> args){
		return new JVirtualInvoke(base,methRef,args);
	}
	
	public String toString(){
        StringBuffer buffer = new StringBuffer();

        buffer.append("Virtualinvoke" + " " + base.toString() +
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
