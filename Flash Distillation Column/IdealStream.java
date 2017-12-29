// this class is a child class to the nonInertStream and it handles the chemicals that are not intert in the system
public class IdealStream extends nonInertStream  
{
	
	//CONSTRUCTORS
	public IdealStream(Chemical[]chemicals)
	{
		super(chemicals);
		super.setTemperature(0.0);// default temperature, will be set for later 
	}
 
	public IdealStream(Chemical[] chemicals, double temperature)
 {
  super(chemicals,temperature);
 }
 
	public IdealStream(IdealStream original)
 {
  super(original);
 }

	//CLONE
	
	public IdealStream clone()
  {
   return new IdealStream(this);
  }
 
	//METHODS
	
	public double Ki(int elementIndex, double pressure,double flashT)
	{
		//Ki = (Pi_sat)/(P)
		return getChemical(elementIndex).saturationPressure(flashT)/pressure;
	}

	public double findTotalPressure(double temperature)
 {
  return super.findTotalPressure(temperature);
 }
 
	public double findInverseOfTotalPressure(double temperature)
 {
  
  return super.findInverseOfTotalPressure(temperature);
 }
 


}
