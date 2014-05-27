package lnu.asm.jimple;

import java.util.List;

import org.objectweb.asm.Type;

public class DoubleConstant extends Constant{
	
	public final double value;

    protected DoubleConstant(double value)
    {
        this.value = value;
    }

    public static DoubleConstant v(double value)
    {
        return new DoubleConstant(value);
    }

    public boolean equals(Object c)
    {
        return c instanceof DoubleConstant && ((DoubleConstant) c).value == value;
    }

	@Override
	public List<ValueBox> getUseBoxes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getType() {
		return Type.DOUBLE_TYPE;
	}
	
	public String toString()
    {
        String doubleString = new Double(value).toString();
        
        if(doubleString.equals("NaN") || 
            doubleString.equals("Infinity") ||
            doubleString.equals("-Infinity"))
            return "#" + doubleString;
        else
            return doubleString;
    }
	

}
