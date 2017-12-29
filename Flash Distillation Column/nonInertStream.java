import java.text.DecimalFormat;

//THIS IS THE PARENT CLASS OF NON-IDEAL AND IDEAL CLASSES FOR CONDENSIBLE COMPONENTS

public abstract class nonInertStream extends Stream
{
	//CONSTRUCTORS
	public nonInertStream(Chemical[]chemicals)
	{
		super(chemicals);
	}
  
	public nonInertStream(Chemical[] chemicals, double temperature)
	{ 
		super(chemicals,temperature);
	}

	public nonInertStream(nonInertStream original)
	{
		super(original);
	}
	
	//CLONING
	public abstract Stream clone();
	
	//METHODS
	public abstract double Ki(int index, double pressure, double Tflash);
   
	//Calculates mole fraction for a liquid stream
	public double getXi(int elementIndex,double F, double inletT,double V,double pressure)
  {
   double xi;
   xi=(this.moleFraction(elementIndex)*F)/(F+(this.Ki(elementIndex, pressure,inletT)-1)*V);
   return xi;
  }
	
	//Calculating mole fraction for a vapour stream
	public double getYi(int elementIndex,double pressure,double xi,double inletT)
  {
   double yi;
   yi=xi*this.Ki(elementIndex, pressure,inletT);
   return yi;
  }
  
	public double totalHeatofVapourization(double pressure)
	{
		double sumVp=0;
		double Tr;
		try 
		{
			for(int i=0;i<this.getChemicals().length;i++)
			{
				if(this.moleFraction(i)>0)
				{
					Tr=this.getChemical(i).findBoilingPoint(pressure)/this.getChemical(i).getCriticalTemperature();
					sumVp+=this.getChemical(i).getFlowrate()*this.getChemical(i).getHeatOfVaporization(Tr); 
				}
			} 
		}
		catch (SecurityException | IllegalArgumentException e) {
			System.out.println("error in calculating heat of vaporization");
			e.printStackTrace();
			System.exit(0);
		}
		return sumVp;
	}

	public double inletEnthalpy(double Tinlet,double pressure)
  {
   //Assumes that the Inlet Stream is all liquid and uses the lowest bubble point as the reference temperature
  
   double enthalpy=0;
   
    for(int i=0;i<this.getChemicals().length;i++)
    {
     enthalpy+=this.getChemical(i).getFlowrate()*this.getChemical(i).getEnthalpy(Tinlet,this.findLowestBubblePointTemperature(pressure),true );
    } 

   return enthalpy;
  }
  
	public double liquidEnthalpy(double pressure,double operatingTemperature)
	{	 
		double enthalpy=0; 
		double Tref;
   
		Tref=this.findLowestBubblePointTemperature(pressure);
   
		for(int i=0;i<this.getChemicals().length;i++)
		{		
			if(this.moleFraction(i)>0)
			{
				enthalpy+=this.getChemical(i).getFlowrate()*this.getChemical(i).getEnthalpy(operatingTemperature,Tref,true );
			}
		} 
		return enthalpy;
	}
	
	public double vapourEnthalpy(double pressure,double operatingTemperature)
  { 
  // uses the lowest bubble point as the reference temperature
    
   double Tref=this.findLowestBubblePointTemperature(pressure);
   double TboilingPoint;
   double enthalpy=0;
   
   for(int i=0;i<this.getChemicals().length;i++)
   {
    TboilingPoint=this.getChemical(i).findBoilingPoint(pressure);
    if(this.moleFraction(i)>0)
    {
      //multiplying the flow rate by the enthalpy 
      enthalpy+=this.getChemical(i).getFlowrate()*this.getChemical(i).getEnthalpy(TboilingPoint,Tref,true );
    }

   } 

   
    for(int i=0;i<this.getChemicals().length;i++)
    {
     TboilingPoint=this.getChemical(i).findBoilingPoint(pressure);
     if (this.moleFraction(i)>0)
     {  // integrating the Cpl with respect with the boiling point and the flash temperature 
      enthalpy+=this.getChemical(i).getFlowrate()*8.314*this.getChemical(i).getEnthalpy(operatingTemperature,TboilingPoint,false);
     }
    } 
   return enthalpy;
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
    properties+="\nFeed stream: \n";
    properties+=this.toString();
    
   }
   else// outlet streams
   {
    properties+="\nIn outlet: ";
    
    if(streamType==1)// vapour stream
    { 
     properties+=threeDecimals.format(this.totalFlowrate())+"kmols/h ";
     properties+="of GAS\n\n";
     properties+="temperature: "+threeDecimals.format(this.getTemperature())+"K \n\n";
     properties+="compositions: \n";
    
     for (int i=0;i<this.getChemicals().length;i++)
     {
      properties+= threeDecimals.format(100*this.moleFraction(i))+"%  ";
      properties+=this.getChemical(i).toString();
     }  
    }
    else// liquid 
    { 
     properties+=threeDecimals.format(this.totalFlowrate())+"kmols/h of ";
     properties+="of LIQUID\n\n";
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
