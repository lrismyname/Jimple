package lnu.asm.jimple.util;


import java.util.HashMap;

import lnu.asm.jimple.VerboseJimple;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class JimClassVisitor extends ClassVisitor{
	protected ClassNode cn;
	protected HashMap<String,MethodNode> methodList=new HashMap<String,MethodNode>();

	public JimClassVisitor() {
		super(Opcodes.ASM4);
	}
	public JimClassVisitor(ClassNode cn) {
		super(Opcodes.ASM4);
		this.cn=cn;
		setMethodMap();
	}
	
	public void startPrint(){
		//cn.accept(this);
		/*PrintMethodVisitor pmv; 
		methodNode = (ArrayList<MethodNode>) cn.methods;
		for(MethodNode mn :methodNode){
			pmv= new PrintMethodVisitor(mn);
			mn.accept(pmv);
		}*/	
	}
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){
		System.out.println(name + " extends " +superName+"{");
	}
	
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value){
		System.out.println("   "+ desc+"  " + name);
		return null;
	}
	
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		System.out.println("Method "+name +"  -----  "+ desc);
		MethodVisitor mv;
		MethodNode mn=methodList.get(name+desc);
		mv=super.visitMethod(access, name, desc, signature, exceptions);
		//System.out.println("TTTTTTTTTTT=====> "+super.visitMethod(access, name, desc,signature, exceptions));
		
		return new VerboseJimple(mv,mn);
	}
	
	public void visitEnd(){
		System.out.println("}");
	}
	
	
	public void setMethodMap(){
		for(MethodNode mn :cn.methods){
			methodList.put(mn.name+mn.desc, mn);
		}	
	}
	
}
