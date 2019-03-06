//package eu.stdevel.jtrainer;

import java.util.Random;
import java.util.ArrayList;
import java.io.*;

/*import android.os.Environment;
import android.util.Log;*/



/*

	#####################
	questionCatalog class
	#####################
	
	
	
	FUNCTION										USAGE
	###CONSTRUCTORS###
	questionCatalog(String filename)				Constructor; gets filename of catalog as param
	questionCatalog(String filename, String name,		Constructor for new catalogs
	String desc, String author, String date,
	int limit)
	
	###EDIT FUNCTIONS###
	saveQuestion(int pointer, String question,		Saves a question with defined information
		int type, String hint, String explanation,
		ArrayList<String> answers,
		ArrayList<Integer> correctAnswers)
	saveCatalog()									Saves the whole catalog
	
	###MODE FUNCTIONS###
	setRandom(boolean bool)							Sets/unsets random mode
	setEndless(boolean bool)						Sets/unsets endless mode
	
	###DEBUG FUNCTIONS###
	debugMsg(String msg)							Enters a log message; used for debug purposes
	getIOException()								Tells if the file couldn't be openend
	countChars(String s, char c)					Counts the chars of a string; used for checking answers
	
	###CATALOG FUNCTIONS###
	getSize()										Gets the amount of questions in the catalog
	getCatalogName()								Tells the name of the catalog
	getCatalogDesc()								Gets the catalog description
	getCatalogAuthor()								Returns the author name
	getCatalogDate()								Tells the creation date of the catalog
	getCatalogLimit()								Returns the catalog time limit
	printCatalog()									Dumps the whole catalog using debugMsg()
	
	###RANDOM FUNCTIONS###
	remove(int id)									Deletes an entry of the random index
	printRandomIndex()								Dumps the random index using debugMsg()
	nextQuestion()									Returns the id of the next question

*/



