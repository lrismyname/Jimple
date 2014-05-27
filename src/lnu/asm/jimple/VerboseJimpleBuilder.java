package lnu.asm.jimple;




import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import lnu.asm.ASM;
import lnu.asm.jimple.entities.JBody;
import lnu.asm.jimple.entities.JClass;
import lnu.asm.jimple.entities.JMethod;
import lnu.asm.jimple.util.JimResolver;

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

import soot.jimple.Stmt;


public class VerboseJimpleBuilder extends MethodVisitor{
	private StringBuilder buf = new StringBuilder();
	private static String[] OPCODES = org.objectweb.asm.util.Printer.OPCODES;
	private HashMap<Integer,Local> localVarMap ;
	private LinkedList<Local> locals ;
	private String opref="op_";
	private int index=-1;
	private String outPutCode;
	private JBody myJimBody;
	protected LocalGenerator lg;  // for generated locals not in orig src
	protected JMethod jimMethod;
	protected int lastVarIndex;  // index != 10
	protected String refLoclIndex;  // index for ref local
	
	
	protected Stack<Operation> OpStack = new Stack<Operation>();
	/**
     * The label names. This map associate String values to Label keys.
     */
    protected Map<Label, String> labelNames;
	
	public VerboseJimpleBuilder(MethodVisitor mv, JMethod jimMethod) {
    	super(Opcodes.ASM4, mv);
    	this.jimMethod=jimMethod;
    	myJimBody=new JBody(jimMethod);
    	lg = new LocalGenerator(myJimBody);
    	setLocalsVar(jimMethod.getMethodNode(), myJimBody);
	}
	
