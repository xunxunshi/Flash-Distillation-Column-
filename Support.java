import java.util.Scanner;
import java.util.InputMismatchException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
  
//Helps with the validation for proper input values. All methods in this class will be static and do not require constructors.

public final class Support
{
	//Scans and validates if INTEGER input from user is between bounds. If not valid input, displays error message
	public static int inputInt(String errorMessage, int lower_bound, int upper_bound)
	{
		int userInput = -1; //Initializes to -5000 since 0 might be an option
		
		while (userInput < lower_bound || userInput > upper_bound)
		{
			boolean inputIsInteger = false;
			Scanner keyboard = new Scanner(System.in);
			while (!inputIsInteger)
			{
				try
				{
					userInput = keyboard.nextInt();
					inputIsInteger = true;
				}
				catch(InputMismatchException e)
				{
					keyboard.nextLine();
					System.out.println(errorMessage);
				}
			}
			if (userInput < lower_bound || userInput > upper_bound)
				System.out.println("The input is outside the allowable bounds");
		}
    
		return userInput;
	}

	//Scans and validates if DOUBLE input from user is between bounds. If not valid input, displays error message
	public static double inputDouble(String errorMessage, int lower_bound, int upper_bound)
	{
		double userInput = 0.0;
		Scanner keyboard = new Scanner(System.in);
		do
		{
			boolean inputIsDouble = false;
			while (!inputIsDouble)
			{
				try
				{
					userInput = keyboard.nextDouble();
					inputIsDouble = true; //input successful
				}
				catch(InputMismatchException e)
				{
					keyboard.nextLine();
					System.out.println(errorMessage);
				}
			}
			if (userInput < lower_bound || userInput > upper_bound)
				System.out.println("The input is outside the allowable bounds. Please enter a number between " + lower_bound + " and " + upper_bound);
		} while (userInput < lower_bound || userInput > upper_bound);
  
		return userInput;
	}

