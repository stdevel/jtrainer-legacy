import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.ArrayList;
import java.io.*;


/**
 * question catalog class
 *
 * @author Christian Stankowic
 * @version 1.7
 */
public class questionCatalog {
    /**
     * catalog name
     */
    private String catalogName;

    /**
     * catalog short description
     */
    private String catalogDescription;

    /**
     * catalog author name and mail, e.g. max.mustermann <mmust@foo.bar>
     */
    private String catalogAuthor;

    /**
     * catalog creation date
     */
    private String catalogDate;

    /**
     * catalog time limit in minutes (0 for unlimited)
     */
    private int catalogLimit;

    /**
     * array of question objects
     */
    public question[] questions;

    /**
     * flag whether catalog is repeated
     */
    private boolean modeEndless;

    /**
     * flag whether questions are selected randomly (random)
     * or in ordered mode (ordered)
     */
    private String catalogMode = "random";        // mode (random/ordered)

    /**
     * access index
     */
    final ArrayList<Integer> elementList = new ArrayList<>();

    /**
     * random number generator
     */
    final Random randomGenerator = new Random();

    /**
     * pointer to current question
     */
    int thisQuestion = -1;

    /**
     * Constructor, loads catalog from file
     * @param filename the question catalog to load
     */
    questionCatalog(String filename) {

        //try loading catalog
        try {

            // declarations
            BufferedReader targetFile = new BufferedReader(
                    new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8)
            );
            this.debugMsg("About to open file: " + filename);

            // get catalog meta information

            // read catalog metadata
            int numbQuestions = Integer.parseInt(targetFile.readLine());
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
            for (int i = 0; i < numbQuestions; i++) {
                // read question and type
                qText = targetFile.readLine();
                debugMsg("qText: " + qText);
                qType = Integer.parseInt(targetFile.readLine());
                debugMsg("qType: " + qType);

                // read information based on type
                if (qType == 0) {
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
                } else {
                    // multiple answers - read next 5 lines as answers
                    qAnswers[0] = targetFile.readLine();
                    qAnswers[1] = targetFile.readLine();
                    qAnswers[2] = targetFile.readLine();
                    qAnswers[3] = targetFile.readLine();
                    qAnswers[4] = targetFile.readLine();

                    // read correct answers
                    temp = targetFile.readLine();

                    if (countChars(temp, ',') > 0) {
                        // multiple answers exist
                        String[] qCorrectAnswers = new String[countChars(temp, ',') + 1];
                        qCorrectAnswers = temp.split(",");

                        // read hint and explanation
                        qHint = targetFile.readLine();
                        debugMsg("Read hint: '" + qHint + "'");
                        qExplanation = targetFile.readLine();
                        debugMsg("Read explanation: '" + qExplanation + "'");

                        // create object and return initial values
                        questions[i] = new question(qText, qType, qHint, qExplanation);

                        // define correct answers
                        for (int ii = 0; ii < 5; ii++) {
                            questions[i].addAnswer(qAnswers[ii]);
                        }
                        for (String qCorrectAnswer : qCorrectAnswers) {
                            questions[i].setCorrect(Integer.parseInt(qCorrectAnswer));
                        }
                    } else {
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
                        for (int ii = 0; ii < 5; ii++) {
                            questions[i].addAnswer(qAnswers[ii]);
                        }
                        questions[i].setCorrect(Integer.parseInt(temp));

                    }

                }

            }

            // close file
            targetFile.close();
            debugMsg("Closed file");

            // create access index
            for (int i = 0; i < this.questions.length; i++) {
                this.elementList.add(i);
                debugMsg("Added " + i + " to random index.");
            }

        } catch (java.io.IOException exp) {
            // write error message
            debugMsg("Error while opening file " + filename);
            debugMsg("Stack trace:");
            exp.printStackTrace();
        }

    }

    /**
     * sets the question catalog mode
     * @param mode mode string ('ordered' or 'random')
     */
    public void setMode(String mode) {
        // set mode based on string
        if (mode.equals("ordered")) {
            this.catalogMode = "ordered";
        } else {
            this.catalogMode = "random";
        }
    }

    /**
     * sets endless mode
     * @param bool boolean whether endless mode is enabled
     */
    public void setEndless(boolean bool) {
        // set flag based on param
        this.modeEndless = bool;
    }

    /**
     * returns the amount of char occurrences in a string
     * @param s string
     * @param c character
     * @return amount of occurences
     */
    private static int countChars(String s, char c) {
        return s.replaceAll("[^" + c + "]", "").length();
    }

    /**
     * Prints debug messages to the console
     * @param debugMessage the message to display
     */
    private void debugMsg(String debugMessage) {
        System.out.println("QC: " + debugMessage);
    }

    /**
     * returns the amount of questions
     * @return amount of questions
     */
    public int getSize() {
        return this.questions.length;
    }

    /**
     * returns the amount of remaining questions
     * @return the amount of remaining questions
     */
    public int getRemainingQuestions() {
        return this.elementList.size();
    }

    /**
     * dumps catalog data on the console
     */
    public void printCatalog() {
        for (int i = 0; i < this.questions.length; i++) {
            debugMsg("Question #" + i + ", " + (i + 1) + "/" + this.questions.length);
            this.questions[i].printQuestionData();
        }
    }

    /**
     * returns the question catalog name
     * @return question catalog name
     */
    public String getCatalogName() {
        return this.catalogName;
    }

    /**
     * returns the question catalog description
     * @return question catalog description
     */
    public String getCatalogDesc() {
        return this.catalogDescription;
    }

    /**
     * returns the question catalog author and mail address
     * @return question catalog author and mail address
     */
    public String getCatalogAuthor() {
        return this.catalogAuthor;
    }

    /**
     * returns the question catalog creation date
     * @return question catalog creation date
     */
    public String getCatalogDate() {
        return this.catalogDate;
    }

    /**
     * returns the question catalog time limit
     * @return question catalog time limit
     */
    public int getCatalogLimit() {
        return this.catalogLimit;
    }

    /**
     * removes an question from the list of remaining questions
     * @param id question ID
     */
    private void remove(int id) {
        for (int i = 0; i < this.getRemainingQuestions(); i++) {
            if (elementList.get(i) == id) {
                this.elementList.remove(i);
            }
        }
    }

    /**
     * dumps the access index on the console
     */
    public void printRandomIndex() {
        // write out access index if entries found
        if (this.getRemainingQuestions() > 0) {
            for (int i = 0; i < this.getRemainingQuestions(); i++) {
                debugMsg("RI: #" + i + " ==> " + elementList.get(i));
            }
        } else {
            // no entries
            debugMsg("RI: EMPTY");
        }
    }

    /**
     * retrieves the next question ID based on the mode
     * @return question ID
     */
    public int nextQuestion() {
        if (this.catalogMode.equals("ordered")) {
            // ordered mode --> was this the last question?
            if ((this.thisQuestion + 1) == this.questions.length) {
                // yes - restart or abort (based on mode)
                if (this.modeEndless) {
                    debugMsg("Ordered mode, last question, endless mode - will continue from beginning");
                    thisQuestion = 0;
                    return 0;
                } else {
                    debugMsg("Ordered mode, last question, NO endless mode - will end right now");
                    return -1;
                }
            } else {
                // no - increase counter and return value
                debugMsg("Ordered mode, remaining questions, will continue with next question");
                thisQuestion++;
                return thisQuestion;
            }

        } else {
            // random mode --> get next possible random ID and remove entry from array
            if (this.getRemainingQuestions() >= 1 || this.modeEndless) {
                int temp = this.elementList.get(randomGenerator.nextInt(this.getRemainingQuestions()));
                debugMsg("Random mode, remaining questions, will continue with question #" + temp);
                if (!this.modeEndless) {
                    this.remove(temp);
                }
                return temp;
            } else {
                // no questions remaining --> return error
                debugMsg("Random mode, once, NO remaining questions, will stop right now");
                return -1;
            }
        }
    }

}