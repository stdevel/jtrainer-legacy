//package eu.stdevel.jtrainer;

import java.util.ArrayList;

//import android.util.Log;



/*

	##############
	question class
	##############
	
	
	
	TYPE		EXPLANATION
	0			Answer needs to be typed in
	(1			DEPRECATED: Multiple answers; once is correct)
	2			Multiple choice
	
	
	
	FUNCTION										USAGE
	###CONSTRUCTORS###
	question(String q, int t, String h, String e)	New question; params: q=question, t=type, h=hint, e=explanation
	question()										New empty question
	
	###DATASET FUNCTIONS###
	addAnswer(String answer)						Adds answer (String param)
	TODO: addAnswer with true for automatic "correct" mapping
	setCorrect(int answer)							Marks answer (Integer param) as correct
	
	###SET FUNCTIONS###
	setQuestion(String q)							Sets the question
	setType(int t)									...the type
	setHint(String h)								...the hint
	setExplanation(String e)						...the explanation
	
	###GET FUNCTIONS###
	getQuestion()									Returns the question
	getHint()										...the hint
	getExplanation()								...the explanation
	getType()										...the type
	getNumbAnswers()								...the amount of answers
	getAnswers()									...the answer as String array
	getCorrectAnswers()								...the correct answers as Integer array
	
	###DEBUG FUNCTIONS###
	printQuestionData()								Prints all defined information as dump
	debugMsg(String msg)							Prints a debug message

*/



