
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.Object;
public class numericalMethods
{

	// The following variables are static variables because they will be used in conjunction with a static method
	// This will be set to true if a value was found and false when no true value was found 
	public static boolean rootCheckForRd;

	 
		 
	/*The following numerical method will be used with an interface method named function 
	 *This interface allows every other class to input their own function into the ridderMethod 
	 * */
	public static double riddersMethod(double yValue, Object obj,String equationName, double lowerBound,double upperBound,method function ) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		double xR, xL, xU , xM, xrOld, error ,test;
		double part1, part2, part3, part4 ; 
		double ifFirstRound; 
		double lambdaRoots=0; 
		error=100;
		ifFirstRound=0;
		xL=lowerBound;
		xU= upperBound;
		xM=(lowerBound+upperBound)/2.;
		
		xR=0.;
		xrOld=0.;
		
		rootCheckForRd=false;
		int counter =0;
		
		
		while(xL>=lowerBound && xU<=upperBound)
		{	
			// this was a super long function so we split it into three parts  
			part1=xM-xL;
			part2=(Math.signum((yValue - function.invokeMethod(equationName, xL,obj)) - (yValue - function.invokeMethod(equationName, xU,obj)))) * (yValue - function.invokeMethod(equationName, xM,obj));
			part3=	(yValue - function.invokeMethod(equationName, xM,obj)) * (yValue - function.invokeMethod(equationName, xM,obj)) - (yValue - function.invokeMethod(equationName, xL,obj)) * (yValue - function.invokeMethod(equationName, xU,obj));
			counter++;
			if (part3<=0)// asymptote check to ensure that NAN does not happen 
			{ 
				rootCheckForRd=false;
				break;
			}
			else 
			{ 

				part4= Math.sqrt(part3);
				xR=xM+part1*part2/part4;
			
				if(ifFirstRound==0)  
				{
					error=100;
				}
				else if (ifFirstRound>0) 
				{
					error=Math.abs((xR-xrOld)/xR)*100;
				}
				ifFirstRound++;
				xrOld=xR;
				
				if((yValue-function.invokeMethod(equationName,xL,obj))*(yValue-function.invokeMethod(equationName,xU,obj))>0)// if the value goes out of bound 
				{
				 
					rootCheckForRd=false;
					break;
				}
				
				if(error<=0.001)
				{
				test=(yValue-function.invokeMethod(equationName,xR,obj));
				
					if(Math.abs(test)>0.001)
					{
						rootCheckForRd=false;
						break;
					}
					else 
					{
						rootCheckForRd=true; 
						lambdaRoots=xR;
						return lambdaRoots;
					}
				}
				else if(xR<xM)
				{
					if((yValue-function.invokeMethod(equationName,xR,obj))*(yValue-function.invokeMethod(equationName,xL,obj))<0)
					{
						xU=xR;
						xM=(xL+xU)/2.;
					}
					else if((yValue-function.invokeMethod(equationName,xR,obj))*(yValue-function.invokeMethod(equationName,xM,obj))<0)
					{
						xL=xR;
						xM=(xL+xU)/2.;
					}
					else if ((yValue-function.invokeMethod(equationName,xM,obj))*(yValue-function.invokeMethod(equationName,xU,obj))<0)
					{
						xL=xR;
						xM=(xL+xU)/2.;
					}
				}
				else if(xR>xM)
				{
					if((yValue-function.invokeMethod(equationName,xR,obj))*(yValue-function.invokeMethod(equationName,xU,obj))<0)
					{
						xL=xR;
						xM=(xL+xU)/2.;
					}
					else if ((yValue-function.invokeMethod(equationName,xR,obj))*(yValue-function.invokeMethod(equationName,xM,obj))<0)
					{
						xL=xM;
						xM=(xL+xU)/2.;
					}
					else if ((yValue-function.invokeMethod(equationName,xL,obj))*(yValue-function.invokeMethod(equationName,xM,obj))<0)
					{
						xU=xM;
						xM=(xU+xL)/2.;
						
					}		
				}	
			}
		}

		rootCheckForRd=false; 
		return lambdaRoots;

	}

	
}
