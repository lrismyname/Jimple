package lnu.asm.jimple;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import lnu.asm.ASM;
import lnu.asm.printer.ClassPrinter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public class ByteCodeVisitor extends MethodVisitor{
	private final String path;
	

	public ByteCodeVisitor(String path) throws FileNotFoundException, IOException {
		super(Opcodes.ASM4, null);
		this.path=path;
		//setup_ASM();
		//printByteCode();
		
		ClassReader cr= new ClassReader(new FileInputStream(path));
		ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
		ClassPrinter cp=new ClassPrinter(cw);
		cr.accept(cp,0);
	}
	
	private void setup_ASM() {
		ASM.addPath(path);
		System.out.println();
	}
	
	private void printByteCode() {
		ClassNode classNode  = ASM.getClassNode("Arithmetic");
		//new ClassPrinter(classNode);
		//classNode.accept(new ClassPrinter(classNode));
	    //classNode.accept(new ClassPrinter(classNode));
		
	}

}
