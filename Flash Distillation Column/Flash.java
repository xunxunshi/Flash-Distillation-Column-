import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
public class Flash implements method

{ 
// the following streams will be made if the inlet stream contains at least one condensable chemical
 nonInertStream inletStream;
 nonInertStream outletVapour;
 nonInertStream outletLiquid;
 
 
 
 //the following stream will only be made if the inlet stream contains least one incondensable chemical  
 //this stream is a placeholder for the inert chemicals so that they do not get factored in until the final vapour outlet calculations
 InertStream vapourInertStream;
 
 // this boolean is set to true in the default constructor 
 private boolean ifSolutionFound;
 
 private double temperature; // temperature set by user
 //if it is case 1 or 2, then this temperature is set equal to the inletT
 //in case 3 , this is set as the flash T
 
 private double pressure;
 private int caseNum;
 private boolean isIdeal;
 
 
 private int counterNumOfInertVapour,counterNumOfNonInertChemicals; 

//CONSTRUCTORS  
 public Flash(Stream userStream, double temperature, double pressure, int caseNum,boolean isIdeal)
 {
  this.temperature = temperature;
  this.pressure = pressure;
  
 
  if(caseNum>3||caseNum<1)
  {
   System.out.println("You have chosen a invalid case number. Please, choose a case from 1 to 3");
  }
  else
  {
   this.caseNum=caseNum;  
   
    
   Chemical[] nonInertChemicals=userStream.getChemicals();// assume all chemicals input are not inert 
                 // later checked for the inert chemicals which are moved to InertVapourChemicals 
   Chemical[] InertVapourChemicals;
   
   counterNumOfNonInertChemicals=userStream.getChemicals().length;
    counterNumOfInertVapour=0; 
   
    //the following code finds out how many inert chemicals are present 
   for (int i=0;i<userStream.getChemicals().length;i++)
   {
    if(userStream.getChemical(i).getIfCondensible()==false)
    {
     counterNumOfInertVapour++;   
     counterNumOfNonInertChemicals--;
    } 
   }
   
   
   if(counterNumOfNonInertChemicals>0)// stream created only when at least one non-inert chemical is present (only then will the flash be performed)
   {
    if(isIdeal==true)
    {
      if(caseNum==1||caseNum==2)
      {
       //In cases 1 and 2, only the Inlet Stream's temperature is known,
       this.inletStream = new IdealStream((nonInertChemicals),temperature); //copies chemicals and temperature
       
       this.outletVapour= new IdealStream(nonInertChemicals); //copies chemicals, but does not set temperature
       this.outletLiquid = new IdealStream(nonInertChemicals);
   }
      
      else  
      {
       //In case 3, only the Flash Temperature is known
       this.outletVapour= new IdealStream(nonInertChemicals,temperature);// copies chemicals and temperature of the input stream
       this.outletLiquid= new IdealStream(nonInertChemicals,temperature);
       this.inletStream=new IdealStream(nonInertChemicals);//copies only the chemicals
      } 
    }
    
    else//this statement to creates "nonIdealSteam" objects
    {
     if(caseNum==1||caseNum==2)
     {
      this.inletStream = new NonIdealStream((nonInertChemicals),temperature);
      this.outletVapour= new NonIdealStream((nonInertChemicals));
      this.outletLiquid = new NonIdealStream((nonInertChemicals)); 
     }
     else
     {
      this.outletVapour= new NonIdealStream((nonInertChemicals),temperature);
      this.outletLiquid=new NonIdealStream((nonInertChemicals),temperature);
      this.inletStream=new NonIdealStream(nonInertChemicals);
     }
    }
 
   }   
   
 
   if(counterNumOfInertVapour>0)// create inert stream only when there is in-condensable gas
   {
    InertVapourChemicals= new Chemical[counterNumOfInertVapour];
    int counterVChem=0;
    for (int i=0;i<userStream.getChemicals().length;i++)
    {
     if(userStream.getChemical(i).getIfCondensible()==false)
     {
       InertVapourChemicals[counterVChem]=userStream.getChemical(i);
       if(counterNumOfNonInertChemicals>0)// in the event that the streams is composed of only in-condensable gases so the streams associated to flash were never created
       {
        this.outletVapour.removeChemical(i);
         this.outletLiquid.removeChemical(i);
         this.inletStream.removeChemical(i);     
       }
       counterVChem++;
     }
    }
    this.vapourInertStream=new InertStream(InertVapourChemicals);
   }   
  }
  this.isIdeal=isIdeal; 
  this.ifSolutionFound=true;//every time an error is caught throughout the various methods, all calculations stops.
 
  System.out.println();
 
 }
 
