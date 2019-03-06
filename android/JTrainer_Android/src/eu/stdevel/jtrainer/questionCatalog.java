package eu.stdevel.jtrainer;

import java.util.Random;
import java.util.ArrayList;
import java.io.*;

import android.os.Environment;
import android.util.Log;



/*

	####################
	FRAGENKATALOG-KLASSE
	####################
	
	
	
	FUNKTIONSNAME									NUTZEN
	###KONSTRUKTOR###
	questionCatalog(String filename)				Konstuktor, erhaelt Dateinamen als Parameter
	###KATALOGFUNTIONEN###
	setMode(String mode)							Modus setzen (zufaellig/geordnet), erhaelt als Parameter ("random" [zufaellig] oder "ordered" [geordnet])
	setEndless(boolean bool)						Endlosmodus setzen/entfernen, erhaelt als Parameter (true [ja] oder false [nein])
	nextQuestion()									Naechste Fragen-ID uebergeben
	###DATENSATZFUNKTIONEN###
	getSize()										Bezieht die Anzahl der hinterlegten Fragen
	printCatalog()									Gibt den Fragenkatalog aus
	getCatalogName()								Bezieht den Fragenkatalog-Namen
	getCatalogDesc()								...die Beschreibung des Fragenkatalogs
	getCatalogAuthor()								...den Autor
	getCatalogDate()								...das Datum des Fragenkatalogs
	getCatalogLimit()								...das Zeitlimit des Katalogs
	###ZUFALLSMODUSFUNKTIONEN###
	remove(int id)									Eintrag aus dem Zugriffsindex entfernen
	printRandomIndex()								Zugriffsindex ausgeben
	###DEBUG-FUNKTIONEN###
	countChars(String s, char c)					Zaehlt die Vorkommen eines Buchstabens in einem String, wird zur Antworten-Analyse benoetigt
	debugMsg(String msg)							Gibt eine Debug-Nachricht aus, dient zur Fehleranalyse innerhalb Funktionen
	getIOException()								Gibt aus, ob die Datei nicht geoeffnet werden konnte

*/



