package lnu.asm.jimple;



public interface ValueBox {
	
    /** Sets the value contained in this box as given.  Subject to canContainValue() checks. */
    public void setValue(Value value);

    /** Returns the value contained in this box. */
    public Value getValue();

    /** Returns true if the given Value fits in this box. */
    public boolean canContainValue(Value value);

}