  //getters
 public boolean getIfSolutionFound()
 {
  return this.ifSolutionFound;
 }

 public double getTemp()
 {
  return this.temperature;
 }
 public int getCaseNum()
 {
  return this.caseNum;
 } 
  public boolean getIfIdeal()
  {
   return this.isIdeal;
  }
  public nonInertStream getInletStream()
  {
   if(this.isIdeal==false)
   {
    NonIdealStream returnInlet=  new NonIdealStream((NonIdealStream)inletStream);
    return returnInlet;
   }
   else
   {
    IdealStream returnInlet=  new IdealStream(( IdealStream)inletStream);
    return returnInlet;
   }
  }
  
  public nonInertStream getOutletVapourStream()
  {
   if(this.isIdeal==false)
   {
    NonIdealStream returnOutletVapour=  new NonIdealStream((NonIdealStream)outletVapour);
    return returnOutletVapour;
   }
   else
   {
    IdealStream returnOutletVapour=  new IdealStream(( IdealStream)outletVapour);
    return returnOutletVapour;
   }
  }
  public nonInertStream getOutletLiquidStream()
  {
   if(this.isIdeal==false)
   {
    NonIdealStream returnOutletLiquid=  new NonIdealStream((NonIdealStream)outletLiquid);
    return returnOutletLiquid;
   }
   else
   {
    IdealStream returnOutletLiquid =new IdealStream(( IdealStream)outletLiquid);
    return returnOutletLiquid;
   }
  }
  public InertStream getVapourInertStream()
  { 
    InertStream returnInerts=  new InertStream (vapourInertStream);
    return returnInerts;  
  }
   
public double getPressure()  
{
 return this.pressure;
}
public double getT()  
{
 return this.temperature;
}
  

//SETTERS
 public void setPressure(double pressure)
 {
  this.pressure = pressure;
 }

 public void setTemperature(double temperature)
 {
  this.temperature = temperature;
 }
 

 public void clearFlashStreams()
 {
  this.outletVapour = null;
  this.outletLiquid = null;
  this.inletStream = null;
 }

 public void setInletStream(Stream inletStream)
 {
  if(this.isIdeal==true)
  {
   this.inletStream= new IdealStream((IdealStream)inletStream);   
  }
  else
  {
   this.inletStream= new NonIdealStream((NonIdealStream)inletStream);   
 }
}
 public void setOutletVapour(Stream outletVapourStream)
 {
  if(this.isIdeal==true)
  {
   this.outletVapour= new IdealStream((IdealStream)outletVapourStream);   
  }
  else
  {
   this.outletVapour= new NonIdealStream((NonIdealStream)outletVapourStream);   
 }
} 
 public void setOutletLiquid(Stream outletLiquidStream)
 {
  if(this.isIdeal==true)
  {
   this.outletLiquid= new IdealStream((IdealStream)outletLiquidStream);   
  }
  else
  {
   this.outletLiquid= new NonIdealStream((NonIdealStream)outletLiquidStream);   
 }
} 

 public void setInertStream(Stream inertStream)
 {
 
   this.vapourInertStream= new InertStream((InertStream)inertStream);  
  
} 
 

//METHODS

//The following methods was used to find Q for case 1 
public double ReturnQCase1() 
{
// For first case, it is operating temperature is constant and set by user   
 this.inletStream.setTemperature(temperature);
 this.outletVapour.setTemperature(temperature);
 this.outletLiquid.setTemperature(temperature); 
 
  try {
 //1 - Find if separable
    if(this.isSeparable()==true)
     { 
     //2 find the VF Ratios 
       double Vf=this.findVFRatio();
      //3 - Find and set the new flow rates for vapour components 
       this.findOutletVapour(Vf);
      //4 - Find and set the new flow rates for liquid components 
       this.findOutletLiquid(Vf);
      //5 - Find Q
       return this.getQ();  
     }     
    else
     { 
     ifSolutionFound=false; // if no separation is found there is no solution 
        return 0;
     }
   } 
  catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
    | NoSuchMethodException | NoValueException e) 
  { 
   ifSolutionFound=false; // catch errors that may arise when solving for VF
           //the system have failed to find a solution 
   return 0;
  } 
 
}
  // The following two methods are used to find flash T (case2) 
