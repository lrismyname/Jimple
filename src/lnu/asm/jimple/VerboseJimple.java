package lnu.asm.jimple;




import java.util.HashMap;
import java.util.Map;

import lnu.asm.ASM;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;

public class VerboseJimple extends MethodVisitor{
	private int maxStack;
	private int maxLocals;
	private StringBuilder buf = new StringBuilder();
	private static String[] OPCODES = org.objectweb.asm.util.Printer.OPCODES;
	private static String[] TYPES = org.objectweb.asm.util.Printer.TYPES;
	private HashMap<Integer,String> localVar ;
	private String opref="op_";
	private int index=-1;
	private String dupValue=null;
	private String outPutCode;
	/**
     * The label names. This map associate String values to Label keys.
     */
    protected Map<Label, String> labelNames;
	
	public VerboseJimple(MethodVisitor mv, MethodNode mn) {
    	super(Opcodes.ASM4, mv);
    	this.maxStack=mn.maxStack;
    	this.maxLocals=mn.maxLocals;
    	setLocalsVar(mn);
	}
	
	public void setLocalsVar(MethodNode mn){
		localVar = new HashMap<Integer,String>();
		for(LocalVariableNode lvn: mn.localVariables){
			//System.out.println(lvn.name+"-------------------"+lvn.index);
			localVar.put(lvn.index, lvn.name);
		}
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		// TODO Auto-generated method stub
		return super.visitAnnotation(desc, visible);
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		// TODO Auto-generated method stub
		return super.visitAnnotationDefault();
	}

	@Override
	public void visitAttribute(Attribute attr) {
		// TODO Auto-generated method stub
		super.visitAttribute(attr);
	}

	@Override
	public void visitCode() {
		super.visitCode();
	}

	@Override
	public void visitEnd() {
		// TODO Auto-generated method stub
		super.visitEnd();
	}

	/**
     * Visits a field instruction. A field instruction is an instruction that
     * loads or stores the value of a field of an object.
     * 
     * @param opcode
     *            the opcode of the type instruction to be visited. This opcode
     *            is either GETSTATIC, PUTSTATIC, GETFIELD or PUTFIELD.
     * @param owner
     *            the internal name of the field's owner class (see
     *            {@link Type#getInternalName() getInternalName}).
     * @param name
     *            the field's name.
     * @param desc
     *            the field's descriptor (see {@link Type Type}).
     */
	// Visits a field instruction.
	@Override
	public void visitFieldInsn(int opcode, String owner, String name,
			String desc) {
		String opc=OPCODES[opcode];
		if(opc.equals("GETSTATIC")){
			outPutCode=opref+(index+1)+"= "+owner+"."+name;
			index++;
		}
		System.out.println(outPutCode);
	}

	
	//Visits the current state of the local variables and operand stack elements.
	@Override
	public void visitFrame(int type, int nLocal, Object[] local, int nStack,
			Object[] stack) {
		// TODO Auto-generated method stub
		super.visitFrame(type, nLocal, local, nStack, stack);
	}
	
	/**
     * Visits an IINC instruction.
     * 
     * @param var
     *            index of the local variable to be incremented.
     * @param increment
     *            amount to increment the local variable by.
     */

	@Override
	public void visitIincInsn(int var, int increment) {
		//iload
		outPutCode=opref+(index+1)+"="+localVar.get(var);
		index++;
		System.out.println(outPutCode);
		//ICONST
		outPutCode=opref+(index+1)+"="+increment;
		index++;
		System.out.println(outPutCode);
		//iadd
		outPutCode=opref+(index-1)+"="+opref+(index-1)+" + "+opref+index;
		index--;
		System.out.println(outPutCode);
		//istore
		outPutCode=localVar.get(var)+"="+opref+index;
		index--;
		System.out.println(outPutCode);
		
	
	}
	
	
	
