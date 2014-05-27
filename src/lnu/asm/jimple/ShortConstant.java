package lnu.asm.jimple;

import java.util.List;

import org.objectweb.asm.Type;

public class ShortConstant extends Constant{
	
	public final int value;

    protected ShortConstant(int value)
    {
        this.value = value;
    }

    public static ShortConstant v(int value)
    {
        return new ShortConstant(value);
    }

    public boolean equals(Object c)
    {
        return c instanceof ShortConstant && ((ShortConstant) c).value == value;
    }

	@Override
	public List<ValueBox> getUseBoxes() {
		return null;
	}

	@Override
	public Type getType() {
		return Type.SHORT_TYPE;
	}
	
	 public String toString()
	    {
	        return new Integer(value).toString();
	    }


}
