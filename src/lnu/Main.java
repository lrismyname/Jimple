package lnu;


import java.io.IOException;
import lnu.asm.jimple.TransToJimple;


public class Main {
	public static void main(String[] args) throws IOException {
	
		/* test files Directory */
		String testing_file_path = "/Documents and Settings/Administrator/workspace/Jimple/bin/lnu/test/";	
		/* Select testing java file */
		String path = testing_file_path+"java_files/Arithmetic.class";
		//InputStream in=Main.class.getResourceAsStream("lnu/test/Arithmetic.class");
		System.out.println(path);
		new TransToJimple(path);
		System.exit(-1);
		

	}
	

}
