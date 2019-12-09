import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.JOptionPane;
import java.io.*;
import java.util.Random;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;



public class JTrainer extends JFrame implements ItemListener
{
	
	//declarations
	
		//global variables
		final static String version = "1.7";
		final static boolean betaRelease = true;
		
		//load translation (if any)
		//public localizationFile locale = new localizationFile("locale.txt");
		public localizationFile locale = new localizationFile(System.getProperty("user.dir") + File.separator + "locale.txt");
		
		//database variables
		public questionCatalog thisCatalog;
		int catalogQuestions;
		String catalogName;
		String catalogDescription;
		String catalogAuthor;
		String catalogDate;
		int catalogLimit;
		
		//runtime variables
		private boolean errorFlag=false, randomMode=false, debugMode=false, hintUsed=false, timeOver=false;
		int thisQuestion;
		private int numbCorrect=0, numbIncorrect=0, numbAnswered=0, numbHints=0;
		private String catalogMode="random";
		private boolean catalogEndless=false;
		private String temp[];
		private int tempCorrect[];
		public int answer[] = new int[5];
		private String lastDir = "";
		
		//timer variables
		private int seconds, mins, secs, tempTime;
		
		//timer
		Timer countdown;		//countdown timer
		Timer ticker;			//countdown ticker timer
		
		//elements
		private JTextArea txtQuestion;
		private JLabel lblStatistic, lblCatalog, lblTimer;
		private JButton btnHint, btnAnswer, btnNext;
		private JCheckBox chkAnswer[] = new JCheckBox[5];
		private JRadioButton radAnswer[] = new JRadioButton[5];
		private JTextField txtAnswer;
		private ButtonGroup grpAnswers;
		private JPanel panCheckboxes, panRadiobuttons, panTextfield, panBar, panLbls, panBar2, panContent;
		
		//menu
		JMenuBar mnbMenu;
		
			//file
			JMenu mnuFile;
			JMenuItem mniOpen, mniDetails, mniAbort, mniEnd, mniExit;
			
			//settings
			JMenu mnuSettings;
			ButtonGroup bgrSettings, bgrSettings2;
			JRadioButtonMenuItem mrbRandom, mrbOrdered, mrbOnce, mrbEndless;
			
			//help
			JMenu mnuHelp;
			JMenuItem mniAbout;
	
		//icons
		Icon iconCorrect = new ImageIcon(this.getClass().getResource("/res/correct.png"));
		Icon iconIncorrect = new ImageIcon(this.getClass().getResource("/res/incorrect.png"));
		Icon iconEnd = new ImageIcon(this.getClass().getResource("/res/end.png"));
		Icon iconAbort = new ImageIcon(this.getClass().getResource("/res/abort.png"));
		Icon iconAnswer = new ImageIcon(this.getClass().getResource("/res/answer.png"));
		Icon iconNext = new ImageIcon(this.getClass().getResource("/res/next.png"));
		Icon iconHint = new ImageIcon(this.getClass().getResource("/res/eye.png"));
		Icon iconOpen = new ImageIcon(this.getClass().getResource("/res/open.png"));
	
	
	
	public static void main(String[] args)
	/* main method */
	{
		//instance new window, set size and show
		JTrainer fenster = new JTrainer("JTrainer v." + version);
		fenster.pack();
		fenster.setSize(520, 500);
		fenster.setVisible(true);
	}
	
	
	