	/**
     * Visits a zero operand instruction.
     * 
     * @param opcode
     *            the opcode of the instruction to be visited. This opcode is
     *            either NOP, ACONST_NULL, ICONST_M1, ICONST_0, ICONST_1,
     *            ICONST_2, ICONST_3, ICONST_4, ICONST_5, LCONST_0, LCONST_1,
     *            FCONST_0, FCONST_1, FCONST_2, DCONST_0, DCONST_1, IALOAD,
     *            LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD, SALOAD,
     *            IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE, CASTORE,
     *            SASTORE, POP, POP2, DUP, DUP_X1, DUP_X2, DUP2, DUP2_X1,
     *            DUP2_X2, SWAP, IADD, LADD, FADD, DADD, ISUB, LSUB, FSUB, DSUB,
     *            IMUL, LMUL, FMUL, DMUL, IDIV, LDIV, FDIV, DDIV, IREM, LREM,
     *            FREM, DREM, INEG, LNEG, FNEG, DNEG, ISHL, LSHL, ISHR, LSHR,
     *            IUSHR, LUSHR, IAND, LAND, IOR, LOR, IXOR, LXOR, I2L, I2F, I2D,
     *            L2I, L2F, L2D, F2I, F2L, F2D, D2I, D2L, D2F, I2B, I2C, I2S,
     *            LCMP, FCMPL, FCMPG, DCMPL, DCMPG, IRETURN, LRETURN, FRETURN,
     *            DRETURN, ARETURN, RETURN, ARRAYLENGTH, ATHROW, MONITORENTER,
     *            or MONITOREXIT.
     */
	@Override
	public void visitInsn(int opcode) {
		String opc=OPCODES[opcode];
		if(opc.startsWith("ICONST")||opc.startsWith("LCONST")||opc.startsWith("DCONST")){
			
			if(opc.equals("ICONST_M1")){
				outPutCode=opref+(index+1)+"= "+(-1);
			}else{
				outPutCode=opref+(index+1)+"= "+opc.charAt(opc.length()-1);
			}
			index++;
		}else if(opc.equals("IADD")){
			outPutCode=opref+(index-1)+"= "+opref+(index-1)+" + "+opref+index;
			index--;
		}else if(opc.equals("ISUB")){
			outPutCode=opref+(index-1)+"= "+opref+(index-1)+" - "+opref+index;
			index--;
		}else if(opc.equals("IMUL")){
			outPutCode=opref+(index-1)+"= "+opref+(index-1)+" * "+opref+index;
			index--;
		}else if(opc.equals("IDIV")){
			outPutCode=opref+(index-1)+"= "+opref+(index-1)+" / "+opref+index;
			index--;
		}else if(opc.equals("IREM")){
			outPutCode=opref+(index-1)+"= "+opref+(index-1)+" % "+opref+index;
			index--;
		}else if(opc.equals("INEG")){
			outPutCode=opref+index+"= "+"-"+opref+index;
		}else if(opc.equals("ISHL")){
			outPutCode=opref+(index-1)+"= "+opref+(index-1)+" <<= "+opref+index;
			index--;
		}else if(opc.equals("IRETURN")){
			outPutCode="return  "+opref+index;
			index--;
		}else if(opc.equals("DUP")){
			outPutCode=opref+(index+1)+"= "+ dupValue;
			index++;
		}
		System.out.println(outPutCode);
	}
	
	
	/**
     * Visits an instruction with a single int operand.
     * 
     * @param opcode
     *            the opcode of the instruction to be visited. This opcode is
     *            either BIPUSH, SIPUSH or NEWARRAY.
     * @param operand
     *            the operand of the instruction to be visited.<br>
     *            When opcode is BIPUSH, operand value should be between
     *            Byte.MIN_VALUE and Byte.MAX_VALUE.<br>
     *            When opcode is SIPUSH, operand value should be between
     *            Short.MIN_VALUE and Short.MAX_VALUE.<br>
     *            When opcode is NEWARRAY, operand value should be one of
     *            {@link Opcodes#T_BOOLEAN}, {@link Opcodes#T_CHAR},
     *            {@link Opcodes#T_FLOAT}, {@link Opcodes#T_DOUBLE},
     *            {@link Opcodes#T_BYTE}, {@link Opcodes#T_SHORT},
     *            {@link Opcodes#T_INT} or {@link Opcodes#T_LONG}.
     */

