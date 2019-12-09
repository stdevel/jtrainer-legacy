import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.ArrayList;
import java.io.*;



public class questionCatalog
/* question catalog class */
{
	private String catalogName;					// name of catalog
	private String catalogDescription;			// description of catalog
	private String catalogAuthor;				// catalog author
	private String catalogDate;					// creation date of catalog
	private int catalogLimit;					// time limit in minutes
	public question[] questions;				// question array
	private boolean modeEndless;				// endless mode enabled?
	private String catalogMode = "random";		// mode (random/ordered)
	
	//access declarations
	final ArrayList<Integer> elementList = new ArrayList<>();	// access index
	final Random randomGenerator = new Random();				// random generator
	int thisQuestion=-1;										// question ID (ordered mode)
	
	
	
	questionCatalog(String filename)
	/* constructor with file name */
	{
		
		//try loading catalog
		try
		{
			
			// declarations
			BufferedReader targetFile = new BufferedReader(
					new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8)
			);
			this.debugMsg("About to open file: " + filename);
				
			// get catalog meta information

			// variables
			int numbQuestions = Integer.parseInt(targetFile.readLine());		// number of questions

			// read catalog metadata
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
				
			// save questions and answers in array
				
					// write debug message
					debugMsg("About to read " + numbQuestions + " questions from catalog file: " + filename);
					
					// (temporary) variable declarations
					this.questions = new question[numbQuestions];
					int qType;
					String qText;
					String qHint;
					String qExplanation;
					String[] qAnswers = new String[5];
					String temp;
					
					// read values in loop
					for(int i = 0; i< numbQuestions; i++)
					{
						// read question and type
						qText = targetFile.readLine();
						debugMsg("qText: " + qText);
						qType = Integer.parseInt(targetFile.readLine());
						debugMsg("qType: " + qType);
						
						// read information based on type
						if(qType == 0)
						{
							// only one answer - define next line as answer and write debug message
							qAnswers[0] = targetFile.readLine();
							qHint = targetFile.readLine();
							qExplanation = targetFile.readLine();
							debugMsg("Read answer: '" + qAnswers[0] + "'");
							debugMsg("Read hint: '" + qHint + "'");
							debugMsg("Read explanation: '" + qExplanation + "'");
							
							// create object and return initial values
							questions[i] = new question(qText, qType, qHint, qExplanation);
							questions[i].addAnswer(qAnswers[0]);
						}
						else
						{
							// multiple answers - read next 5 lines as answers
							qAnswers[0] = targetFile.readLine();
							qAnswers[1] = targetFile.readLine();
							qAnswers[2] = targetFile.readLine();
							qAnswers[3] = targetFile.readLine();
							qAnswers[4] = targetFile.readLine();
							
							// read correct answers
							temp = targetFile.readLine();
							
							if(countChars(temp, ',') > 0)
							{
								// multiple answers exist
								String[] qCorrectAnswers = new String[countChars(temp, ',')+1];
								qCorrectAnswers = temp.split(",");
								
								// read hint and explanation
								qHint = targetFile.readLine();
								debugMsg("Read hint: '" + qHint + "'");
								qExplanation = targetFile.readLine();
								debugMsg("Read explanation: '" + qExplanation + "'");
								
								// create object and return initial values
								questions[i] = new question(qText, qType, qHint, qExplanation);
								
								// define correct answers
								for(int ii=0;ii<5;ii++) { questions[i].addAnswer(qAnswers[ii]); }
								for (String qCorrectAnswer : qCorrectAnswers) {
									questions[i].setCorrect(Integer.parseInt(qCorrectAnswer));
								}
							}
							else
							{
								// one answer exists
								String[] qCorrectAnswers = new String[1];
								qCorrectAnswers[0] = temp;
								
								// read hint and explanation
								qHint = targetFile.readLine();
								debugMsg("Read hint: '" + qHint + "'");
								qExplanation = targetFile.readLine();
								debugMsg("Read explanation: '" + qExplanation + "'");
								
								// create object and return initial values
								questions[i] = new question(qText, qType, qHint, qExplanation);
								
								// define correct answers
								for(int ii=0;ii<5;ii++) { questions[i].addAnswer(qAnswers[ii]); }
								questions[i].setCorrect(Integer.parseInt(temp));
								
							}

						}
						
					}
				
				// close file
				targetFile.close();
				debugMsg("Closed file");
				
				// create access index
				for(int i=0;i<this.questions.length;i++)
				{
					this.elementList.add(i);
					debugMsg("Added " + i + " to random index.");
				}
		
		}
		catch(java.io.IOException exp)
		{
			// write error message
			debugMsg("Error while opening file " + filename);
			debugMsg("Stack trace:");
			exp.printStackTrace();	
		}
		
	}
	
	public void setMode(String mode)
	/* set mode (random/ordered) */
	{
		// set mode based on string
		if(mode.equals("ordered")) { this.catalogMode = "ordered"; }
		else { this.catalogMode = "random"; }
	}
	
	public void setEndless(boolean bool)
	/* set/unset endless mode */
	{
		// set flag based on param
		this.modeEndless = bool;
	}
	
	private static int countChars(String s, char c)
	/* counts the occurrences of a char in a string */
	{
		return s.replaceAll("[^" + c + "]", "").length();
	}
	
	private void debugMsg(String msg)
	/* write debug message */
	{
		// write message
		System.out.println("QC: " + msg);
	}
	
	public int getSize()
	/* write number of entries */
	{
		return this.questions.length;
	}

	public int getRemainingQuestions()
	/* return amount of remaining questions */
	{
		return this.elementList.size();
	}
	
	public void printCatalog()
	/* write out question catalog */
	{
		for(int i=0;i<this.questions.length;i++)
		{
			debugMsg("Question #" + i + ", " + (i+1) + "/" + this.questions.length);
			this.questions[i].printQuestionData();			
		}
	}
	
	//function for getting catalog meta information
	public String getCatalogName() { return this.catalogName; }
	public String getCatalogDesc() { return this.catalogDescription; }
	public String getCatalogAuthor() { return this.catalogAuthor; }
	public String getCatalogDate() { return this.catalogDate; }
	public int getCatalogLimit() { return this.catalogLimit; }

	private void remove(int id)
	/* remove entry from access index */
	{
		for(int i=0;i<this.getRemainingQuestions();i++)
		{
			if(elementList.get(i) == id) { this.elementList.remove(i); }
		}
	}
	
	public void printRandomIndex()
	/* write out access index */
	{
		// write out access index if entries found
		if(this.getRemainingQuestions() > 0)
		{
			for(int i=0;i<this.getRemainingQuestions();i++)
			{
				debugMsg("RI: #" + i + " ==> " + elementList.get(i));
			}
		}
		else
		{
			// no entries
			debugMsg("RI: EMPTY");
		}
	}
	
	public int nextQuestion()
	/* return next question ID */
	{
		// get next question ID based on mode
		if(this.catalogMode.equals("ordered"))
		{
			// ordered mode --> was this the last question?
			if((this.thisQuestion+1) == this.questions.length)
			{
				// yes - restart or abort (based on mode)
				if(this.modeEndless)
				{
					debugMsg("Ordered mode, last question, endless mode - will continue from beginning");
					thisQuestion=0;
					return 0;
				}  else {
					debugMsg("Ordered mode, last question, NO endless mode - will end right now");
					return -1;
				}
			}
			else
			{
				// no - increase counter and return value
				debugMsg("Ordered mode, remaining questions, will continue with next question");
				thisQuestion++;
				return thisQuestion;
			}
			
		}
		else
		{
			// random mode --> get next possible random ID and remove entry from array
			if(this.getRemainingQuestions() >= 1 || this.modeEndless)
			{
				int temp = this.elementList.get(randomGenerator.nextInt(this.getRemainingQuestions()));
				debugMsg("Random mode, remaining questions, will continue with question #" + temp);
				if(!this.modeEndless) { this.remove(temp); }
				return temp;
			}
			else
			{
				// no questions remaining --> return error
				debugMsg("Random mode, once, NO remaining questions, will stop right now");
				return -1;
			}
		}
	}
	
}