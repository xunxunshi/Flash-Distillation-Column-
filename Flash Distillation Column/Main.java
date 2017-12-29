import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.io.File;

public class Main {

public static void main(String[] args) {
 
		//These are necessary incase of poor installation on the user side
      
	 Scanner keyboard = new Scanner(System.in);
	 System.out.println("WELCOME TO GROUP 2'S FLASH VESSEL SIMULATION PROGRAM");
	 int choice = -1;

	 do
	 {
		 System.out.println(" __  __       _         __  __                    ");          
		 System.out.println("|  \\/  |     (_)       |  \\/  |                  ");
		 System.out.println("| \\  / | __ _ _ _ __   | \\  / | ___ _ __  _   _  ");
		 System.out.println("| |\\/| |/ _` | | '_ \\  | |\\/| |/ _ \\ '_ \\| | | | ");
		 System.out.println("| |  | | (_| | | | | | | |  | |  __/ | | | |_| | ");
		 System.out.println("|_|  |_|\\__,_|_|_| |_| |_|  |_|\\___|_| |_|\\__,_| ");
		 System.out.println();
		 
		 String s = new StringBuilder()
				 .append("1: Add a new chemical to the Library \n")
				 .append("2: Edit the properties of an existing chemical \n")  
				 .append("3: List the currently available chemicals \n")
				 .append("4: Perform an IDEAL flash calculation \n")
				 .append("5: Perform a NON-IDEAL flash calculation \n")
				 .append("6: Delete a chemical from the library \n")
				 .append("0: Exit the program \n\n")
				 .append("Please enter the number corresponding to your desired action:")
				 .toString();
		 System.out.println(s);
   
		 choice = Support.inputInt("Please enter one of the listed option as an Integer: ", 0, 6); //Takes an integer input from the user and makes sure it is between 0 and 6
   
		 	//0. EXITS THE PROGRAM
		 	if (choice == 0)
		 	{
		 		System.out.println("Thank you for using our Flash Simulator! See you later!");
		 		break;
		 	}

		 	//1. ADDS A NEW CHEMICAL TO THE LIBRARY
		 	else if (choice == 1)
		 	{
		 		String folderName = "ChemicalData";
		 		Support.checkFolderExists(folderName);

		 		System.out.println("What is the name of the chemical you would like to add?");
		 		String chem_name = keyboard.nextLine();
		 		chem_name = chem_name.toLowerCase();
     
		 		//Check if the file already exists
		 		File isFile = new File(folderName + "/" + chem_name + ".txt");
		 		if (isFile.exists() && !isFile.isDirectory())
		 		{
		 			System.out.println("A chemical with this name already exists!");
		 			continue;
		 		}
		 		else
		 		{
		 			boolean yes_no = Support.yes_no("Are you sure you would like to input the properties of " + chem_name+"? (yes/no)");
		 			
		 			if (!yes_no) 
		 				continue;

		 			String file_name = folderName + "/" + chem_name + ".txt";
		 			
		 			//Prints chemical name to file
		 			Support.readToFile(file_name, "Chemical: " + chem_name);
		 			
		 			System.out.println("NOTE: Please obtain the Critical Constants and Acentric Factors from Table 2-141 in: "+ "Perry's Handbook 7th edition or newer. \n");
		 			
		 			for(int j = 0; j < 8; j++) //Makes sure that all the properties are printed into the file using Support.setChemicalData
		 			{ 
		 				Support.setChemicalData(j,file_name,chem_name);
		 			}
		 			yes_no = Support.yes_no("Are you satisfied with the following information? (yes/no)");
     
		 			if (yes_no)
		 			{
		 				System.out.println("Successfully recorded chemical: " + chem_name);
		 				try 
		 				{
		 					TimeUnit.SECONDS.sleep(1); //Gives the user enough time to note that chemical was recorded successfully
		 				}
		 				catch (InterruptedException e) {}
		 			}
		 			else
		 			{
		 				File delete_file = new File(file_name);
		 				delete_file.delete(); 
		 				System.out.println("Your new chemical has been deleted.");
		 			}
		 		}
		 	}//END OF OPTION 1
 
		 	//2. EDITS THE PROPERTIES OF AN EXISTING CHEMICAL
		 	else if (choice == 2)
		 	{
		 		String folderName = "ChemicalData";
		 		Support.checkFolderExists(folderName);
		 		System.out.println("Reading all the available chemicals...");
		 		File path = new File(folderName+"/");
		 		//Searches the "path" and sizes itself according to the number of files found - holding references to the file names
		 		File[] findChemicals = path.listFiles();
                                
		 		boolean selection = false;
		 		while (selection != true)
		 		{
		 			System.out.println("These are all the currently available chemical files:");
		 			if (findChemicals.length > 0)
		 			{
		 				for (int i = 0; i < findChemicals.length; i++)
		 				{
		 					System.out.println((i+1)+ ": " + Support.getChemicalName(findChemicals[i].toString()));
		 				}
		 			}
		 			else
		 			{
		 				System.out.println("No chemical files found. Please add some new chemicals!"); 
		 				break;
		 			}

		 			System.out.println("Please enter the NAME of the CHEMICAL you wish to see the available properties for (example: ethane)");
		 			String chem_name = keyboard.nextLine();
		 			chem_name.toLowerCase();
		 			File chemical_properties = new File("ChemicalData/"+chem_name + ".txt");
		 			String the_chemical = "ChemicalData/"+chem_name + ".txt";
     
		 			if (chemical_properties.exists())
		 			{		
		 				System.out.println("Resetting the values of the chemical");
		 				try
		 				{
		 						chemical_properties.delete();
		 						chemical_properties.createNewFile();
		 						for(int j = 0; j < 8; j++)  //This will have to change if adding more properties
		 						{ 
		 							Support.setChemicalData(j,the_chemical,chem_name);
		 						}
		 				}
		 				catch (IOException e) {}    
		 				try 
		 				{
		 					System.out.println("...");
		 					TimeUnit.SECONDS.sleep(1);
		 				}	
		 				catch (InterruptedException e) {}
		 				selection = true;
		 			}
		 			else  
		 				System.out.println("Sorry, that is not an available chemical. Please, try again.");
		 		}  
		 	}//END OF CHOICE 2
  
		 	//3. LIST CURRENTLY AVAILABLE CHEMICALS
		 	else if (choice == 3)
		 	{
		 		String folderName = "ChemicalData";
		 		Support.checkFolderExists(folderName);
		 		System.out.println("Reading all the available chemicals...");
		 		
		 		File fileFolder = new File(folderName);
		 		File path = new File(folderName+"/");
		 		
		 		//File array that searches the "path" and sizes itself according to the number of files found, holding references to the file names.
		 		File[] findChemicals = path.listFiles();
                                
		 		boolean selection = false;
		 		while (selection != true)
		 		{
		 			System.out.println("These are all the currently available chemical files:");
		 			if (fileFolder.isDirectory() == false)
		 			{
		 				System.out.println("Directory not found, please check to make sure that it exists. \n");
		 				break;
		 			}
		 			if (findChemicals.length > 0)
		 			{
		 				for (int i = 0; i < findChemicals.length; i++)
		 				{
		 					System.out.println((i+1)+ ": " + Support.getChemicalName(findChemicals[i].toString()));
		 				}
		 			}
		 			else
		 			{	
		 				System.out.println("No chemical files found. Please, add some new chemicals."); 
		 				break;
		 			}

		 			System.out.println("Please enter the name of a CHEMICAL you wish to see the available properties for: ");
		 			String chem_name = keyboard.nextLine();  //has been pre-defined in a previous menu option, takes user input
		 			chem_name.toLowerCase();
		 			File chemical_properties = new File("ChemicalData/"+chem_name + ".txt");
		 			String the_chemical = "ChemicalData/"+chem_name + ".txt";
     
		 			if (chemical_properties.exists())
		 			{ 
		 				Support.readChemicalData(the_chemical);  
		 				try 
		 				{
		 					System.out.println("...");
		 					TimeUnit.SECONDS.sleep(1);
		 				}	
		 				catch (InterruptedException e) {}
		 				selection = true;
		 			}
		 			else
		 				System.out.println("Sorry, that is not an available chemical. Please, try again.");
		 			}   
		 	}//END OF CHOICE 3
		 	
		 	//4. SOLVE IDEAL CASE
		 	else if (choice == 4)
		 	{
		 		String folderName = "ChemicalData";
		 		Support.checkFolderExists(folderName);
     
		 		File path = new File(folderName+"/");
		 		
		 		//File array that searches the "path" and sizes itself according to the number of files found, holding references to the file names.
		 		File[] findChemicals = path.listFiles();

		 		System.out.println("How many chemical species are present in the feed stream?");
		 		int num_of_chem = Support.inputInt("ERROR: That is not a proper input. Please enter a valid integer between 1 and 6 ", 1,  6);
		 		
		 		Chemical[] chemical_Stream = new Chemical[num_of_chem];  

		 		int counter = 0; //Keeps track of number of chemicals entered so far
		 		while (counter < num_of_chem)
		 		{  
		 			if (findChemicals.length < 1)
		 			{
		 				System.out.println("There are no chemicals in the folder. Please, add chemicals from the main menu.");
		 				counter = -1;
		 				break;
		 			}
     
		 			boolean selection = false;
		 			while (selection != true)
		 			{
		 				System.out.println("These are all the currently available chemical files");
     
		 				if (findChemicals.length > 0)
		 				{
		 					for (int i = 0; i < findChemicals.length; i++)
		 					{
		 						System.out.println((i+1)+ ": " + Support.getChemicalName(findChemicals[i].toString()));
		 					}
		 					if (findChemicals.length < num_of_chem)
		 					{
		 						System.out.println("WARNING: The number of chemicals in the file is less than the specified number in the stream, please add more chemicals using main menu option 1"); 
		 						break;
		 					}
		 				}
		 				else
		 				{
		 					System.out.println("No chemical files found. Please, add chemicals from the main menu."); 
		 					break;
		 				}
     
		 				System.out.println("Please enter the name of chemical number "+(counter+1)+".");
		 				String chem_name = keyboard.nextLine();
		 				chem_name.toLowerCase();
		 				String the_chemical = "ChemicalData/"+chem_name + ".txt";
     
		 				File chemical_properties = new File(the_chemical);
		 				if (chemical_properties.exists())
		 				{
		 					boolean duplicate = false;
		 					for(int i = 0; i < chemical_Stream.length; i++) //Checks for duplicate chemicals
		 					{
		 						try
		 						{
		 							if ((chemical_Stream[i].getName()).equals(chem_name))
		 							{
		 								duplicate = true;
		 								break;
		 							}   
		 						}
		 						catch(NullPointerException e) {}
		 					}
		 					if (duplicate == true)
		 					{
		 						System.out.println("WARNING: This chemical already exists in the stream. No duplicates are allowed.");
		 						break;
		 					}
  
		 					System.out.println("What is the mass flow rate (kmol/h) of " + chem_name + "?");  
		 					double massFlowRate = Support.inputDouble("ERROR: That is not a valid flow rate", 0 , 100000000);
		 					double[][] extractFromLibrary = Support.getProperties("ChemicalData/"+chem_name + ".txt");
		 					//extractFromLibrary[0][] = molar_mass;
		 					//extractFromLibrary[1][] = t_crit;
		 					//extractFromLibrary[2][] = p_crit;
		 					//extractFromLibrary[3][] = acentric_factor;
		 					//extractFromLibrary[4][] = antoine_constants
		 					//extractFromLibrary[5][] = gas_heat_constants
		 					//extractFromLibrary[6][] = liquid_heat_constants
		 					//extractFromLibrary[7][] = latent_heat_constants
  
		 					double [][][] parameters= new double [chemical_Stream.length][4][];
  
		 					parameters[counter][0]= Support.returnArray(3,extractFromLibrary[4][0],extractFromLibrary[4][1],extractFromLibrary[4][2],0,0); // antoine parameter
		 					parameters[counter][1]= Support.returnArray(5, extractFromLibrary[6][0],extractFromLibrary[6][1],extractFromLibrary[6][2],extractFromLibrary[6][3],extractFromLibrary[6][4]); //cpL
		 					parameters[counter][2]= Support.returnArray(4,extractFromLibrary[5][0],extractFromLibrary[5][1],extractFromLibrary[5][2],extractFromLibrary[5][3],0); //cpV
		 					parameters[counter][3]= Support.returnArray(4,extractFromLibrary[7][0],extractFromLibrary[7][1],extractFromLibrary[7][2],extractFromLibrary[7][3],0); //cp heat of vaporization
   
		 					double molarFlowRate =massFlowRate/extractFromLibrary[0][0]; // kmol/h
		 					double Tc=extractFromLibrary[1][0]; //Kelvin
		 					double Pc=Support.convertBarToKPas(extractFromLibrary[2][0]); //Kpa
		 					double w=extractFromLibrary[3][0];
		 					
		 					//Check for ethanol (different required equation)
		 					boolean ifCPEquation1=true;
		 					boolean isCondensible = true;
		 					if (chem_name.equals("ethanol"))
		 						ifCPEquation1 = true; //set to true for now
		 					if (chem_name.equals("nitrogen"))
		 						isCondensible = false;
  
		 					Chemical aChemical = new Chemical(chem_name,molarFlowRate,parameters[counter][0],parameters[counter][1],parameters[counter][2],parameters[counter][3],Tc,Pc,w,ifCPEquation1,isCondensible);
		 					chemical_Stream[counter] = aChemical.clone();
		 					counter ++; 
		 					selection = true;
		 				}
		 				else  
		 				{
		 					System.out.println("That is not an available chemical. Please, try again!");
		 					break;
		 				}  
     
		 			}
		 		}
		 		
		 		//Loop to pick a case
		 		if (counter > 0)
		 		{
		 			int idealChoice = -1;
		 			do
		 			{
		 				//Choosing a specific case
		 				s = new StringBuilder()
		 						.append("Please enter the number corresponding to your desired action: \n")
		 						.append("1: Case 1 - Constant Temperature Flash given tank P, T (also calculates Q to maintain T)\n")
		 						.append("2: Case 2 - Adiabatic Flash given tank P, feed T (also calculates adiabatic flash T) \n")  
		 						.append("3: Case 3 - Adiabatic Flash given tank P, flash T (also calculates adiabatic feed T)\n")
		 						.append("0: Exit the program\n")
		 						.toString();
		 				System.out.println(s);

		 				idealChoice = Support.inputInt("Please enter a listed option as an Integer. \n", 0, 3); 

		 				//Ideal Case 1
		 				if(idealChoice == 1)
		 				{
		 					System.out.println("What is the pressure within the flash vessel in kPa?");
		 					double pressureInlet = Support.inputDouble("Sorry, that is not a valid pressure value. Limit of 1MPa enforced for ideal cases.",0,1000000);
        
		 					System.out.println("What is the temperature of the stream and flash vessel in Kelvin?");
		 					double Tinlet = Support.inputDouble("Sorry, that is not a valid temperature value. Only values in the range 0 - 10,000K are allowed.",0,10000);
		 					Stream inletStream= new IdealStream(chemical_Stream,Tinlet);
		 					if(Support.ifTempWithinRange(inletStream, Tinlet, pressureInlet))
		 					{
		 						Flash VesselCase1= new Flash(inletStream,Tinlet, pressureInlet,1,true);
		 						try
		 						{
		 							System.out.println(VesselCase1.toString()+"");
		 						} 
		 						catch (SecurityException | IllegalArgumentException e) 
		 						{
		 							e.printStackTrace();
		 						}
		 					}
		 					else
		 					{
		 							System.out.println("Please, enter a new temperature in Kelvin.");
		 					} 
		 				}
      
		 				//Ideal Case 2
		 				else if(idealChoice == 2)
		 				{
		 					System.out.println("What is the pressure within the flash vessel in kPa?");
		 					double pressureInlet = Support.inputDouble("Sorry, that is not a valid pressure value. Limit of 1MPa enforced for ideal cases.",0,1000000);
        
		 					//Ask for the constant temperature of the flash vessel
		 					System.out.println("What is the temperature of the inlet stream in Kelvin?");
		 					double Tinlet = Support.inputDouble("Sorry, that is not a valid temperature value. Only values in the range 0 - 10,000K are allowed.",0,10000);
		 					Stream inletStream= new IdealStream(chemical_Stream,Tinlet);
       
		 					System.out.println("Case 2 Results are as follows:\n");
		 					System.out.println("T inlet: "+ Tinlet + "K");
  
		 					Flash VesselCase2= new Flash(inletStream,Tinlet, pressureInlet,2,true);
		 					try
		 					{
		 						System.out.println(VesselCase2.toString()+"");
		 					}
		 					catch (SecurityException | IllegalArgumentException e) 
		 					{
		 						e.printStackTrace();
		 					} 
        
		 				}
 
		 				//Ideal Case 3
		 				else if(idealChoice == 3)
		 				{
		 					System.out.println("What is the pressure within the flash vessel in kPa?");
		 					double pressureInlet = Support.inputDouble("Sorry, that is not a valid pressure value. Limit of 1MPa enforced for ideal cases.",0,1000000);
        
		 					//Ask for the constant temperature of the flash vessel
		 					System.out.println("What is the temperature of the flash vessel in Kelvin?");
		 					double Toperating = Support.inputDouble("Sorry, that is not a valid temperature value. Only values in the range 0 - 10,000K are allowed.",0,10000);
		 					Stream flashStream= new IdealStream(chemical_Stream,Toperating);
		 					Flash VesselCase3= new Flash(flashStream,Toperating,pressureInlet,3,true);
       
		 					try {
		 						System.out.println(VesselCase3.toString()+"");
		 					} 
		 					catch (SecurityException | IllegalArgumentException e) 
		 					{
		 						e.printStackTrace();
		 					} 
		 				}
		 			} while (idealChoice != 0);
   
		 		}
		 	}//END OF CASE 4

		 	//5. NON-IDEAL RUN
		 	else if (choice == 5)
		 	{
		 		String folderName = "ChemicalData";
		 		Support.checkFolderExists(folderName);
		 		File path = new File(folderName+"/");
		 		File[] findChemicals = path.listFiles();

		 		System.out.println("How many chemical species are present in the feed stream?");
		 		int num_of_chem = Support.inputInt("Warning: That is not a valid input. Please, enter a valid integer between 1 and 6", 1,  6);
		 		Chemical[] chemical_Stream = new Chemical[num_of_chem];  
  
		 		int counter = 0;
		 		while (counter < num_of_chem)
		 		{  
		 			if (findChemicals.length < 1)
		 			{
		 				System.out.println("There are no chemicals in the folder, please add some more");
		 				counter = -1;
		 				break;
		 			}
     
		 			boolean selection = false;
		 			while (selection != true)
		 			{
		 				System.out.println("These are all the currently available chemical files");
		 				if (findChemicals.length > 0)
		 				{
		 					for (int i = 0; i < findChemicals.length; i++)
		 					{
		 						System.out.println((i+1)+ ": " + Support.getChemicalName(findChemicals[i].toString()));
		 					}
		 					if (findChemicals.length < num_of_chem)
		 					{
		 						System.out.println("The number of chemicals in the file is less than the specified number in the stream, please add more chemicals using main menu option 1"); 
		 						break;
		 					}
		 				}
		 				else
		 				{
		 					System.out.println("No chemical files found, please add some new chemicals!"); 
		 					break;
		 				}
		 				System.out.println("Please enter the name of chemical number "+(counter+1)+".");
		 				String chem_name = keyboard.nextLine();  //has been pre-defined in a previous menu option, takes user input
		 				chem_name.toLowerCase();
     
		 				//Use the File class object to extract data from the txt files
		 				String the_chemical = "ChemicalData/"+chem_name + ".txt";
     
		 				File chemical_properties = new File(the_chemical);
		 				if (chemical_properties.exists())
		 				{
		 					boolean duplicate = false;
		 					for(int i = 0; i < chemical_Stream.length; i++)
		 					{
		 						try
		 						{
		 							if ((chemical_Stream[i].getName()).equals(chem_name))
		 							{
		 								duplicate = true;
		 								break;
		 							}      
		 						}
		 						catch(NullPointerException e) {}
		 					}
		 					if (duplicate == true)
		 					{
		 						System.out.println("Sorry, this chemical already exists in the stream, no duplicates allowed");
		 						break;
		 					}
		 					else if(chem_name.equals("water"))
		 						{
		 						System.out.println("Sorry, due to the nature of the Peng-Robenson VLE, water cannot be part of the non-ideal case. It is only available in the ideal calculator (option 4)");
		 						break;
		 						}
  
		 					System.out.println("What is the mass flow rate of " + chem_name + "?");  
		 					double massFlowRate = Support.inputDouble("Error, that is not a valid flow rate", 0 , 100000000);
		 					double[][] extractFromLibrary = Support.getProperties("ChemicalData/"+chem_name + ".txt");
		 					//extractFromLibrary[0][] = molar_mass;
		 					//extractFromLibrary[1][] = t_crit;
		 					//extractFromLibrary[2][] = p_crit;
		 					//extractFromLibrary[3][] = acentric_factor;
		 					//extractFromLibrary[4][] = antoine_constants
		 					//extractFromLibrary[5][] = gas_heat_constants
		 					//extractFromLibrary[6][] = liquid_heat_constants
		 					//extractFromLibrary[7][] = latent_heat_constants
  
		 					double [][][] parameters= new double [chemical_Stream.length][4][];
  
		 					parameters[counter][0]= Support.returnArray(3,extractFromLibrary[4][0],extractFromLibrary[4][1],extractFromLibrary[4][2],0,0); // antoine parameter
		 					parameters[counter][1]= Support.returnArray(5, extractFromLibrary[6][0],extractFromLibrary[6][1],extractFromLibrary[6][2],extractFromLibrary[6][3],extractFromLibrary[6][4]); //cpL
		 					parameters[counter][2]= Support.returnArray(4,extractFromLibrary[5][0],extractFromLibrary[5][1],extractFromLibrary[5][2],extractFromLibrary[5][3],0); //cpV
		 					parameters[counter][3]= Support.returnArray(4,extractFromLibrary[7][0],extractFromLibrary[7][1],extractFromLibrary[7][2],extractFromLibrary[7][3],0); //cp heat of vaporization

		 					double molarFlowRate =massFlowRate/extractFromLibrary[0][0]; // kmol/h
		 					double Tc=extractFromLibrary[1][0];
		 					double Pc=Support.convertBarToKPas(extractFromLibrary[2][0]);
		 					double w=extractFromLibrary[3][0];
		 					//checks for ethanol basically
		 					boolean ifCPEquation1=true;
		 					boolean isCondensible = true;
		 					if (chem_name.equals("ethanol"))  //can potentially check for all exceptions in Perry's, between 10-20 of them****
		 						ifCPEquation1=true;
		 					if (chem_name.equals("nitrogen"))
		 						isCondensible = false;
  
		 					Chemical aChemical = new Chemical(chem_name,molarFlowRate,parameters[counter][0],parameters[counter][1],parameters[counter][2],parameters[counter][3],Tc,Pc,w,ifCPEquation1,isCondensible);
		 					chemical_Stream[counter] = aChemical.clone();
		 					
		 					counter ++; 
		 					selection = true;
		 				}
		 				else  
		 				{
		 					System.out.println("Sorry, that is not an available chemical, please try again!");
		 					break;
		 				}  
     
		 			}
		 		}
		 		
		 		//Loop for cases
		 		if (counter > 0)
		 		{
		 			int idealChoice = -1;
		 			do
		 			{
		 				//Choosing a specific case
		 				s = new StringBuilder()
		 						.append("Please enter the number corresponding to your desired action: \n")
		 						.append("1: Case 1 - Constant Temperature Flash given tank P,T (also calculates Q to maintain T)\n")
		 						.append("2: Case 2 - Adiabatic Flash given tank P, feed T (also calculates adiabatic flash T) \n")  
		 						.append("3: Case 3 - Adiabatic Flash given tank P, flash T (also calculates adiabatic feed T)\n")
		 						.append("0: Exit the program \n")
		 						.toString();
		 				System.out.println(s);

		 				idealChoice = Support.inputInt("Please enter a listed option as an Integer. \n", 0, 3); 
		 				
		 				//Case 1
		 				if(idealChoice == 1)
		 				{
		 					System.out.println("What is the pressure within the flash vessel in kPa?");
		 					double pressureInlet = Support.inputDouble("Sorry, that is not a valid pressure value. A limit of 1MPa is enforced.",0,1000000);
		 					System.out.println("What is the temperature within the flash vessel in Kelvin?");
		 					double Tinlet = Support.inputDouble("Sorry, that is not a valid temperature value. The temperature must be in the range of 0 - 10000 K",0,10000); 
		 					Stream inletStream=new NonIdealStream(chemical_Stream,Tinlet);
 
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
		 							System.out.println("Please enter a new temperature");    
		 						}
		 					}   
		 				}
      
		 				//Case 2
		 				else if(idealChoice == 2)
		 				{
		 					System.out.println("What is the pressure within the flash vessel in kPa?");
		 					double pressureInlet = Support.inputDouble("Sorry, that is not a valid pressure value. A limit of 1MPa is enforced.",0,1000000);
		 					System.out.println("What is the inlet temperature to the flash vessel in Kelvin?");
		 					double Tinlet = Support.inputDouble("Sorry, that is not a valid temperature value. The temperature must be in the range of 0 - 10000 K",0,10000);
       
		 					Stream inletStreamCase2=new NonIdealStream(chemical_Stream,Tinlet);
		 					Flash VesselCase2= new Flash(inletStreamCase2,Tinlet, pressureInlet,2,false); 
		 					try
		 					{ System.out.println("Case 2");
		 					System.out.println(VesselCase2.toString()+"");
		 					}catch (SecurityException | IllegalArgumentException e) 
		 					{
		 						e.printStackTrace();
		 					}   
		 				}
		 				
		 				//Activate Case 3
		 				else if(idealChoice == 3)
		 				{
		 					System.out.println("What is the pressure within the flash vessel in kPa?");
		 					double pressureInlet = Support.inputDouble("Sorry, that is not a valid pressure value. A limit of 1MPa is enforced.",0,1000000);
		 					System.out.println("What is the flash temperature of the vessel in Kelvin?");
		 					double Toperating = Support.inputDouble("Sorry, that is not a valid temperature value. The temperature must be in the range of 0 - 10000 K",0,10000);
		 					
		 					Stream flashStream= new IdealStream(chemical_Stream,Toperating);
		 					Flash VesselCase3= new Flash(flashStream,Toperating,pressureInlet,3,false);
		 					try {
		 						System.out.println("\n Case 3");
		 						System.out.println(VesselCase3.toString()+"");
		 					} catch (SecurityException | IllegalArgumentException e) 
		 					{
		 						e.printStackTrace();
		 					} 
		 				}
		 			} while (idealChoice != 0);
		 		}
		 	}
		 	
