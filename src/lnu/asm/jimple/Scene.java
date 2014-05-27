package lnu.asm.jimple;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;

import lnu.asm.ASM;
import lnu.asm.jimple.entities.JClass;
import lnu.vps.VpsSpec;

public class Scene {
	private int init_count = 0;
	private String[] application_filter;
	public static Scene scene=new Scene();;
	private final HashMap<String,JClass> name2class = new HashMap<String,JClass>();
    private LinkedList<JClass> applications = new LinkedList<JClass>();
    private LinkedList<JClass> library = new LinkedList<JClass>();
    
    
    public static Scene v() {
		return scene;
	}
    
    
    public LinkedList<JClass> getApplicationClasses(){
    	return applications;
    }
    
    public HashMap<String,JClass> nameToClass(){
    	return name2class;
    }
    
	
    public void setup_Filter(VpsSpec vps){
    	String[] include = vps.includeStrings;
		application_filter = new String[include.length];
		for (int i = 0; i < include.length; i++) {
			String str = include[i];
			String filter = str.replace('.', '/');
			application_filter[i] = filter;
			System.out.println("application filter :" + str + "\t" + filter
					+ "");
		}
    }
    
	public void initialize(ClassNode cNode) {
		//...............................................
		if(Scene.v().nameToClass().containsKey(cNode.name)){
			return; // Already initialized
		}
		//...............................................

		init_count++;

		String superName = cNode.superName;
		ClassNode superClass = null;
		JClass jSuperClass = null;
		if (superName != null) { // java.lang.Object
			/* Start recursive initialization of super classes */
			superClass = ASM.getClassNode(superName);
			initialize(superClass);
			jSuperClass = JClass.getClass(superName);
		}
		boolean isApplication = isApplication(cNode);
		JClass jClass = new JClass(cNode, jSuperClass, isApplication);
		if (jSuperClass != null)
			jSuperClass.addSubClass(jClass);

		List<String> ifaces = cNode.interfaces; // Initialize super ifaces
		for (String iface : ifaces) {
			// System.out.println(cNode.name +" --> "+iface);
			JClass iClass = initializeInterface(iface);
			iClass.addSubClass(jClass);
		}

		// ..............................................................................
		/*
		 * JMethod clinit = jClass.getMethodBySignature("<clinit>()V"); if
		 * (clinit!=null) worklist.add(clinit);
		 */
		// ..............................................................................
		//....................................
		Scene.v().nameToClass().put(jClass.getClassName(),jClass);
		//....................................
		String access = "";
		if (jClass.isAbstract())
			access = "(abstract)";
		System.out.println(init_count + "\tInitializing " + cNode.name + "\t"
				+ access);
		
		// update application class list
		updateClassList(jClass);

	}

	private JClass initializeInterface(String iface_name) {
		ClassNode iNode = ASM.getClassNode(iface_name);
		if(Scene.v().nameToClass().containsKey(iface_name))
		//if (initialized.contains(iNode))
			return JClass.getClass(iface_name); // Already initialized
		else {
			init_count++;

			List<String> ifaces = iNode.interfaces;
			if (ifaces.size() > 1)
				throw new RuntimeException(
						"Interface extending more than one interface?");
			JClass superClass = null;
			if (ifaces.size() > 0) {
				String superIface = ifaces.get(0);
				superClass = initializeInterface(superIface);
				// System.err.println(iface_name +" --> "+superIface);
			}
			boolean isApplication = isApplication(iNode);
			JClass jClass = new JClass(iNode, superClass, isApplication);
			if (superClass != null) {
				superClass.addSubClass(jClass);
			}
			//......................................
			Scene.v().nameToClass().put(jClass.getClassName(),jClass);
			//......................................

			System.out.println(init_count + "\tInitializing " + iNode.name
					+ "\t(interface)");
			
			updateClassList(jClass);
			
			return jClass;
		}
	}

	private boolean isApplication(ClassNode cNode) {
		String className = cNode.name;
		for (String filter : application_filter) {
			if (className.startsWith(filter))
				return true;
		}
		return false;
	}
	
	private void updateClassList(JClass jCalss){
		if(jCalss.isApplication) applications.add(jCalss);
		else library.add(jCalss);
	}
		
}
