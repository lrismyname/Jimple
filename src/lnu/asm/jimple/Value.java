package lnu.asm.jimple;

import java.util.List;

import org.objectweb.asm.Type;


public interface Value {
	
	/** Returns a List of boxes corresponding to Values 
     * which are used by (ie contained within) this Value. */
    public List<ValueBox> getUseBoxes();

    /** Returns the Soot type of this Value. */
    public Type getType();
    

}