public double findflashTbasedOnQ() throws SecurityException , IllegalAccessException , IllegalArgumentException , InvocationTargetException, NoSuchMethodException
{ 
 double Tflash=0;  //assume Tflash is first 0 
 boolean ifTempfound=false; 
 
 // Tflash was bound between bubblePointTemperature and dewPointTemperature
 double minT=this.inletStream.bubblePointTemperature(pressure);
 double maxT=this.inletStream.dewPointTemperature(pressure);

 while (ifTempfound != true)
//this loop goes on either until the iteration is out of bound, or until the flashT is found 
//what happens in this while loop is that it will continuously iterate for the flash temperature by setting the bounds as minT and maxT 
//when a solution is not found at that bound, it will then iterate with a more narrow bound (ie try first with a bound of 0-10,and if
//it does not work, it will try again with a bound of 0.01-9.99, then itterate with 0.02-9.98, etc)
//there are many reasons that the flashT can be fail to be found over the large bound, including following reasons:
  //failure to find V/F value
  //Separation is not possible at such flashT
  //flash T does not satisfy the Q=0 requirement
  //thus all of these factors with a large bound could offset the iteration, by going over the iterations numerous times with narrower and naroower bounds
  //this leads to a better iteration value  

 {
  if(minT>maxT)// when this happens the iteration have covered all of the bound 
  {
   System.out.println("No Flash Temperature was Found.");
   ifSolutionFound=false;
   break;
  }
  else// if we did not cover all of the bounds 
  {
   {
    try
    {
     double Qmin=QatTempCase2(minT);// try to see if this temperature bound satisfies all requirement EXCEPT for the Q=0 
    }
    catch(NoValueException e)// if there is any of the errors mentioned above we must truncate the bound again to iterate with a smaller bound
           // these includes like 
    {
     minT=minT+0.1;
    }
    try
    {
     double Qmax=QatTempCase2(maxT);// try to see if this temperature bound satisfies all requirement except for the Q=0 
    }
    catch(NoValueException e)// if there is any of the errors mentioned above we must truncate the bound again to iterate with a smaller bound
    {
     maxT=maxT-0.1;
    }
   
    try
    {
     Tflash=numericalMethods.riddersMethod(0, this,"QatTempCase2", minT,maxT, this);// try to iterate for TFlash with the bound 
     if(numericalMethods.rootCheckForRd==false)
     {
      ifTempfound=false;
      throw new NoValueException("");
     }
     else
     {
      //5 - FIND Q   
      ifTempfound=true;
      return Tflash;
     }
    
    }

     catch (NoValueException e)// If the iteration fails to find a value that satisfies Q=0, we truncates the bound and try itterating again
     { 
          minT=minT+0.01;
          maxT=maxT-0.01;
     } 
   }
  }
 }
 ifSolutionFound = false;
 return Tflash;
 
}

/*the following method checks if at a certain operating flash temperature such that the stream is separable
 * it checks if V/F value exists 
 * sets all the chemical components within the flash outlet streams with the V/F values 
 * and then it checks if Q=0 for that flash temperature 
 * This method will be what findflashTBasedonQ() will be iterating over 
 * */
private double QatTempCase2(double tempFlash) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, NoValueException
{
 // set these flash streams to the iterated temperature 
 this.outletLiquid.setTemperature(tempFlash);
 this.outletVapour.setTemperature(tempFlash);
  
 // check for if separable
 boolean ifSeperatable=this.isSeparable();
 
 //If the numerical method's static variable has been set for false after that method was called, this means that it was not separable at this TFlash
 //In theory, this should always be within separable since the temperature flash should be itterating over bubblepoint or dewpoint
 //but this is set in case the last iteration accidently went slightly off bounds (set by the loop), and so that it would catch the error before further calculations
 if(numericalMethods.rootCheckForRd==false|| ifSeperatable==false)
 {
  throw new NoValueException("");   
 }
 
 double VF=findVFRatio();
 
 //if V/F ratio is out of bounds or the numerical method's static variable has been set to false, this means that V/F value can not be found at this Tflash
 if (VF>1||VF<0||numericalMethods.rootCheckForRd==false)
 {  
   throw new NoValueException("");
  
 }
 else// in the case that it is separable and V/F value can be found 
 {
  //FIND AND SET THE NEW FLOWRATES FOR VAPOUR COMPONENTS
   this.findOutletVapour(VF);
  //FIND AND SET THE NEW FLOWRATES FOR LIQUID COMPONENTS
   this.findOutletLiquid(VF);  
 } 
  //FIND Q     
  return this.getQ();  
}


