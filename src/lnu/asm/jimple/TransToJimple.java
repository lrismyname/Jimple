package lnu.asm.jimple;


import java.io.FileNotFoundException;
import java.io.IOException;

public class TransToJimple {
	
	public TransToJimple(String path) throws FileNotFoundException, IOException{
		
		new ByteCodeVisitor(path);
	}
	

}