	public void setLocalsVar(MethodNode mn, JBody jb){
		int localssize=mn.localVariables.size();
		locals = jb.getLocals();
		localVarMap=new HashMap<Integer,Local>();
		lastVarIndex=localssize==11 ? 11:localssize-1;
		System.out.println("lastVarIndex -------------------"+lastVarIndex);
		for(LocalVariableNode lvn: mn.localVariables){
			Local tempLocal=lg.generateLocal(lvn.desc);
			localVarMap.put(lvn.index, tempLocal);
			locals.add(tempLocal);
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
		System.out.println("------------------Begin-------------------");
		System.out.println(myJimBody.getLocals().toString());
		System.out.println(myJimBody.getUnits().toString());
		System.out.println("------------------End-------------------");
		
		jimMethod.setBody(myJimBody);
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

	/**
     * Visits the current state of the local variables and operand stack
     * elements. This method must(*) be called <i>just before</i> any
     * instruction <b>i</b> that follows an unconditional branch instruction
     * such as GOTO or THROW, that is the target of a jump instruction, or that
     * starts an exception handler block. The visited types must describe the
     * values of the local variables and of the operand stack elements <i>just
     * before</i> <b>i</b> is executed.<br>
     * <br>
     * (*) this is mandatory only for classes whose version is greater than or
     * equal to {@link Opcodes#V1_6 V1_6}. <br>
     * <br>
     * The frames of a method must be given either in expanded form, or in
     * compressed form (all frames must use the same format, i.e. you must not
     * mix expanded and compressed frames within a single method):
     * <ul>
     * <li>In expanded form, all frames must have the F_NEW type.</li>
     * <li>In compressed form, frames are basically "deltas" from the state of
     * the previous frame:
     * <ul>
     * <li>{@link Opcodes#F_SAME} representing frame with exactly the same
     * locals as the previous frame and with the empty stack.</li>
     * <li>{@link Opcodes#F_SAME1} representing frame with exactly the same
     * locals as the previous frame and with single value on the stack (
     * <code>nStack</code> is 1 and <code>stack[0]</code> contains value for the
     * type of the stack item).</li>
     * <li>{@link Opcodes#F_APPEND} representing frame with current locals are
     * the same as the locals in the previous frame, except that additional
     * locals are defined (<code>nLocal</code> is 1, 2 or 3 and
     * <code>local</code> elements contains values representing added types).</li>
     * <li>{@link Opcodes#F_CHOP} representing frame with current locals are the
     * same as the locals in the previous frame, except that the last 1-3 locals
     * are absent and with the empty stack (<code>nLocals</code> is 1, 2 or 3).</li>
     * <li>{@link Opcodes#F_FULL} representing complete frame data.</li></li>
     * </ul>
     * </ul> <br>
     * In both cases the first frame, corresponding to the method's parameters
     * and access flags, is implicit and must not be visited. Also, it is
     * illegal to visit two or more frames for the same code location (i.e., at
     * least one instruction must be visited between two calls to visitFrame).
     * 
     * @param type
     *            the type of this stack map frame. Must be
     *            {@link Opcodes#F_NEW} for expanded frames, or
     *            {@link Opcodes#F_FULL}, {@link Opcodes#F_APPEND},
     *            {@link Opcodes#F_CHOP}, {@link Opcodes#F_SAME} or
     *            {@link Opcodes#F_APPEND}, {@link Opcodes#F_SAME1} for
     *            compressed frames.
     * @param nLocal
     *            the number of local variables in the visited frame.
     * @param local
     *            the local variable types in this frame. This array must not be
     *            modified. Primitive types are represented by
     *            {@link Opcodes#TOP}, {@link Opcodes#INTEGER},
     *            {@link Opcodes#FLOAT}, {@link Opcodes#LONG},
     *            {@link Opcodes#DOUBLE},{@link Opcodes#NULL} or
     *            {@link Opcodes#UNINITIALIZED_THIS} (long and double are
     *            represented by a single element). Reference types are
     *            represented by String objects (representing internal names),
     *            and uninitialized types by Label objects (this label
     *            designates the NEW instruction that created this uninitialized
     *            value).
     * @param nStack
     *            the number of operand stack elements in the visited frame.
     * @param stack
     *            the operand stack types in this frame. This array must not be
     *            modified. Its content has the same format as the "local"
     *            array.
     * @throws IllegalStateException
     *             if a frame is visited just after another one, without any
     *             instruction between the two (unless this frame is a
     *             Opcodes#F_SAME frame, in which case it is silently ignored).
     */
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
		OpStack.push(getLoadOp(var));
		//ICONST
		OpStack.push(getIIncConstant(increment));
		//iadd
		pushArithmeticOp("ADD");
		//istore
		getIIncStore(var,OpStack.pop());
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
		
		//====================================================================
		
		if(opc.contains("CONST")){
			OpStack.push(getConstOp(opc));
			//System.out.println("locals----------------->"+locals.toString());
		}else if(opc.contains("ADD")){
			pushArithmeticOp("ADD");
		}else if(opc.contains("SUB")){
			pushArithmeticOp("SUB");
		}else if(opc.contains("MUL")){
			pushArithmeticOp("MUL");
		}else if(opc.contains("DIV")){
			pushArithmeticOp("DIV");
		}else if(opc.contains("REM")){
			pushArithmeticOp("REM");
		}else if(opc.contains("NEG")){
			negateValue(opc);
		}else if(opc.contains("DUP")){
			dupStackOperation(opc);
		}else if(opc.contains("POP")){
			popStackOperation(opc);
		}else if (opc.contains("SWAP")){
			swapStackOperation();
		}
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
		if(opc.equals("BIPUSH")){
			OpStack.push(JLoadOp.v(ByteConstant.v(operand)));
			
		}else if(opc.equals("SIPUSH")){
			OpStack.push(JLoadOp.v(ShortConstant.v(operand)));
		}else if(opc.equals("NEWARRAY")){
			//  ......................................NEWARRAY............................
			SingleOp op=(SingleOp) OpStack.pop();
			OpStack.push(JArrayRefOp.v(JArrayRef.v(op.getValue(),getArrayType(operand))));
		}
	}
	
    /**
     * Visits an invokedynamic instruction.
     * 
     * @param name
     *            the method's name.
     * @param desc
     *            the method's descriptor (see {@link Type Type}).
     * @param bsm
     *            the bootstrap method.
     * @param bsmArgs
     *            the bootstrap method constant arguments. Each argument must be
     *            an {@link Integer}, {@link Float}, {@link Long},
     *            {@link Double}, {@link String}, {@link Type} or {@link Handle}
     *            value. This method is allowed to modify the content of the
     *            array so a caller should expect that this array may change.
     */
	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm,
			Object... bsmArgs) {
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
		String opc=OPCODES[opcode];
		
		if(opc.equals("GOTO")){
			myJimBody.getUnits().add(JGotoUnit.v(label));
		}else if(opc.equals("JSR")){
			// It is used to implement Java's finally clause
		}else if(opc.equals("IFNULL")){
			// object == null
		}else if(opc.equals("IFNONNULL")){
			// object != null
		}else {
			storeJumpUnit(opc,label);
		}
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
		/*if (cst instanceof String) {
			outPutCode=opref+(index+1)+"= "+(String)cst;
        } else if (cst instanceof Float) {
        	outPutCode=opref+(index+1)+"= "+(Float)cst;
        } else {
        	outPutCode=opref+(index+1)+"= "+(Long)cst;
        }
		index++;
		System.out.println(outPutCode);*/
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
		List<Value> args;
		
		System.out.println(opc+"----------------------------------\t"+owner+" name \t"+name+" desc\t"+desc);
		
		if(opc.equals("INVOKESPECIAL")){
			//---------------------------------------
    		if (Scene.v().nameToClass().containsKey(owner)) {
    			args=getParameterList(desc);
    			SingleOp base=(SingleOp)OpStack.pop();
    			JClass jClass=JClass.getClass(owner);
    			JMethodRef jmRef= JMethodRef.v(JimResolver.resolveExplicit(jClass, name+desc));
    			jmRef.setSignature(owner);
    			myJimBody.getUnits().add(JInvokeStmtUnit.v(JSpecialInvoke.v(base.getValue(), jmRef,args)));  
    		}
    		else {
    			System.out.println("No target for INVOKESPECIAL\t"+owner+"\t"+name+"\t"+desc);
    		}
		}else if(opc.equals("INVOKEVIRTUAL")){
			//.................................................problems...............
	 		if (Scene.v().nameToClass().containsKey(owner)) {
	 			args=getParameterList(desc);
    			SingleOp base=(SingleOp)OpStack.pop();
    			System.out.println(base.getValue().toString()+"  ppppppppppppppppppppppppppppppp Base \t"+base.getType());
    			JClass jClass=JClass.getClass(owner);
    			JMethodRef jmRef= JMethodRef.v(JimResolver.resolveExplicit(jClass, name+desc));
    			jmRef.setSignature(owner);
    			//-------------------------------------- in case of pop
    			OpStack.push(JVirtualInvoke.v(base.getValue(), jmRef,args ));
    			//--------------------------------------
    			if(jmRef.getType().equals(Type.VOID_TYPE)){
    				myJimBody.getUnits().add(JInvokeStmtUnit.v(OpStack.peek()));
    			}  
    		}
    		else {
    			System.out.println("No target for INVOKESPECIAL\t"+owner+"\t"+name+"\t"+desc);
    		}
			 
		}else if(opc.equals("INVOKESTATIC")){
			
		}else if(opc.equals("INVOKEINTERFACE")){
			/*args=getParameterList(desc);
			SingleOp base=(SingleOp)OpStack.pop();
			System.out.println(base.getValue().toString()+"  ppppppppppppppppppppppppppppppp Base \t"+base.getType());
*/
			
			if (Scene.v().nameToClass().containsKey(owner)) {
				args=getParameterList(desc);
    			SingleOp base=(SingleOp)OpStack.pop();
    			JClass jClass=JClass.getClass(base.getType().getInternalName());
    			JMethodRef jmRef= JMethodRef.v(JimResolver.resolveExplicit(jClass, name+desc));
    			jmRef.setSignature(owner);
    			
    			//-------------------------------------- in case of pop
    			OpStack.push(JInterfaceInvoke.v(base.getValue(), jmRef,args ));
    			//--------------------------------------
    			if(jmRef.getType().equals(Type.VOID_TYPE)){
        			//System.out.println(" llllllllllllll ppppppppppppppppppppppppppppppp void\t");
    				myJimBody.getUnits().add(JInvokeStmtUnit.v(OpStack.peek()));
    			}
				
    			//System.out.println(base.getValue().toString()+"  ppppppppppppppppppppppppppppppp Base \t"+base.getType());
			} else {
    			System.out.println("No target for INVOKEINTERFACE\t"+owner+"\t"+name+"\t"+desc);
    		}		
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

	
    /**
     * Visits an annotation of a parameter this method.
     * 
     * @param parameter
     *            the parameter index.
     * @param desc
     *            the class descriptor of the annotation class.
     * @param visible
     *            <tt>true</tt> if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or <tt>null</tt> if
     *         this visitor is not interested in visiting this annotation.
     */
	@Override
	public AnnotationVisitor visitParameterAnnotation(int parameter,
			String desc, boolean visible) {
		return super.visitParameterAnnotation(parameter, desc, visible);
	}

	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt,
			Label... labels) {
		super.visitTableSwitchInsn(min, max, dflt, labels);
	}

	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler,
			String type) {
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
		if(opc.equals("NEW")){
			ClassNode tgtClass = ASM.getClassNode(type);
			Scene.v().initialize(tgtClass);
			System.out.println("                new=====================================>>>>>"+type);
			OpStack.push(getStackRefOp(JNewRefOp.v(JNewRef.v(Type.getObjectType(type),JClass.getClass(type)))));
		}
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
		//==========================================
		
		String opc=OPCODES[opcode];
		if(opc.contains("LOAD")){
			OpStack.push(getLoadOp(var));
		}else if(opc.contains("STORE")){
			myJimBody.getUnits().add(getStoreStemt(var,OpStack.pop()));	
		}else {
			// Ret = return;
		}
		
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
    
    
    private Operation getConstOp(String opcode){
    	Constant constant;
    	if(opcode.startsWith("I")){
			if(opcode.equals("ICONST_M1"))
				constant=IntConstant.v(-1);
			else 
				constant=IntConstant.v((int)Character.getNumericValue(opcode.charAt(7)));
		}else if(opcode.startsWith("L")){
			constant=LongConstant.v((long)Character.getNumericValue(opcode.charAt(7)));
		}else if(opcode.startsWith("F")){
			constant=FloatConstant.v((float)Character.getNumericValue(opcode.charAt(7)));
		}else if(opcode.startsWith("D")){
			constant=DoubleConstant.v((double)Character.getNumericValue(opcode.charAt(7)));
		}else{
			// AConstNull
			constant=NullConstant.v();
		}
    	return JLoadOp.v(constant);
    }
    
    private Operation getIIncConstant(int i){
    	return JLoadOp.v(IntConstant.v(i));
    }
    
    
    private void getIIncStore(int varindex,Operation op  ){
    	Local local;
    	local=lg.generateLocal(op.getType().toString());
    	local.setName("$"+local.getName());
    	localVarMap.put(varindex, local);
    	myJimBody.getLocals().add(local);
    	myJimBody.getUnits().add(JStoreUnit.v(local,op));
    }
    
    
    private Operation getLoadOp(int varIndex){
    	return JLoadOp.v(localVarMap.get(varIndex));
    }
    
    private Unit getStoreStemt(int varIndex,Operation op ){
    	Local local;
    	//System.out.println("varIndex -------------------"+varIndex);
    	if(varIndex>lastVarIndex && ! localVarMap.containsKey(varIndex) ){
    		
    		local=lg.generateLocal(op.getType().toString());
    		myJimBody.getLocals().add(local);
    		localVarMap.put(varIndex, local);
    		lastVarIndex=varIndex;
    	}else {
    		local=localVarMap.get(varIndex);
    		//System.out.println("getStoreStemt -------------------"+op.getType());
    	}
    		
    	
    	// array ref set base
    	if(op instanceof JArrayRefOp) ((JArrayRefOp) op).setBase(local);
    	/*if(local.getType().toString().startsWith("[")){
    	}*/
    	System.out.println("Local ------->  "+local.getType().toString()+"----------------op---->"+op.getType().toString());
    	return JStoreUnit.v(local,op);
    }
    
    private Operation getStackRefOp(Operation op ){
    	Local local;
    	System.out.println("           getStackRefOp =====================================>>>>>"+op.toString());
    	local=lg.generateLocal(op.getType().toString());
    	local.setName("$"+local.getName());
    	myJimBody.getLocals().add(local);
    	myJimBody.getUnits().add(JStoreUnit.v(local,op));
    	
    	return JLoadOp.v(local);
    }
    
    private void pushArithmeticOp(String opc ){
    	SingleOp op1=OpStack.peek() instanceof SingleOp ? (SingleOp) OpStack.pop() : (SingleOp) getStackRefOp(OpStack.pop());
		SingleOp op2=OpStack.peek() instanceof SingleOp ? (SingleOp) OpStack.pop() : (SingleOp) getStackRefOp(OpStack.pop());;
		Value leftValue=op2.getValue();
		Value rightValue=op1.getValue();
		Operation op;
		 if(opc.equals("ADD")){
			 op=Jadd.v(leftValue, rightValue);
		 }else if(opc.equals("SUB")){
			 op=Jsub.v(leftValue, rightValue);
	 	 }else if(opc.equals("MUL")){
	 		op=Jmul.v(leftValue, rightValue);	
	 	 }else if(opc.equals("DIV")){
	 		op=Jdiv.v(leftValue, rightValue);
	 	 }else if(opc.equals("REM")){
		 		op=Jrem.v(leftValue, rightValue);
		 }else{
	 		throw new RuntimeException("pushArithmeticOp error");
	 	 }  
		 OpStack.push(op);
		 
		 
    }
    
    private Type getArrayType(int operand){
    	Type type;
		switch(operand){
		case Opcodes.T_BOOLEAN:{
			type=Type.getType("[Z");
			break;
		}	
		case Opcodes.T_CHAR:{
			type=Type.getType("[C");
			break;
		}	
		case Opcodes.T_FLOAT:{
			type=Type.getType("[F");
			break;
		}	
		case Opcodes.T_DOUBLE:{
			type=Type.getType("[D");
			break;
		}	
		case Opcodes.T_BYTE:{
			type=Type.getType("[B");
			break;
		}	
		case Opcodes.T_SHORT:{
			type=Type.getType("[S");
			break;
		}	
		case Opcodes.T_INT:{
			type=Type.getType("[I");
			break;
		}	
		case Opcodes.T_LONG:{
			type=Type.getType("[J");
			break;
		}
		default: type=null;
		}
		return type;
    }
    
    private void dupStackOperation(String opc){
    	Operation op1st,op2nd,op3d;
    	if(opc.equals("DUP")){
    		OpStack.push(OpStack.peek());
    	}else if(opc.equals("DUP_X1")){
    		op1st=OpStack.pop();
    		op2nd=OpStack.pop();
    		OpStack.push(op1st);
    		OpStack.push(op2nd);
    		OpStack.push(op1st);
    	}else if(opc.equals("DUP_X2")){
    		op1st=OpStack.pop();
    		op2nd=OpStack.pop();
    		op3d=OpStack.pop();
    		OpStack.push(op1st);
    		OpStack.push(op3d);
    		OpStack.push(op2nd);
    		OpStack.push(op1st);	
    	}else if(opc.equals("DUP2")){
    		op1st=OpStack.pop();
    		op2nd=OpStack.pop();
    		OpStack.push(op2nd);
    		OpStack.push(op1st);
    		OpStack.push(op2nd);
    		OpStack.push(op1st);
    	}else if(opc.equals("DUP2_X1")){
    		op1st=OpStack.pop();
    		op2nd=OpStack.pop();
    		op3d=OpStack.pop();
    		OpStack.push(op2nd);
    		OpStack.push(op1st);
    		OpStack.push(op3d);
    		OpStack.push(op2nd);
    		OpStack.push(op1st);
    	}else if(opc.equals("DUP2_X2")){
    		Operation op4th;
    		op1st=OpStack.pop();
    		op2nd=OpStack.pop();
    		op3d=OpStack.pop();
    		op4th=OpStack.pop();
    		OpStack.push(op2nd);
    		OpStack.push(op1st);
    		OpStack.push(op4th);
    		OpStack.push(op3d);
    		OpStack.push(op2nd);
    		OpStack.push(op1st);
    	}
    }
    
    private void storeJumpUnit(String opc,Label label){
    	
    	Operation condition=null;
    	SingleOp op1=(SingleOp)OpStack.pop();
    	SingleOp op2;
    	Value leftValue;
    	Value rightValue;
    	if(opc.length()>4){
    		op2=(SingleOp)OpStack.pop();
    		leftValue=op2.getValue();
    		rightValue=op1.getValue();
    	}else{
    		leftValue=op1.getValue();
    		rightValue=IntConstant.v(0);	
    	}
    	
    	condition=JCondition.v(leftValue, rightValue);
    	
    	if(opc.equals("IFEQ") || opc.equals("IF_ICMPEQ")){	
    		((JCondition)condition).setSymbol(" == ");	
    	}else if(opc.equals("IFNE") || opc.equals("IF_ICMPNE")){
    		((JCondition)condition).setSymbol(" != ");	
    	}else if(opc.equals("IFLT") || opc.equals("IF_ICMPLT")){
    		((JCondition)condition).setSymbol(" < ");	
    	}else if(opc.equals("IFGE") || opc.equals("IF_ICMPGE")){
    		((JCondition)condition).setSymbol(" >= ");	
    	}else if(opc.equals("IFGT") || opc.equals("IF_ICMPGT")){
    		((JCondition)condition).setSymbol(" > ");	
    	}else if(opc.equals("IFLE") || opc.equals("IF_ICMPLE")){
    		((JCondition)condition).setSymbol(" <= ");	
    	}else if(opc.equals("IF_ACMPEQ")){
			// object == object
		}else if(opc.equals("IF_ACMPNE")){
			// object != object
		}
    	
    	myJimBody.getUnits().add(JIfUnit.v(condition,label));
    	
    }
    
    private void popStackOperation(String opc) {
    	Type type=OpStack.peek().getType();
		if(opc.equals("POP2") && !(type.equals(Type.DOUBLE_TYPE)|| type.equals(Type.LONG_TYPE))) OpStack.pop();;
		OpStack.pop();
	}
    
    private void swapStackOperation() {
    	if(OpStack.size()>1){
    		Operation op1=OpStack.pop();
        	Operation op2=OpStack.pop();
        	OpStack.push(op1);
        	OpStack.push(op2);
    	}else throw new RuntimeException("swapStackOperation error");

	}
    
    private void negateValue(String opc) {
		Operation op=(SingleOp)OpStack.pop();
		OpStack.push(JNegOp.v(op));
	}
    
    public List<Value> getParameterList(String desc){

    	List<Value> args=new ArrayList<Value>();;
    	//"(" index is 0
    	int end = desc.indexOf(")");
    	if(end>1){
    		int length = end-1;
    		SingleOp op;
    		for(int i=0;i<length;i++){
    			op=(SingleOp)OpStack.pop();
        		args.add(op.getValue());
    		}
    	}else{
    		Void vd=Void.v();
    		args.add(vd);
    	}
    	return args;
    }
    

    
    
}
