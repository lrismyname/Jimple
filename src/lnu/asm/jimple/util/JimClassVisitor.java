package lnu.asm.jimple.util;


import java.util.HashMap;

import lnu.asm.jimple.VerboseJimpleBuilder;
import lnu.asm.jimple.entities.JClass;
import lnu.asm.jimple.entities.JField;
import lnu.asm.jimple.entities.JMethod;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class JimClassVisitor extends ClassVisitor{
	JClass jimClass;
	protected HashMap<String,MethodNode> methodList=new HashMap<String,MethodNode>();

	public JimClassVisitor() {
		super(Opcodes.ASM4);
	}
	public JimClassVisitor(JClass jclass) {
		super(Opcodes.ASM4);
		jimClass=jclass;
		setMethodMap();
	}

	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){
	//	System.out.println(name + " extends " +superName+"{");
		System.out.println(name + signature +superName+"{");
	}
	
/*	public void visitSource(String source, String name, String desc){
		
	}
	
	public void visitOuterClass(String owner, String name, String desc){
		
	}
	
	public AnnotationVisitor visitAnnotation(String desc, boolean visible){
		return null;
	}
	
	
	
	public void visitInnerClass(String name, String outerName, String innerName, int access){
		
	}
	*/
	
	public void visitAttribute(Attribute attr){
		System.out.println("Class Attribute: "+attr.type);	
		super.visitAttribute(attr);
	}
	
	
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value){
		jimClass.getFields().add(new JField(name,Type.getType(desc),access));
		System.out.println(" desc:  "+ desc+"  name: " + name+ " value: "+value);
		return null;
	}
	
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		System.out.println("Method "+name +"  --signature--  "+signature+"  --desc--  "+ desc);
		MethodVisitor mv;
		MethodNode mn=methodList.get(name+desc);
		JMethod jimMethod = JimResolver.resolveExplicit(jimClass, name+desc);
		//jimClass.getMethodMap().put(name+desc, jimMethod);
		mv=super.visitMethod(access, name, desc, signature, exceptions);
		return new VerboseJimpleBuilder(mv,jimMethod);
	}
	
	public void visitEnd(){
		System.out.println("}");
	}
	
	
	public void setMethodMap(){
		for(MethodNode mn :jimClass.getClassNode().methods){
			methodList.put(mn.name+mn.desc, mn);
		}	
	}
	
}
