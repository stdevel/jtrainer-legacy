import java.io.*;
import java.nio.charset.StandardCharsets;


/**
 * localization file class
 *
 * @author Christian Stankowic
 * @version 1.7
 */
public class localizationFile {
    /**
     * locale version (must match JTrainer version)
     */
    final static double localeVersion = 1.7;

    /**
     * array for translations, enlarge if required
     */
    final String[] value = new String[100];

    /**
     * file version (must match JTrainer version)
     */
    double fileVersion;

    /**
     * Language file author
     */
    String fileAuthor;

    /**
     * Flag whether file was loaded successfully
     */
    boolean fileLoaded = true;


    /**
     * localization file constructor
     * @param file localization file path
     */
    localizationFile(String file) {

        try {
            // try instancing FileReader and BufferedReader
            System.out.println("About to load " + file + "...");
            BufferedReader targetFile = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

            // read version
            fileVersion = Double.parseDouble(targetFile.readLine());
            fileAuthor = targetFile.readLine();

            // temporary counter and string for reading a line
            String temp;
            int pointer = 0;

            // read strings if version matches
            if (fileVersion >= localeVersion) {
                // version matches - read strings
                debugMsg("Loading localization file '" + file + "' from '" + fileAuthor + "' - version '" + fileVersion + "', class version is: '" + localeVersion + "'");
                while (true) {
                    // read next line
                    temp = targetFile.readLine();

                    // abort reading if EOF reached
                    if (temp == null) {
                        break;
                    } else {
                        // add line to string array
                        value[pointer] = temp;
                        pointer++;
                    }
                }

                // write message for loaded file
                debugMsg("Loaded " + (pointer - 1) + " definitions from locale.txt");
                fileLoaded = true;
            } else {
                // version doesn't match --> abort and load standard translation
                debugMsg("Wrong localization file version '" + fileVersion + "', needed version is: '" + localeVersion + "' or higher.");
                this.loadDefaults();
            }
        } catch (java.io.IOException exp) {
            // write error message
            debugMsg("Error loading " + file + ", falling back to built-in english...\n");
            debugMsg("Stack trace:");
            exp.printStackTrace();

            // load standard translation
            this.loadDefaults();
        }
    }


    private void loadDefaults() {
        // load standard translation
        value[0] = "This program is an pre-release - it hasn't been finished, yet.";
        value[1] = "Feel free to report any bugs/errors you may encounter.";
        value[2] = "Important information";
        value[3] = "JTrainer v.";
        value[4] = "File";
        value[5] = "Open catalog";
        value[6] = "Catalog details";
        value[7] = "Catalog name:";
        value[8] = "Description:";
        value[9] = "Author:";
        value[10] = "Date:";
        value[11] = "Details about";
        value[12] = "Abort training";
        value[13] = "Exit training";
        value[14] = "Exit";
        value[15] = "Settings";
        value[16] = "Random mode";
        value[17] = "Ordered mode";
        value[18] = "Once";
        value[19] = "Endless";
        value[20] = "?";
        value[21] = "About...";
        value[22] = "JTrainer is an exam simulator.";
        value[23] = "JTrainer is open source and published under the terms of GPL.";
        value[24] = "Visit the project page for further details:";
        value[25] = "https://github.com/stdevel/jtrainer-legacy";
        value[26] = "About JTrainer v.";
        value[27] = "Answer question";
        value[28] = "Next question";
        value[29] = "Answer A";
        value[30] = "Answer B";
        value[31] = "Answer C";
        value[32] = "Answer D";
        value[33] = "Answer E";
        value[34] = "Type answer here...";
        value[35] = "Please open a catalog!";
        value[36] = "Selected file:";
        value[37] = "Number of questions:";
        value[38] = "Question";
        value[39] = "That file cannot be opened.";
        value[40] = "File import aborted";
        value[41] = "You didn't answer the question correctly.";
        value[42] = "You answered the question correctly.";
        value[43] = "You finished the catalog, click on 'Exit training', to see a short summary.";
        value[44] = "answered questions";
        value[45] = "Correct answers:";
        value[46] = "Incorrect answers:";
        value[47] = "Show hint";
        value[48] = "You already read the hint!";
        value[49] = "Answered using hints:";
        value[50] = "CORRECT";
        value[51] = "WRONG";
        value[52] = "Time's over!";
        value[53] = "success rate:";
        value[54] = "from";
        value[55] = "questions";
        value[56] = "answers";
        value[57] = "left";

        // set flag for not-loaded file
        fileLoaded = false;
    }


    /**
     * returns a translated string
     * @param pointer ID to translatable string
     * @return translated string
     */
    public String getValue(int pointer) {
        return value[pointer];
    }


    /**
     * dumps defined or loaded translations
     */
    public void printLocaleData() {
        // was a translation file successfully loaded?
        if (!this.fileLoaded) {
            // no - dump integrated definitions
            debugMsg("locales file was NOT loaded - using built-in localization");
            for (int i = 0; i < this.value.length; i++) {
                debugMsg("BUILT-IN #" + i + ": " + this.value[i]);
            }
        } else {
            //Yes - dump loaded definitions
            debugMsg("locales file was SUCCESSFULLY loaded");
            for (int i = 0; i < this.value.length; i++) {
                debugMsg("LOCALES #" + i + ": " + this.value[i]);
            }
        }
    }


    /**
     * Prints debug messages to the console
     * @param debugMessage the message to display
     */
    private void debugMsg(String debugMessage) {
        System.out.println("LF: " + debugMessage);
    }

}