	public static void setChemicalData(int j, String file_name, String chem_name)
	{
		Scanner keyboard = new Scanner(System.in);
		String variable = null;
		String units = null;
		//make sure - String marker - has NO SPACES, and is followed by a colon and a space, ex: "MolarMass: "
		String marker = null;
		//this is the value that is written to the text file
       
		//check if we are dealing with multivariable constants. ex: A, B, C. This affects how the markers are placed
		boolean multiValueCheck = false;
		
		//adding some limits to the values that can be inputted 
		double lower_limit = -1000000000;  //priliminary limit, should be overwritten below
		double upper_limit =  1000000000;  //Priliminary limit, should be overwritten below
      
		//used for multi variable inputs
		String letter = null;
       	if (j == 0)
       	{
       		variable = "Molar Mass";
       		marker = "MolarMass: ";  //BE VERY CAREFUL WITH THE MARKERS, follow format of one word followed by a colon
       		units = "g/mol";
       		lower_limit = 0;
         	upper_limit = 1000000;
       	}
       	else if (j == 1)
       	{
    	   		variable = "Critical Temperature";
    	   		marker = "CriticalTemperature: ";
    	   		units = "Kelvin";
    	   		lower_limit = -273.15;
    	   		upper_limit = 10000; //
       	}	
       	else if (j == 2)
       	{
       		variable = "Critical Pressure";
       		marker = "CriticalPressure: ";  
       		units = "bar";
       		lower_limit = 0; //none of that vacuum stuff
       		upper_limit = 1000; //higher pressure will make our assumptions unreasonable
       	}
       	else if (j == 3)
       	{
       		variable = "Acentric Factor";
       		marker = "AcentricFactor: ";
       		units = "unitless";
       		//must be between 0 and 1
       		lower_limit = 0;
       		upper_limit = 1;
       	}
       // A little more tricky with multiple values, use an array
       else if (j == 4)
       {
         multiValueCheck = true;
         //not really sure what the limits should be
         variable = "Antoine's Constant";
         marker = "AntoinesConstants: ";
         marker.toLowerCase();
         System.out.println("The Constants should be obtained from Appendix B, Table B.2 in: \n"+
                              "Introduction to Chemical Engineering 7th Edition, in SI Units \nJ.M.Smith, H.C.Van Ness and M.M.Abbott \n" +
                            "or any reputable source that outputs the vapour pressure in kPa using T in Kelvin.\n");
         for (int k = 0; k < 3; k++)
         {
           //VERY IMPORTANT: the first constant will contain the marker but not the rest
           if (k == 0)
           {
             letter = "A";
             System.out.println("What is the " + variable + " " + letter + " for T in Kelvin and Psat in kPa? \n");
             double theValue = Support.inputDouble("Error. Please enter a valid numerical " + variable, -1000000000, 100000000);
             Support.readToFile(file_name,marker + "\n" + Double.toString(theValue) + " ");
             continue;
           }
           if (k==1)
           {
             letter = "B";
           }
           if (k==2)
           {
             letter = "C";
           }
             System.out.println("What is the " + variable + " " + letter + " for T in Kelvin and Psat in kPa? \n");
             double theValue = Support.inputDouble("Error. Please enter a valid numerical " + variable, -1000000000, 100000000);
             Support.readToFile(file_name, Double.toString(theValue) + " ");
         }
       }
///
       
       else if (j==5)
       {
         multiValueCheck = true;
         //not really sure what the limits should be
         variable = "Gas Heat Capacity Constants: ";
         marker = "Gas_Heat_Cap_Constants: ";
         marker.toLowerCase();
         System.out.println("The Constants should be obtained from Appendix C, Table C.1 in: \n"+
                              "Introduction to Chemical Engineering 7th Edition, in SI Units \n by J.M.Smith, H.C.Van Ness and M.M.Abbott");
         for (int k = 0; k < 4; k++)
         {
           //VERY IMPORTANT: the first constant will contain the marker but not the rest
           if (k==0)
           {
             letter = "A";
             System.out.println("What are the " + variable + " " + letter +" from Appendix C, Table C.1? \n");
             double theValue = Support.inputDouble("Error. Please enter a valid numerical " + variable, -1000000000, 100000000);
             Support.readToFile(file_name,marker + "\n" + Double.toString(theValue) + " ");
             continue; //Do not want to run the remaining lines
           }
           if (k==1)
           {
             letter = "B";
           }
           if (k==2)
           {
             letter = "C";
           }
           if (k==3)
           {
             letter = "D";
           }
                      
           System.out.println("What is the " + variable + " " + letter + " from Appendix C, Table C.1? \n");
           double theValue = Support.inputDouble("Error. Please enter a valid numerical " + variable, -1000000000, 100000000);
           Support.readToFile(file_name, Double.toString(theValue) + " ");
           }
         }
///
      else if (j==6)
       {
         multiValueCheck = true;
         //not really sure what the limits should be
         variable = "Liquid Heat Capacity Constants: ";
         marker = "Liquid_Heat_Cap_Constants: ";
         marker.toLowerCase();
         System.out.println("The Constants should be obtained from Table 2-153 in: \n"+
                              "Perry's Handbook 7th edition or newer \n");
         for (int k = 0; k < 5; k++)
         {
           //VERY IMPORTANT: the first constant will contain the marker but not the rest
           if (k==0)
           {
             letter = "C1";
             System.out.println("What are the " + variable + " " + letter +" from Table 2-150? \n");
             double theValue = Support.inputDouble("Error. Please enter a valid numerical " + variable, -1000000000, 100000000);
             Support.readToFile(file_name,marker + "\n" + Double.toString(theValue) + " ");
             continue; //Do not want to run the remaining lines
           }
           if (k==1)
           {
             letter = "C2";
           }
           if (k==2)
           {
             letter = "C3";
           }
           if (k==3)
           {
             letter = "C4";
           }
           if (k==4)
           {
             letter = "C5";
           }
                      
           System.out.println("What is the " + variable + " " + letter + " from Table 2-150? \n");
           double theValue = Support.inputDouble("Error. Please enter a valid numerical " + variable, -1000000000, 100000000);
           Support.readToFile(file_name, Double.toString(theValue) + " ");
         }
      }
///   
            else if (j==7)
       {
         multiValueCheck = true;
         //not really sure what the limits should be
         variable = "Heat of Vaporization Capacity Constants: ";
         marker = "Latent_Heat_Constants: ";
         marker.toLowerCase();
         System.out.println("The Constants should be obtained from Table 2-150 in: \n"+
                              "Perry's Handbook 7th edition or newer ");
         for (int k = 0; k < 5; k++)
         {
           //VERY IMPORTANT: the first constant will contain the marker but not the rest
           if (k==0)
           {
             letter = "C1";
             System.out.println("What are the " + variable + " " + letter +" from Table 2-150? \n");
             double theValue = Support.inputDouble("Error. Please enter a valid numerical " + variable, -1000000000, 100000000);
             Support.readToFile(file_name,marker + Double.toString(theValue) + " ");
             continue; //Do not want to run the remaining lines
           }
           if (k==1)
           {
             letter = "C2";
           }
           if (k==2)
           {
             letter = "C3";
           }
           if (k==3)
           {
             letter = "C4";
           }
           if (k==4)
           {
             letter = "C5";
           }
                      
           System.out.println("What is the " + variable + " " + letter + " from Table 2-150? \n");
           double theValue = Support.inputDouble("Error. Please enter a valid numerical " + variable, -1000000000, 100000000);
           Support.readToFile(file_name, Double.toString(theValue) + " ");
         }
      }
///
    
       if (!multiValueCheck)
         //helps sure that that the marker is always consistent regardless of programmer error
       {
         marker.toLowerCase();
         boolean reasonable = false;
         do
         {
           double theValue = -1;
           System.out.println("What is the " + variable + " of " + chem_name + " in " + units +  "? \n");
           theValue = Support.inputDouble("Error. Please enter a valid numerical " + variable, -1000000000, 100000000);  
           if (theValue <= lower_limit)
             System.out.println("The entered value is below the lower limit allowed");
           else if (theValue >= upper_limit)
             System.out.println("The entered value is above the upper limit allowed");
           else
           {
           reasonable = true;
           Support.readToFile(file_name,marker + Double.toString(theValue) + " " + units);
           }
         }
         while (!reasonable);
       }
}

