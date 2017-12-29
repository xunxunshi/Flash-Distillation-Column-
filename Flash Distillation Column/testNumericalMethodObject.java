import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class testNumericalMethodObject extends numericalMethods implements method
{
 
	
	public double findanswerRD()
	{
		
	double answer=0;
	
	try 
		{
		answer=numericalMethods.riddersMethod(0, this, "RootSqrtFunction", 0.6, 0.8, this);
		}
	catch (NoSuchMethodException |SecurityException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e )
		{
			e.printStackTrace();
		} 
	return answer;	
	
	}
	
	public double RootSqrtFunction(double x)
	{
		double y;
		y=Math.pow(x, 3.)-10*x*x+5;		
		
		return y;
	}
	
	public double RootSqrtFunctionDerviative(double x)
	{
		double y;
		y=3*Math.pow(x, 2)-20*x;
		return y;
	}
	public double invokeMethod(String equationName, double Value, Object obj)
	{

		double answer;
		answer=0;
		
		// note that equationName should be in format of functionName,parameter1,parameter2,etc 
	 		
			try {

				Method function=obj.getClass().getDeclaredMethod(equationName, double.class);
				answer=(double)function.invoke(obj,Value);
			} 
			catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|SecurityException|NoSuchMethodException e)
			{
				
				e.printStackTrace();
			} 
		
			return answer;
			
	}


}
