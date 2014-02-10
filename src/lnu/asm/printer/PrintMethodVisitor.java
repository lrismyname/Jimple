package lnu.asm.printer;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.util.TraceSignatureVisitor;

public class PrintMethodVisitor extends MethodVisitor {

	private StringBuilder buf = new StringBuilder();
	private static String[] OPCODES = org.objectweb.asm.util.Printer.OPCODES;
	private static String[] TYPES = org.objectweb.asm.util.Printer.TYPES;
	
    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for internal
     * type names in bytecode notation.
     */
    public static final int INTERNAL_NAME = 0;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for field
     * descriptors, formatted in bytecode notation
     */
    public static final int FIELD_DESCRIPTOR = 1;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for field
     * signatures, formatted in bytecode notation
     */
    public static final int FIELD_SIGNATURE = 2;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for method
     * descriptors, formatted in bytecode notation
     */
    public static final int METHOD_DESCRIPTOR = 3;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for method
     * signatures, formatted in bytecode notation
     */
    public static final int METHOD_SIGNATURE = 4;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for class
     * signatures, formatted in bytecode notation
     */
    public static final int CLASS_SIGNATURE = 5;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for field or
     * method return value signatures, formatted in default Java notation
     * (non-bytecode)
     */
    public static final int TYPE_DECLARATION = 6;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for class
     * signatures, formatted in default Java notation (non-bytecode)
     */
    public static final int CLASS_DECLARATION = 7;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for method
     * parameter signatures, formatted in default Java notation (non-bytecode)
     */
    public static final int PARAMETERS_DECLARATION = 8;

    /**
     * Constant used in {@link #appendDescriptor appendDescriptor} for handle
     * descriptors, formatted in bytecode notation
     */
    public static final int HANDLE_DESCRIPTOR = 9;
	
    /**
     * Tab for class members.
     */
    protected String tab = "  ";

    /**
     * Tab for bytecode instructions.
     */
    protected String tab2 = "    ";

    /**
     * Tab for table and lookup switch instructions.
     */
    protected String tab3 = "      ";

    /**
     * Tab for labels.
     */
    protected String ltab = "   ";

    /**
     * The label names. This map associate String values to Label keys.
     */
    protected Map<Label, String> labelNames;

	
    /**
     * Constructs a new {@link MethodVisitor}.
     * 
     * @param api
     *            the ASM API version implemented by this visitor. Must be one
     *            of {@link Opcodes#ASM4}.
     */
    public PrintMethodVisitor() {
        super(Opcodes.ASM4, null);
    }

    /**
     * Constructs a new {@link MethodVisitor}.
     * 
     * @param api
     *            the ASM API version implemented by this visitor. Must be one
     *            of {@link Opcodes#ASM4}.
     * @param mv
     *            the method visitor to which this visitor must delegate method
     *            calls. May be null.
     */
    public PrintMethodVisitor(final MethodVisitor mv) {
    	super(Opcodes.ASM4, mv);
    }
    
    // -------------------------------------------------------------------------
    // Annotations and non standard attributes
    // -------------------------------------------------------------------------

