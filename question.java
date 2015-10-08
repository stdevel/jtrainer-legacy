import java.util.ArrayList;



/*

	#########
	question class
	#########
	
	
	
	QUESTION TYPE		DESCRIPTION
	0				answer has to be typed in - only one correct answer
	1				multiple answers - one correct
	2				multiple answers - multiple correct
	
	
	
	FUNCTION NAME						DESCRIPTION
	###CONSTRUCTOR###
	question(String q, int t, String h, String e)		Konstruktor, erhaelt als Parameter die Frage (q), den Typ (t), einen Hinweis (h) und eine Erlaeuterung (e) als Parameter
	###DATASET FUNCTIONS###
	addAnswer(String answer)					Fuegt eine Antwort hinzu, hinzuzufuegende Antwort wird als String-Parameter uebergeben
	setCorrect(int answer)					Markiert eine bestimmte Antwort als richtig, die ID der Frage wird als Parameter uebergeben
	###GETTER/SETTER###
	getQuestion()							retrieves the question
	getHint()							...the hint
	getExplanation()						...the explanation
	getType()							...the question type
	getNumbAnswers()						...the amount of answers
	getAnswers()							...an answer array
	getCorrectAnswers()						...an array of correct answers
	###DEBUG FUNCTIONS###
	printQuestionData()						dumps all defined question data
	debugMsg(String msg)					writes a debug message

*/



public class question
/* question class */
{
	
	//variables
	String question;										//question
	String hint;											//hint
	String explanation;									//explanation
	int type;											//type (see above)
	ArrayList<String> answers = new ArrayList<String>();			//answers
	ArrayList<Integer> correctAnswers = new ArrayList<Integer>();	//correct answers
	
	
	
	question(String q, int t, String h, String e)
	/* constructor */
	{
		//setting information and write debug message
		this.question = q;		//question
		this.type	= t;			//type
		this.hint = h;			//hint
		this.explanation = e;		//explanation
		debugMsg("Created question with type='" + t + "', question='" + q + "', hint='" + h + "' and explanation='" + e + "'");
	}
	
	protected void finalize()
	/* destructor */
	{
		//clear objects and write debug message
		this.answers.clear();
		this.correctAnswers.clear();
		debugMsg("Killed question, goodbye!");
	}
	
	
	
	private void debugMsg(String msg)
	/* write debug message */
	{
		System.out.println("Q: " + msg);
	}
	
	public void addAnswer(String answer)
	/* add answer */
	{
		if(this.type == 0)
		{
			//type 0 - set answer and flag for correct answer
			this.answers.add(answer);
			this.correctAnswers.add(0);
			debugMsg("Detected type 0 question, set answer '" + answer + "' to the correct answer.");
		}
		else
		{
			//type 1 oder 2 - add answer
			this.answers.add(answer);
			debugMsg("Added answer '" + answer + "' to list of answers - don't forget to set the correct answer");
		}
	}
	
	public void setCorrect(int answer)
	/* mark existing answer as correct */
	{
		
		if(this.type == 0)
		{
			//type 0 - answer already defined
			debugMsg("Detected type 0 question, correct answer is already defined, droog!");
		}
		else if(this.type == 1)
		{
			//type 1 - already a correct answer given?
			if(this.correctAnswers.size() != 0)
			{
				//yes --> error
				debugMsg("Error, correct answer of this question has already been defined (" + this.correctAnswers.get(0) + ", " + this.answers.get(this.correctAnswers.get(0)) + ")");
			}
			else
			{
				//no --> add if exists
				if(answer > this.answers.size() && this.answers.contains(answer) == false)
				{
					//error --> answer not in index
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
		{
			//type 2 - mark as correct if answer exists and not already in index
			if(answer > this.answers.size() && this.answers.contains(answer) == false)
			{
				//error --> answer not in index
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
	/* get question */
	{
		//return question
		return this.question;
	}
	
	public String getHint()
	/* get hint */
	{
		//return hint
		return this.hint;
	}
	
	public String getExplanation()
	/* get explanation */
	{
		//return hint
		return this.explanation;
	}
	
	public int getType()
	/* get question type */
	{
		//return type
		return this.type;
	}
	
	public int getNumbAnswers()
	/* get amount of answers */
	{
		//one answer if type 0
		if(this.type == 0) { return 1; }
		else
		{
			//maybe multiple answers --> read and return
			return this.answers.size();
		}
	}
	
	public String[] getAnswers()
	/* get answers */
	{
		//return answers
		String[] questionAnswers = new String[this.answers.size()];
		for(int i=0;i<this.answers.size();i++) { questionAnswers[i] = this.answers.get(i); }
		return questionAnswers;
	}
	
	public int[] getCorrectAnswers()
	/* get id's of correct answers */
	{
		//return id's
		int[] questionCorrectAnswers = new int[this.correctAnswers.size()];
		for(int i=0;i<this.correctAnswers.size();i++) { questionCorrectAnswers[i] = this.correctAnswers.get(i); }
		return questionCorrectAnswers;
	}
	
	public void printQuestionData()
	/* write out dataset */
	{
		//print question and hint
		debugMsg("Question: " + this.question);
		debugMsg("Hint: " + this.hint);
		
		//print type and answers
		switch(this.type)
		{
			case 0:		//type 0
						
							//dump type and correct answer
							debugMsg("Type: 0 (insert-question)");
							debugMsg("Correct answer: " + this.answers.get(0));
						
						break;
			
			case 1:		//type 1
						
							//dump type and correct answer
							debugMsg("Type: 0 (insert-question)");
							debugMsg("Correct answer: " + this.answers.get(0));
							debugMsg("Type: 1 (one correct answer)");
							for(int i=0; i<this.answers.size();i++) { debugMsg("Answer #" + i + ": " + this.answers.get(i)); }
							debugMsg("Correct answer: " + this.correctAnswers.get(0) + " (" + this.answers.get(this.correctAnswers.get(0)) + ")");
							
						break;
			
			case 2:		//type 2
						
							//dump type and correct answer
							debugMsg("Type: 2 (multiple answers)");
							for(int i=0;i<this.answers.size();i++) { debugMsg("Answer #" + i + ": " + this.answers.get(i)); }
							debugMsg("Correct answers:");
							for(int id : this.correctAnswers) { debugMsg("Answer #" + id + ": " + this.answers.get(id)); }
						
						break;
			
			default:		//other - database error
						debugMsg("Type: other (err? database problem)");
		}
		
		//write out explanation
		debugMsg("Explanation: " + this.explanation);
	}
	
}