public class question
/* Question class */
{

	//Generate class tag
	private static final String tag = question.class.getSimpleName();
	
	//Variables
	boolean debugMode = false;
	String question;												//question
	String hint;													//hint
	String explanation;												//explanation
	int type;														//type
	ArrayList<String> answers = new ArrayList<String>();			//answers
	ArrayList<Integer> correctAnswers = new ArrayList<Integer>();	//correct answers
	
	
	
	/* ############
	 * CONSTRUCTORS
	 * ############
	 */
	
	question(String q, int t, String h, String e, boolean debug)
	/* New question with defined information */
	{
		//Enabling debugging if wanted
		if(debug == true) { this.debugMode = true; }
		
		//Setting information
		this.question = q;
		this.type = t;
		this.hint = h;
		this.explanation = e;
		debugMsg("Created question with type='" + t + "', question='" + q + "', hint='" + h + "' and explanation='" + e + "'");
	}
	
	question(String q, int t, String h, String e)
	/* New question with defined information without debugging */
	{
		this(q, t, h, e, false);
	}
	
	question()
	/* New empty question */
	{
		//Setting placeholder information
		this("NONE", 2, "NONE", "NONE");
	}
	
	protected void finalize()
	/* Destructor */
	{
		//Clear objects
		this.answers.clear();
		this.correctAnswers.clear();
		debugMsg("Killed question, goodbye!");
	}
	
	
	
	/* #################
	 * DATASET FUNCTIONS
	 * #################
	 */
	
	public void addAnswer(String answer)
	/* Adds answer */
	{
		//Adding answer depending on type
		if(this.type == 0)
		{
			//Type 0/direct input; setting answer and flag to first (and only) answer
			this.answers.add(answer);
			this.correctAnswers.add(0);
			debugMsg("Detected type 0 question, set answer '" + answer + "' to the correct answer.");
		}
		else
		{
			//Type 1 or 2; adding answer
			this.answers.add(answer);
			debugMsg("Added answer '" + answer + "' to list of answers - don't forget to set the correct answer");
		}
	}
	
	public void setCorrect(int answer)
	/* Defining existing answer as correct */
	{
		//Defining depending on question type
		if(this.type == 0)
		//Type 0/direct input
		{
			//answer already defined while creating question
			debugMsg("Detected type 0 question, correct answer is already defined, droog!");
		}
		else if(this.type == 1)
		//Type 1; multiple answers - once correct
		{
			//Already as correct defined answer found?
			if(this.correctAnswers.size() != 0)
			{
				//Yes; error
				debugMsg("Error, correct answer of this question has already been defined (" + this.correctAnswers.get(0) + ", " + this.answers.get(this.correctAnswers.get(0)) + ")");
			}
			else
			{
				//No; adding answer if exists
				if(answer > this.answers.size() && this.answers.contains(answer) == false)
				{
					//Error; answer doesn't exist
					debugMsg("Answer #" + answer + " out of bound (" + this.answers.size() + ", 0-" + (this.answers.size()-1) +"), won't define as correct.");
				}
				else
				{
					//Answer exists; adding
					this.correctAnswers.add(answer);
					debugMsg("Set correct answer to #" + answer + " (" + this.answers.get(answer) + ")");
				}
			}
		}
		else if(this.type == 2)
		//Type 2; multiple answer - multiple correct
		{
			//Defining as correct if answers exists and not in index
			if(answer > this.answers.size() && this.answers.contains(answer) == false)
			{
				//Error; answer doesn't exist
				debugMsg("Answer #" + answer + " out of bound (" + this.answers.size() + ", 0-" + (this.answers.size()-1) +"), won't define as correct.");
			}
			else
			{
				//Adding to index
				this.correctAnswers.add(answer);
				debugMsg("Marked answer #" + answer + " (" + this.answers.get(answer) + ") " +"as correct.");
			}
		}
		
	}
	
	
	
	/* #############
	 * SET FUNCTIONS
	 * #############
	 */
	
	private void setQuestion(String q) { this.question = q; }
	private void setType(int t) { this.type = t; }
	private void setHint(String h) { this.hint = h; }
	private void setExplanation(String e) { this.explanation = e; }
	
	
	
	/* #############
	 * GET FUNCTIONS
	 * #############
	 */
	
	public String getQuestion() { return this.question; }
	public String getHint() { return this.hint; }
	public String getExplanation() { return this.explanation; }
	public int getType() { return this.type; }
	
	public int getNumbAnswers()
	/* Get amount of answers */
	{
		//One answer if type 0
		if(this.type == 0)
		{
			return 1;
		}
		//Maybe multiple answers
		else
		{
			return this.answers.size();
		}
	}
	
	public String[] getAnswers()
	/* Return answers */
	{
		//Copy answer to temp array; return array
		String[] questionAnswers = new String[this.answers.size()];
		for(int i=0;i<this.answers.size();i++) { questionAnswers[i] = this.answers.get(i); }
		return questionAnswers;
	}
	
	public int[] getCorrectAnswers()
	/* Get IDs of correct answers */
	{
		//Copy IDs to temp array; return array
		int[] questionCorrectAnswers = new int[this.correctAnswers.size()];
		for(int i=0;i<this.correctAnswers.size();i++) { questionCorrectAnswers[i] = this.correctAnswers.get(i); }
		return questionCorrectAnswers;
	}
	
	
	
	/* ###############
	 * DEBUG FUNCTIONS
	 * ###############
	 */
	
	public void printQuestionData()
	/* Dump dataset */
	{
		debugMsg("Question: " + this.question);
		debugMsg("Hint: " + this.hint);
		
		//Dump type and answers		
		switch(this.type)
		{
			case 0:		//Type 0
							debugMsg("Type: 0 (insert-question)");
							debugMsg("Correct answer: " + this.answers.get(0));
						break;
			
			case 1:		//Type 1
							debugMsg("Type: 0 (insert-question)");
							debugMsg("Correct answer: " + this.answers.get(0));
							debugMsg("Type: 1 (one correct answer)");
							for(int i=0; i<this.answers.size();i++) { debugMsg("Answer #" + i + ": " + this.answers.get(i)); }
							debugMsg("Correct answer: " + this.correctAnswers.get(0) + " (" + this.answers.get(this.correctAnswers.get(0)) + ")");
						break;
			
			case 2:		//Type 2
							debugMsg("Type: 2 (multiple answers)");
							for(int i=0;i<this.answers.size();i++) { debugMsg("Answer #" + i + ": " + this.answers.get(i)); }
							debugMsg("Correct answers:");
							for(int id : this.correctAnswers) { debugMsg("Answer #" + id + ": " + this.answers.get(id)); }
						break;
			
			default:	//Error
						debugMsg("Type: other (err? database problem)");
		}
		
		//Dump explanation
		debugMsg("Explanation: " + this.explanation);
	
	}
	
	//private void debugMsg(String msg) { Log.d(tag, msg); }
	private void debugMsg(String msg) { if(this.debugMode == true) { System.out.println(tag + ": " + msg); } }
	
}