    /**
     * Visits the default value of this annotation interface method.
     * 
     * @return a visitor to the visit the actual default value of this
     *         annotation interface method, or <tt>null</tt> if this visitor is
     *         not interested in visiting this default value. The 'name'
     *         parameters passed to the methods of this annotation visitor are
     *         ignored. Moreover, exacly one visit method must be called on this
     *         annotation visitor, followed by visitEnd.
     */
    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        if (mv != null) {
            return mv.visitAnnotationDefault();
        }
        return null;
    }

    /**
     * Visits an annotation of this method.
     * 
     * @param desc
     *            the class descriptor of the annotation class.
     * @param visible
     *            <tt>true</tt> if the annotation is visible at runtime.
     * @return a visitor to visit the annotation values, or <tt>null</tt> if
     *         this visitor is not interested in visiting this annotation.
     */
    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (mv != null) {
            return mv.visitAnnotation(desc, visible);
        }
        return null;
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
        if (mv != null) {
            return mv.visitParameterAnnotation(parameter, desc, visible);
        }
        return null;
    }

    /**
     * Visits a non standard attribute of this method.
     * 
     * @param attr
     *            an attribute.
     */
    @Override
    public void visitAttribute(Attribute attr) {
        if (mv != null) {
            mv.visitAttribute(attr);
        }
    }
    

    /**
     * Starts the visit of the method's code, if any (i.e. non abstract method).
     */
    @Override
    public void visitCode() {
        if (mv != null) {
            mv.visitCode();
        }
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
    @Override
    public void visitFrame(final int type, final int nLocal,
            final Object[] local, final int nStack, final Object[] stack) {
        buf.setLength(0);
        buf.append(ltab);
        buf.append("FRAME ");
        switch (type) {
        case Opcodes.F_NEW:
        case Opcodes.F_FULL:
            buf.append("FULL [");
            appendFrameTypes(nLocal, local);
            buf.append("] [");
            appendFrameTypes(nStack, stack);
            buf.append(']');
            break;
        case Opcodes.F_APPEND:
            buf.append("APPEND [");
            appendFrameTypes(nLocal, local);
            buf.append(']');
            break;
        case Opcodes.F_CHOP:
            buf.append("CHOP ").append(nLocal);
            break;
        case Opcodes.F_SAME:
            buf.append("SAME");
            break;
        case Opcodes.F_SAME1:
            buf.append("SAME1 ");
            appendFrameTypes(1, stack);
            break;
        }
        System.out.println(buf.toString());
    }

    // -------------------------------------------------------------------------
    // Normal instructions
    // -------------------------------------------------------------------------

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
    public void visitInsn(final int opcode) {
        buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]);
        System.out.println(buf.toString());
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
    public void visitIntInsn(final int opcode, final int operand) {
        buf.setLength(0);
        buf.append(tab2)
                .append(OPCODES[opcode])
                .append(' ')
                .append(opcode == Opcodes.NEWARRAY ? TYPES[operand] : Integer
                        .toString(operand));
        System.out.println(buf.toString());
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
    public void visitVarInsn(final int opcode, final int var) {
        buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]).append(' ').append(var);
        System.out.println(buf.toString());
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
    public void visitTypeInsn(final int opcode, final String type) {
        buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]).append(' ');
        appendDescriptor(INTERNAL_NAME, type);
        System.out.println(buf.toString());
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
    @Override
    public void visitFieldInsn(int opcode, String owner, String name,
            String desc) {
    	buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]).append(' ');
        appendDescriptor(INTERNAL_NAME, owner);
        buf.append('.').append(name).append(" : ");
        appendDescriptor(FIELD_DESCRIPTOR, desc);
        System.out.println(buf.toString());
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
    public void visitMethodInsn(final int opcode, final String owner,
            final String name, final String desc) {
        buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]).append(' ');
        appendDescriptor(INTERNAL_NAME, owner);
        buf.append('.').append(name).append(' ');
        appendDescriptor(METHOD_DESCRIPTOR, desc);
        System.out.println(buf.toString());
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
        buf.setLength(0);
        buf.append(tab2).append("INVOKEDYNAMIC").append(' ');
        buf.append(name);
        appendDescriptor(METHOD_DESCRIPTOR, desc);
        buf.append(" [");
        appendHandle(bsm);
        buf.append(tab3).append("// arguments:");
        if (bsmArgs.length == 0) {
            buf.append(" none");
        } else {
            buf.append('\n').append(tab3);
            for (int i = 0; i < bsmArgs.length; i++) {
                Object cst = bsmArgs[i];
                if (cst instanceof String) {
                    appendString((String) cst);
                } else if (cst instanceof Type) {
                    buf.append(((Type) cst).getDescriptor()).append(".class");
                } else if (cst instanceof Handle) {
                    appendHandle((Handle) cst);
                } else {
                    buf.append(cst);
                }
                buf.append(", ");
            }
            buf.setLength(buf.length() - 2);
        }
        buf.append('\n');
        buf.append(tab2).append("]\n");
        System.out.print(buf.toString());
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
    	buf.setLength(0);
        buf.append(tab2).append(OPCODES[opcode]).append(' ');
        appendLabel(label);
        System.out.println(buf.toString());
    }

    /**
     * Visits a label. A label designates the instruction that will be visited
     * just after it.
     * 
     * @param label
     *            a {@link Label Label} object.
     */
    @Override
    public void visitLabel(final Label label) {
        buf.setLength(0);
        buf.append(ltab);
        appendLabel(label);
        System.out.println(buf.toString());
    }

    // -------------------------------------------------------------------------
    // Special instructions
    // -------------------------------------------------------------------------

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
    public void visitLdcInsn(final Object cst) {
        buf.setLength(0);
        buf.append(tab2).append("LDC ");
        if (cst instanceof String) {
            appendString((String) cst);
        } else if (cst instanceof Type) {
            buf.append(((Type) cst).getDescriptor()).append(".class");
        } else {
            buf.append(cst);
        }
        System.out.println(buf.toString());
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
    public void visitIincInsn(final int var, final int increment) {
        buf.setLength(0);
        buf.append(tab2).append("IINC ").append(var).append(' ')
                .append(increment);
        System.out.println(buf.toString());
    }

    /**
     * Visits a TABLESWITCH instruction.
     * 
     * @param min
     *            the minimum key value.
     * @param max
     *            the maximum key value.
     * @param dflt
     *            beginning of the default handler block.
     * @param labels
     *            beginnings of the handler blocks. <tt>labels[i]</tt> is the
     *            beginning of the handler block for the <tt>min + i</tt> key.
     */
    @Override
    public void visitTableSwitchInsn(final int min, final int max,
            final Label dflt, final Label... labels) {
        buf.setLength(0);
        buf.append(tab2).append("TABLESWITCH\n");
        for (int i = 0; i < labels.length; ++i) {
            buf.append(tab3).append(min + i).append(": ");
            appendLabel(labels[i]);
            buf.append('\n');
        }
        buf.append(tab3).append("default: ");
        appendLabel(dflt);
        System.out.println(buf.toString());
    }

    /**
     * Visits a LOOKUPSWITCH instruction.
     * 
     * @param dflt
     *            beginning of the default handler block.
     * @param keys
     *            the values of the keys.
     * @param labels
     *            beginnings of the handler blocks. <tt>labels[i]</tt> is the
     *            beginning of the handler block for the <tt>keys[i]</tt> key.
     */
    @Override
    public void visitLookupSwitchInsn(final Label dflt, final int[] keys,
            final Label[] labels) {
        buf.setLength(0);
        buf.append(tab2).append("LOOKUPSWITCH\n");
        for (int i = 0; i < labels.length; ++i) {
            buf.append(tab3).append(keys[i]).append(": ");
            appendLabel(labels[i]);
            buf.append('\n');
        }
        buf.append(tab3).append("default: ");
        appendLabel(dflt);
        System.out.println(buf.toString());
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
    public void visitMultiANewArrayInsn(final String desc, final int dims) {
        buf.setLength(0);
        buf.append(tab2).append("MULTIANEWARRAY ");
        appendDescriptor(FIELD_DESCRIPTOR, desc);
        buf.append(' ').append(dims);
        System.out.println(buf.toString());
    }
    // -------------------------------------------------------------------------
    // Exceptions table entries, debug information, max stack and max locals
    // -------------------------------------------------------------------------

    /**
     * Visits a try catch block.
     * 
     * @param start
     *            beginning of the exception handler's scope (inclusive).
     * @param end
     *            end of the exception handler's scope (exclusive).
     * @param handler
     *            beginning of the exception handler's code.
     * @param type
     *            internal name of the type of exceptions handled by the
     *            handler, or <tt>null</tt> to catch any exceptions (for
     *            "finally" blocks).
     * @throws IllegalArgumentException
     *             if one of the labels has already been visited by this visitor
     *             (by the {@link #visitLabel visitLabel} method).
     */
    @Override
    public void visitTryCatchBlock(final Label start, final Label end,
            final Label handler, final String type) {
        buf.setLength(0);
        buf.append(tab2).append("TRYCATCHBLOCK ");
        appendLabel(start);
        buf.append(' ');
        appendLabel(end);
        buf.append(' ');
        appendLabel(handler);
        buf.append(' ');
        appendDescriptor(INTERNAL_NAME, type);
        System.out.println(buf.toString());
    }

    /**
     * Visits a local variable declaration.
     * 
     * @param name
     *            the name of a local variable.
     * @param desc
     *            the type descriptor of this local variable.
     * @param signature
     *            the type signature of this local variable. May be
     *            <tt>null</tt> if the local variable type does not use generic
     *            types.
     * @param start
     *            the first instruction corresponding to the scope of this local
     *            variable (inclusive).
     * @param end
     *            the last instruction corresponding to the scope of this local
     *            variable (exclusive).
     * @param index
     *            the local variable's index.
     * @throws IllegalArgumentException
     *             if one of the labels has not already been visited by this
     *             visitor (by the {@link #visitLabel visitLabel} method).
     */
    @Override
    public void visitLocalVariable(final String name, final String desc,
            final String signature, final Label start, final Label end,
            final int index) {
        buf.setLength(0);
        buf.append(tab2).append("LOCALVARIABLE ").append(index).append(' ').append(name).append(' ');
        appendDescriptor(FIELD_DESCRIPTOR, desc);
        buf.append(' ');
        appendLabel(start);
        buf.append(' ');
        appendLabel(end);
        buf.append('\n');

        if (signature != null) {
            buf.append(tab2);
            appendDescriptor(FIELD_SIGNATURE, signature);

            TraceSignatureVisitor sv = new TraceSignatureVisitor(0);
            SignatureReader r = new SignatureReader(signature);
            r.acceptType(sv);
            buf.append(tab2).append("// declaration: ")
                    .append(sv.getDeclaration()).append('\n');
        }
        System.out.print(buf.toString());
    }

    /**
     * Visits a line number declaration.
     * 
     * @param line
     *            a line number. This number refers to the source file from
     *            which the class was compiled.
     * @param start
     *            the first instruction corresponding to this line number.
     * @throws IllegalArgumentException
     *             if <tt>start</tt> has not already been visited by this
     *             visitor (by the {@link #visitLabel visitLabel} method).
     */
    @Override
    public void visitLineNumber(final int line, final Label start) {
        buf.setLength(0);
        buf.append(tab2).append("LINENUMBER ").append(line).append(' ');
        appendLabel(start);
        System.out.println(buf.toString());
    }

    /**
     * Visits the maximum stack size and the maximum number of local variables
     * of the method.
     * 
     * @param maxStack
     *            maximum stack size of the method.
     * @param maxLocals
     *            maximum number of local variables for the method.
     */
    @Override
    public void visitMaxs(final int maxStack, final int maxLocals) {
        buf.setLength(0);
        buf.append(tab2).append("MAXSTACK = ").append(maxStack);
        System.out.println(buf.toString());

        buf.setLength(0);
        buf.append(tab2).append("MAXLOCALS = ").append(maxLocals);
        System.out.println(buf.toString());
    }

    /**
     * Visits the end of the method. This method, which is the last one to be
     * called, is used to inform the visitor that all the annotations and
     * attributes of the method have been visited.
     */
    @Override
    public void visitEnd() {
        if (mv != null) {
            mv.visitEnd();
        }
    }
    // ------------------------------------------------------------------------
    // Utility methods
    // ------------------------------------------------------------------------



    /**
     * Appends an internal name, a type descriptor or a type signature to
     * {@link #buf buf}.
     * 
     * @param type
     *            indicates if desc is an internal name, a field descriptor, a
     *            method descriptor, a class signature, ...
     * @param desc
     *            an internal name, type descriptor, or type signature. May be
     *            <tt>null</tt>.
     */
    protected void appendDescriptor(final int type, final String desc) {
        if (type == CLASS_SIGNATURE || type == FIELD_SIGNATURE
                || type == METHOD_SIGNATURE) {
            if (desc != null) {
                buf.append("// signature ").append(desc).append('\n');
            }
        } else {
            buf.append(desc);
        }
    }
    
    /**
     * Appends the name of the given label to {@link #buf buf}. Creates a new
     * label name if the given label does not yet have one.
     * 
     * @param l
     *            a label.
     */
    protected void appendLabel(final Label l) {
        if (labelNames == null) {
            labelNames = new HashMap<Label, String>();
        }
        String name = labelNames.get(l);
        if (name == null) {
            name = "L" + labelNames.size();
            labelNames.put(l, name);
        }
        buf.append(name);
    }
    
    private void appendFrameTypes(final int n, final Object[] o) {
        for (int i = 0; i < n; ++i) {
            if (i > 0) {
                buf.append(' ');
            }
            if (o[i] instanceof String) {
                String desc = (String) o[i];
                if (desc.startsWith("[")) {
                    appendDescriptor(FIELD_DESCRIPTOR, desc);
                } else {
                    appendDescriptor(INTERNAL_NAME, desc);
                }
            } else if (o[i] instanceof Integer) {
                switch (((Integer) o[i]).intValue()) {
                case 0:
                    appendDescriptor(FIELD_DESCRIPTOR, "T");
                    break;
                case 1:
                    appendDescriptor(FIELD_DESCRIPTOR, "I");
                    break;
                case 2:
                    appendDescriptor(FIELD_DESCRIPTOR, "F");
                    break;
                case 3:
                    appendDescriptor(FIELD_DESCRIPTOR, "D");
                    break;
                case 4:
                    appendDescriptor(FIELD_DESCRIPTOR, "J");
                    break;
                case 5:
                    appendDescriptor(FIELD_DESCRIPTOR, "N");
                    break;
                case 6:
                    appendDescriptor(FIELD_DESCRIPTOR, "U");
                    break;
                }
            } else {
                appendLabel((Label) o[i]);
            }
        }
    }
    
    protected void appendHandle(final Handle h) {
        buf.append('\n').append(tab3);
        int tag = h.getTag();
        buf.append("// handle kind 0x").append(Integer.toHexString(tag))
                .append(" : ");
        switch (tag) {
        case Opcodes.H_GETFIELD:
            buf.append("GETFIELD");
            break;
        case Opcodes.H_GETSTATIC:
            buf.append("GETSTATIC");
            break;
        case Opcodes.H_PUTFIELD:
            buf.append("PUTFIELD");
            break;
        case Opcodes.H_PUTSTATIC:
            buf.append("PUTSTATIC");
            break;
        case Opcodes.H_INVOKEINTERFACE:
            buf.append("INVOKEINTERFACE");
            break;
        case Opcodes.H_INVOKESPECIAL:
            buf.append("INVOKESPECIAL");
            break;
        case Opcodes.H_INVOKESTATIC:
            buf.append("INVOKESTATIC");
            break;
        case Opcodes.H_INVOKEVIRTUAL:
            buf.append("INVOKEVIRTUAL");
            break;
        case Opcodes.H_NEWINVOKESPECIAL:
            buf.append("NEWINVOKESPECIAL");
            break;
        }
        buf.append('\n');
        buf.append(tab3);
        appendDescriptor(INTERNAL_NAME, h.getOwner());
        buf.append('.');
        buf.append(h.getName());
        buf.append('(');
        appendDescriptor(HANDLE_DESCRIPTOR, h.getDesc());
        buf.append(')').append('\n');
    }

    public void appendString(String s) {
        buf.append('\"');
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c == '\n') {
                buf.append("\\n");
            } else if (c == '\r') {
                buf.append("\\r");
            } else if (c == '\\') {
                buf.append("\\\\");
            } else if (c == '"') {
                buf.append("\\\"");
            } else if (c < 0x20 || c > 0x7f) {
                buf.append("\\u");
                if (c < 0x10) {
                    buf.append("000");
                } else if (c < 0x100) {
                    buf.append("00");
                } else if (c < 0x1000) {
                    buf.append('0');
                }
                buf.append(Integer.toString(c, 16));
            } else {
                buf.append(c);
            }
        }
        buf.append('\"');
    }
    
    public String toSting(){
		 return buf.toString();
    }

}
