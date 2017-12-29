// this class is for the non-ideal streams. It extends the nonInertStream class and thus it handles chemicals that are not inert in the system

public class NonIdealStream extends nonInertStream  
{ 

	//CONSTRUCTORS
	public NonIdealStream(Chemical[]chemicals)
 {
  super(chemicals);
  super.setTemperature(0.0);// default temperature, will be set for later 
 }
 
	public NonIdealStream(Chemical[] chemicals, double temperature)
 {
  super(chemicals,temperature);
 }
 
	public NonIdealStream(NonIdealStream original)
 {
  super(original);
 }
    
	//CLONING
	public NonIdealStream clone()
	{
		System.out.println("the input stream temp"+this.getTemperature());
		return new NonIdealStream(this);
	}

	//METHODS
	//methods below are used to find fugacity in the liquid and vapour streams to find the Ki value 
	//Ai , Bi, A and B are all parameters used to solve the cubic equation of state 
	//matrices are since the parameters in the cubic euqations takes into consideration the interactions between two species at a time 
  
	public double[] findAi(double temperature, double pressure)
	{
		double Ai[] = new double[super.getChemicals().length];
		for(int i = 0; i < Ai.length; i++)
		{
			Ai[i] = super.getChemical(i).Ai(temperature, pressure);
		}
		return Ai;
	}

	public double[] findBi(double temperature, double pressure)
	{
		double Bi[] = new double[super.getChemicals().length];
  
		for(int i = 0; i < Bi.length; i++)
		{
			Bi[i] = super.getChemical(i).Bi(temperature, pressure);
		}
		return Bi;
	}
 
	public double findA(double temperature, double pressure)
	{
		int count = super.getChemicals().length;
 
		double Ai[] = this.findAi(temperature, pressure);
  
		double A2Dmatrix[][] = new double[count][count];
  
		for(int i = 0; i < count; i++)
		{
			for(int j = 0; j < count;j++)
			{
				A2Dmatrix[i][j]=Math.sqrt(Ai[i]*Ai[j]);
			}
		}
		//Z matrix is the feed composition 
		double Zmatrix[] = new double[Ai.length];
  
		for(int i = 0; i < Ai.length; i++)
		{
			Zmatrix[i] = super.moleFraction(i);
		}
  
		double A1Dmatrix[] = new double[Ai.length];
   
  for(int i = 0; i < Ai.length; i++)
  {
   for(int j = 0;j< Ai.length;j++)
   {
    A1Dmatrix[i]+=Zmatrix[j]*A2Dmatrix[i][j];
   }
  }
  
  double A = 0;
  
  for(int i = 0; i < Ai.length;i++)
  {
   A += A1Dmatrix[i]*Zmatrix[i];
  }
  
  return A; 
 }
 
	public double findB(double temperature, double pressure)
 {
  double Bi[] = this.findBi(temperature, pressure);
  double B = 0;
  double Zmatrix[] = new double[Bi.length];
  
  for(int i = 0; i < Bi.length; i++)
  {
   Zmatrix[i] = super.moleFraction(i);
  }
   //this is the loop that finds the B value
   for(int i = 0; i < Bi.length; i++)
   {
    B += Zmatrix[i]*Bi[i];
   }
   
   return B;
 }
 
	// this method find the Z values in the cubic equation of state 
	// Z^3 + c2 Z^2 + c1 Z + c0 = 0
	public double[] findZ(double temperature, double pressure) 
 {
  
  double A = this.findA(temperature, pressure);
  double B = this.findB(temperature, pressure);
  
  //cubic coefficients
  double c0 = -(A*B-Math.pow(B, 2)-Math.pow(B, 3));
  double c1 = A-3.*B*B-2.*B;
  double c2 = -(1.-B);

  double p = (3.*c1-c2*c2)/3;
  double q = (2.*Math.pow(c2, 3)-9*c2*c1+27.*c0)/27.;
  double D = q*q/4.+Math.pow(p, 3)/27.;
    // D is used to find of the cubic equation of state will have one or three roots
  // if D>0 then only one root 
  //if D<0 then three roots. The lowest root is the Z in liquid and the highest root in the Z in vapour 
  
  if(D>0)
  {
   
  
 double P = Math.pow((-q/2.)+Math.sqrt(D),1./3.);
   double Q = Math.pow((-q/2.)-Math.sqrt(D),1./3.);
   
   double result[] = new double[1];
   result[0] = (P+Q)-c2/3.;
   
   return result;
 
  }
  
  else
  { 
   double m = 2.*Math.sqrt(-p/3.);
   double n = 3.*q/(p*m);
   
   double theta = Math.acos(n)/3.;
   
   double root1 = m *Math.cos(theta);
   double root2 = m*Math.cos(theta+4.*Math.PI/3.);
   double root3 = m*Math.cos(theta+2.*Math.PI/3.);
   double Z[] = new double[3];
   Z[0] = root1-c2/3.;
   Z[1] = root2-c2/3.;
   Z[2] = root3-c2/3.;
  
   double result[] = new double[2];
   result[0] = Z[0];// maxZ
   result[1] = Z[0];//minZ
   
   for(int i = 1; i < 3; i++)
   {
    if((Z[i]>result[0]) && Z[i] > 0)
    {
     result[0] = Z[i];

    }
    
    if((Z[i]<result[1]) && Z[i] > 0)
    {
     result[1]=Z[i];
    }
 
   } 
   return result;
  }
 }
 
	private double findFugacity(int elementIndex, double temperature, double pressure, boolean isLiquid)
 {
  double Bi = super.getChemical(elementIndex).Bi(temperature,pressure);
  double B = this.findB(temperature, pressure);
  double A = this.findA(temperature, pressure);
  double sumXA = 0;
  
  for(int i = 0; i < super.getChemicals().length; i++)
  {
   sumXA += super.moleFraction(i)*Math.sqrt(super.getChemical(i).Ai(temperature,pressure)*super.getChemical(elementIndex).Ai(temperature,pressure));
  }
  
  if (isLiquid)
  {
   
   double ZL = this.findZ(temperature, pressure)[1];
   return Math.exp((Bi/B)*(ZL-1)-Math.log(ZL-B)-(A/(2.8284*B))*Math.log((ZL+2.4142*B)/(ZL-0.4142*B))*(2*(sumXA/A)-(Bi/B)));
    
  }
  else
  {
   double ZV;
  
   ZV = this.findZ(temperature, pressure)[0];
     return Math.exp((Bi/B)*(ZV-1)-Math.log(ZV-B)-(A/(2.8284*B))*Math.log((ZV+2.4142*B)/(ZV-0.4142*B))*(2*(sumXA/A)-(Bi/B)));
   
   
  }
 }

	public double findTotalPressure(double temperature)
 {
     return super.findTotalPressure(temperature);
 }
 
	public double findInverseOfTotalPressure(double temperature)
 {
  
  return super.findInverseOfTotalPressure(temperature);
 }
 
	public double Ki(int elementIndex, double pressure,double temperature)
 { 
  return this.findFugacity(elementIndex,temperature,pressure,true)/this.findFugacity(elementIndex,temperature,pressure,false);
 }

}