	//Checks if the folder exists. If not, it creates one.
	public static void checkFolderExists(String folderName)
	{
		File finder = new File(folderName);
     
		if (finder.isDirectory() == false)
		{	
			System.out.println("Directory not found. Created the proper directory.");
			finder.mkdir();
		}
	}

	//Asks a yes or no question to the user and turns answer into true or false
	public static boolean yes_no(String question)
	{
		Scanner keyboard = new Scanner(System.in);
		boolean returnAnswer = false;
		System.out.println(question);
		String yes_no = "Placeholder";
	
		while (yes_no.equals("Great Success!") != true)
		{
			yes_no = keyboard.nextLine();
			if (yes_no.equals("y") || yes_no.equals("yes") || yes_no.equals("Yes"))
			{
				returnAnswer = true;
				yes_no = "Great Success!";
			}
			else if (yes_no.equals("n") || yes_no.equals("no") || yes_no.equals("No"))
			{
				returnAnswer = false;
				yes_no = "Great Success!";
			}
			else
			{      
				System.out.println("Please enter either \"Yes\" or \"No\".");
			}
		}
		return returnAnswer;
	}

	//Attempts to obtain the name of a chemical in a file. Returns null if no chemical name was found.
	public static String getChemicalName(String file_name)
	{
		//define all the variables that will be obtained here
		String chemical = null;
		Scanner inputStream = null;
  
		try
		{
			inputStream = new Scanner(new FileInputStream(file_name));
		}
		catch (FileNotFoundException e)
		{
			System.out.println("This file does not exist");
		}
   
		
		String information = null;
		//Looks for information that displays the chemical name
		while (inputStream.hasNext())
		{
			information = inputStream.next();
			if(information.equals("Chemical:"))
			{
				chemical = inputStream.next();
				break;
			}
			else
			{
				System.out.println("Error! Name of the chemical not found!");
			}
		}	
		return chemical;
	}

