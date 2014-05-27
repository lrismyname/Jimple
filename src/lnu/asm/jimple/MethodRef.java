package lnu.asm.jimple;

import lnu.asm.jimple.entities.JClass;
import lnu.asm.jimple.entities.JMethod;


public interface MethodRef extends Ref{
    public JClass ownerClass();
    public JMethod getMethod();
    public String getSignature();
}
