package lnu.asm.jimple;

import java.util.List;

import org.objectweb.asm.Type;

public class StringConstant extends Constant{
	
	public final String value;

    protected StringConstant(String value)
    {
        this.value = value;
    }

    public static StringConstant v(String value)
    {
        return new StringConstant(value);
    }

    public boolean equals(Object c)
    {
        return c instanceof StringConstant && ((StringConstant) c).value == value;
    }


	@Override
	public List<ValueBox> getUseBoxes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type getType() {
		return null;
	}

}