	//Returns an array containing the properties of a chemical from a file
	public static double[][] getProperties(String file_name)
	{
		//read the file using inputStream
		Scanner inputStream = null;
		double[][] properties = new double[8][];
		//   properties [0] = molar_mass;
		//   properties [1] = t_crit;
		//   properties [2] = p_crit;
		//   properties [3] = acentric_factor;
		for (int i = 0; i < 4; i++)
		{
			properties[i] = new double[1];
		}
		properties[4] = new double[3];  //Antoine_constants
		properties[5] = new double[4];  //Gas_heat_constants
		properties[6] = new double[5];  //Liquid_heat_constants
		properties[7] = new double[5];  //Latent_heat_constants
   
		try
		{
			inputStream = new Scanner(new FileInputStream(file_name));
		}
			catch (FileNotFoundException e)
		{
			System.out.println("This file does not exist");
		}
		
		String information = null;
		while (inputStream.hasNext())
		{
			information = inputStream.next();
			if (information.equals("MolarMass:") && inputStream.hasNextDouble())
				properties[0][0] = inputStream.nextDouble();
   
			if (information.equals("CriticalTemperature:") && inputStream.hasNextDouble())
				properties[1][0] = inputStream.nextDouble();
 
			if (information.equals("CriticalPressure:") && inputStream.hasNextDouble())
				properties[2][0] = inputStream.nextDouble();
  
			if (information.equals("AcentricFactor:") && inputStream.hasNextDouble())
				properties[3][0] = inputStream.nextDouble();
 
			if (information.equals("AntoinesConstants:") && inputStream.hasNextDouble())
			{
				for (int i = 0; i < 3; i++)
				{
					if (inputStream.hasNextDouble())
					{
						properties[4][i] = inputStream.nextDouble();
					}
					else
					{
						System.out.println("There are missing Antoines constants!");
						break;
					}
				}
			}
			if (information.equals("Gas_Heat_Cap_Constants:") && inputStream.hasNextDouble())
			{
				for (int i = 0; i < 4; i++)
				{
					if (inputStream.hasNextDouble())
					{
						properties[5][i] = inputStream.nextDouble();
					}
					else
					{
						System.out.println("There are missing gas heat capacity constants!");
						break;
					}
				}
			}
			if (information.equals("Liquid_Heat_Cap_Constants:") && inputStream.hasNextDouble())
			{
				for (int i = 0; i < 5; i++)
				{
					if (inputStream.hasNextDouble())
					{
						properties[6][i] = inputStream.nextDouble();
					}
					else
					{
						System.out.println("There are missing liquid heat capacity constants!");
						break;
					}
				}
			}
			if (information.equals("Latent_Heat_Constants:") && inputStream.hasNextDouble())
			{
				for (int i = 0; i < 5; i++)
				{
					if (inputStream.hasNextDouble())
						properties[7][i] = inputStream.nextDouble();
					else
					{
						System.out.println("There are missing latent heat constants!");
						break;
					}
				}
			}  
		}
		return properties;
	}
  
