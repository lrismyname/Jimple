package lnu.asm.jimple;

import lnu.asm.jimple.entities.JClass;

import org.objectweb.asm.Type;

public interface NewRef extends Ref{
	public JClass getBase();
    public Type getType();
}
