package lnu;

import java.io.IOException;
import java.util.Iterator;

import lnu.asm.jimple.Scene;
import lnu.asm.jimple.TransToJimple;
import lnu.asm.jimple.entities.JClass;
import lnu.asm.jimple.entities.JMethod;
import lnu.vps.VpsParser;
import lnu.vps.VpsSpec;

public class Main {
	public static void main(String[] args) throws IOException {

		/* test files Directory */
		String vps_path = "/Documents and Settings/Administrator/workspace/mysoot/bin/lnu/test/";
		/* Select testing java file */
		String path = vps_path + "vps_files/observer.vps";
		// InputStream
		// in=Main.class.getResourceAsStream("lnu/test/Arithmetic.class");
		VpsSpec vps = VpsParser.parseFile(path);
		new TransToJimple(vps);

		System.out.println("*** initialized classes ***");

		System.out.println("*initializedClass size=*"+ Scene.v().nameToClass().size());

		Iterator<JClass> it = Scene.v().getApplicationClasses().iterator();
		while (it.hasNext()) {
			JClass jc = it.next();
			System.out.println("*name=*" + jc.getClassName()
					+ "    *application=* " + jc.isApplication);

			if (jc.getSuperClass() != null) {
				System.out.println("   *Super class name=*"
						+ jc.getSuperClass().getClassName());
			}
			
			for(JMethod jm:jc.getMethodMap().values()){
				System.out.println(jm.signature+"\nAll Methods parmTypes====> "+jm.getparmTypes()+"\t All Methods return type====> "+jm.getRetType());
				if(jm.getBody()!= null)
					System.out.println("\n Test Body====> "+jm.getBody().toString());
			}
		}

		System.exit(-1);

	}

}
