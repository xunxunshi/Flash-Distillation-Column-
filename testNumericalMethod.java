import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class testNumericalMethod 
{
 	public static void main (String []arg)
	{
		testNumericalMethodObject  hi= new testNumericalMethodObject ();
		double x=  hi.findanswerRD();
		double y=Math.pow(x, 3.)-10*x*x+5;		
 		System.out.println("the x calculated from ridder'"
				+ "s method is"+x);
		 
	}
	
	
}
