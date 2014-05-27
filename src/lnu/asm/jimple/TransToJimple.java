package lnu.asm.jimple;



import java.io.File;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import lnu.asm.ASM;
import lnu.asm.jimple.entities.JClass;
import lnu.asm.jimple.util.JimClassVisitor;
import lnu.asm.printer.ClassPrinter;
import lnu.vps.VpsSpec;


public class TransToJimple {
	
	private final VpsSpec vps;
	
	public TransToJimple(VpsSpec vps)  {
		this.vps = vps;
		/* Setup application filter */
		Scene.v().setup_Filter(vps);
		setup_ASM();
		setup_applications();
	}

	private void setup_ASM() {
		/* Setup Class Path */
		String[] paths = vps.classPaths;
		System.out
				.println("***///////////////////////////////////////////////////////////////////////////////***");
		System.out.println("*** ASM Class Paths ***");
		for (String path : paths) {
			System.out.println(path);
			ASM.addPath(path);
		}
		System.out.println();
	}

	private void setup_applications() {
		ClassNode classNode = null;
		List<MethodNode> meths;
		
		// Find/Handle java.lang.Object Class  	
		classNode = ASM.getClassNode("java/lang/Object");
		Scene.v().initialize(classNode);  // Adds clinit to worklist
		
		
		// Find/Handle java.lang.System Class  	
		classNode = ASM.getClassNode("java/lang/System");
		Scene.v().initialize(classNode);  // Add clinit to worklist
		
		String[] ePoints = vps.entryPoints;
		for (String main_class : ePoints) {
			main_class = main_class.replace('.', File.separatorChar);
			classNode = ASM.getClassNode(main_class);
			Scene.v().initialize(classNode);
		}

		System.out.println("super class name:  " + classNode.superName);
		JClass jimClass = JClass.getClass(classNode.name);
		JimClassVisitor JCVisitor = new JimClassVisitor(jimClass);
		classNode.accept(JCVisitor);
		ClassPrinter cp = new ClassPrinter();// print test
		classNode.accept(cp);
	}

}
