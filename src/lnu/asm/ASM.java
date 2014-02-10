package lnu.asm;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

public class ASM {
	private static final HashSet<String> missing = new HashSet<String>();
	private static final HashMap<String,ClassNode> name2node = new HashMap<String,ClassNode>();
	
	public static ClassNode getClassNode(String className) {
		ClassNode classNode = name2node.get(className);
		if (classNode == null && !missing.contains(className)) {
			String fileName = '/'+className+".class";
			System.out.println(fileName);
			ClassReader cr = null;
			try {
				InputStream in=ASM.class.getResourceAsStream(fileName);
				cr=new ClassReader(in);
			} catch (IOException e) {
				System.err.println("ASM: Can not find binary for "+className);
				missing.add(className);
				throw new RuntimeException();
				//return null;
			}
			classNode = new ClassNode();
			cr.accept(classNode, ClassReader.EXPAND_FRAMES);
			name2node.put(className,classNode);
		}
		return classNode;
	}
	
	public static void addPath(String path) {
		File path_file = new File(path);
		addPath(path_file);
	}
	
	public static void addPath(File path) {
        try {
    		URL url = path.toURI().toURL();
    		URLClassLoader sysloader=(URLClassLoader)ClassLoader.getSystemClassLoader();
            Class sysclass=URLClassLoader.class;
            Method method=sysclass.getDeclaredMethod("addURL", new Class[] {URL.class});
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] {url});
            
//            URL[] urls = sysloader.getURLs();
//            for (URL u : urls) {
//            	System.out.println(u);
//            }
         
        }
        catch (Exception t) {
            t.printStackTrace();
            throw new RuntimeException("Error, could not add URL to system classloader");
        }
        

	}

}
