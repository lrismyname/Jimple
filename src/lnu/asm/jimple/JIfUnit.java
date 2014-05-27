package lnu.asm.jimple;

import org.objectweb.asm.Label;

import soot.jimple.Jimple;

public class JIfUnit implements IfStmt{
	
	Label label;
	Operation operation;
	public JIfUnit(Operation op, Label lb){
		label=lb;
		operation=op;
	}
	
	public static JIfUnit v(Operation op, Label lb){
		return new JIfUnit(op,lb);
	}

	@Override
	public Operation getCondition() {
		return operation;
	}

	@Override
	public void setCondition(Operation condition) {
		operation=condition;
	}

	@Override
	public Label getLable() {
		return label;
	}
	
	public String toString(){
		return "IF "+getCondition().toString()+"  goto  " + getLable().toString();
	}

}