/*The following methods are used to determine inlet temperature (case 3) 
 *This method performs in a similar manner to the method ReturnQCase1()  (case1)
 * The difference is that it iterates for a Inlet temperature at the end such that it would satisfies the energy balance of  Q=0 
 * */
public double ReturnTinlet()  
{
 outletLiquid.setTemperature(this.temperature);
 outletVapour.setTemperature(this.temperature);
 double temp=0;//inlet temperature 

 //Note that inlet Temperature must be greater than the flash temperature due to the dynamics of a flash seperator 
 double maxT=this.temperature+300;// set maximal of the inlet temperature to be 1000K greater 
 double minT=outletLiquid.getTemperature();// set the minimal temperature to be at T flash 
  
 try{ 
 // same as ReturnQCase1()
   if(this.isSeparable()== true)
   {
    double VF=findVFRatio(); 
    this.findOutletVapour(VF); 
    this.findOutletLiquid(VF);
     temp=numericalMethods.riddersMethod(0, this, "QatTempCase3", minT,maxT, this);// This iterates for the inlet T such that it satisfies the Q=0 equation 

    if(numericalMethods.rootCheckForRd == false)
    {
     temp=0;
     
     throw new NoValueException("");
    } 
    else
    {
        return temp;  
    }
   }
   else
   {
       ifSolutionFound=false;
   }
  } 
  catch (SecurityException | IllegalAccessException | IllegalArgumentException
    | InvocationTargetException |NoSuchMethodException e) 
  {    
   ifSolutionFound=false;
  }
  catch (NoValueException e)
  {
   System.out.println("No inlet temperature was found for this seperation to be fesasible.");
   ifSolutionFound=false;
  }
  return 0;  

}

public double QatTempCase3(double inletTemp) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
{
 this.inletStream.setTemperature(inletTemp); 
 double Q=getQ();// find Q with this inlet Temperature declared 
 return Q;  
}

//ALL  METHODS BELOW ARE SHARED BY ALL 3 CASES

/*the following method checks for separation based on dew point and bubble point
 * It does so by getting the inlet Stream's dew point and bubble point temperature and comparing it to the outlet stream's temperature 
 * however, in none iteration cases , the system can exit if no bubble point or dew point is found 
 * in other cases this may require several checks 
 * */
public boolean isSeparable() throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException 
{
 if(caseNum==2) //Case 2 needs iteration, therefore try and catch block can be omitted 
 {
  if ((outletLiquid.getTemperature() < inletStream.dewPointTemperature(this.pressure)) && (outletLiquid.getTemperature() > inletStream.bubblePointTemperature(this.pressure)))
  { 
   return true;
  }
  else 
  {
   return false;
  }
 }
 else
 {
  try//otherwise when an error is caught , it will set the boolean ifSolutionFound to false, and execution will stop. 
  {
   double dewP=inletStream.dewPointTemperature(this.pressure);

   //check to see if the the following dew point and buble point temperature exist 
   //if they do not system exits 
   if(numericalMethods.rootCheckForRd==false)
   {
    throw new NoValueException(" not dew point temperature found");
   }
   
   double bubbleP=inletStream.bubblePointTemperature(this.pressure);
   if(numericalMethods.rootCheckForRd==false)
   {
    throw new NoValueException("no bubble point temperature found");
   }   //check to see if separation is possible 
   if ((outletLiquid.getTemperature() < dewP) && (outletLiquid.getTemperature() >bubbleP))
   { 
    return true;
   }
   else 
   {
    return false;
   }
  }
  catch(NoValueException e)
  {  
    System.out.println("the bubble point or dew point calculation failed to be performed");
    ifSolutionFound=false;
  
  }
  return false;// default 
 }
}

