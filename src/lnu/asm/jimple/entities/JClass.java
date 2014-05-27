package lnu.asm.jimple.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;


public class JClass {
	protected final ClassNode classNode;
	protected String name, packageName;
	protected int modifiers;
	protected JClass superClass;
    protected JClass outerClass;
    public boolean isApplication;
    protected boolean isPhantom;
    protected List<JField> fields = new LinkedList<JField>();
    protected HashMap<String,JMethod> sig2method = new HashMap<String,JMethod>(); // sig = name+desc
    protected HashMap<String,JMethod> interfaces = new HashMap<String,JMethod>();
	protected JMethod[] methods ;
	private static final HashMap<String,JClass> name2class = new HashMap<String,JClass>();	
	public final ArrayList<JClass> subClasses = new ArrayList<JClass>();

	public JClass (ClassNode cNode,JClass superClass,boolean isApplication){
		classNode=cNode;
		this.superClass = superClass;
		this.isApplication = isApplication;
		this.name=cNode.name;
		name2class.put(cNode.name, this);
		methods=addMethods();
	}
	
	public boolean isInterface() {return (classNode.access & Opcodes.ACC_INTERFACE) != 0;}
	public boolean isAbstract() {return (classNode.access & Opcodes.ACC_ABSTRACT) != 0;}
	public void addSubClass(JClass sub) { subClasses.add(sub);}
	public static JClass getClass(String className) { 
		JClass clz = name2class.get(className);
		if (clz == null) {
			throw new RuntimeException("Accessing non-initialized class: "+className);
		}
		return clz;
	}
	
	public JMethod getMethodBySignature(String signature) {
		return sig2method.get(signature);
	}
	
	public void setFields(List<JField> f){
		fields=f;
	}
	
	public List<JField> getFields(){
		return fields;
	}
	
	
	public HashMap<String,JMethod> getInterfaces(){
		return interfaces;
	}
	
	
	public ClassNode getClassNode(){
		return classNode;
	}
	
	public HashMap<String,JMethod> getMethodMap(){
		return sig2method;
	}
	
	public String getClassName(){
		return name;
	}
	
	
	public JClass getSuperClass(){
		return superClass;
	}
	
	
	public JMethod[] getMethods(){
		return methods;
	}
	private JMethod[] addMethods() {
		List<MethodNode> meths = classNode.methods;
		JMethod[] methods = new JMethod[meths.size()];
		for (int i=0;i<methods.length;i++) {					
			MethodNode m = meths.get(i);
			methods[i] = new JMethod(m,m.name,this);
			sig2method.put(m.name+m.desc, methods[i]);
		}
		return methods;
	}

}