		 	//6. DELETE A CHEMICAL
		 	else if (choice == 6)
		 	{  
		 		String folderName = "ChemicalData";
		 		Support.checkFolderExists(folderName);
		 		int selection = -1;
		 		while (selection != 2)
		 		{
		 			System.out.println("These are all the currently available chemical files");
		 			File path = new File(folderName+"/");
		 			File[] findChemicals = path.listFiles();
		 			if (findChemicals.length > 0)
		 			{
		 				for (int i = 0; i < findChemicals.length; i++)
		 				{
		 					System.out.println((i+1)+ ": " + Support.getChemicalName(findChemicals[i].toString()));
		 				}
		 			}
		 			else
		 			{
		 				System.out.println("No chemical files found, please add some new chemicals before deleting!"); 
		 				break;
		 			}
		 			System.out.println("Would you like to delete a chemical? \n"
		 				+"1 - Delete a chemical \n"
                          +"2 - Exit \n");
		 			selection = Support.inputInt("Please enter a listed option as an Integer. \n", 1,2);
		 			if (selection == 1)
		 			{
		 				int choice_delete = -1;
		 				while (choice_delete != 2)
		 				{
		 					System.out.println("Please enter the name of the CHEMICAL (no need for file extensions) you would like to delete out of the following: ");
		 					for (int i = 0; i < findChemicals.length; i++)
		 					{
		 						System.out.println(findChemicals[i].toString());
		 					}
		 					String chem_name = keyboard.nextLine();  //has been pre-defined in a previous menu option, takes user input
		 					chem_name.toLowerCase();
		 					File delete_file = new File("ChemicalData/"+chem_name + ".txt");
		 					if (delete_file.exists())
		 					{
		 						System.out.println("The file has been found");
		 						System.out.println("Are you sure you want to delete the information on " + chem_name + "? \n"
                            + "1 - Delete \n"
                            + "2 - Go back");
		 						int final_moment = Support.inputInt("Please enter either 1 or 2", 1, 2);
		 						if (final_moment == 1)
		 						{
		 							delete_file.delete(); 
		 							System.out.println("The information on " + chem_name + " has been deleted");
		 							selection = 2;
		 							break;
		 						}
		 						else
		 							System.out.println("The chemical was not deleted");
		 					}
		 					else
		 					{
		 						System.out.println("The specified chemical does not exist");
		 						break;
		 					} 
		 				}
		 			}
		 		}
		 	}
	 	}while (choice != 0);
	 keyboard.close();
   	}
 }//END OF MAIN
