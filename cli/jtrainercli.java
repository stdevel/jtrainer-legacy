import java.util.Scanner;

public class jtrainercli
{
	
	/*
		PARAM			EXPLANATION
		--help / -h			show help
		--debug / -v			Activate debug outputs
		--ordered / -o			ordered mode (standard is random)
		--endless / -e			endless mode (standard is once)
		
		COMMANDS
		help				show help
		open <file>			opens file
		info				shows information about file
		next				show next question
		question			shows question and selected answers
		hint				show hint
		ans				check question
		expl				shows the explanation
		stop				stops the simulation
		exit				exit program
	
	*/
	
	
	
	//Generic variables
	private String version = "0.3";
	private String debugFile = "debug.log";
	private localizationFile locale;
	
	//Mode variables
	private boolean debugMode = false;
	private boolean randomMode = true;
	private boolean endlessMode = false;
	private boolean active = false;
	
	//Ressources
	private String filename = "";
	private questionCatalog thisCatalog;
	private Scanner keyboard = new Scanner(System.in);
	
	//Simulation variables
	private boolean fileLoaded = false;
	private boolean firstQuestion = false;
	private boolean hintUsed = false;
	private int numbAnswered, numbCorrect, numbIncorrect, numbHints;
	private int thisQuestion = -1;
	private int[] selAnswers = new int[5];
	private boolean answered=false;
	
	//Main function; instances cli
	public static void main(String[] args) { jtrainercli main = new jtrainercli(args); }
	
	//Debug function
	private void debugMsg(String msg) { if(this.debugMode == true) { System.out.println("CLI: " + msg); } }
	
	jtrainercli(String[] args)
	/* CLI constructor */
	{
		
		boolean trigHelp = false;
		
		//Parse arguments if any
		if(args.length > 0)
		{
			for(int i=0;i<args.length;i++)
			{
				//Print out argument
				debugMsg("Parsing argument " + i + ": " + args[i]);
				
				//Do some stuff if known argument
				if(args[i].equals("--help") || args[i].equals("-h")) { trigHelp = true; }					//show help
				else if(args[i].equals("--debug") || args[i].equals("-v")) { this.debugMode = true; }		//debug mode
				else if(args[i].equals("--ordered") || args[i].equals("-o")) { this.randomMode = false; }		//ordered mode
				else if(args[i].equals("--endless") || args[i].equals("-e")) { this.endlessMode = true; }		//endless mode
				else { this.filename = args[i]; }											//filename
			}
			
			//Start simulation if catalog loaded correctly
			//if(thisCatalog.getIOException() == false) { startSimulation(); }
			//else { System.out.println(locale.getValue(32)); }
			
		}
		
		//Loading locale
		if(this.debugMode == true) { locale = new localizationFile("locale.txt", true); }
		else { locale = new localizationFile("locale.txt"); }
		
		//Show help if wanted or start simulation
		if(trigHelp == true) { showParamHelp(); }
		else
		{
			//Instance catalog if selected
			if(this.filename.equals("") == false) {
				if(this.debugMode == true) { thisCatalog = new questionCatalog(filename, true); this.fileLoaded = true; }
				else { thisCatalog = new questionCatalog(filename); this.fileLoaded = true; }
				
				//Settings params
				if(this.randomMode == true) { this.thisCatalog.setRandom(true); }
				else { this.thisCatalog.setRandom(false); }
				if(this.endlessMode == true) { this.thisCatalog.setEndless(true); }
				else { this.thisCatalog.setEndless(false); }
			}
			
			startSimulation();
		}
		
	}
	
	void loadFile(String filename)
	/* Load a question catalog file */
	{
		if(this.debugMode == true) { thisCatalog = new questionCatalog(filename, true); this.fileLoaded = true; }
		else { thisCatalog = new questionCatalog(filename); }
		
		if(this.thisCatalog.getIOException() == false) { fileLoaded = true; } else { fileLoaded = false; System.out.println(locale.getValue(32)); }
		
		//Settings params
		if(this.randomMode == true) { this.thisCatalog.setRandom(true); }
		else { this.thisCatalog.setRandom(false); }
		if(this.endlessMode == true) { this.thisCatalog.setEndless(true); }
		else { this.thisCatalog.setEndless(false); }
	}
	
