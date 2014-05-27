package lnu.asm.jimple;

import org.objectweb.asm.Label;

public class JGotoUnit implements Unit{
	Label label;
	
	public JGotoUnit(Label lb){
		label=lb;
	}
	
	public static JGotoUnit v(Label label){
		return new JGotoUnit(label);
	}
	
	public Label getLabel(){
		return label;
	}
	
	public void setLabel(Label lb){
		label=lb;
	}
	
	public String toString(){
		return "goto  "+label.toString();
	}

}
