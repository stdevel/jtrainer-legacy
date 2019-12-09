import java.util.ArrayList;


/**
 * question class
 *
 * @author Christian Stankowic
 * @version 1.7
 */
public class question {
    /**
     * question text
     */
    final String question;

    /**
     * hint text
     */
    final String hint;

    /**
     * explanation text
     */
    final String explanation;

    /**
     * Question type, select from:
     * 0 - answer needs to be entered manually
     * 1 - one answer needs to be selected from a radio button list
     * 2 - multiple answers need to be selected from a check box list
     */
    final int type;

    /**
     * array list of answers
     */
    final ArrayList<String> answers = new ArrayList<>();

    /**
     * array list of correct answers
     */
    final ArrayList<Integer> correctAnswers = new ArrayList<>();


    /**
     * Constructor, creates a question
     * @param q question text
     * @param t type
     * @param h hint text
     * @param e explanation text
     */
    question(String q, int t, String h, String e) {
        // setting information and write debug message
        this.question = q;
        this.type = t;
        this.hint = h;
        this.explanation = e;
        debugMsg("Created question with type='" + t + "', question='" + q + "', hint='" + h + "' and explanation='" + e + "'");
    }

    /**
     * Prints debug messages to the console
     * @param debugMessage the message to display
     */
    private void debugMsg(String debugMessage) {
        System.out.println("Q: " + debugMessage);
    }

    /**
     * adds an answer to the question
     * @param answer answer text
     */
    public void addAnswer(String answer) {
        if (this.type == 0) {
            // type 0 - set answer and flag for correct answer
            this.answers.add(answer);
            this.correctAnswers.add(0);
            debugMsg("Detected type 0 question, set answer '" + answer + "' to the correct answer.");
        } else {
            // type 1 oder 2 - add answer
            this.answers.add(answer);
            debugMsg("Added answer '" + answer + "' to list of answers - don't forget to set the correct answer");
        }
    }

    /**
     * marks an answer as correct answer
     * @param answer answer ID
     */
    public void setCorrect(int answer) {
        if (this.type == 0) {
            // type 0 - answer already defined
            debugMsg("Detected type 0 question, correct answer is already defined, droog!");
        } else if (this.type == 1) {
            // type 1 - already a correct answer given?
            if (this.correctAnswers.size() != 0) {
                // yes --> error
                debugMsg("Error, correct answer of this question has already been defined (" + this.correctAnswers.get(0) + ", " + this.answers.get(this.correctAnswers.get(0)) + ")");
            } else {
                // no --> add if exists
                if (answer > this.answers.size() && !this.answers.contains(answer)) {
                    // error --> answer not in index
                    debugMsg("Answer #" + answer + " out of bound (" + this.answers.size() + ", 0-" + (this.answers.size() - 1) + "), won't define as correct.");
                } else {
                    this.correctAnswers.add(answer);
                    debugMsg("Set correct answer to #" + answer + " (" + this.answers.get(answer) + ")");
                }
            }
        } else if (this.type == 2) {
            // type 2 - mark as correct if answer exists and not already in index
            if (answer > this.answers.size() && !this.answers.contains(answer)) {
                // error --> answer not in index
                debugMsg("Answer #" + answer + " out of bound (" + this.answers.size() + ", 0-" + (this.answers.size() - 1) + "), won't define as correct.");
            } else {
                this.correctAnswers.add(answer);
                debugMsg("Marked answer #" + answer + " (" + this.answers.get(answer) + ") " + "as correct.");
            }
        }

    }

    /**
     * returns the question text
     * @return question text
     */
    public String getQuestion() {
        return this.question;
    }

    /**
     * returns the hint text
     * @return hint text
     */
    public String getHint() {
        return this.hint;
    }

    /**
     * returns the explanation
     * @return explanation text
     */
    public String getExplanation() {
        return this.explanation;
    }

    /**
     * returns the question type
     * @return question type
     */
    public int getType() {
        return this.type;
    }

    /**
     * returns the number of answers
     * @return amount of answers
     */
    public int getNumbAnswers() {
        // one answer if type 0
        if (this.type == 0) {
            return 1;
        } else {
            // maybe multiple answers --> read and return
            return this.answers.size();
        }
    }

    /**
     * returns an array of answer texts
     * @return array of answer texts
     */
    public String[] getAnswers() {
        // return answers
        String[] questionAnswers = new String[this.answers.size()];
        for (int i = 0; i < this.answers.size(); i++) {
            questionAnswers[i] = this.answers.get(i);
        }
        return questionAnswers;
    }

    /**
     * returns an array of correct answer texts
     * @return array of correct answer texts
     */
    public int[] getCorrectAnswers() {
        // return id's
        int[] questionCorrectAnswers = new int[this.correctAnswers.size()];
        for (int i = 0; i < this.correctAnswers.size(); i++) {
            questionCorrectAnswers[i] = this.correctAnswers.get(i);
        }
        return questionCorrectAnswers;
    }

    /**
     * dumps question data on the console
     */
    public void printQuestionData() {
        // print question and hint
        debugMsg("Question: " + this.question);
        debugMsg("Hint: " + this.hint);

        // print type and answers
        switch (this.type) {
            case 0:        // type 0

                // dump type and correct answer
                debugMsg("Type: 0 (insert-question)");
                debugMsg("Correct answer: " + this.answers.get(0));

                break;

            case 1:        // type 1

                // dump type and correct answer
                debugMsg("Type: 0 (insert-question)");
                debugMsg("Correct answer: " + this.answers.get(0));
                debugMsg("Type: 1 (one correct answer)");
                for (int i = 0; i < this.answers.size(); i++) {
                    debugMsg("Answer #" + i + ": " + this.answers.get(i));
                }
                debugMsg("Correct answer: " + this.correctAnswers.get(0) + " (" + this.answers.get(this.correctAnswers.get(0)) + ")");

                break;

            case 2:        // type 2

                // dump type and correct answer
                debugMsg("Type: 2 (multiple answers)");
                for (int i = 0; i < this.answers.size(); i++) {
                    debugMsg("Answer #" + i + ": " + this.answers.get(i));
                }
                debugMsg("Correct answers:");
                for (int id : this.correctAnswers) {
                    debugMsg("Answer #" + id + ": " + this.answers.get(id));
                }

                break;

            default:        // other - database error
                debugMsg("Type: other (err? database problem)");
        }

        // write out explanation
        debugMsg("Explanation: " + this.explanation);
    }

}