	public static void readChemicalData(String file_name)
	{
		//Obtain the chemical name
		String chemical = getChemicalName(file_name);
		//obtain all the chemical properties
		double[][] properties = getProperties(file_name);
   
		//after the whole textfile is read, the following displays all of the found data
		
		int check = 0;
		if (chemical != null)
			System.out.println("The following information was found on "+chemical);
		else
		{
			System.out.println("name of chemical not found!");
			check++;
		}
		if (properties[0][0] != 0.0)
			System.out.println(chemical + "'s Molar Mass is: " + properties[0][0]);
		else
		{
			System.out.println("Chemical's Molar Mass not found!");
			check++;
		}
		if (properties[1][0] != 0.0)
			System.out.println(chemical + "'s Critical Temperature is: " + properties[1][0]);
		else
		{
			System.out.println("Chemical's Critical Temperature not found!");
			check++;
		}
		if (properties[2][0] != 0.0)
			System.out.println(chemical + "'s Critical Pressure is: " + properties[2][0]);
		else
		{
			System.out.println("Chemical's Critical Pressure not found!");
			check++;
		}
		if (properties[3][0] != 0.0)
			System.out.println(chemical + "'s Acentric Factor is: " + properties[3][0]);
		else
		{
			System.out.println("Chemical's Acentric Factor not found!");
			check++;
		}
		if (properties[4][0] != 0.0 || properties[4][1] != 0.0 || properties[4][0] != 0.0)
		{
			System.out.println(chemical + "'s AntoinesConstants are: \nA: " +properties[4][0]+"\n"
                      +"B: "+properties[4][1] +"\n"
                        +"C: "+properties[4][2]);
		}
		else
		{
			System.out.println("Chemical's Antoine's Constants not found!");
			check++;
		}

		if (properties[5][0] != 0.0 || properties[5][1] != 0.0 || properties[5][2] != 0.0 || properties[5][3] != 0.0)
		{
			System.out.println(chemical + "'s Gas Heat Capacity Constants are: \nA: " + properties[5][0]+"\n"
                      +"B: " + properties[5][1] +"\n"
                        +"C: " + properties[5][2] +"\n"
                     +"D: " + properties[5][3] +"\n");
		}
		else
		{
			System.out.println("Gas Heat Capacity Constants not found!");
			check++;
		}
		if (properties[6][0] != 0.0 || properties[6][1] != 0.0 || properties[6][2] != 0.0 || properties[6][3] != 0.0 || properties[6][4] != 0.0)
		{
			System.out.println(chemical + "'s Liquid Heat Capacity Constants are: \nC1: " + properties[6][0]+"\n"
                      +"C2: " + properties[6][1] +"\n"
                        +"C3: " + properties[6][2] +"\n"
                     +"C4: " + properties[6][3] +"\n"
                     +"C5: " + properties[6][4] +"\n");
		}
		else
		{
			System.out.println("Liquid Heat Capacity Constants not found!");
			check++;
		}
		if (properties[7][0] != 0.0 || properties[7][1] != 0.0 || properties[7][2] != 0.0 || properties[7][3] != 0.0 || properties[7][4] != 0.0)
			System.out.println(chemical + "'s Liquid Heat Capacity Constants are: \nC1: " + properties[7][0]+"\n"
                      +"C2: " + properties[7][1] +"\n"
                        +"C3: " + properties[7][2] +"\n"
                     +"C4: " + properties[7][3] +"\n"
                     +"C5: " + properties[7][4] +"\n");
		else
		{
			System.out.println("Latent Heat Constants not found!");
			check++;
		} 

		if (check > 0)
			System.out.println(check+ " pieces of required information were not found. \n"+
                       "Please check that the file has all the required information. \n"+
                      "IGNORE ERROR if missing liquid heat capacity or latent heat constants for incompressible gases");
	}

	//Prints input string to file_name.txt
	public static void readToFile(String file_name, String input)
	{
		PrintWriter outputStream = null;
		
		boolean append = true;                       
		try
		{
			outputStream = new PrintWriter(new FileOutputStream(file_name, append));                              
		}
		catch (FileNotFoundException e)
		{
			System.out.println("The file could not be created");
		}
		outputStream.println(input);
	}
  
	public static double[] returnArray(int arraySize, double a,double b, double c, double d, double e)
 {
  double[] newArray= new double[arraySize];
  newArray[0]=a;
  newArray[1]=b;
  newArray[2]=c;
  if(arraySize==4)
  {
   newArray[3]=d; 
  }
  if(arraySize==5)
  {
   newArray[3]=d; 
   newArray[4]=e; 
  }
  return newArray;
 }

	//Conversion methods
	public static double convertCelsiusToKelvin(double Celsius)
	{
		return Celsius+273.15;
	}
  
	
	public static double convertKelvinToCelsius(double Kelvin)
	{
		return Kelvin-273.15;
	}
 
	
	public static double convertBarToKPas(double bar)
	{
		return bar*100; //
	}
	

	public static double convertKPasToBar(double Kpas)
	{
		return Kpas/100;
	}
	
	//Checks for temperature and pressure

	public static boolean ifTempWithinRange(Stream inlet, double T, double pressure)
	{
		boolean ifInRange=true;
		if(T<inlet.findLowestBubblePointTemperature(pressure))
		{
			ifInRange=false;
		}
		return ifInRange;
	}
	

	public static boolean ifPressureWithinRange(Stream inlet, double pressure)
	{
		boolean ifInRange=true;
		double lowestSaturationPressure=0;
 
		for(int i = 0; i < inlet.getChemicals().length; i ++)
		{
			if(inlet.getChemical(i).findBoilingPoint(pressure)==inlet.findLowestBubblePointTemperature(pressure))
			{
				lowestSaturationPressure=inlet.getChemical(i).saturationPressure(inlet.getChemical(i).findBoilingPoint(pressure));
			}
		}
		if(lowestSaturationPressure<pressure)
		{
			ifInRange=false;
			System.out.println("Please enter a pressure value that is greater than "+lowestSaturationPressure);
		}
		return ifInRange;
	}
  
}