package lnu.asm.jimple.entities;

import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import soot.SootClass;



public class JMethod {
	
	MethodNode mNode;
	JClass owner;
	private String name;
	public final String signature;
	 /** A list of parameter types taken by this <code>SootMethod</code> object, 
     * in declaration order. */
    private List<Type> parameterTypes;

   /** The return type of this object. */
    private Type returnType;
    private JClass declaringClass;
    private boolean isDeclared;
    private JBody activeBody;

	
	
	public JMethod(MethodNode mn, String name, JClass owner){
		mNode=mn;
		this.name=name;
		this.owner=owner;
		signature=mn.name+mn.desc;
		parameterTypes=Arrays.asList(Type.getArgumentTypes(mn.desc));
		returnType=Type.getReturnType(mn.desc);
	}
   
	public void setBody(JBody body){
		activeBody=body;
	}
	
	public JClass getOwnerClass(){
		return owner;
	}
	
	public void setDeclaringClass(JClass jClass){
		declaringClass=jClass;
	}
	
	public JClass getDeclaringClass(){
		return declaringClass;
	}
	
	public MethodNode getMethodNode(){
		return mNode;
	}
	
	
	public String toString(){
		return activeBody.toString();
	}
	
	public String getSignature(){
		return signature;
	}
	
	public void setRetType(Type ret){
		returnType=ret;
	}
	
	public Type getRetType(){
		return returnType;
	}
	
	public List<Type> getparmTypes(){
		return parameterTypes;
	}

	public void setparmTypes(List<Type> pams){
		parameterTypes = pams;
	}
	
	public JBody getBody(){
		return activeBody;
	}

}
