package lnu.asm.printer;



import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassPrinter extends ClassVisitor {

	public ClassPrinter() {
		super(Opcodes.ASM4);
		//this.cn=cn;
		//startPrint();
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
		mv=super.visitMethod(access, name, desc, signature, exceptions);
		//System.out.println("TTTTTTTTTTT=====> "+super.visitMethod(access, name, desc,signature, exceptions));
		
		return new PrintMethodVisitor(mv);
	}
	
	public void visitEnd(){
		System.out.println("}");
	}
	
	
}
