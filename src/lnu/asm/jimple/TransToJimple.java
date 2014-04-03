package lnu.asm.jimple;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import lnu.asm.jimple.util.JimClassVisitor;
import lnu.asm.printer.ClassPrinter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class TransToJimple {
	
	public TransToJimple(String path) throws FileNotFoundException, IOException {
		ClassReader cr= new ClassReader(new FileInputStream(path));
		ClassNode classNode = new ClassNode();
		cr.accept(classNode, ClassReader.EXPAND_FRAMES);
		JimClassVisitor JCVisitor = new JimClassVisitor(classNode);
		cr.accept(JCVisitor,0);
		ClassPrinter cp=new ClassPrinter();
		cr.accept(cp,0);
		
	}
	
	
	

}
