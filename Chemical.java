import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

public class Chemical implements method
{
 private static final double R=8.314;
 
 private String name;
 private boolean ifCplEqu1;
 private double flowrate; //V*yi THIS IS MOLAR FLOWRATE
 private double omega;
 private double A, B, C; //Antoine's coefficients
 private double cpL[], cpV[];
 private double lambdaC1,lambdaC2,lambdaC3,lambdaC4;
 private double criticalTemperature;
 private double criticalPressure;
 private boolean ifCondensable;
 
 //CONSTRUCTORS
 public Chemical(String name, double flowrate,double[] Antoine,double[] cpLvalue,double cpVvalue[], double[] lambdaParameters,double Tc,double Pc,double omega, boolean ifCplEqu1,boolean ifCondensible)
 {
  // Ensure input array sizes are correct 
  if(Antoine.length!=3)
  {
   System.out.println("error in inputting antonine parameter array size ");
   System.exit(0);
  }
  if(cpVvalue.length!=4)
  {
   System.out.println("error in inputting heat capacity parameter array size ");
   System.exit(0);
  }
  if(cpLvalue.length!=5)
  {
   System.out.println("error in inputting heat capacity parameter array size ");
   System.exit(0);
  }
  if(lambdaParameters.length!=4)
  {
   System.out.println("error in inputting heat of vaporization parameter array size ");
   System.exit(0);
  }
  
  // copy the variables 
  this.name = name;
  this.flowrate = flowrate;
  this.cpL=new double [cpLvalue.length];
  this.cpV=new double [cpVvalue.length];
  for (int i=0;i<cpVvalue.length;i++)
  {
	  this.cpV[i]=cpVvalue[i];
  }
  for (int i=0;i<cpLvalue.length;i++)
  { 
   this.cpL[i]=cpLvalue[i];
  }
  
  A=Antoine[0];
  B=Antoine[1];
  C=Antoine[2];
  lambdaC1=lambdaParameters[0];
  lambdaC2=lambdaParameters[1];
  lambdaC3=lambdaParameters[2];
  lambdaC4=lambdaParameters[3];

  this.ifCplEqu1=ifCplEqu1;
  this.criticalTemperature=Tc;
  this.criticalPressure=Pc;
  this.omega=omega;
  this.ifCondensable=ifCondensible;
  
 }
 
 public Chemical(Chemical original)
 {
  this.name = original.name;
  this.flowrate = original.flowrate;
  this.A=original.A;
  this.B=original.B;
  this.C=original.C;
  this.lambdaC1=original.lambdaC1;
  this.lambdaC2=original.lambdaC2;
  this.lambdaC3=original.lambdaC3;
  this.lambdaC4=original.lambdaC4;
  this.ifCplEqu1=original.ifCplEqu1;

  this.cpL=new double [original.cpL.length];
  this.cpV=new double [original.cpV.length];
  for (int i=0;i<original.cpV.length;i++)
  {
   this.cpV[i]=original.cpV[i];
  }
  for (int i=0;i<original.cpL.length;i++)
  {
   
   this.cpL[i]=original.cpL[i];
  }
  this.criticalTemperature=original.criticalTemperature;
  this.criticalPressure=original.criticalPressure;
  this.omega=original.omega;
  this.ifCondensable=original.ifCondensable;
 }
 
 //CLONING
 public Chemical clone()
 {
  return new Chemical(this);
 }
 
 //SETTERS
 public void setName(String name)
 {
  this.name = name;
 }
 
 public void setFlowrate(double flowrate)
 {
  this.flowrate = flowrate;
 }

 public void setOmega(double omega)
 {
	 this.omega = omega;
 }


 //GETTERS
 public double getCriticalTemperature()
 {
  return this.criticalTemperature;
 }
 
 public double getCriticalPressure()
 {
  return this.criticalPressure;
 }
 
 public double getOmega()
 {
  return this.omega;
 }

 public boolean getIfCondensible()
 {
	 return this.ifCondensable;
 }

 public static double getR()
 {
  return Chemical.R;
 }
 
 public String getName()
 {
  return this.name;
 }
 
 public double getFlowrate()
 {
  return this.flowrate;
 }

 public boolean getIfCplEqu1()
 {
   return this.ifCplEqu1;
 }
  
 public double[] getAntoines()
 {
    double[] temp = {this.A, this.B,this.C};
    return temp;
 }

 public double[] getCpL()
 {
  if (this.cpL != null)
  {
    double[] temp = new double[this.cpL.length];
    for(int i = 0; i < this.cpL.length; i++)
    {
     temp[i] = this.cpL[i]; 
    }
    return temp;
  }
  else
  {
    System.out.println("Warning, the cpL[] instance variable is currently null.");
    return null;
    
  }
 }
  
 public double[] getCpV()
 {
  if (this.cpV != null)
  {
    double[] temp = new double[this.cpV.length];
    for(int i = 0; i < this.cpV.length; i++)
    {
     temp[i] = this.cpV[i]; 
    }
    return temp;
  }
  else
  {
    System.out.println("Warning, the cpV[] instance variable is currently null.");
    return null;
  }
 } 

