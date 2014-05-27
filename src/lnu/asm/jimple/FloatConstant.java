package lnu.asm.jimple;

import java.util.List;

import org.objectweb.asm.Type;

public class FloatConstant extends Constant{
	
	public final float value;

    protected FloatConstant(float value)
    {
        this.value = value;
    }

    public static FloatConstant v(float value)
    {
        return new FloatConstant(value);
    }

    public boolean equals(Object c)
    {
        return c instanceof FloatConstant && ((FloatConstant) c).value == value;
    }

	@Override
	public List<ValueBox> getUseBoxes() {
		return null;
	}

	@Override
	public Type getType() {
		return Type.FLOAT_TYPE;
	}
	
	 public String toString()
	    {
	        String floatString = new Float(value).toString();
	        
	        if(floatString.equals("NaN") || 
	            floatString.equals("Infinity") ||
	            floatString.equals("-Infinity"))
	            return "#" + floatString + "F";
	        else
	            return floatString + "F";
	    }

}
