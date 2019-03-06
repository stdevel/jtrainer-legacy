package eu.stdevel.jtrainer;

import java.util.ArrayList;

import android.util.Log;



/*

	##########
	FRAGENKLASSE
	##########
	
	
	
	FRAGENTYPEN	ERLAEUTERUNG
	0				Antwort muss selbst eingegebenen werden, nur eine richtige Antwort
	1				Mehrere moegliche Antworten, eine davon richtig
	2				Mehrere moegliche Antworten, mehrere davon richtig
	
	
	
	FUNKTIONSNAME										NUTZEN
	###KONSTRUKTOR###
	question(String q, int t, String h, String e)		Konstruktor, erhaelt als Parameter die Frage (q), den Typ (t), einen Hinweis (h) und eine Erlaeuterung (e) als Parameter
	###DATENSATZFUNKTIONEN###
	addAnswer(String answer)							Fuegt eine Antwort hinzu, hinzuzufuegende Antwort wird als String-Parameter uebergeben
	setCorrect(int answer)								Markiert eine bestimmte Antwort als richtig, die ID der Frage wird als Parameter uebergeben
	###INTERAKTIONSFUNKTIONEN###
	getQuestion()										Bezieht die Frage
	getHint()											Bezieht den Hinweis
	getExplanation()									...die Erklaerung der Antwort
	getType()											...den Typ der Frage
	getNumbAnswers()									...die Anzahl der Fragen
	getAnswers()										...die Antworten in Form eines Arrays
	getCorrectAnswers()									...die richtigen Antworten in Form eines Arrays
	###DEBUG-FUNKTIONEN###
	printQuestionData()									Gibt alle definierten Informationen aus
	debugMsg(String msg)								Gibt eine Debug-Nachricht aus, dient zur Fehleranalyse innerhalb Funktionen

*/