	JTrainer(String titel)
	/* class with graphical user interface */
	{
		//get standards
		super(titel);
		
		//don't close application when clicking on 'close' - WindowListener closes application
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				//clean exit
				end();
				System.exit(0);
			}
		});
		
		//show information about beta release (if required)
		if(betaRelease == true) { JOptionPane.showMessageDialog(null, locale.getValue(0) + "\n\n" + locale.getValue(1), locale.getValue(2), JOptionPane.INFORMATION_MESSAGE); }
		
		//set layout
		setLayout(new BorderLayout());
		debugMsg("GUI class was started");
		
		//instance elements
		
			//menu bar
			mnbMenu = new JMenuBar();
			
				//file
				mnuFile = new JMenu(locale.getValue(4));
				mniOpen = new JMenuItem(locale.getValue(5), KeyEvent.VK_O);
				mniOpen.setIcon(iconOpen);
				mniOpen.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//open catalog
						openCatalog(e);
					}	
				});
				mniOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
				mniDetails = new JMenuItem(locale.getValue(6));
				mniDetails.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//show details about catalog
						JOptionPane.showMessageDialog(null, locale.getValue(7) + " " + catalogName + "\n" + locale.getValue(8) + " " + catalogDescription + "\n" + locale.getValue(37) + " " + catalogQuestions + "\n" + locale.getValue(9) + " " + catalogAuthor + "\n" + locale.getValue(10) + " " + catalogDate, locale.getValue(11) + " '" + catalogName + "'", JOptionPane.INFORMATION_MESSAGE);
					}
				});
				mniAbort = new JMenuItem(locale.getValue(12), KeyEvent.VK_A);
				mniAbort.setIcon(iconAbort);
				mniAbort.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//abort
						btnAnswer.setIcon(iconAnswer);
						end();
					}
				});
				mniAbort.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
				mniEnd = new JMenuItem(locale.getValue(13));
				mniEnd.setIcon(iconEnd);
				mniEnd.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//end
						btnAnswer.setIcon(iconAnswer);
						end();
					}
				});
				mniExit = new JMenuItem(locale.getValue(14), KeyEvent.VK_Q);
				mniExit.setIcon(iconIncorrect);
				mniExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
				mniExit.addActionListener(new ActionListener()
				{
					//exit
					public void actionPerformed(ActionEvent e)
					{
						//try abort timer
						try
						{
							debugMsg("About to kill timers...");
							countdown.cancel();
							ticker.cancel();
							debugMsg("Killed timers!");
						}
						catch(NullPointerException exp)
						{
							debugMsg("No timers active - can't kill them");
						}
						
						//stop application
						debugMsg("Application end - goodbye!");
						System.exit(0);
					}
				});
				
				//Add menu elements for "file"
				mnuFile.add(mniOpen);
				mnuFile.addSeparator();
				mnuFile.add(mniDetails);
				mnuFile.addSeparator();
				mnuFile.add(mniAbort);
				mnuFile.add(mniEnd);
				mnuFile.addSeparator();
				mnuFile.add(mniExit);
				
				//settings
				mnuSettings = new JMenu(locale.getValue(15));
				ButtonGroup bgrSettings = new ButtonGroup();
				ButtonGroup bgrSettings2 = new ButtonGroup();
				mrbRandom = new JRadioButtonMenuItem(locale.getValue(16));
				mrbRandom.setSelected(true);
				mrbOrdered = new JRadioButtonMenuItem(locale.getValue(17));
				bgrSettings.add(mrbRandom);
				bgrSettings.add(mrbOrdered);
				mrbOnce = new JRadioButtonMenuItem(locale.getValue(18));
				mrbOnce.setSelected(true);
				mrbEndless = new JRadioButtonMenuItem(locale.getValue(19));
				bgrSettings2.add(mrbOnce);
				bgrSettings2.add(mrbEndless);
				
				//add item listeners
				mrbRandom.addItemListener(this);
				mrbOrdered.addItemListener(this);
				mrbOnce.addItemListener(this);
				mrbEndless.addItemListener(this);
				
				//Add menu elements for "settings"
				mnuSettings.add(mrbRandom);
				mnuSettings.add(mrbOrdered);
				mnuSettings.addSeparator();
				mnuSettings.add(mrbOnce);
				mnuSettings.add(mrbEndless);
				
				//help
				mnuHelp = new JMenu(locale.getValue(20));
				mniAbout = new JMenuItem(locale.getValue(21));
				mniAbout.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						//info dialog
						final String part1=locale.getValue(22), part2=locale.getValue(23), part3=locale.getValue(24), part4=locale.getValue(25), part5=locale.getValue(26);
						JOptionPane.showMessageDialog(null, part1 + "\n\n" + part2 + "\n\n\n" + part3 + "\n" + part4, part5 + version, JOptionPane.INFORMATION_MESSAGE);
					}	
				});
				mnuHelp.add(mniAbout);
				
			//add menues
			mnbMenu.add(mnuFile);
			mnbMenu.add(mnuSettings);
			mnbMenu.add(mnuHelp);
			
			//buttons
			btnHint = new JButton(locale.getValue(47));
			btnHint.setIcon(iconHint);
			btnAnswer = new JButton(locale.getValue(27));
			btnAnswer.setIcon(iconAnswer);
			btnNext = new JButton(locale.getValue(28));
			btnNext.setIcon(iconNext);
			
			//text fields
			lblStatistic = new JLabel();
			lblStatistic.setSize(50, lblStatistic.getHeight());
			lblCatalog = new JLabel();
			lblTimer = new JLabel();
			lblTimer.setSize(50, lblStatistic.getHeight());
			txtQuestion = new JTextArea(10,40);
			txtQuestion.setLineWrap(true);
			txtQuestion.setWrapStyleWord(true);
			txtQuestion.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
			txtQuestion.setEditable(false);
			JScrollPane scrQuestion = new JScrollPane(txtQuestion);
			scrQuestion.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			//scrQuestion.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
			
			//answer boxes
			
				//check boxes
				chkAnswer[0] = new JCheckBox(locale.getValue(29));
				chkAnswer[1] = new JCheckBox(locale.getValue(30));
				chkAnswer[2] = new JCheckBox(locale.getValue(31));
				chkAnswer[3] = new JCheckBox(locale.getValue(32));
				chkAnswer[4] = new JCheckBox(locale.getValue(33));
				
					//listener for check boxes
					chkAnswer[0].addItemListener(this);
					chkAnswer[1].addItemListener(this);
					chkAnswer[2].addItemListener(this);
					chkAnswer[3].addItemListener(this);
					chkAnswer[4].addItemListener(this);
				
				//radio buttons
				grpAnswers = new ButtonGroup();
				radAnswer[0] = new JRadioButton(locale.getValue(29));
				radAnswer[1] = new JRadioButton(locale.getValue(30));
				radAnswer[2] = new JRadioButton(locale.getValue(31));
				radAnswer[3] = new JRadioButton(locale.getValue(32));
				radAnswer[4] = new JRadioButton(locale.getValue(33));
				grpAnswers.add(radAnswer[0]);
				grpAnswers.add(radAnswer[1]);
				grpAnswers.add(radAnswer[2]);
				grpAnswers.add(radAnswer[3]);
				grpAnswers.add(radAnswer[4]);
				
					//listener for radio buttons
					radAnswer[0].addItemListener(this);
					radAnswer[1].addItemListener(this);
					radAnswer[2].addItemListener(this);
					radAnswer[3].addItemListener(this);
					radAnswer[4].addItemListener(this);
				
				//text field
				txtAnswer = new JTextField(locale.getValue(34));
			
			
			//checkbox panel
			panCheckboxes = new JPanel(new GridLayout(0,1));
			panCheckboxes.add(chkAnswer[0]);
			panCheckboxes.add(chkAnswer[1]);
			panCheckboxes.add(chkAnswer[2]);
			panCheckboxes.add(chkAnswer[3]);
			panCheckboxes.add(chkAnswer[4]);
			
			//radio button panel
			panRadiobuttons = new JPanel(new GridLayout(0,1));
			panRadiobuttons.add(radAnswer[0]);
			panRadiobuttons.add(radAnswer[1]);
			panRadiobuttons.add(radAnswer[2]);
			panRadiobuttons.add(radAnswer[3]);
			panRadiobuttons.add(radAnswer[4]);
			
			//textfield panel
			panTextfield = new JPanel(new GridLayout(10,1));
			panTextfield.setPreferredSize(new Dimension(0, 35));
			panTextfield.add(txtAnswer);
			
			//label panel
			panLbls = new JPanel(new GridLayout(1,0));
			panLbls.add(lblStatistic);
			panLbls.add(lblTimer);
			panLbls.add(lblCatalog);
			
			//bar panel (open, abort)
			panBar = new JPanel(new GridLayout(0,1));
			panBar.add(mnbMenu);
			panBar.add(panLbls);
			
			//content panel (textbox, checkbox/radiobutton panel)
			panContent = new JPanel(new GridLayout(0,1));
			panContent.add(scrQuestion);
			
			//bar panel 2 (hint, answer, next)
			panBar2 = new JPanel(new GridLayout(1,0));
			panBar2.add(btnHint);
			panBar2.add(btnAnswer);
			panBar2.add(btnNext);
			
		//add elements
		add(panBar, BorderLayout.PAGE_START);
		add(panContent, BorderLayout.CENTER);
		add(panBar2, BorderLayout.PAGE_END);
		
		//disable elements
		mniDetails.setEnabled(false);
		mniAbort.setEnabled(false);
		mniEnd.setEnabled(false);
		mniEnd.setEnabled(false);
		mniAbort.setEnabled(false);
		chkAnswer[0].setEnabled(false);
		chkAnswer[1].setEnabled(false);
		chkAnswer[2].setEnabled(false);
		chkAnswer[3].setEnabled(false);
		chkAnswer[4].setEnabled(false);
		radAnswer[0].setEnabled(false);
		radAnswer[1].setEnabled(false);
		radAnswer[2].setEnabled(false);
		radAnswer[3].setEnabled(false);
		radAnswer[4].setEnabled(false);
		txtAnswer.setEnabled(false);
		btnHint.setEnabled(false);
		btnAnswer.setEnabled(false);
		btnNext.setEnabled(false);
		
		//ActionListener
			
			//hint
			btnHint.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					//print hint if not used yet
					if(hintUsed == false)
					{
						JOptionPane.showMessageDialog(null, thisCatalog.questions[thisQuestion].getHint(), locale.getValue(47), JOptionPane.INFORMATION_MESSAGE);
						hintUsed = true;
						btnHint.setEnabled(false);
						debugMsg("Hint was used, disabled button");
						numbHints++;
						debugMsg("Increased counter of used hints");
					}
					else
					{
						//hint already used
						JOptionPane.showMessageDialog(null, locale.getValue(48), locale.getValue(47), JOptionPane.INFORMATION_MESSAGE);
						debugMsg("User tried to use hint twice. ;-)");
					}
				}
			});
			
			//answer
			btnAnswer.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent evt)
				{
					//answer question
					answerQuestion();
				}
			});
			
			//next question
			btnNext.addActionListener(new ActionListener()
			{	
				public void actionPerformed(ActionEvent evt)
				{
					//show next question (if any)
					btnAnswer.setIcon(iconAnswer);
					nextQuestion();
				}	
			});
		
		//set hint
		txtQuestion.setText(locale.getValue(35));
		
		//write debug message
		debugMsg("GUI class brought up");
	}
	
	
	
	public void openCatalog(ActionEvent evt)
	/* read question catalog */
	{
		//instance FileChooser and show dialog
		String path;
		if(this.lastDir.equals("") == false) { path= this.lastDir; }
		else { path= System.getProperty("user.dir"); }
		final JFileChooser fc = new JFileChooser(path);
		debugMsg("FileChooser was opened starting at: '" + path + "'");
		int returnVal = fc.showOpenDialog(JTrainer.this);
		debugMsg("FileChooser was closed");
		
		//continue if a file was selected
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			//return file name and save last directory
			File file = fc.getSelectedFile();
			debugMsg("FileChooser selected file '" + fc.getSelectedFile() + "' at dir '" + fc.getSelectedFile().getParent() + "'");
			this.lastDir = fc.getSelectedFile().getParent();
			
			//try loading catalog
			this.thisCatalog = new questionCatalog(file.getPath());
			
			//set flags and modes
			this.thisCatalog.setMode(this.catalogMode);
			this.thisCatalog.setEndless(this.catalogEndless);
			
			//set catalog meta information
			catalogName = this.thisCatalog.getCatalogName();
			catalogQuestions = this.thisCatalog.getSize();
			catalogDescription = this.thisCatalog.getCatalogDesc();
			catalogAuthor = this.thisCatalog.getCatalogAuthor();
			catalogDate = this.thisCatalog.getCatalogDate();
			catalogLimit = this.thisCatalog.getCatalogLimit();
			
			//start simulation
			start();
		}
		else
		{
			//opening aborted
			txtQuestion.setText(locale.getValue(40) + "\n");
			debugMsg("Opening file was aborted");
		}
	}
	
	
	
	public void start()
	/* starting simulation */
	{
		debugMsg("Simulation about to start");
		
		//disable/enable elements
		mniOpen.setEnabled(false);
		mniDetails.setEnabled(true);
		mniAbort.setEnabled(true);
		chkAnswer[0].setEnabled(true);
		chkAnswer[1].setEnabled(true);
		chkAnswer[2].setEnabled(true);
		chkAnswer[3].setEnabled(true);
		chkAnswer[4].setEnabled(true);
		lblStatistic.setEnabled(true);
		btnHint.setEnabled(true);
		btnAnswer.setEnabled(true);
		mrbOrdered.setEnabled(false);
		mrbRandom.setEnabled(false);
		mrbOnce.setEnabled(false);
		mrbEndless.setEnabled(false);
		lblCatalog.setText(catalogName);
		
		//enable timer (if set)
		if(catalogLimit > 0)
		{
			//set timer
			debugMsg("About to set timer to '" + catalogLimit + "' minutes (" + catalogLimit*60 + " seconds)");
			countdown = new Timer();
			ticker = new Timer();
			ticker.schedule(new Ticker(catalogLimit*60), 0, 1000);
			countdown.schedule(new StopTask(), catalogLimit*60*1000);
			debugMsg("Set timer to '" + catalogLimit + "' minutes (" + catalogLimit*60 + " seconds)");
		}
		else
		{
			//no timer
			debugMsg("No catalog limit set, won't enable timer");
		}
		
		//write debug message and load next question
		debugMsg("Simulation started");
		nextQuestion();
	
	}
	
	
	
	public void nextQuestion()
	/* show the next question */
	{
		//reset hint
		debugMsg("About to show next question");
		for(int i=0;i<5;i++)
		{
			answer[i] = 0;
			debugMsg("Reset answer #" + i + " to 0.");
			radAnswer[i].setText("");
			radAnswer[i].setSelected(false);
			chkAnswer[i].setText("");
		}
		grpAnswers.clearSelection();
		hintUsed = false;
		btnAnswer.setEnabled(true);
		btnNext.setEnabled(false);
		debugMsg("Enabled hint");
		
		//get id
		thisQuestion = this.thisCatalog.nextQuestion();
		debugMsg("This question is: " + thisQuestion);
		
		//remaining questions?
		if(thisQuestion != -1)
		{
			//Yes --> next question
			
			//enable hint button if hint defined
			if(this.thisCatalog.questions[thisQuestion].getHint().equals("NONE") == false) { btnHint.setEnabled(true); }
			else { btnHint.setEnabled(false); }
			
			//write question and set title
			txtQuestion.setText(this.thisCatalog.questions[thisQuestion].getQuestion());
			if(catalogEndless == false) {
				lblStatistic.setText(" " + locale.getValue(38) + " " + (thisQuestion+1) + "/" + catalogQuestions + " (" + (this.thisCatalog.getRemainingQuestions()+1) + " " + locale.getValue(57) + ")");
			} else {
				lblStatistic.setText(" " + locale.getValue(38) + " " + (thisQuestion + 1) + "/" + catalogQuestions);
			}
			debugMsg("Title set to: " + (thisQuestion+1) + "/" + catalogQuestions);
			
			//remove previous elements
			panContent.remove(panCheckboxes);
			panContent.remove(panRadiobuttons);
			panContent.remove(panTextfield);
			panContent.repaint();
			
			//enable and set interface elements based on type
			switch(this.thisCatalog.questions[thisQuestion].getType())
			{
				
				case	0:		//type 0
								//add panel
								panContent.add(panTextfield);
								txtAnswer.setEnabled(true);
								txtAnswer.setText(locale.getValue(34));
								panContent.validate();
								debugMsg("Added textfield panel to gui");
							break;
				
				case	1:		//type 1
								//add panel
								panContent.add(panRadiobuttons);
								panContent.validate();
								debugMsg("Added radiobutton panel to gui");
								
								//set answer
								temp = this.thisCatalog.questions[thisQuestion].getAnswers();
								debugMsg("Got answers array from question #" + thisQuestion);
								for(int i=0;i<5;i++)
								{
									if(temp[i].equals("XXX_NOANS") == false)
									{
										radAnswer[i].setEnabled(true);
										radAnswer[i].setText(temp[i]);
										debugMsg("Enabled radio button #" + i + " and set text to: '" + temp[i] + "'");
									}
								}
							break;
				
				case	2:		//type 2
								//add panel
								panContent.add(panCheckboxes);
								panContent.validate();
								debugMsg("Added checkbox panel to gui");
								
								//set answer
								temp = this.thisCatalog.questions[thisQuestion].getAnswers();
								debugMsg("Got answers array from question #" + thisQuestion);
								//set number of answers
								txtQuestion.setText(txtQuestion.getText() + " (" + this.thisCatalog.questions[thisQuestion].getCorrectAnswers().length + " " + locale.getValue(56) + ")");
								//set answer if not marked as "unused"
								for(int i=0;i<5;i++)
								{
									if(temp[i].equals("XXX_NOANS") == false)
									{
										chkAnswer[i].setEnabled(true);
										chkAnswer[i].setText(temp[i]);
										chkAnswer[i].setSelected(false);
										debugMsg("Enabled checkbox #" + i + " and set text to: '" + temp[i] + "'");
									}
									else
									{
										chkAnswer[i].setEnabled(false);
										chkAnswer[i].setText("");
										chkAnswer[i].setSelected(false);
										debugMsg("Disabled checkbox #" + i + " as it's unused for this question");
									}
								}
								break;
				
				default:		//unknown type
								debugMsg("Unknown question type, err?");
			}
			
			//reset answers
			for(int i=0;i<5;i++)
			{
				answer[i] = 0;
				debugMsg("Reset answer #" + i + " to 0.");
			}
		
		}
		else
		{
			//no questions remaining
			debugMsg("There no questions in the big pot called question catalog anymore! :-|");
			end();
		}
	
	}
	
	
	
	public void itemStateChanged(ItemEvent e)
	/* react on clicked items */
	{
		//get source
		Object source = e.getItemSelectable();
		debugMsg("itemStateChanged, ItemEvent: " + e.getItemSelectable());
		
		//do some stuff based on clicked element
		for(int i=0;i<5;i++)
		{
			if(source == chkAnswer[i])
			{
				debugMsg("Checkbox " + i +" clicked");
				if(answer[i] == 0)
				{
					answer[i] = 1;
					debugMsg("answer[" + i + "] is now 1.");
				}
				else
				{
					answer[i] = 0;
					debugMsg("answer[" + i + "] is now 0.");
				}
			}
			else if(source == radAnswer[i])
			{
				debugMsg("RadioButton " + i +" clicked");
				debugMsg("answer[" + i + "] is now 1.");
				for(int ii=0;ii<5;ii++) { answer[ii] = 0; }
				answer[i] = 1;
			}
		}
		
		if(source == mrbRandom)
		{
			//random mode
			debugMsg("mrbRandom clicked");
			this.catalogMode = "random";
			mrbOnce.setEnabled(true);
			//mrbEndless.setEnabled(true);
			debugMsg("mode is now random");
		}
		else if(source == mrbOrdered)
		{
			//ordered mode
			this.catalogMode = "ordered";
			this.catalogEndless = false;
			mrbOnce.setEnabled(true);
			mrbOnce.setSelected(true);
			//mrbEndless.setEnabled(false);
			debugMsg("mode is now ordered, order is now once");
		}
		else if(source == mrbOnce)
		{
			//once mode
			this.catalogEndless = false;
			debugMsg("order is now once");
		}
		else if(source == mrbEndless)
		{
			//endless mode
			this.catalogEndless = true;
			debugMsg("order is now endless");
		}
		
		//dump button states --> testing
		//JOptionPane.showMessageDialog(null, "mode: " + mode + "\norder: " + order, "Buttons", JOptionPane.INFORMATION_MEssAGE);
		//txtQuestion.append("but0: " + answer[0] + "\nbut1: " + answer[1] + "\nbut2:" + answer[2] + "\nbut3: " + answer[3] + "\nbut4: " + answer[4] + "\n");
		
	}
	
	
	
	public void answerQuestion()
	/* check answered question */
	{
		debugMsg("About to check all answers");
		debugMsg("Before check - correct: " + numbCorrect + ", incorrect: " + numbIncorrect + ", with-hints: " + numbHints + ", answered: " + numbAnswered);
		for(int i=0;i<5;i++) { debugMsg("answer #" + i + ": " + answer[i]); }
		
		switch(this.thisCatalog.questions[thisQuestion].getType())
		{
			
			case 0:		//type 0
							//check if entered text matches --> get correct answer
							temp = this.thisCatalog.questions[thisQuestion].getAnswers();
							debugMsg("Entered '" + txtAnswer.getText() + "', correct answer is '" + temp[0] + "'");
							if(txtAnswer.getText().equals(temp[0]))
							{
								//answer correct
								errorFlag = false;
								debugMsg("Question was answered correct");
								debugMsg("Increased counter of correct answers");
							}
							else
							{
								//answer incorrect
								errorFlag = true;
								debugMsg("Wrong answer detected :-(");
								debugMsg("Increased counter of incorrect answers");
							}
						break;
			
			case 1:		//type 1
							//check if selected answers are correct
							tempCorrect = null;
							tempCorrect = this.thisCatalog.questions[thisQuestion].getCorrectAnswers();
							
							//dump correct answers
							for(int i=0;i<tempCorrect.length;i++) { debugMsg("correct-marked answer #" + i + ": " + tempCorrect[i]); }
							
							//reset error flag and check answers
							errorFlag = false;
							for(int i=0;i<5;i++)
							{
								//is this a as correct marked answer?
								if(i == tempCorrect[0])
								{
									//Yes --> is it selected?
									if(answer[i] == 1)
									{
										//Yes --> correct
										//if(radAnswer[i].isEnabled()) { radAnswer[i].setText(radAnswer[i].getText() +  " [" + locale.getValue(50) + "]"); }
										debugMsg("correct-marked answer is marked :-)");
									}
									else
									{
										//No --> incorrect
										errorFlag = true;
										if(radAnswer[i].isEnabled()) { radAnswer[i].setText(radAnswer[i].getText() +  " [" + locale.getValue(51) + "]"); }
										debugMsg("correct-marked answer is NOT marked :-(");
									}
								}
								else
								{
									//other answer --> is it selected?
									if(answer[i] == 1)
									{
										//Yes --> incorrect
										errorFlag = true;
										if(radAnswer[i].isEnabled()) { radAnswer[i].setText(radAnswer[i].getText() +  " [" + locale.getValue(51) + "]"); }
										debugMsg("wrong-marked answer is marked :-(");
									}
									else
									{
										//No --> correct
										//if(radAnswer[i].isEnabled()) { radAnswer[i].setText(radAnswer[i].getText() +  " [" + locale.getValue(50) + "]"); }
										debugMsg("wrong-marked answer is NOT marked :-)");
									}
								}
								
							}
						
						break;
			
			case 2:		//type 2
						
							//check if given answers are correct
							tempCorrect = null;
							tempCorrect = this.thisCatalog.questions[thisQuestion].getCorrectAnswers();
							errorFlag = false;
							
							//dump correct answers
							for(int i=0;i<tempCorrect.length;i++) { debugMsg("correct-marked answer #" + i + ": " + tempCorrect[i]); }
							
							//reset error flag and check answers
							errorFlag = false;
							for(int i=0;i<5;i++)
							{
								//is this a as correct marked answer?
								if(isCorrect(i, tempCorrect) == true)
								{
									//Yes --> is it marked?
									debugMsg("checking correct-marked answer: #" + i);
									if(answer[i] == 1)
									{
										//Yes --> correct
										//if(chkAnswer[i].isEnabled()) { chkAnswer[i].setText(chkAnswer[i].getText() +  " [" + locale.getValue(50) + "]"); }
										debugMsg("correct-marked answer is marked :-)");
									}
									else
									{
										//No --> incorrect
										errorFlag = true;
										if(chkAnswer[i].isEnabled()) { chkAnswer[i].setText(chkAnswer[i].getText() +  " [" + locale.getValue(51) + "]"); }
										debugMsg("correct-marked answer is NOT marked :-(");
									}
								}
								else
								{
									//wrong marked answer --> is it selected?
									debugMsg("wrong-marked answer: #" + i);
									if(answer[i] == 1)
									{
										//Yes --> incorrect
										errorFlag = true;
										if(chkAnswer[i].isEnabled()) { chkAnswer[i].setText(chkAnswer[i].getText() +  " [" + locale.getValue(51) + "]"); }
										debugMsg("wrong-marked answer is marked :-(");
									}
									else
									{
										//No --> correct
										//if(chkAnswer[i].isEnabled()) { chkAnswer[i].setText(chkAnswer[i].getText() +  " [" + locale.getValue(50) + "]"); }
										debugMsg("wrong-marked answer is NOT marked :-)");
									}
								}
								
							}
						
						break;
			
			default:		//unknown type
						debugMsg("Unknown question type, err?");
			
			
			
		}
		
		//write messages based on flag and increase counters
		if(errorFlag == true)
		{
			numbIncorrect++;
			debugMsg("Question was answered wrong");
			txtQuestion.append("\n\n" + locale.getValue(41));
		}
		else
		{
			numbCorrect++;
			debugMsg("Question was answered correctly");
			txtQuestion.append("\n\n" + locale.getValue(42));
		}
		
		//write explanation (if given)
		if(this.thisCatalog.questions[thisQuestion].getExplanation().equals("NONE") == false) { txtQuestion.append("\n\n" + this.thisCatalog.questions[thisQuestion].getExplanation()); }
		
		//disable/enable buttons
		btnAnswer.setEnabled(false);
		btnHint.setEnabled(false);
		btnNext.setEnabled(true);
		
		//increase counters
		numbAnswered++;
		thisQuestion++;
		debugMsg("After check - correct: " + numbCorrect + ", incorrect: " + numbIncorrect + ", with-hints: " + numbHints + ", answered: " + numbAnswered);
	}
	
	
	public boolean isCorrect(int targetAnswer, int[] targetCorrectAnswers)
	/* check whether the answer is correct */
	{
		for(int i=0;i<targetCorrectAnswers.length;i++)
		{
			if(targetAnswer == targetCorrectAnswers[i]) { return true; }
		}
		return false;
	}
	
	
	
	public void end()
	/* end simulation */
	{
		debugMsg("About to exit simulation...");
		
		//try stopping timer
		try
		{
			debugMsg("About to kill timers...");
			countdown.cancel();
			ticker.cancel();
			debugMsg("Killed timers!");
		}
		catch(NullPointerException exp)
		{
			debugMsg("No timers active - can't kill them");
		}
		
		//disable/enable elements
		lblTimer.setText("");
		mniDetails.setEnabled(false);
		mniEnd.setEnabled(false);
		mniAbort.setEnabled(false);
		for(int i=0;i<5;i++)
		{
			radAnswer[i].setEnabled(false);
			radAnswer[i].setText(locale.getValue(29+i));
			chkAnswer[i].setEnabled(false);
			chkAnswer[i].setText(locale.getValue(29+i));
		}
		btnHint.setEnabled(false);
		btnAnswer.setEnabled(false);
		btnNext.setEnabled(false);
		mniOpen.setEnabled(true);
		lblCatalog.setText("");
		lblStatistic.setText("");
		lblStatistic.setEnabled(false);
		mrbOrdered.setEnabled(true);
		mrbRandom.setEnabled(true);
		mrbOnce.setEnabled(true);
		mrbEndless.setEnabled(true);
		panContent.remove(panCheckboxes);
		panContent.remove(panRadiobuttons);
		panContent.remove(panTextfield);
		panContent.repaint();
		debugMsg("Buttons/elements enabled/disabled");
		
		//time expired?
		if(timeOver == true)
		{
			//Yes --> write message
			txtQuestion.setText(locale.getValue(52) + "\n\n");
			debugMsg("Timer expired");
		}
		else
		{
			//No --> empty display
			debugMsg("Normal stop, no timer expiration");
			txtQuestion.setText("");
		}
		
		//Write hint if questions were answers
		if(numbAnswered > 0)
		{
			//dump statistic
			if(catalogEndless == true)
			{
				//endless mode
				txtQuestion.append(numbAnswered + " " + locale.getValue(44) + "\n" + locale.getValue(45) + " " + numbCorrect + "\n" + locale.getValue(46) + " " + numbIncorrect + "\n" + locale.getValue(49) + " " + numbHints + "\n\n");
			}
			else
			{
				//ordered mode --> number of questions = number of questions in catalog
				txtQuestion.append(numbAnswered + " " + locale.getValue(44) + " (" + locale.getValue(54) + " " + catalogQuestions + " " + locale.getValue(55) + ")"+ "\n" + locale.getValue(45) + " " + numbCorrect + "\n" + locale.getValue(46) + " " + numbIncorrect + "\n" + locale.getValue(49) + " " + numbHints + "\n\n");
			}
			debugMsg("Answered: " + numbAnswered + ", Correct: " + numbCorrect + ", Incorrect: " + numbIncorrect + ", Used hints: " + numbHints);
			
			//calculate and write success rate
			
				//correct: 10 pts
				//correct with hint: 5 pts
				//incorrect: 0 pts
				
				//variables
				double pktMax, pktCorrect, pktQuote;
				
				//calculate maximum of points based on mode
				if(catalogEndless == true)
				{
					//endless mode --> number of answered questions
					pktMax = numbAnswered * 10;
					debugMsg("Endless mode, pktMax is answered questions (" + numbAnswered + ") * 10 ==> " + pktMax);
				}
				else
				{
					//once-mode --> number of questions in catalog
					pktMax = catalogQuestions * 10;
					debugMsg("Normal mode, pktMax is number of questions in catalog (" + pktMax + ") * 10 ==> " + pktMax);
				}
				
				//scored points (correct answers - hint penalty
				pktCorrect = (numbCorrect*10) - (numbHints*5);
				debugMsg("Points are: (numbCorrect*10) - (numbHints*5) ==> " + pktCorrect);
				
				//calculate and print score
				pktQuote = (pktCorrect*100)/pktMax;
				debugMsg("Quote is (pktCorrect*100)/pktMax ==> " + (int) pktQuote + "% (" + pktQuote + ")");
				if(pktQuote > 0) { txtQuestion.append(locale.getValue(53) + " " + (int) pktQuote + "%"); }
				else { txtQuestion.append(locale.getValue(53) + " 0%"); }
		}
		else
		{
			//show information to open catalog
			txtQuestion.append(locale.getValue(35));
		}
		
		//reset counters
		numbAnswered=0;
		numbCorrect=0;
		numbIncorrect=0;
		numbHints=0;
		debugMsg("Counters were reset");
		debugMsg("Simulation stopped");
	}
	
	class StopTask extends TimerTask
	/* task for stopping simulation */
	{
		public void run()
		{
			timeOver=true;
			System.out.println("ST: Time's over, stopping simulation!");
			countdown.cancel();
			ticker.cancel();
			end();
		}
	}
	
	class Ticker extends TimerTask
	/* ticker task for countdown */
	{
		//variables
		private int seconds;
		
		Ticker(int seconds)
		/* constructor */
		{
			this.seconds = seconds;
		}
		
		public void run()
		/* main procedure */
		{
			//print (formatted) remaining time --> get amount of remaining minutes
			tempTime = seconds/60;
			
			//set remaining minutes and seconds (if any)
			if(tempTime != 0)
			{
				mins = tempTime;
				secs = seconds - (mins*60);
			}
			else
			{
				mins = 0;
				secs = seconds;
			}
			
			//print time - set 0 as prefix if 10 seconds or less
			if(secs < 10)
			{
				lblTimer.setText(mins + ":" + "0" + secs);
			}
			else
			{
				lblTimer.setText(mins + ":" + secs);
			}
			
			//decrease seconds
			seconds--;
		}
	}	
	
	public void debugMsg(String debugMessage)
	/* write debug message */
	{
		//write message
		System.out.println("JT: " + debugMessage);
	}

}