package lnu.asm.jimple.entities;



import java.util.LinkedList;

import lnu.asm.jimple.Local;
import lnu.asm.jimple.Unit;

import org.objectweb.asm.tree.MethodNode;

import soot.Trap;



public class JBody {
	
	/** The method associated with this Body. */
    protected JMethod method = null;

    /** The chain of locals for this Body. */
    protected LinkedList<Local> localChain = new LinkedList<Local>();

    /** The chain of traps for this Body. */
    protected LinkedList<Trap> trapChain = new LinkedList<Trap>();

    /** The chain of units for this Body. */
    protected LinkedList<Unit> unitChain = new LinkedList<Unit>(new LinkedList<Unit>());

    public JBody(JMethod m)
    {
        this.method = m;
    }
    
    public JMethod getMethod()
    {
        if(method == null)
            throw new RuntimeException("no method associated w/ body");
        return method;
    }
    
    public void setMethod(JMethod method)
    {
        this.method = method;
    }
    
    public int getLocalCount()
    {
        return localChain.size();
    }

    /** Returns a backed chain of the locals declared in this Body. */
    public LinkedList<Local> getLocals() {return localChain;}

    /** Returns a backed view of the traps found in this Body. */
    public LinkedList<Trap> getTraps() {return trapChain;}
    
    public LinkedList<Unit> getUnits()
    {
        return unitChain;
    }
    
    public String toString(){
    	return 		localChain.toString()+"\n"+unitChain.toString();
    }

}