//the following method uses the V/F ratio to determine the liquids component
//It then goes on to set all the chemical component of the liquid stream with the corresponding molecular weight  
public void findOutletLiquid(double VoverF)
{
 Chemical[] outletLiquid = new Chemical[inletStream.getChemicals().length];
 
 double xi,chemicalFlowRate;
 double LiquidFlowRate=(1-VoverF)*inletStream.totalFlowrate();
 double vapourFlowRate=VoverF*inletStream.totalFlowrate();
 for(int i = 0; i < inletStream.getChemicals().length; i++)
  {
   outletLiquid[i] = inletStream.getChemical(i).clone();
   xi=this.inletStream.getXi(i, inletStream.totalFlowrate(),this.outletLiquid.getTemperature(), vapourFlowRate, pressure); 
   chemicalFlowRate= LiquidFlowRate*xi; 
   outletLiquid[i].setFlowrate(chemicalFlowRate);
  }
 this.outletLiquid.setChemicals(outletLiquid);
}

//the following method uses the V/F ratio to determine the vapour component
//It then goes on to set all the chemical component of the vapour stream with the corresponding molecular weight 

public void findOutletVapour(double VoverF)
{
 Chemical[] outletVapourChemical = new Chemical[inletStream.getChemicals().length];
 
 double xi=0;
   double yi=0;
   double chemicalFlowRate=0;
 double VapourFlowRate=VoverF*inletStream.totalFlowrate();
 for(int i = 0; i < inletStream.getChemicals().length;i++)
  {
   outletVapourChemical[i] = inletStream.getChemical(i).clone();
    
   xi=this.inletStream.getXi(i, inletStream.totalFlowrate(),outletLiquid.getTemperature(), VapourFlowRate, pressure);
   yi=this.inletStream.getYi(i, pressure, xi, outletLiquid.getTemperature());
    chemicalFlowRate=VapourFlowRate*yi;
   outletVapourChemical[i].setFlowrate(chemicalFlowRate);
  
  }

 this.outletVapour.setChemicals(outletVapourChemical);
}
//This method performs the energy calculations based on the inlet and outlet vapours's enthalpy
public double getQ()
{
 {
   double Q;
   double Tout=outletLiquid.getTemperature();
   double Tin=inletStream.getTemperature();
   Q=outletLiquid.liquidEnthalpy(pressure,Tout)+outletVapour.vapourEnthalpy(pressure,Tout)+outletVapour.totalHeatofVapourization(pressure)-inletStream.inletEnthalpy(Tin,pressure);
   return Q; 
 } 
}

//This Method is the function of G(V/F)  
//Other methods iterates over this method with different V/F values to determine one that would result in the G(V/F)=0
public double findSumG(double VFratio)
{
double g=0;
double ki=0;
for(int i=0;i<inletStream.getChemicals().length;i++)
{ 
 ki= this.inletStream.Ki(i, this.pressure, outletLiquid.getTemperature());
 double zi=inletStream.moleFraction(i);
 g+=zi*(ki-1)/(1+(ki-1)*(VFratio));
}
 
return g;
}

//This method iterates for the method above (findSumG(double VFratio)) 
public double findVFRatio() throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,NoValueException, NoSuchMethodException
{
 
   if(caseNum==2)// only in case 2 you need iteration , so it will let the method that is calling this method deal with the error handling
  {
   double VoverF = 0;// initially set to 0 
   VoverF = numericalMethods.riddersMethod(0, this, "findSumG", 0, 1, this);  //using ridder's method , find a V/F ratio that results in a f(G) equation that satisfies between 0 and 1
   return VoverF; 
  }
  else
  {   
    try// use try here because if an error is caught it will automatically use this
     {
      double VoverF=0;
      VoverF = numericalMethods.riddersMethod(0, this, "findSumG", 0, 1, this);
      
      if(numericalMethods.rootCheckForRd==false||VoverF<=0||VoverF>1)// if the numerical method fails to give a value or find a value out of bound
      { 
       throw new NoValueException("");
      }
      else
      {
        return VoverF; 
      }
      }
     catch(NoValueException e)
     {
     System.out.println("the system can not find a viable V/F factor that satisifes the solution");
     ifSolutionFound=false;
     return 0;
     } 
   }
  
}


