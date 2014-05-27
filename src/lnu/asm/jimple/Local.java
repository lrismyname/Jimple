package lnu.asm.jimple;

import org.objectweb.asm.Type;


public interface Local extends Value 
{
	
	 /** Returns the name of the current Local variable. */
    public String getName();

    /** Sets the name of the current variable. */
    public void setName(String name);

    /** Sets the type of the current variable. */
    public void setType(Type t);

}
