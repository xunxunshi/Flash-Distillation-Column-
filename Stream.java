 import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

// this class is an abstract class that acts as a base class for the InertStream and NonInertStream 

public abstract class Stream implements method
{
 private Chemical[] chemicals;
 private double temperature;


 
 public Stream(Chemical[]chemicals)
 {
  this.chemicals = new Chemical[chemicals.length];
  
  for(int i = 0; i < chemicals.length; i++)
  {
   this.chemicals[i] = chemicals[i].clone();
  }
 }
 
 public Stream(Chemical[] chemicals, double temperature)
 {
  this.chemicals = new Chemical[chemicals.length];
  
  for(int i = 0; i < chemicals.length; i++)
  {
   this.chemicals[i] = chemicals[i].clone();
  }
  this.temperature = temperature;
 }

 public Stream(Stream original)
 {
  this.chemicals = new Chemical[original.chemicals.length];
  for(int i = 0; i < original.chemicals.length; i++)
  {
   this.chemicals[i] = original.chemicals[i].clone();
  }
  this.temperature = original.temperature;
 }

 public abstract Stream clone();

 
 public void setChemicals(Chemical[] chemicals)
 {
  this.chemicals = new Chemical[chemicals.length];
  
  for(int i = 0; i < chemicals.length; i++)
  {
   this.chemicals[i] = chemicals[i].clone();
  }
 }
 // allows the user to add chemicals 
 public void addChemical(Chemical chemical)
 {
  Chemical[] temporaryChemicals = new Chemical[this.chemicals.length + 1];
  for(int i = 0; i < chemicals.length; i++)
  {
   temporaryChemicals[i] = this.chemicals[i].clone();
  }
  temporaryChemicals[this.chemicals.length] = chemical.clone();
  
  this.chemicals = temporaryChemicals;
 }
 
 // allows the user to add chemicals to the library 
 
 public void removeChemical(int index)
 {
  Chemical [] temporaryChemicals = new Chemical[this.chemicals.length - 1];

   
  for(int i = 0; i<this.chemicals.length; i++)
  {
   if(i < index)
   {  temporaryChemicals[i] = this.chemicals[i].clone();    
   } 
   else if(i>index)
   {
    temporaryChemicals[i-1] = this.chemicals[i].clone();    
   }
  }

  this.chemicals = temporaryChemicals;
 }
 public void setTemperature(double temperature)
 {
  this.temperature = temperature;
 }
 
 
 public Chemical[] getChemicals()
 {
  Chemical[] tempChemicals = new Chemical[this.chemicals.length];
  for(int i = 0; i < this.chemicals.length; i++)
  {
   tempChemicals[i] = this.chemicals[i].clone();
  }
  return tempChemicals;
 }
 
 public Chemical getChemical(int index)
 {
  return this.chemicals[index].clone();
 }
 
 public double getTemperature()
 {
  return this.temperature;
 }


 
 public double totalFlowrate()
 {
  double totalFlowrate = 0;
  for(int i = 0; i < chemicals.length; i++)
  {
   totalFlowrate += chemicals[i].getFlowrate();
  }
  return totalFlowrate;
 }
 
 public double chemicalFlowrate(int index)
 {
  return chemicals[index].getFlowrate();
 }
 
 public double moleFraction(int index)
 {
  return this.chemicals[index].getFlowrate()/this.totalFlowrate();
 }
 
 // this is used in other classes to set the reference temperature 
 
 public double findLowestBubblePointTemperature(double pressure)
 {
  
  double minTbp = this.getChemical(0).findBoilingPoint(pressure);
 
  for(int i = 1; i < this.chemicals.length; i ++)
  {
   
   if (minTbp > this.getChemical(i).findBoilingPoint(pressure))
   {
    minTbp = this.getChemical(i).findBoilingPoint(pressure);
   }
  }
  return minTbp;
  
 }
 
 public double findHighestDewPointTemperature(double pressure)
 {
  double maxTdp = this.getChemical(0).findBoilingPoint(pressure);
  for(int i = 1; i < this.chemicals.length; i ++)
  {
   if (maxTdp < this.getChemical(i).findBoilingPoint(pressure))
   {
    maxTdp = this.getChemical(i).findBoilingPoint(pressure);
   }
  }
  return maxTdp;
 }
 
 
 

 public double bubblePointTemperature(double pressure) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
 {
  double bpTemp=0;
 
   try {
    bpTemp = numericalMethods.riddersMethod(pressure, this, "findTotalPressure", this.findLowestBubblePointTemperature(pressure), 372., this);
   } 
   catch (NoSuchMethodException e) 
   {
    e.printStackTrace();
   }
    
  return bpTemp;
 
 }
 
 public double dewPointTemperature(double pressure) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
 {
  double dpTemp=0;
   
  try {
   dpTemp = numericalMethods.riddersMethod(1./pressure, this, "findInverseOfTotalPressure", this.findLowestBubblePointTemperature(pressure), this.findHighestDewPointTemperature(pressure), this);
   } 
  catch (NoSuchMethodException e) 
   { 
   e.printStackTrace();
   }
  
  return dpTemp;
 }
 
 public double findTotalPressure(double temperature)
 {
  double pressure = 0;
  for(int i = 0; i < this.getChemicals().length;i++)
  { 
   pressure += this.moleFraction(i)*this.getChemical(i).saturationPressure(temperature);
  }
  
  return pressure;
 }
 
 public double findInverseOfTotalPressure(double temperature)
 {
  double inversePressure = 0;
  for(int i = 0; i<this.getChemicals().length;i++)
  {
   inversePressure += this.moleFraction(i)/this.getChemical(i).saturationPressure(temperature);
  }
  return inversePressure;
 }
 
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
 
   

 public String toString()
 {  
  String properties="\n";
  properties+="temperature: "+this.getTemperature()+" K \n\n";
  properties+="compositions: \n";
  for (int i=0;i<this.getChemicals().length;i++)
  {
   properties+=this.getChemical(i).toString()+"";
   
  } 

  return properties;
 }
 
 public String StreamProperties(double pressure, int streamType)
 {
  String properties="";
  DecimalFormat threeDecimals = new DecimalFormat("###.###");
  if(streamType==0 )// feed stream 
  { 
   properties+="Feed stream: \n";
   properties+=this.toString();
   
  }
  else// outlet streams
  {
   properties+="\n In outlet,";
   
   if(streamType==1)// vapour stream
   { 
    properties+=threeDecimals.format(this.totalFlowrate())+"kmols of ";
    properties+=" of gas:\n\n";
    properties+="temperature: "+threeDecimals.format(this.getTemperature())+"K \n\n";
    properties+="compositions: \n";
    for (int i=0;i<this.getChemicals().length;i++)
    {
     properties+= threeDecimals.format(100*this.moleFraction(i))+"%  ";
     properties+=this.getChemical(i).toString();
    }  
   }
   else// liquid stream
   { 
    properties+=threeDecimals.format(this.totalFlowrate())+"kmols of ";
    properties+=" Liquid:\n\n";
    properties+="temperature: "+threeDecimals.format(this.getTemperature())+"K \n\n";
    properties+="compositions: \n";
    for (int i=0;i<this.getChemicals().length;i++)
    {
     properties +=threeDecimals.format(100*this.moleFraction(i))+"%  ";
     properties+=this.getChemical(i).toString();
    }
    
   }
  }
  return properties;
  
 }
 
 
   

}
