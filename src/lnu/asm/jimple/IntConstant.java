package lnu.asm.jimple;

import java.util.List;

import org.objectweb.asm.Type;



public class IntConstant extends Constant{
	

	public final int value;

    protected IntConstant(int value)
    {
        this.value = value;
    }

    public static IntConstant v(int value)
    {
        return new IntConstant(value);
    }

    public boolean equals(Object c)
    {
        return c instanceof IntConstant && ((IntConstant) c).value == value;
    }

	@Override
	public List<ValueBox> getUseBoxes() {
		return null;
	}

	@Override
	public Type getType() {
		return Type.INT_TYPE;
	}
	
	 public String toString()
	    {
	        return new Integer(value).toString();
	    }

}