 public double[] getLambda()
 {
   double[] temp = {this.lambdaC1,this.lambdaC2,this.lambdaC3, this.lambdaC4};
   return temp;
 }

//SETTERS
 
public void setIfCplEqul(boolean ifCplEqu1)
{
 this.ifCplEqu1 = ifCplEqu1;
}

public void setAntoine(double A, double B, double C)
{
  this.A = A;
  this.B = B;
  this.C = C;
}

public void setCpL(double[] cpL)
{
 for(int i = 0; i < cpL.length; i++)
 {
  this.cpL[i] = cpL[i];
 }
}

 public void setCpV(double[] cpV)
{
 for(int i = 0; i < cpV.length; i++)
 {
  this.cpV[i] = cpV[i];
 }
}

public void setLambda(double lambdaC1, double lambdaC2, double lambdaC3, double lambdaC4)
{
this.lambdaC1 = lambdaC1;
this.lambdaC2 = lambdaC2;
this.lambdaC3 = lambdaC3;
this.lambdaC4 = lambdaC4;
}
 
public void setCriticalTemperature(double criticalTemperature)
{
 this.criticalTemperature = criticalTemperature;
}

public void setCriticalPressure(double criticalPressure)
{
 this.criticalPressure = criticalPressure;
}

public void setIfCondensable(boolean ifCondensable)
{
 this.ifCondensable = ifCondensable;
}

//METHODS
 
public double getEnthalpy(double Ttop, double Tbottom, boolean isLiquid)
{
double heatCap;

if(isLiquid==true)
{  
	  heatCap=(cpL[0])*(Ttop-Tbottom)+cpL[1]*.5*(Math.pow(Ttop, 2)-Math.pow(Tbottom, 2))+ cpL[2]*(1/3.)*(Math.pow(Ttop, 3)-Math.pow(Tbottom, 3))+cpL[3]*(0.25)*(Math.pow(Ttop, 4)-Math.pow(Tbottom, 4))+cpL[4]*(1/5.)*(Math.pow(Ttop, 5)-Math.pow(Tbottom, 5)); 
	  heatCap=heatCap/1000.;
}

else
{
	  heatCap=cpV[0]*(Ttop-Tbottom)+cpV[1]*.5*(Math.pow(Ttop, 2)-Math.pow(Tbottom, 2))+ cpV[2]*(1./3.)*(Math.pow(Ttop, 3)-Math.pow(Tbottom, 3))+cpV[3]/((1/4.)*(Math.pow(Ttop, 4)-Math.pow(Tbottom, 4)));
} 

return heatCap;

}

 public double getHeatOfVaporization(double Tr)
 {
  double heatV;
  heatV=(lambdaC1)*Math.pow((1.-Tr),((lambdaC2)+(lambdaC3)*Tr+(lambdaC4)*Math.pow(Tr, 2)));
   
  return heatV/1000;
 }
 
 public double saturationPressure(double temperature)
 {
  return Math.exp(this.A - this.B/(this.C + temperature));
 }

 public double findBoilingPoint(double pressure)
 {
  return this.B/(this.A-Math.log(pressure))-this.C;
 }
  
 public boolean ifEquation1ForCP()
 {
  return ifCplEqu1;
 }
 
 public double Tr(double temperature)
 {
	 return temperature/this.criticalTemperature;
 }
 
//Below are methods used for non-ideal case
 
 public double kappa()
 {
	 return 0.37464+1.54226*this.omega-0.26992*Math.pow(this.omega,2);
 }
 
 public double alpha(double temperature)
 {
	 return Math.pow(1+this.kappa()*(1-Math.pow(this.Tr(temperature),0.5)),2);
 }
 
 public double ai(double temperature)
 {     
	 return 0.45724*Math.pow(Chemical.getR(),2)*Math.pow(this.getCriticalTemperature(),2)*this.alpha(temperature)/this.getCriticalPressure();
 }
 
 public double bi()
 {
	 return 0.07780*Chemical.getR()*this.getCriticalTemperature()/this.getCriticalPressure();
 }
 
 public double Ai(double temperature, double pressure)
 {
	 return this.ai(temperature)*pressure/(Math.pow(Chemical.getR(),2)*Math.pow(temperature, 2));
 }
 
 public double Bi(double temperature, double pressure)
 {
	 return this.bi()*pressure/(Chemical.getR()*temperature);   
 }
 
 public String toString()
 {
  DecimalFormat threeDecimals= new DecimalFormat("###.###");
  String chemicalProperties="";
  
   chemicalProperties+=this.name+": ";
   chemicalProperties+=threeDecimals.format(this.flowrate)+"kmol/h \n";
  
  return chemicalProperties;
 }
 
 //INTERFACE IMPLEMENTATION
 
 public double invokeMethod(String equationName, double Value, Object obj)
 {

  double answer;
  answer=0;
  
  // note that equationName should be in format of functionName,parameter1,parameter2,etc 
  try {
   
   Method function=obj.getClass().getDeclaredMethod(equationName, double.class);
   
   try {
    answer=(double)function.invoke(obj,Value);
   } catch (IllegalAccessException e) {
    
    e.printStackTrace();
   } catch (IllegalArgumentException e) {
    
    e.printStackTrace();
   } catch (InvocationTargetException e) {
    
    e.printStackTrace();
   }
   
  } catch (NoSuchMethodException e1) {
   
   e1.printStackTrace();
  } catch (SecurityException e1) {
   
   e1.printStackTrace();
  }
  
 return answer;
   

 }

  
}