	void catalogInfo()
	/* Shows meta data of loaded question catalog */
	{
		if(this.fileLoaded == true)
		{
			System.out.println(locale.getValue(37) + " '" + this.thisCatalog.getCatalogName() + "' " + locale.getValue(0) + " " + this.thisCatalog.getCatalogAuthor());
			System.out.println(locale.getValue(38) + " " + this.thisCatalog.getCatalogDesc());
			System.out.println(locale.getValue(39) + " " + this.thisCatalog.getCatalogDate());
			System.out.println(locale.getValue(40) + " " + this.thisCatalog.getSize());
		}
		else { System.out.println(locale.getValue(33)); }
	}
	
	void startSimulation()
	/* Start simulation */
	{
		this.firstQuestion = true;
		String selection = "";
		this.active = true;
		//debugMsg("About to start simulation with modeRandom=" + this.randomMode + ", endlessMode=" + this.endlessMode + " and filename=" + this.filename);
		
		//Do stuff while simulation active
		while(this.active == true || selection.equals(""))
		{
			//Show prompt and get input if no input detected
			if(selection.equals(""))
			{
				System.out.print("> ");
				selection = keyboard.nextLine();
			}
			else if(selection.equals("help")) { showHelp(); selection=""; }							//show help
			else if(selection.contains("open")) { if(selection.length() > 5) { loadFile(selection.substring(5)); } selection=""; }		//open catalog
			else if(selection.equals("info")) { catalogInfo(); selection=""; }							//shows meta data of catlaog
			else if(selection.equals("next")) { nextQuestion(); selection=""; }						//next question
			else if(selection.equals("hint")) { System.out.println(locale.getValue(31)); selection=""; }		//show hint - not available in this mode
			else if(selection.equals("expl")) { showExplanation(); selection=""; }						//show exlanation
			else if(selection.equals("stop")) { endSimulation(); selection=""; }						//exit simulation
			else if(selection.equals("stat")) { showStats(); selection=""; }							//show statistics
			else if(selection.equals("exit")) { endSimulation(); System.out.println(locale.getValue(24)); this.active=false; }					//exit simulation + program
			else
			{
				//Unknown command
				System.out.println(locale.getValue(6) + " " + selection + "\n" + locale.getValue(7));
				selection = "";
			}
		}
	}
	
	void showParamHelp()
	/* Show param help */
	{
		//locale = new localizationFile("locale.txt");
		System.out.println("\nJTrainerCLI v." + this.version + " " + locale.getValue(0) + " Christian Stankowic <info@stankowic-development.net>\n");
		System.out.println(locale.getValue(1));
		System.out.println(locale.getValue(2));
		System.out.println(locale.getValue(3));
		System.out.println(locale.getValue(4));
		System.out.println(locale.getValue(5));
	}
	
	void showHelp()
	/* Show help */
	{
		System.out.println(locale.getValue(8));
		System.out.println(locale.getValue(9));
		System.out.println(locale.getValue(35));
		System.out.println(locale.getValue(36));
		System.out.println(locale.getValue(10));
		System.out.println(locale.getValue(11));
		System.out.println(locale.getValue(12));
		System.out.println(locale.getValue(13));
		System.out.println(locale.getValue(14));
		System.out.println(locale.getValue(34));
		System.out.println(locale.getValue(15) + "\n");
		System.out.println(locale.getValue(16));
	}
	