//This is the interface that was implemented to allow for invoking of methods
//By inputing the name of the equation, and this as the object, and the initial X as the value, it will go through ALL methods inside that object, and invoke that method with the value X
//This method is then passed through  
//Because the following method is implemented as an interface , when you call ridder's method and input this 
//It will actually first find the method below 
//This allows us to use Ridder's method FOR EVERY possible methods in this class or have access to methods in other classes 
public double invokeMethod(String equationName, double Value, Object obj)
{
 double answer;
 answer=0;
 // note that equationName should be in format of functionName,parameter1,parameter2,etc 
  try {
   Method function=obj.getClass().getDeclaredMethod(equationName, double.class);
   answer=(double)function.invoke(obj,Value);
  } catch (IllegalAccessException |IllegalArgumentException|NoSuchMethodException |InvocationTargetException|SecurityException e1) 
{
   if(caseNum!=2) // note that case 2 requires itteration , so this error may be caught for many cases during the itteration, and does not need to printed everytime
   {
       e1.printStackTrace(); 
   }
  }  
return answer;
}

// The following method considers all Inert Streams and this adds the chemicals reserved from the inert gas stream onto the flash streams and the inlet stream again
private void considerInert()
{
 if( counterNumOfInertVapour>0)
 {
      for(int i=0;i<vapourInertStream.getChemicals().length;i++)
      {
       this.inletStream.addChemical(vapourInertStream.getChemical(i).clone());
       this.outletVapour.addChemical(vapourInertStream.getChemical(i).clone());  
      }  
 }
}
/*This is where the properties of all the chemicals are displayed 
 * Note the calculations and everything REALLY happens here 
 * 
 * */
public String toString()
{
 
 String properties="";
 DecimalFormat threeDecimals= new DecimalFormat("###.###");
 if(counterNumOfNonInertChemicals>0)// the system have condensable chemicals, meaning flash was performed
 {
 
  if(caseNum==1)
  {  
   double Q;
  
   try {
    Q = ReturnQCase1();
    if(ifSolutionFound==true)
    {    
     System.out.println("Seperation Performed! "+"\n");
     considerInert();
     properties+=inletStream.StreamProperties(pressure, 0);
     properties+=outletVapour.StreamProperties(pressure, 1);
     properties+=outletLiquid.StreamProperties(pressure, 2);
     properties+="\n Q:"+ threeDecimals.format(Q)+"KJ\n";  
    }
    else
    { 
     properties="Separation Not Viable"; 
    }
   } catch (SecurityException | IllegalArgumentException e)
   { 
    properties="Separation Not Viable"; 
   }  
  }  
  else if(caseNum==2)
  { 
   double Tflash;
   
       try 
       {   Tflash = this.findflashTbasedOnQ();   
     
       if(ifSolutionFound==true)
       {
        properties+="Tflash"+threeDecimals.format(Tflash)+"K";
        properties+="\n Q:"+ threeDecimals.format(this.getQ())+"KJ \n";
        considerInert();
        properties+=inletStream.StreamProperties(pressure, 0);
        properties+=outletVapour.StreamProperties(pressure,1);
        properties+=outletLiquid.StreamProperties(pressure ,2)+"\n";
       }
       else
       {  
        properties+="Separation Not Viable"; 
       }
           
       } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
         | NoSuchMethodException e) 
       {
        e.printStackTrace();
       }          
  }
  else//cases==3
  {  
     {
     double T=this.ReturnTinlet();
     if(ifSolutionFound==true)
     {
     properties+="T"+threeDecimals.format(T)+"K \n";
     properties+="Q:"+threeDecimals.format(getQ())+"KJ \n";
     considerInert();
     properties+=inletStream.StreamProperties(pressure, 0);
     properties+=outletVapour.StreamProperties(pressure, 1);
     properties+=outletLiquid.StreamProperties(pressure,  2)+"\n";  
     }
     else 
     {
        properties+="Separation Not Viable";  
     }
    }
  }
  
    
 }
  else// the system is entire incondensible gases
 {
  System.out.println("No seperation was performed because there is only incondensible gases");
  properties+=vapourInertStream.StreamProperties(pressure,1);
 }
 return properties;
}
 


}
