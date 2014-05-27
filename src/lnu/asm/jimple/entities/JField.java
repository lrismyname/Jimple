package lnu.asm.jimple.entities;

import org.objectweb.asm.Type;


public class JField {
	
	String name;
    Type type;
    int modifier;
    
    public JField(String name, Type type, int modifier){
    	this.name=name;
    	this.type=type;
    	this.modifier=modifier;
    }

}