public class questionCatalog
/* Fragenkatalog-Klasse */
{

	//Klassen-Tag generieren
	private static final String tag = "questionCatalog";
	
	//Variablendeklarationen
	private int numbQuestions;				//Anzahl der Fragen
	private String catalogName;				//Name des Katalogs
	private String catalogDescription;		//Beschreibung des Katalogs
	private String catalogAuthor;			//Autor des Katalogs
	private String catalogDate;				//Datum des Katalogs
	private int catalogLimit;				//Zeitlimit in Minuten
	public question questions[];			//Fragen-Array
	private boolean modeEndless;			//Endlosmodus/einmalig
	private boolean modeRandom;				//Zufallsmodus/geordnet
	private boolean fileNotFound;			//IOException bekannt?
	
	//Zugriffsdeklarationen
	ArrayList<Integer> elementList = new ArrayList<Integer>();	//Zugriffsindex
	Random randomGenerator = new Random();				//Zufallsgenerator
	int thisQuestion=-1;								//Fragen-ID (geordneter Modus)
	
	
	
	questionCatalog(String filename)
	/* Konstruktur mit Dateiangabe */
	{
		
		//Versuche Katalog zu laden
		try
		{
			
			//Deklarationen
			//BufferedReader targetFile = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
			
			
			File f = new File(Environment.getExternalStorageDirectory()+"/"+filename);
			FileInputStream fileIS = new FileInputStream(f);
			BufferedReader targetFile = new BufferedReader(new InputStreamReader(fileIS));
			
			this.debugMsg("About to open file: " + filename);
				
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
					this.questions = new question[numbQuestions];
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
							questions[i] = new question(qText, qType, qHint, qExplanation);
							questions[i].addAnswer(qAnswers[0]);
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
								questions[i] = new question(qText, qType, qHint, qExplanation);
								
								//(Richtige) Antworten definieren
								for(int ii=0;ii<5;ii++)
								{
									questions[i].addAnswer(qAnswers[ii]);
								}
								for(int ii=0;ii<qCorrectAnswers.length;ii++)
								{
									questions[i].setCorrect(Integer.parseInt(qCorrectAnswers[ii]));
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
								questions[i] = new question(qText, qType, qHint, qExplanation);
								
								//(Richtige) Antworten definieren
								for(int ii=0;ii<5;ii++)
								{
									questions[i].addAnswer(qAnswers[ii]);
								}
								questions[i].setCorrect(Integer.parseInt(temp));
								
							}

						}
						
					}
				
				//Datei schliessen
				targetFile.close();
				debugMsg("Closed file");
				
				//Zugriffsindex erstellen
				for(int i=0;i<this.questions.length;i++)
				{
					this.elementList.add(i);
					debugMsg("Added " + i + " to random index.");
				}
				
				//Exception deaktivieren
				this.fileNotFound = false;
		
		}
		catch(java.io.IOException exp)
		{
			//Gebe Fehler aus
			debugMsg("Error while opening file " + filename);
			debugMsg("Stack trace:");
			exp.printStackTrace();
			this.fileNotFound = true;
		}
		
	}
	
	public void setRandom(boolean bool)
	/* Zufallsmodus setzen/entfernen */
	{
		//Flag je nach uebergebenen Wert setzen
		this.modeRandom = bool;
	}
	
	public void setEndless(boolean bool)
	/* Endlosmodus setzen/entfernen */
	{
		//Flag je nach uebergebenen Wert setzen
		this.modeEndless = bool;
	}
	
	private void debugMsg(String msg)
	/* Debug-Nachricht ausgeben */
	{
		//Meldung ausgeben
		Log.d(tag, msg);
	}
	
	public boolean getIOException()
	/* Evtl. bekannte IOException bekanntgeben */
	{ return this.fileNotFound; }
	
	private static int countChars(String s, char c)
	/* Zaehlt die Vorkommen eines Buchstabens in einem Strings */
	{
		return s.replaceAll("[^" + c + "]", "").length();
	}
	
	public int getSize()
	/* Anzahl der Eintraege ausgeben */
	{
		return this.questions.length;
	}
	
	public void printCatalog()
	/* Fragenkatalog ausgeben */
	{
		for(int i=0;i<this.questions.length;i++)
		{
			debugMsg("Question #" + i + ", " + (i+1) + "/" + this.questions.length);
			this.questions[i].printQuestionData();			
		}
	}
	
	//Funktionen zum Beziehen von Kataloginformationen
	public String getCatalogName() { return this.catalogName; }
	public String getCatalogDesc() { return this.catalogDescription; }
	public String getCatalogAuthor() { return this.catalogAuthor; }
	public String getCatalogDate() { return this.catalogDate; }
	public int getCatalogLimit() { return this.catalogLimit; }

	private void remove(int id)
	/* Eintrag aus dem Zugriffsindex entfernen */
	{
		for(int i=0;i<this.elementList.size();i++)
		{
			if(elementList.get(i) == id) { this.elementList.remove(i); }
		}
	}
	
	public void printRandomIndex()
	/* Zugriffsindex ausgeben */
	{
		//Index ausgeben, falls noch Eintraege vorhanden
		if(this.elementList.size() > 0)
		{
			for(int i=0;i<this.elementList.size();i++)
			{
				debugMsg("RI: #" + i + " ==> " + elementList.get(i));
			}
		}
		else
		{
			//Keine Eintraege mehr vorhanden
			debugMsg("RI: EMPTY");
		}
	}
	
	public int nextQuestion()
	/* Naechste Fragen-ID uebergeben */
	{
		//Naechste Frage je nach Modus laden
		if(this.modeRandom == false)
		{
			//Geordneter Modus --> war dies die letzte Frage?
			if((this.thisQuestion+1) == this.questions.length)
			{
				//Ja, je nach Modus von vorne beginnen oder abbrechen
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
				//Nein erhoehe Counter und uebergebe Wert
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
				//Keine Frage mehr uebrig, uebergebe Fehlercode
				debugMsg("Random mode, once, NO remaining questions, will stop right now");
				return -1;
			}
		}
	}
	
}