	void nextQuestion()
	/* Next question */
	{
	
		//Show next question if file loaded
		if(this.fileLoaded == true)
		{
		
			//Disable first question flag if still set
			if(this.firstQuestion == true) { this.firstQuestion = false; }
			boolean errorFlag = false;
			
			//Do some stuff (deleting preselected answers, disabling hooks, etc...)
			this.answered=false;
			String answer="";
			for(int i=0;i<5;i++) { this.selAnswers[i] = 0; }
			this.hintUsed = false;
			
			//Get next question id
			this.thisQuestion = thisCatalog.nextQuestion();
			
			//Display question if exists
			if(this.thisQuestion != -1)
			{
				//Showing question and answers
				showQuestion();
				
				//Ask for answer
				while(this.answered == false)
				{
					System.out.print(locale.getValue(17) + " ");
					
					//Get answer depending on type
					String[] thisAnswers = this.thisCatalog.questions.get(this.thisQuestion).getAnswers();
					if(thisAnswers.length > 1)
					{
						//Type 1/2
						int tempCorrect[] = new int[5];
						tempCorrect = this.thisCatalog.questions.get(thisQuestion).getCorrectAnswers();
						answer = "";
						
						//Print all as correct marked answers as dump
						for(int i=0;i<tempCorrect.length;i++) { debugMsg("correct-marked answer #" + i + ": " + tempCorrect[i]); }
						
						while(answer.equals("") && this.answered == false)
						{
							answer = keyboard.next();
							if(answer.equals("question")) { showQuestion(); answer=""; }
							else if(answer.equals("hint")) { showHint(); answer=""; }
							else if(answer.equals("next")) { System.out.println(locale.getValue(18)); answer=""; }
							else if(answer.equals("ans")) { this.answered=true; }
							else
							{
								//Try to convert string as answer id
								try
								{
									int temp = Integer.parseInt(answer);
									if(this.selAnswers[temp] == 0)
									{
										this.selAnswers[temp] = 1;
										System.out.println(locale.getValue(19) + " #" + temp);
									}
									else
									{
										this.selAnswers[temp] = 0;
										System.out.println(locale.getValue(20) + " #" + temp);
									}
									answer="";
								}
								catch(Exception e)
								{
									System.out.println(locale.getValue(21));
									answer="";
								}

							}
						}
						
						//Checking answers
						this.answered=true;
						for(int i=0;i<5;i++)
						{
						
							//Check if answer is correct
							if(isCorrect(i, tempCorrect) == true)
							{
								//Correct answer - was it also marked by user?
								debugMsg("checking correct-marked answer: #" + i);
								if(selAnswers[i] == 1)
								{
									//Yes --> correct
									System.out.println(i + ": (x) " + thisAnswers[i]);
									debugMsg("correct-marked answer is marked :-)");
								}
								else
								{
									//No --> wrong
									errorFlag = true;
									System.out.println(i + ": ( !) " + thisAnswers[i]);
									debugMsg("correct-marked answer is NOT marked :-(");
								}
							}
							else
							{
								//Wrong answer- was it marked by the user?
								debugMsg("wrong-marked answer: #" + i);
								if(selAnswers[i] == 1)
								{
									//Yes --> wrong
									errorFlag = true;
									System.out.println(i + ": (x!) " + thisAnswers[i]);
									debugMsg("wrong-marked answer is marked :-(");
								}
								else
								{
									//No --> correct
									System.out.println(i + ": ( ) " + thisAnswers[i]);
									debugMsg("wrong-marked answer is NOT marked :-)");
								}
							}
								
						}
					}
					else
					{
						//Type 0
						while(answer.equals(""))
						{
							answer = keyboard.next();
							if(answer.equals("question")) { showQuestion(); answer=""; }
							else if(answer.equals("hint")) { showHint(); answer=""; }
							else if(answer.equals(thisAnswers[0]) == false) { errorFlag=true; }
							this.answered=true;
						}
					}
					
					//Increasing counters
					this.numbAnswered++;
					if(errorFlag == true)
					{
						this.numbIncorrect++;
						System.out.println(locale.getValue(23));
					}
					else
					{
						this.numbCorrect++;
						System.out.println(locale.getValue(22));
					}
					
				}
				
			}
			else { endSimulation(); }
		
		}
		else
		{
			//File not loaded
			System.out.println(locale.getValue(33));
		}
	
	
	}

	public boolean isCorrect(int targetAnswer, int[] targetCorrectAnswers)
	/* Check if answer is correct */
	{
		for(int i=0;i<targetCorrectAnswers.length;i++)
		{ if(targetAnswer == targetCorrectAnswers[i]) { return true; } }
		return false;
	}
	