public class questionCatalog
/* Fragenkatalog-Klasse */
{

	//Klassen-Tag generieren
	private static final String tag = questionCatalog.class.getSimpleName();
	
	//Variablendeklarationen
	private int numbQuestions;				//Anzahl der Fragen
	private String catalogName;				//Name des Katalogs
	private String catalogDescription;		//Beschreibung des Katalogs
	private String catalogAuthor;			//Autor des Katalogs
	private String catalogDate;				//Datum des Katalogs
	private int catalogLimit;				//Zeitlimit in Minuten
	//public question questions[];			//Fragen-Array
	public ArrayList<question> questions = new ArrayList<question>();
	private boolean modeEndless;			//Endlosmodus/einmalig
	private boolean modeRandom;				//Zufallsmodus/geordnet
	private boolean fileNotFound;			//IOException bekannt?
	private boolean debugMode = false;
	
	//Zugriffsdeklarationen
	ArrayList<Integer> elementList = new ArrayList<Integer>();	//Zugriffsindex
	Random randomGenerator = new Random();				//Zufallsgenerator
	int thisQuestion=-1;								//Fragen-ID (geordneter Modus)
	
	
	
	/* ############
	 * CONSTRUCTORS
	 * ############
	 */
	
	questionCatalog(String filename, boolean debug)
	/* Constructor for existing catalogs */
	{
		//Enabling debugging if wanted
		if(debug == true) { this.debugMode = true; }
		
		//Try to load catalog
		try
		{
			
			//Declaring
			//File f = new File(Environment.getExternalStorageDirectory()+"/"+filename);
			//FileInputStream fileIS = new FileInputStream(f);
			//BufferedReader targetFile = new BufferedReader(new InputStreamReader(fileIS));
			
			BufferedReader targetFile = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
			
				debugMsg("About to open file: " + filename);
				
				//Eckdaten des Katalogs beziehen
				numbQuestions = Integer.parseInt(targetFile.readLine());
				debugMsg("Read number of questions: " + numbQuestions);
				catalogName = targetFile.readLine();
				debugMsg("Read catalog name: " + catalogName);
				catalogDescription = targetFile.readLine();
				debugMsg("Read catalog description: " + catalogDescription);
				catalogAuthor = targetFile.readLine();
				debugMsg("Read catalog author: " + catalogAuthor);
				catalogDate = targetFile.readLine();
				debugMsg("Read catalog date: " + catalogDate);
				catalogLimit = Integer.parseInt(targetFile.readLine());
				debugMsg("Read catalog time limit: " + catalogLimit);
				
				//Fragen und Antworten in Arrays sichern
				
					//Debug-Nachricht ausgeben
					debugMsg("About to read " + numbQuestions + " questions from catalog file: " + filename);
					
					//(Temporaere) Variablendeklarationen
					//this.questions = new question[numbQuestions];
					//question questions[] = new question[numbQuestions];
					int qType;
					String qText = new String("");
					String qHint = new String("");
					String qExplanation = new String("");
					String qAnswers[] = new String[5];
					String temp = new String("");
					
					//Werte in Schleife einlesen
					for(int i=0;i<numbQuestions;i++)
					{
						//Frage und Typ einlesen
						qText = targetFile.readLine();
						debugMsg("qText: " + qText);
						qType = Integer.parseInt(targetFile.readLine());
						debugMsg("qType: " + qType);
						
						//Je nach Typ Antwort(en) einlesen
						if(qType == 0)
						{
							//Nur eine Antwort, naechste Zeile als Antwort definieren und Debug-Nachricht ausgeben
							qAnswers[0] = targetFile.readLine();
							qHint = targetFile.readLine();
							qExplanation = targetFile.readLine();
							debugMsg("Read answer: '" + qAnswers[0] + "'");
							debugMsg("Read hint: '" + qHint + "'");
							debugMsg("Read explanation: '" + qExplanation + "'");
							
							//Objekt erstellen und Initialwerte uebergeben
							/*questions[i] = new question(qText, qType, qHint, qExplanation);
							questions[i].addAnswer(qAnswers[0]);*/
							
							if(this.debugMode == true) { questions.add(new question(qText, qType, qHint, qExplanation, true)); }
							else { questions.add(new question(qText, qType, qHint, qExplanation)); }
							questions.get(i).addAnswer(qAnswers[0]);
						}
						else
						{
							//Mehrere Antworten, die naechsten fuenf Zeilen als Antworten einlesen
							qAnswers[0] = targetFile.readLine();
							qAnswers[1] = targetFile.readLine();
							qAnswers[2] = targetFile.readLine();
							qAnswers[3] = targetFile.readLine();
							qAnswers[4] = targetFile.readLine();
							
							//Korrekte Antwort(en) einlesen
							temp = targetFile.readLine();
							
							if(countChars(temp, ',') > 0)
							{
								//mehrere Antworten vorhanden
								String qCorrectAnswers[] = new String[countChars(temp, ',')+1];
								qCorrectAnswers = temp.split("\\,");
								
								//Tipp und Erklaerung lesen
								qHint = targetFile.readLine();
								debugMsg("Read hint: '" + qHint + "'");
								qExplanation = targetFile.readLine();
								debugMsg("Read explanation: '" + qExplanation + "'");
								
								//Objekt erstellen und Initialwerte uebergeben
								//questions[i] = new question(qText, qType, qHint, qExplanation);
								//questions.add(new question(qText, qType, qHint, qExplanation));
								if(this.debugMode == true) { questions.add(new question(qText, qType, qHint, qExplanation, true)); }
								else { questions.add(new question(qText, qType, qHint, qExplanation)); }
								
								//(Richtige) Antworten definieren
								for(int ii=0;ii<5;ii++)
								{
									//questions[i].addAnswer(qAnswers[ii]);
									questions.get(i).addAnswer(qAnswers[ii]);
								}
								for(int ii=0;ii<qCorrectAnswers.length;ii++)
								{
									//questions[i].setCorrect(Integer.parseInt(qCorrectAnswers[ii]));
									questions.get(i).setCorrect(Integer.parseInt(qCorrectAnswers[ii]));
								}
								
							}
							else
							{
								//eine Antwort vorhanden
								String qCorrectAnswers[] = new String[1];
								qCorrectAnswers[0] = temp;
								
								//Tipp und Erklaerung definieren
								qHint = targetFile.readLine();
								debugMsg("Read hint: '" + qHint + "'");
								qExplanation = targetFile.readLine();
								debugMsg("Read explanation: '" + qExplanation + "'");
								
								//Objekt erstellen und Initialwerte uebergeben
								//questions[i] = new question(qText, qType, qHint, qExplanation);
								//questions.add(new question(qText, qType, qHint, qExplanation));
								if(this.debugMode == true) { questions.add(new question(qText, qType, qHint, qExplanation, true)); }
								else { questions.add(new question(qText, qType, qHint, qExplanation)); }
								
								//(Richtige) Antworten definieren
								for(int ii=0;ii<5;ii++)
								{
									//questions[i].addAnswer(qAnswers[ii]);
									questions.get(i).addAnswer(qAnswers[ii]);
								}
								//questions[i].setCorrect(Integer.parseInt(temp));
								questions.get(i).setCorrect(Integer.parseInt(temp));
								
							}
							
						}

					}
				
				
				//Datei schliessen
				targetFile.close();
				debugMsg("Closed file");
				
				//Zugriffsindex erstellen
				//for(int i=0;i<this.questions.length;i++)
				for(int i=0;i<this.questions.size();i++)	
				{
					this.elementList.add(i);
					debugMsg("Added " + i + " to random index.");
				}
				
				//Exception deaktivieren
				this.fileNotFound = false;
		
		}
		catch(java.io.IOException exp)
		{
			//File not found
			debugMsg("Error while opening file " + filename);
			debugMsg("Stack trace:");
			if(this.debugMode == true) { exp.printStackTrace(); }
			fileNotFound = true;
		}
		
		
	}
	
	questionCatalog(String filename)
	/* Constructor for existing catalogs without debugging */
	{
		this(filename, false);
	}
	
	questionCatalog(String filename, String name, String desc, String author, String date, int limit)
	/* Constructor for new catalog */
	{
		
		debugMsg("About to create new catalog '" + name + "' (" + desc + ") by " + author + " with date (" + date + ") and time limit of " + limit + ".");
		//Assigning meta information
		//this.numbQuestions = 0;				//Amount of questions
		this.catalogName 		= name;			//Catalog name
		this.catalogDescription = desc;			//Catalog description
		this.catalogAuthor 		= author;		//Catalog author
		this.catalogDate 		= date;			//Catalog date
		this.catalogLimit 		= limit;		//Catalog time limit
		thisQuestion = 0;
		
		//Check whether the file is writable or not
		//File f = new File(Environment.getExternalStorageDirectory()+"/"+filename);
		//if(f.exists() || f.canWrite() == false || f.canRead() == false) { this.fileNotFound = true; }
		
		//Try to create the file
		//File f = new File(Environment.getExternalStorageDirectory()+"/"+filename);
		File f = new File(filename);
		try {
			f.createNewFile();
			debugMsg("Created catalog file (SDCARD/" + filename + ")");
		} catch (IOException e) {
			//e.printStackTrace();
			debugMsg("Error creating catalog file (SDCARD/" + filename + ")");
			this.fileNotFound = true;
		}
		
	}
	
	
	
	/* ##############
	 * EDIT FUNCTIONS
	 * ##############
	 */
	
	public void saveQuestion(int pointer, String question, int type, String hint, String explanation)
	/* Save question */
	{
		//Add question to ArrayList
		debugMsg("Save question #" + pointer + " with type " + type + ", question ('" + question + "'), hint ('" + hint + "') and explanation ('" + explanation + "')");
		//this.questions[pointer] = new question(question, type, hint, explanation);
		//this.questions.get(pointer) = new question(question, type, hint, explanation);
		//TODO: check if exist?
		
	}
	
	//public void addAnswer(int pointer, String answer) { this.questions[pointer].addAnswer(answer); }
	public void addAnswer(int pointer, String answer) { this.questions.get(pointer).addAnswer(answer); }
	//public void setCorrect(int pointer, int answer) { this.questions[pointer].setCorrect(answer); }
	public void setCorrect(int pointer, int answer) { this.questions.get(pointer).setCorrect(answer); }
	
	public void saveCatalog()
	/* Saves the whole catalog */
	{
		//...
		//this.numbQuestions = this.questions.length;
		this.numbQuestions = this.questions.size();
	}
	
	
	
	/* ##############
	 * MODE FUNCTIONS
	 * ##############
	 */
	
	public void setRandom(boolean bool) { this.modeRandom = bool; }		//Sets/unsets random mode
	public void setEndless(boolean bool) { this.modeEndless = bool; }	//Sets/unsets endless mode
	
	
	
	/* ###############
	 * DEBUG FUNCTIONS
	 * ###############
	 */
	
	//private void debugMsg(String msg) { Log.d(tag, msg); }
	private void debugMsg(String msg) { if(this.debugMode == true) { System.out.println(tag + ": " + msg); } }
	public boolean getIOException() { return this.fileNotFound; }
	private static int countChars(String s, char c) { return s.replaceAll("[^" + c + "]", "").length(); }
	
	
	
	/* #################
	 * CATALOG FUNCTIONS
	 * #################
	 */
	
	//public int getSize() { return this.questions.length; }
	public int getSize() { return this.questions.size(); }
	public String getCatalogName() { return this.catalogName; }
	public String getCatalogDesc() { return this.catalogDescription; }
	public String getCatalogAuthor() { return this.catalogAuthor; }
	public String getCatalogDate() { return this.catalogDate; }
	public int getCatalogLimit() { return this.catalogLimit; }
	public void printCatalog()
	/* Dumps whole question catalog */
	{
		//for(int i=0;i<this.questions.length;i++)
		for(int i=0;i<this.questions.size();i++)
		{
			//debugMsg("Question #" + i + ", " + (i+1) + "/" + this.questions.length);
			debugMsg("Question #" + i + ", " + (i+1) + "/" + this.questions.size());
			//this.questions[i].printQuestionData();
			this.questions.get(i).printQuestionData();
		}
	}
	
	
	
	/* ################
	 * RANDOM FUNCTIONS
	 * ################
	 */
	
	private void remove(int id)
	/* Deletes an entry of the random index */
	{
		for(int i=0;i<this.elementList.size();i++)
		{ if(elementList.get(i) == id) { this.elementList.remove(i); } }
	}
	
	public void printRandomIndex()
	/* Dumps the random index */
	{
		//Dump index if remaining
		if(this.elementList.size() > 0)
		{
			for(int i=0;i<this.elementList.size();i++)
			{ debugMsg("RI: #" + i + " ==> " + elementList.get(i)); }
		}
		else
		{
			//No entries left
			debugMsg("RI: EMPTY");
		}
	}
	
	public int nextQuestion()
	/* Returns the id of the next question */
	{
		//Next question; depending on mode
		if(this.modeRandom == false)
		{
			//Ordered mode --> any questions remaining?
			//if((this.thisQuestion+1) == this.questions.length)
			if((this.thisQuestion+1) == this.questions.size())
			{
				//Yes; start again/abort due to mode
				if(this.modeEndless == true)
				{
					debugMsg("Ordered mode, last question, endless mode - will continue from beginning");
					thisQuestion=0;
					return 0;
				}
				else
				{
					debugMsg("Ordered mode, last question, NO endless mode - will end right now");
					return -1;
				}
			}
			else
			{
				//No; increment counter and return value
				debugMsg("Ordered mode, remaining questions, will continue with next question");
				thisQuestion++;
				return thisQuestion;
			}
			
		}
		else
		{
			//Zufallsmodus --> zufaellige (noch) moegliche ID uebergeben und aus Array entfernen, falls noch ID vorhanden
			//TODO: Repeat
			if(this.elementList.size() >= 1 || this.modeEndless == true)
			{
				int temp = this.elementList.get(randomGenerator.nextInt(this.elementList.size()));
				debugMsg("Random mode, remaining questions, will continue with question #" + temp);
				if(this.modeEndless == false) { this.remove(temp); }
				return temp;
			}
			else
			{
				//No questoins remaining; return error code
				debugMsg("Random mode, once, NO remaining questions, will stop right now");
				return -1;
			}
		}
	}
	
}