package lnu.asm.jimple;

import java.util.List;

import org.objectweb.asm.Type;

public class LongConstant extends Constant{
	
	public final long value;

    protected LongConstant(long value)
    {
        this.value = value;
    }

    public static LongConstant v(long value)
    {
        return new LongConstant(value);
    }

    public boolean equals(Object c)
    {
        return c instanceof LongConstant && ((LongConstant) c).value == value;
    }

	@Override
	public List<ValueBox> getUseBoxes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return Type.LONG_TYPE;
	}
	
	public String toString()
    {
        return new Long(value).toString() + "L";
    }

}
