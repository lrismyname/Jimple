package lnu.asm.jimple;



public interface ArrayRef extends Ref {
	public Value getBase();
    public void setBase(Local base);
    public Value getIndex();
    public void setIndex(Value index);

}