public class question
/* Fragenklasse */
{

	//Klassen-Tag generieren
	//private static final String tag = starten.class.getSimpleName();
	private static final String tag = "question";
	
	//Variablendeklarationen
	String question;										//Frage
	String hint;											//Tipp
	String explanation;										//Erklaerung
	int type;												//Typ: 0=eingeben, 1=eine Antwort, 2=mehrere Antworten
	ArrayList<String> answers = new ArrayList<String>();			//Antworten
	ArrayList<Integer> correctAnswers = new ArrayList<Integer>();	//Richtige Antworten
	
	
	
	question(String q, int t, String h, String e)
	/* Konstruktor */
	{
		//Setzen der Informationen und Debug-Nachricht ausgeben
		this.question = q;		//Frage
		this.type	= t;			//Typ
		this.hint = h;			//Tipp
		this.explanation = e;	//Erklaerung
		debugMsg("Created question with type='" + t + "', question='" + q + "', hint='" + h + "' and explanation='" + e + "'");
	}
	
	protected void finalize()
	/* Destruktor */
	{
		//Objekte leeren und Debug-Nachricht ausgeben
		this.answers.clear();
		this.correctAnswers.clear();
		debugMsg("Killed question, goodbye!");
	}
	
	
	
	private void debugMsg(String msg)
	/* Debug-Nachricht ausgeben */
	{
		//System.out.println("Q: " + msg);
		Log.d(tag, msg);
	}
	
	public void addAnswer(String answer)
	/* Antwort hinzufuegen */
	{
		if(this.type == 0)
		{
			//Typ 0, setze Antwort und Flag auf richtige Antwort
			this.answers.add(answer);
			this.correctAnswers.add(0);
			debugMsg("Detected type 0 question, set answer '" + answer + "' to the correct answer.");
		}
		else
		{
			//Type 1 oder 2, fuege Antwort hinzu
			this.answers.add(answer);
			debugMsg("Added answer '" + answer + "' to list of answers - don't forget to set the correct answer");
		}
	}
	
	public void setCorrect(int answer)
	/* Bestehende Antwort als korrekt definieren */
	{
		
		if(this.type == 0)
		//Typ 0-Frage (Antwort selbst eingeben)
		{
			//FEHLER: Antwort schon definiert
			debugMsg("Detected type 0 question, correct answer is already defined, droog!");
		}
		else if(this.type == 1)
		//Typ 1-Frage (eine richtige Antwort, mehrere zur Auswahl stehend)
		{
			//Ist bereits eine korrekte Antwort eingetragen?
			if(this.correctAnswers.size() != 0)
			{
				//Ja, Fehler
				debugMsg("Error, correct answer of this question has already been defined (" + this.correctAnswers.get(0) + ", " + this.answers.get(this.correctAnswers.get(0)) + ")");
			}
			else
			{
				//Nein, trage ein, falls vorhanden
				if(answer > this.answers.size() && this.answers.contains(answer) == false)
				{
					//Fehler, Antwort nicht im Index
					debugMsg("Answer #" + answer + " out of bound (" + this.answers.size() + ", 0-" + (this.answers.size()-1) +"), won't define as correct.");
				}
				else
				{
					this.correctAnswers.add(answer);
					debugMsg("Set correct answer to #" + answer + " (" + this.answers.get(answer) + ")");
				}
			}
		}
		else if(this.type == 2)
		//Typ 2-Frage (mehrere Antworten, mehrere Richtige)
		{
			//Markiere als korrekt, falls noch nicht vorhande und im Index
			if(answer > this.answers.size() && this.answers.contains(answer) == false)
			{
				//Fehler, Antwort nicht im Index
				debugMsg("Answer #" + answer + " out of bound (" + this.answers.size() + ", 0-" + (this.answers.size()-1) +"), won't define as correct.");
			}
			else
			{
				this.correctAnswers.add(answer);
				debugMsg("Marked answer #" + answer + " (" + this.answers.get(answer) + ") " +"as correct.");
			}
		}
		
	}
	
	public String getQuestion()
	/* Frage beziehen */
	{
		//Frage uebergeben
		return this.question;
	}
	
	public String getHint()
	/* Hinweis beziehen */
	{
		//Hinweis uebergeben
		return this.hint;
	}
	
	public String getExplanation()
	/* Erklaerung beziehen */
	{
		//Erklaerung uebergeben
		return this.explanation;
	}
	
	public int getType()
	/* Typ der Frage beziehen */
	{
		//Typ uebergeben
		return this.type;
	}
	
	public int getNumbAnswers()
	/* Anzahl der Antworten beziehen */
	{
		//Eine Antwort, falls Typ 0
		if(this.type == 0)
		{
			return 1;
		}
		//Evtl. mehrere Antworten, auslesen und uebergeben
		else
		{
			return this.answers.size();
		}
	}
	
	public String[] getAnswers()
	/* Antworten beziehen */
	{
		//Antworten uebergeben
		String[] questionAnswers = new String[this.answers.size()];
		for(int i=0;i<this.answers.size();i++)
		{
			questionAnswers[i] = this.answers.get(i);
		}
		return questionAnswers;
	}
	
	public int[] getCorrectAnswers()
	/* IDs der richtigen Antworten beziehen */
	{
		//IDs uebergeben
		int[] questionCorrectAnswers = new int[this.correctAnswers.size()];
		for(int i=0;i<this.correctAnswers.size();i++)
		{
			questionCorrectAnswers[i] = this.correctAnswers.get(i);
		}
		return questionCorrectAnswers;
	}
	
	public void printQuestionData()
	/* Datensatz ausgeben */
	{
	
		//Frage und Tipp ausgeben
		debugMsg("Question: " + this.question);
		debugMsg("Hint: " + this.hint);
		
		//Typ und Antworten ausgeben		
		switch(this.type)
		{
			case 0:		//Antwort selbst eingeben
						
							//Ausgabe des Typs und der richtigen Antwort
							debugMsg("Type: 0 (insert-question)");
							debugMsg("Correct answer: " + this.answers.get(0));
						
						break;
			
			case 1:		//Eine richtige Antwort, mehrere zur Auswahl stehend
						
							//Ausgabe des Typs und der richtigen Antworten
							debugMsg("Type: 0 (insert-question)");
							debugMsg("Correct answer: " + this.answers.get(0));
							
							debugMsg("Type: 1 (one correct answer)");
							for(int i=0; i<this.answers.size();i++)
							{
								debugMsg("Answer #" + i + ": " + this.answers.get(i));
							}
							debugMsg("Correct answer: " + this.correctAnswers.get(0) + " (" + this.answers.get(this.correctAnswers.get(0)) + ")");
							
						break;
			
			case 2:		//Mehrere richtige Antworten --> Antworten ausgeben
						
							//Ausgabe des Typs und der richtigen Antworten
							debugMsg("Type: 2 (multiple answers)");
							for(int i=0;i<this.answers.size();i++)
							{
								debugMsg("Answer #" + i + ": " + this.answers.get(i));
							}
							
							debugMsg("Correct answers:");
							for(int id : this.correctAnswers)
							{
								debugMsg("Answer #" + id + ": " + this.answers.get(id));
							}
						
						break;
			
			default:		//sonstige - Datenbankfehler
						debugMsg("Type: other (err? database problem)");
		}
		
		//Erklaerung ausgeben
		debugMsg("Explanation: " + this.explanation);
	
	}
	
}