	@Override
	public void visitIntInsn(int opcode, int operand) {
		String opc=OPCODES[opcode];
		if(opc.equals("BIPUSH")||opc.equals("SIPUSH")){
			outPutCode=opref+(index+1)+"= "+operand;
			index++;
		}else if(opc.equals("NEWARRAY")){
			outPutCode=opref+index+"= "+TYPES[operand];
		}
		System.out.println(outPutCode);
	}

	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm,
			Object... bsmArgs) {
		// TODO Auto-generated method stub
		super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
	}
	
	
	

	/**
     * Visits a jump instruction. A jump instruction is an instruction that may
     * jump to another instruction.
     * 
     * @param opcode
     *            the opcode of the type instruction to be visited. This opcode
     *            is either IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE, IF_ICMPEQ,
     *            IF_ICMPNE, IF_ICMPLT, IF_ICMPGE, IF_ICMPGT, IF_ICMPLE,
     *            IF_ACMPEQ, IF_ACMPNE, GOTO, JSR, IFNULL or IFNONNULL.
     * @param label
     *            the operand of the instruction to be visited. This operand is
     *            a label that designates the instruction to which the jump
     *            instruction may jump.
     */
	@Override
	public void visitJumpInsn(int opcode, Label label) {
		if(OPCODES[opcode].equals("IFGE")){
			outPutCode=opref+(index+1)+"= 0";
			System.out.println(outPutCode);
			outPutCode="if "+opref+index+" >= "+opref+(index+1)+"  goto "+getLabel(label);
			index--;
		}else if(OPCODES[opcode].equals("IF_ICMPGT")){
			outPutCode="if "+opref+(index-1)+" > "+opref+index+"  goto "+getLabel(label);
			index=index-2;
		}else if(OPCODES[opcode].equals("IF_ICMPLT")){
			outPutCode="if "+opref+(index-1)+" < "+opref+index+"  goto "+getLabel(label);
			index=index-2;
		}else if(OPCODES[opcode].equals("IF_ICMPEQ")){
			outPutCode="if "+opref+(index-1)+" = "+opref+index+"  goto "+getLabel(label);
			index=index-2;
		}
		System.out.println(outPutCode);
	}

	@Override
	public void visitLabel(Label label) {
		// TODO Auto-generated method stub
		super.visitLabel(label);
	}
	
	

	/**
     * Visits a LDC instruction. Note that new constant types may be added in
     * future versions of the Java Virtual Machine. To easily detect new
     * constant types, implementations of this method should check for
     * unexpected constant types, like this:
     * 
     * <pre>
     * if (cst instanceof Integer) {
     *     // ...
     * } else if (cst instanceof Float) {
     *     // ...
     * } else if (cst instanceof Long) {
     *     // ...
     * } else if (cst instanceof Double) {
     *     // ...
     * } else if (cst instanceof String) {
     *     // ...
     * } else if (cst instanceof Type) {
     *     int sort = ((Type) cst).getSort();
     *     if (sort == Type.OBJECT) {
     *         // ...
     *     } else if (sort == Type.ARRAY) {
     *         // ...
     *     } else if (sort == Type.METHOD) {
     *         // ...
     *     } else {
     *         // throw an exception
     *     }
     * } else if (cst instanceof Handle) {
     *     // ...
     * } else {
     *     // throw an exception
     * }
     * </pre>
     * 
     * @param cst
     *            the constant to be loaded on the stack. This parameter must be
     *            a non null {@link Integer}, a {@link Float}, a {@link Long}, a
     *            {@link Double}, a {@link String}, a {@link Type} of OBJECT or
     *            ARRAY sort for <tt>.class</tt> constants, for classes whose
     *            version is 49.0, a {@link Type} of METHOD sort or a
     *            {@link Handle} for MethodType and MethodHandle constants, for
     *            classes whose version is 51.0.
     */
	@Override
	public void visitLdcInsn(Object cst) {
		if (cst instanceof String) {
			outPutCode=opref+(index+1)+"= "+(String)cst;
        } else if (cst instanceof Float) {
        	outPutCode=opref+(index+1)+"= "+(Float)cst;
        } else {
        	outPutCode=opref+(index+1)+"= "+(Integer)cst;
        }
		index++;
		System.out.println(outPutCode);
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		// TODO Auto-generated method stub
		super.visitLineNumber(line, start);
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature,
			Label start, Label end, int index) {
		// TODO Auto-generated method stub
		super.visitLocalVariable(name, desc, signature, start, end, index);
	}

	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		// TODO Auto-generated method stub
		super.visitLookupSwitchInsn(dflt, keys, labels);
	}
	
	
	/**
     * Visits a method instruction. A method instruction is an instruction that
     * invokes a method.
     * 
     * @param opcode
     *            the opcode of the type instruction to be visited. This opcode
     *            is either INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC or
     *            INVOKEINTERFACE.
     * @param owner
     *            the internal name of the method's owner class (see
     *            {@link Type#getInternalName() getInternalName}).
     * @param name
     *            the method's name.
     * @param desc
     *            the method's descriptor (see {@link Type Type}).
     */
	@Override
	public void visitMethodInsn(int opcode, String owner, String name,
			String desc) {
		String opc=OPCODES[opcode];
		if(opc.equals("INVOKEVIRTUAL")){
			//System.out.println(desc);
			if(desc.contains("()")){
				System.out.println(opref+index+"."+name+"()");
				index--;
			}else{
				System.out.println(opref+(index-1)+"."+name+"("+opref+index+")");
				index=index-2;
			}
		}else if(opc.equals("INVOKESPECIAL")){
			//System.out.println(opref+(index-1)+"."+name+"("+opref+index+")");
			index--;
		}
	}
	

	/**
     * Visits a MULTIANEWARRAY instruction.
     * 
     * @param desc
     *            an array type descriptor (see {@link Type Type}).
     * @param dims
     *            number of dimensions of the array to allocate.
     */
	@Override
	public void visitMultiANewArrayInsn(String desc, int dims) {
		System.out.println(opref+index+"= "+desc);
	}

	@Override
	public AnnotationVisitor visitParameterAnnotation(int parameter,
			String desc, boolean visible) {
		// TODO Auto-generated method stub
		return super.visitParameterAnnotation(parameter, desc, visible);
	}

	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt,
			Label... labels) {
		// TODO Auto-generated method stub
		super.visitTableSwitchInsn(min, max, dflt, labels);
	}

	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler,
			String type) {
		// TODO Auto-generated method stub
		super.visitTryCatchBlock(start, end, handler, type);
	}
	
	

	/**
     * Visits a type instruction. A type instruction is an instruction that
     * takes the internal name of a class as parameter.
     * 
     * @param opcode
     *            the opcode of the type instruction to be visited. This opcode
     *            is either NEW, ANEWARRAY, CHECKCAST or INSTANCEOF.
     * @param type
     *            the operand of the instruction to be visited. This operand
     *            must be the internal name of an object or array class (see
     *            {@link Type#getInternalName() getInternalName}).
     */
	@Override
	public void visitTypeInsn(int opcode, String type) {
		String opc=OPCODES[opcode];
		if(opc.equals("ANEWARRAY")){
			outPutCode=opref+index+"= "+type;
		}else if(opc.equals("NEW")){
			outPutCode=opref+(index+1)+"= "+type;	
			dupValue=type;
			index++;
		}
		System.out.println(outPutCode);
	}
	
	
	/**
     * Visits a local variable instruction. A local variable instruction is an
     * instruction that loads or stores the value of a local variable.
     * 
     * @param opcode
     *            the opcode of the local variable instruction to be visited.
     *            This opcode is either ILOAD, LLOAD, FLOAD, DLOAD, ALOAD,
     *            ISTORE, LSTORE, FSTORE, DSTORE, ASTORE or RET.
     * @param var
     *            the operand of the instruction to be visited. This operand is
     *            the index of a local variable.
     */
	@Override
	public void visitVarInsn(int opcode, int var) {
		if(OPCODES[opcode].equals("ISTORE")||OPCODES[opcode].equals("ASTORE")){
			
			outPutCode=localVar.get(var)+"="+opref+index;	
			index--;
		}else if(OPCODES[opcode].equals("ILOAD")||OPCODES[opcode].equals("ALOAD")){
			
			outPutCode=opref+(index+1)+"="+localVar.get(var);
			index++;
		}
		System.out.println(outPutCode);
	}
	
	
	
	/**
     * Appends the name of the given label to {@link #buf buf}. Creates a new
     * label name if the given label does not yet have one.
     * 
     * @param l
     *            a label.
     */
    protected String getLabel(final Label l) {
        if (labelNames == null) {
            labelNames = new HashMap<Label, String>();
        }
        String name = labelNames.get(l);
        if (name == null) {
            name = "Label_" + labelNames.size();
            labelNames.put(l, name);
        }
        return name;
    }
}
