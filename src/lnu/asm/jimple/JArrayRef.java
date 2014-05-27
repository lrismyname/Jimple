package lnu.asm.jimple;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class JArrayRef implements ArrayRef{
	Value base;
	Value index;
	Type type;
	
	public JArrayRef(Value i, Type t){
		type=t;
		index=i;
	}

	public static JArrayRef v(Value index,Type type){
		return new JArrayRef(index,type);
	}

	@Override
	public Value getBase() {
		return base;
	}

	@Override
	public void setBase(Local base) {
		this.base=base;
		
	}

	@Override
	public Value getIndex() {
		return index;
	}

	@Override
	public void setIndex(Value index) {
		this.index=index;
		
	}

	@Override
	public Type getType() {
		return type;
	}
	
	
	@Override
	public List<ValueBox> getUseBoxes() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String toString(){
		return "newarry  "+this.getString(type)+" ["+index+"]";
	}
	
	protected String getString(Type tp){
		String type=tp.toString();
		if(type.equals("[I")){
			type="int";
		}else if(type.equals("[B")){
			type="byte";
		}else if(type.equals("[C")){
			type="char";
		}else if(type.equals("[D")){
			type="double";
		}else if(type.equals("[F")){
			type="float";
		}else if(type.equals("[J")){
			type="long";
		}else if(type.equals("[S")){
			type="short";
		}else if(type.equals("[Z")){
			type="boolean";
		}
		return type;
	}

}
