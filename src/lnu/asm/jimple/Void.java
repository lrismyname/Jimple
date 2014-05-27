package lnu.asm.jimple;

import java.util.List;

import org.objectweb.asm.Type;

public class Void extends Constant{

	public static Void v(){
		return new Void();
	}

	@Override
	public Type getType() {
		return Type.VOID_TYPE;
	}
	
	public String toString()
    {
        return "void";
    }
	
	@Override
	public List<ValueBox> getUseBoxes() {
		return null;
	}

}
