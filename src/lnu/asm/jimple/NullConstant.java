package lnu.asm.jimple;

import java.util.List;

import org.objectweb.asm.Type;


public class NullConstant extends Constant{
	
	public static NullConstant v(){
		return new NullConstant();
	}


	@Override
	public Type getType() {
		return null;
	}
	
	public String toString()
    {
        return " null ";
    }
	
	@Override
	public List<ValueBox> getUseBoxes() {
		return null;
	}

}
