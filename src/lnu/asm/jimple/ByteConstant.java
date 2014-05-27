package lnu.asm.jimple;

import java.util.List;

import org.objectweb.asm.Type;

public class ByteConstant extends Constant{
	
	public final int value;

    protected ByteConstant(int value)
    {
        this.value = value;
    }

    public static ByteConstant v(int value)
    {
        return new ByteConstant(value);
    }

    public boolean equals(Object c)
    {
        return c instanceof ByteConstant && ((ByteConstant) c).value == value;
    }

	@Override
	public List<ValueBox> getUseBoxes() {
		return null;
	}

	@Override
	public Type getType() {
		return Type.BYTE_TYPE;
	}
	
	 public String toString()
	    {
	        return new Integer(value).toString();
	    }

}
