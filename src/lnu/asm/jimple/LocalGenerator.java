package lnu.asm.jimple;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lnu.asm.jimple.entities.JBody;
import lnu.asm.jimple.entities.JLocal;

import org.objectweb.asm.Type;

public class LocalGenerator {
	 private final JBody body;
	 
	    public LocalGenerator(JBody b){
	          body = b;
	    }
	    
	    private transient Set<String> localNames = null; 
	    
	    private boolean bodyContainsLocal(String name){
	        return localNames.contains(name);       
	    }
	    
	    private void initLocalNames() {
	    	localNames = new HashSet<String>();
	        Iterator it = body.getLocals().iterator();
	        while (it.hasNext()){
	            localNames.add(((Local)it.next()).getName());
	        }    	
	    }
	    
	    /**
	     * generates a new soot local given the type
	     */
	    public Local generateLocal(String tdesc){
	        
	    	//store local names for enhanced performance
	    	initLocalNames();
			String name = "v";
			Type type=null;
			//System.out.println("ppppppp-------------------"+tdesc);
			if (tdesc.equals("I")) {
	            while (true){
				    name = nextIntName();
				    type=Type.INT_TYPE;
	                if (!bodyContainsLocal(name)) break;
	            }
			}
	        else if (tdesc.equals("B")) {
	            while (true){
				    name = nextByteName();
				    type=Type.BYTE_TYPE;
	                if (!bodyContainsLocal(name)) break;
	            }
			}
	        else if (tdesc.equals("S")) {
	            while (true){
				    name = nextShortName();
				    type=Type.SHORT_TYPE;
	                if (!bodyContainsLocal(name)) break;
	            }
			}
	        else if (tdesc.equals("Z")) {
	            while (true){
				    name = nextBooleanName();
				    type=Type.BOOLEAN_TYPE;
	                if (!bodyContainsLocal(name)) break;
	            }
			}
			//-------------???????????
	        else if (tdesc.equals("V")) {
	            while (true){
				    name = nextVoidName();
				    type=Type.VOID_TYPE;
	                if (!bodyContainsLocal(name)) break;
	            }
			}
	        else if (tdesc.equals("C")) {
	            while (true){
	                name = nextCharName();
	                type=Type.CHAR_TYPE;
	                if (!bodyContainsLocal(name)) break;
	            }
	            //type = soot.CharType.v();
	        }
			else if (tdesc.equals("D")) {
	            while (true){
				    name = nextDoubleName();
				    type=Type.DOUBLE_TYPE;
	                if (!bodyContainsLocal(name)) break;
	            }
			}
			else if (tdesc.equals("F")) {
	            while (true){
				    name = nextFloatName();
				    type=Type.FLOAT_TYPE;
	                if (!bodyContainsLocal(name)) break;
	            }
			}
			else if (tdesc.equals("J")) {
	            while (true){
				    name = nextLongName();
				    type=Type.LONG_TYPE;
	                if (!bodyContainsLocal(name)) break;
	            }
			}
	        else if (tdesc.startsWith("L")||tdesc.startsWith("[")) {
	            while (true){
	                name = nextRefLikeTypeName();
	                type=Type.getType(tdesc);
	                if (!bodyContainsLocal(name)) break;
	            }
	        }
	        else if (tdesc.equals("null")) {
	            while (true){
	                name = nextNullTypeName();
	                type=null;
	                if (!bodyContainsLocal(name)) break;
	            }
	        }
	        else {
	        	localNames = null;
	            throw new RuntimeException("Unhandled Type of Local variable to Generate - Not Implemented");
	        }
			
	    	localNames = null;
			return createLocal(name, type);
		}

		private int tempInt = -1;
		private int tempVoid = -1;
		private int tempBoolean = -1;
		private int tempLong = -1;
		private int tempDouble = -1;
		private int tempFloat = -1;
	    private int tempRefLikeType = -1;
	    private int tempByte = -1;
	    private int tempShort = -1;
	    private int tempChar = -1;
	    private int tempUnknownType = -1;
	    private int tempNullType = -1;
		
	    private String nextIntName(){
			tempInt++;
			return "i"+tempInt;
		}

	    private String nextCharName(){
			tempChar++;
			return "c"+tempChar;
		}

		private String nextVoidName(){
			tempVoid++;
			return "v"+tempVoid;
		}

		private String nextByteName(){
			tempByte++;
			return "b"+tempByte;
		}

		private String nextShortName(){
			tempShort++;
			return "s"+tempShort;
		}

		private String nextBooleanName(){
			tempBoolean++;
			return "z"+tempBoolean;
		}

		private String nextDoubleName(){
			tempDouble++;
			return "$d"+tempDouble;
		}
	    
		private String nextFloatName(){
			tempFloat++;
			return "f"+tempFloat;
		}

		private String nextLongName(){
			tempLong++;
			return "l"+tempLong;
		}

	    private String nextRefLikeTypeName(){
	        tempRefLikeType++;
	        return "r"+tempRefLikeType;
	    }

	    private String nextUnknownTypeName(){
	    	tempUnknownType++;
	        return "u"+tempUnknownType;
	    }
	    
	    private String nextNullTypeName(){
	    	
			tempNullType++;
	        return "n"+tempUnknownType;
	    }

	    // this should be used for generated locals only
	    private Local createLocal(String name, Type type) {
	        Local local = JLocal.v(name, type);
	       // body.getLocals().add(sootLocal);
			return local;
		}

}
