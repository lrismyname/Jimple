package lnu.asm.jimple;

import java.util.List;

import lnu.asm.jimple.entities.JClass;
import lnu.asm.jimple.entities.JMethod;

import org.objectweb.asm.Type;

public class JMethodRef implements MethodRef{

	private Type retType;
	private JClass owner;
    private JMethod jMehtod;
	private String signature;
	
	public JMethodRef(JMethod jm){
		jMehtod=jm;
		signature=jm.signature;
		owner=jm.getOwnerClass();
		retType=jm.getRetType();
	}
	
	public static JMethodRef v(JMethod jm){
		return new JMethodRef(jm);
	}

	@Override
	public Type getType() {
		return retType;
	}

	@Override
	public JClass ownerClass() {
		return owner;
	}

	@Override
	public JMethod getMethod() {
		return jMehtod;
	}
	
	public void setSignature(String owner) {
		signature="<"+owner+":"+signature+">";
	}

	@Override
	public String getSignature() {
		return signature;
	}
	
	@Override
	public List<ValueBox> getUseBoxes() {
		return null;
	}

	public String toString(){
		return signature.toString();
	}
	

}
