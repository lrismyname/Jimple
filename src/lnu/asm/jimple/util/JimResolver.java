package lnu.asm.jimple.util;

import java.util.ArrayList;

import lnu.asm.jimple.entities.JClass;
import lnu.asm.jimple.entities.JMethod;

public class JimResolver {
	
	public static JMethod resolveExplicit(JClass jClass, String signature) {
    	JMethod tgt = null;
		do {
			 tgt = jClass.getMethodBySignature(signature);
			 jClass = jClass.getSuperClass();
		} while(tgt == null);
		return tgt;
    }
	
    public static JMethod resolveInterface(JClass jClass, String signature) {
		// Find first layer of initialized concrete subclasses 
    	JMethod primary;
		ArrayList<JClass> concrete_subs = new ArrayList<JClass>();
		for (JClass subClass : jClass.subClasses) {
			findSubClasses(subClass,concrete_subs);
		}
		//System.err.println(rtaClass+" --> "+concrete_subs);
		
		// Collect targets starting in each class in first layer 
		for (JClass subClass : concrete_subs) {
			
			primary = resolveExplicit(subClass,signature);
			//tgts.add(primary);
			//for (JClass subsub : subClass.subClasses)    // Search in subclasses
	    		//resolveVirtual(subsub,signature,tgts);
		}
		return null;
    }
    
    private static void findSubClasses(JClass rtaClass, ArrayList<JClass> concrete_subs) {   	
    	if (rtaClass.isInterface() || rtaClass.isAbstract()) {
    		for (JClass subClass : rtaClass.subClasses) {
				findSubClasses(subClass,concrete_subs);
			}
    	}
    	else // Concrete class
    		concrete_subs.add(rtaClass);
    }

}
