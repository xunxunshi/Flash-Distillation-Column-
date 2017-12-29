import java.text.DecimalFormat;

/*
 * this class is an extension of the main and serves as a test run purpose
 * this class will test run the 3  seperation cases between chemical cyclohexane, water,pentane 
 * */

public class testRunNonIdeal extends Main
{
	public static void main (String arg[])
	{
		Chemical[] testChemicals = new Chemical[3];
		
		
		DecimalFormat threeDecimals= new DecimalFormat("###.###");
				
		// the following 3D array consists of this format
		// first box represents the chemical name
		// the second array represtns the type of coefficient 
		// the third array represents the actual parameters 

		double [][][] parameters= new double [testChemicals.length][4][];

		parameters[0][0]= Support.returnArray(3,13.6568,2723.44,-52.532,0,0);// antoine parameter
		parameters[0][1]= Support.returnArray(5, -220600,3118.3,-9.4216,0.010687,0);//cpL
		parameters[0][2]= Support.returnArray(4,-3.876,6.32*Math.pow(10, -2),-2.09*Math.pow(10, -5),0,0);//cpV
		parameters[0][3]= Support.returnArray(4,4.49*Math.pow(10, 7),0.3974,0,0,0);//cp heat of vaporization
			
		double flowRate = 45; //kg/s
		double molarMass= 84.16;
		flowRate=flowRate/molarMass;// kmol/L
		double Tc=553.54;
	//	double PC=SupportingDocument.convertBarToPas(40.75);
		double pc=4075;
		boolean ifCPEquation1=true;
		double w=.212;
		Chemical cyclohexane = new Chemical("cyclohexane",flowRate,parameters[0][0],parameters[0][1],parameters[0][2],parameters[0][3],Tc,pc,w,ifCPEquation1,true);
	
		parameters[1][0]= Support.returnArray(3,13.7667,2451.88,-41.136,0,0);// antoine parameter
		parameters[1][1]= Support.returnArray(5, 1.5908E5,-270.5,0.99537,0,0);//cpL
		parameters[1][2]= Support.returnArray(4,2.464,4.5351E-2,-14.11*Math.pow(10,-6),0,0);//cpV
		parameters[1][3]= Support.returnArray(4,3.91*Math.pow(10, 7),0.38681,0,0,0);//cp heat of vaporization
		
		flowRate=45;
		molarMass=72.15;
		flowRate=flowRate/molarMass;
		Tc=469.6;
		pc=3369.;//kpascal
		ifCPEquation1=true;
		w=0.252;
		Chemical pentane = new Chemical("pentane",flowRate,parameters[1][0],parameters[1][1],parameters[1][2],parameters[1][3],Tc,pc,w,ifCPEquation1,true);

		
		parameters[2][0]= Support.returnArray(3,16.3872,3885.7,-42.98,0,0);// antoine parameter
		parameters[2][1]= Support.returnArray(5,276370,-2090.1,8.125,-0.014116,9.3701E-06);//cpL
		parameters[2][2]= Support.returnArray(4,3.47,1.45E-03,0.00E+00,1.21E+04,0);//cpV
		parameters[2][3]= Support.returnArray(4,5.21E+07,0.3199,-0.212,0.25795,0);//cp heat of vaporization
		
		flowRate=5;// in kg, note that the molar mass will be in kmol/s  
		molarMass=18.02;
		flowRate=flowRate/molarMass;
		Tc=647;
		pc=22055;//kpascal
		ifCPEquation1=true;
		w=.3443;
		
		Chemical water = new Chemical("water",flowRate,parameters[2][0],parameters[2][1],parameters[2][2],parameters[2][3],Tc,pc,w,ifCPEquation1,false);
		
		parameters[2][0]= Support.returnArray(3,13.819,2696.040,-48.833,0,0);// antoine parameter
		parameters[2][1]= Support.returnArray(5,172120,-183.78,0.88734,0,0);//cpL
		parameters[2][2]= Support.returnArray(4,3.025,5.37E-02,-1.68E-5,0,0);//cpV
		parameters[2][3]= Support.returnArray(4,4.4544E+07,0.39002,0,0,0);//cp heat of vaporization
		
		flowRate=10;// in kg, note that the molar mass will be in kmol/s  
		molarMass=86.18;
		flowRate=flowRate/molarMass;
		Tc=507.600;
		pc=3025.;//kpasca
		
		
		ifCPEquation1=true;
		w=.301;
		
		Chemical hexane = new Chemical("hexane",flowRate,parameters[2][0],parameters[2][1],parameters[2][2],parameters[2][3],Tc,pc,w,ifCPEquation1,true);
		Chemical[]allChemicals = {pentane, cyclohexane, hexane, water };
		

		double pressureInlet =100;// in Kpas
		//first case
		//the temperautre here is 373 K in inlet 
		double Tinlet=60;// asks the user in celsius 
		Tinlet=Tinlet+273;
		
		Stream inletStream=new NonIdealStream(allChemicals,Tinlet);
	
		if(Support.ifTempWithinRange(inletStream, Tinlet, pressureInlet))
		{
			Flash VesselCase1= new Flash(inletStream,Tinlet, pressureInlet,1,false);
 
			
			try {

				System.out.println("Case 1");
				System.out.println(VesselCase1.toString()+"");
				
			} catch (SecurityException | IllegalArgumentException e) {

				e.printStackTrace();
			}
		}

		else
		{
			if(Support.ifTempWithinRange(inletStream, Tinlet, pressureInlet)!=true)
			{
				
				System.out.println("please enter new temperature");				
			}
		}			
		
		double Toperating =Tinlet;
		Stream flashStream= new IdealStream(allChemicals,Toperating);
		Flash VesselCase3= new Flash(flashStream,Toperating,pressureInlet,3,false);
		
		try {

			System.out.println("\n Case 3");
			System.out.println(VesselCase3.toString()+"");
	
		} catch (SecurityException | IllegalArgumentException e) 
		{
			e.printStackTrace();
		}	
	
		Tinlet =348;	
		Stream inletStreamCase3=new NonIdealStream(allChemicals,Tinlet);
		Flash VesselCase2= new Flash(inletStreamCase3,Tinlet, pressureInlet,2,false); 
		try
		{	System.out.println("Case 2");
			System.out.println(VesselCase2.toString()+"");
		}catch (SecurityException | IllegalArgumentException e) 
		{
			 
		}	
		
		
	}
	
	
}