	void showQuestion()
	/* Shows question and (selected) answers */
	{
		//Displaying question
		System.out.println(this.thisCatalog.questions.get(this.thisQuestion).getQuestion());
		
		//Display answers if type 1/2
		String[] thisAnswers = this.thisCatalog.questions.get(this.thisQuestion).getAnswers();
		if(thisAnswers.length > 1)
		{
			//Type 1/2; is answer checked?
			for(int i=0;i<thisAnswers.length;i++)
			{
				if(this.selAnswers[i] ==1) { System.out.println(i + ": (x) " + thisAnswers[i]); }
				else { System.out.println(i + ": ( ) " + thisAnswers[i]); }
			}
		}
	}
	
	void endSimulation()
	/* End simulation */
	{
		//Showing statistics if any
		if(this.numbAnswered > 0) { showStats(); }
		
		//Resetting counters and variables
		this.numbAnswered=0;
		this.numbCorrect=0;
		this.numbIncorrect=0;
		this.numbHints=0;
		for(int i=0;i<5;i++) { this.selAnswers[i] = 0; }
		this.fileLoaded = false;
	}
	
	void showHint()
	/* Show hint if available */
	{
		//Show hint if: simulation active, question not -1, no hint used yet, hint not NONE
		if(this.active == true && this.thisQuestion != -1 && this.hintUsed == false && this.thisCatalog.questions.get(this.thisQuestion).getHint().equals("NONE") == false)
		{
			System.out.println(this.thisCatalog.questions.get(this.thisQuestion).getHint());
			this.hintUsed = true;
			this.numbHints++;
		}
		else { System.out.println(locale.getValue(25)); }
	}
	
	void showExplanation()
	/* Shows the explanation */
	{
		if(this.active == true && this.thisQuestion != -1 && this.hintUsed == false && this.thisCatalog.questions.get(this.thisQuestion).getExplanation().equals("NONE") == false)
		{
			System.out.println(this.thisCatalog.questions.get(this.thisQuestion).getExplanation());
		}
		else { System.out.println(locale.getValue(25)); }
	}
	
	void showStats()
	/* Shows the statistics */
	{
		if(this.numbAnswered > 0)
		{
			System.out.println(locale.getValue(26) + "\t" + this.numbAnswered + "\n" + locale.getValue(27) + "\t" + this.numbHints);
			System.out.println(locale.getValue(28) + "\t" + this.numbCorrect + "\n" + locale.getValue(29) + "\t" + this.numbIncorrect);
			
			//Variables
			double pktMax, pktCorrect, pktQuote;
			
			//Maximale Punktzahl je nach Modus berechnen
			if(this.endlessMode == true)
			{
				//Endlosmodus --> Anzahl der beantworteten Fragen
				pktMax = this.numbAnswered * 10;
				debugMsg("Endless mode, pktMax is answered questions (" + this.numbAnswered + ") * 10 ==> " + pktMax);
			}
			else
			{
				//Einmal-Modus --> Anzahl der Fragen im Katalog
				pktMax = this.thisCatalog.getSize() * 10;
				debugMsg("Normal mode, pktMax is number of questions in catalog (" + this.thisCatalog.getSize() + ") * 10 ==> " + pktMax);
			}
			
			//Erreichte Punkte (richtige Fragen - Hinweisabzug)
			pktCorrect = (this.numbCorrect*10) - (this.numbHints*5);
			debugMsg("Points are: (this.numbCorrect*10) - (this.numbHints*5) ==> " + pktCorrect);
			
			//Quote berechnen und ausgeben
			pktQuote = (pktCorrect*100)/pktMax;
			debugMsg("Quote is (pktCorrect*100)/pktMax ==> " + (int) pktQuote + "% (" + pktQuote + ")");
			if(pktQuote > 0) { System.out.println(locale.getValue(30) + " " + (int) pktQuote + "%"); }
			else { System.out.println(locale.getValue(30) + " 0%"); }
			
		}
		else { System.out.println(locale.getValue(31)); }
	}
	
}