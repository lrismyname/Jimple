package lnu.asm.jimple;

import java.util.List;

import lnu.asm.jimple.entities.JClass;

import org.objectweb.asm.Type;

public class JNewRef implements NewRef{
	JClass base;
	Type type;
	
	public JNewRef(Type type,JClass base){
		this.type=type;
		this.base=base;
	}
	
	public static JNewRef v(Type type,JClass base){
		return new JNewRef(type,base);
	}



	@Override
	public Type getType() {
		return type;
	}
	
	@Override
	public List<ValueBox> getUseBoxes() {
		return null;
	}
	
	public String toString(){
		return " new "+ type.getDescriptor();
	}

	@Override
	public JClass getBase() {
		return base;
	}

}
