import java.io.*;



/*

	############
	localization file class
	############
	
	
	
	FUNCTION						USAGE
	
	###CONSTRUCTOR###
	localizationFile(String file)				Loads locale definitions from file
	
	###DATASET FUNCTIONS###
	getValue(int pointer)					Returns definition requested by id
	
	###DEBUG FUNCTIONS###
	printLocaleData()					Dumps all locale definitions
	debugMsg(String msg)				Prints a debug message

*/



public class localizationFile
/* Class for localization files */
{
	
	//Variables
	
		//arrays
		final static double localeVersion = 0.3;				//class version
		private String value[] = new String[100];			//array for locale
		private double fileVersion;						//version of localization file
		private String fileAuthor;						//author
		private boolean fileLoaded = true;				//file loaded?
		private boolean debugMode = false;				//debug mode
	
	
	
	localizationFile(String file, boolean debug)
	/* Constructor; parses file */
	{
		try
		/* Try to load file */
		{
			//Activate debug mode if wanted
			if(debug == true) { this.debugMode = true; }
			
			debugMsg("About to load " + file + "...");
			BufferedReader targetFile = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			
			//Get version
			fileVersion = Double.parseDouble(targetFile.readLine());
			fileAuthor = targetFile.readLine();
			
			String temp;
			int pointer=0;
			
			//Load strings if version correct
			if(fileVersion >= localeVersion)
			{
				//Version correct; load strings
				debugMsg("Loading localization file '" + file + "' from '" + fileAuthor + "' - version '" + fileVersion + "', class version is: '" + localeVersion + "'");
				while(true)
				{
					//Get next line
					temp = targetFile.readLine();
					
					//Abort reading if end reached
					if(temp == null) { break; }
					else
					{
						//Add line
						value[pointer] = temp;
						pointer++;
					}
				}
				
				//File loaded
				debugMsg("Loaded " + (pointer-1) + " definitions from locale.txt");
				fileLoaded = true;
			
			}
			else
			{
				//Incorrect version; load defaults
				debugMsg("Wrong localization file version '" + fileVersion + "', needed version is: '" + localeVersion + "' or higher.");
				this.loadDefaults();
			}
			
		}
		catch(java.io.IOException exp)
		{
			//Error
			debugMsg("Error loading " + file + ", falling back to built-in english...\n");
			debugMsg("Stack trace:");
			if(this.debugMode == true) { exp.printStackTrace(); }
			
			//Load default definitions
			this.loadDefaults();
		}
		
		
	}
	
	
	
	localizationFile(String file)
	/* constructor for localization file without debug mode */
	{
		this(file, false);
	}
	
	
	
	private void loadDefaults()
	/* load default definitions */
	{
		this.fileLoaded = false;
		value[0] = "by";
		value[1] = "USAGE:\tjava jtrainercli [-v] [-o] [-e] filename\n";
		value[2] = "--verbose, -v\tverbose debug mode";
		value[3] = "--ordered, -o\tordered mode (no random questions)";
		value[4] = "--endless, -e\tendless mode (questions will be repeated)";
		value[5] = "filename \tfilename of question catalog";
		value[6] = "Unknown command:";
		value[7] = "See 'help' for a list of supported commands.";
		value[8] = "The following commands are available:";
		value[9] = "help\t\tshows this help";
		value[10] = "next\t\tgets the next question";
		value[11] = "question\tshows the question and selected answers";
		value[12] = "hint\t\tshow a hint (if exists)";
		value[13] = "expl\t\tshows the explanation (if exists)";
		value[14] = "stat\t\tshows the statistics";
		value[15] = "exit\t\texits the program";
		value[16] = "Other inputs are recognized as answers.";
		value[17] = "Answer:";
		value[18] = "Please answer this question!";
		value[19] = "Activated";
		value[20] = "Deactivated";
		value[21] = "Please insert a number!";
		value[22] = "Correct!";
		value[23] = "Incorrect!";
		value[24] = "Bye!";
		value[25] = "Not available.";
		value[26] = "Total answered:";
		value[27] = "...with hints:";
		value[28] = "...correctly:";
		value[29] = "...incorrectly:";
		value[30] = "Quote:";
		value[31] = "Currently unavailable.";
		value[32] = "Error loading file. Quitting...";
		value[33] = "No file loaded. Please load catalog.";
		value[34] = "stop\t\tstops the simulation";
		value[35] = "open\t\topens a file";
		value[36] = "info\t\tshows information about loaded catalog";
		value[37] = "Catalog:";
		value[38] = "Description:";
		value[39] = "Date:";
		value[40] = "Amount of questions:";
	}
	
	
	
	//Return translation
	public String getValue(int pointer) { return value[pointer]; }
	
	
	
	public void printLocaleData()
	/* List all translations */
	{
		//Localization file loaded?
		if(this.fileLoaded == false)
		{
			//Print included strings
			debugMsg("locales file was NOT loaded - using built-in localization");
			for(int i=0;i<this.value.length;i++) { debugMsg("BUILT-IN #" + i + ": " + this.value[i]); }
		}
		else
		{
			//Print loaded strings
			debugMsg("locales file was SUCCESSFULLY loaded");
			for(int i=0;i<this.value.length;i++) { debugMsg("LOCALES #" + i + ": " + this.value[i]); }
		}
	}
	
	
	
	//Print debug message
	private void debugMsg(String debugMessage) { if(this.debugMode == true) { System.out.println("LF: " + debugMessage); } }
	
}