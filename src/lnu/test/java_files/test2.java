package lnu.test.java_files;

public class test2 {
	public static void main(String[] args) {
		int x,y,z;
		//testInterface t2 =new test2();
		//testInterface t3 =new test3();
		test3 t4 =new test3();
		Arithmetic a=new Arithmetic();
		a.basicArithmetic(1,2);
		//y=t3.lag(3);
	    //x=t3.lag(3);
		z=t4.lag(3);
		//x=y+z;
	}
	public int lag(int a){
		int x,y,z;
		
		x=3;
		if(x>2)
		x=a+4;
		else
		y=x-1;
		return 5;
	}

}
