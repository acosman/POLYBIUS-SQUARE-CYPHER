package ie.atu.sw;

import java.util.List;

/**
 * This class contains the menu functionality.
 * A menu is displayed to the user, and the user is prompted
 * to choose a menu option.
 */
public class Menu {
    // Constants
    private static final int MIN_OPTION = 1;
    private static final int MAX_OPTION = 8;
    private static final String INVALID_OPTION_MESSAGE = "[Error] Invalid Selection";
    private static final String EXIT_MESSAGE = "[INFO] Exiting...Bye!";
    private static final String EMPTY_MESSAGE = "[Error] Can't be empty!";
    private static final String DUPLICATE_INPUT_MESSAGE = "[Error] Input directory can't be the same as the Output directory!";
    private static final String DUPLICATE_OUTPUT_MESSAGE = "[Error] Output directory can't be the same as the Input directory!";
    private static final String TYPE_ERROR_MESSAGE = "[Error] Must be numeric!";
    private static final String RANGE_ERROR_MESSAGE = "[Error] Must be between " + MIN_OPTION + " and " + MAX_OPTION + "!";

    private static final String BAD_DIRECTORY_MESSAGE = "[Error] This directory does not exist!";
    private static final String NOT_SETUP_DIRECTORIES = "[Error] The directories are not setup!";
    private static final String NOT_SETUP_KEY = "[Error] The key is not setup!";
    private static final String NOT_SETUP_OUTPUT = "[Error] The output directory is not setup!";
    private static final String NOT_SETUP_INPUT = "[Error] The input directory is not setup!";

    private static final String OPTION_MESSAGE = "Select Option [" + MIN_OPTION +
            "-" + MAX_OPTION + "]> ";

    // Instance fields

    private final Setup setup = new Setup();
    private final Cypher cypher = new Cypher();
    private final FileManager parser = new FileManager();
    private final CreateKey createKey = new CreateKey();

    /**
     * Repeatedly displays the menu and prompts the user for option.
     *
     * The user is required to provide a valid menu option.
     *
     * Once a menu option is provided, the corresponding menu action is executed.
     *
     * Then the user is shown the menu again.
     */
    public void show() {
        boolean quit = false;

        do {
            displayMenu();
            int option = getOption();

            switch (option) {
                // Specify Input File Directory
                case 1:
                    this.setup.setInputDirectoryPath(this.getInputDirectoryFromUser());

                    break;
                // Specify Output File Directory
                case 2:
                    this.setup.setOutputDirectoryPath(this.getOutputDirectoryFromUser());

                    break;
                // Create Key
                case 3:
                    this.createKey.chooseKey();
                    this.cypher.setKey(this.createKey.getKey());
                    this.setup.setHasKey(true);

                    break;
                // Encrypt All Files
                case 4:
                    if (this.setup.isSetup()) {
                        try {
                            List<String> parsedTextsToEncrypt = this.parser.readAndEncrypt(
                                    this.setup.getInputDirectoryPath(),
                                    this.setup.getOutputDirectoryPath(), this.cypher);

                            for (String parsedText : parsedTextsToEncrypt) {
                                String encryptedText = this.cypher.encrypt(parsedText);

                                // Displaying encrypted text
                                System.out.println("\nParsed Text: " + parsedText);
                                System.out.println("Encrypted Text: " + encryptedText);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (!this.setup.hasInputDirectory()) {
                        System.out.println(NOT_SETUP_INPUT);
                    } else if (!this.setup.hasOutputDirectory()) {
                        System.out.println(NOT_SETUP_OUTPUT);
                    } else  {
                        System.out.println(NOT_SETUP_KEY);
                    }

                    break;
                // Decrypt All Files
                case 5:
                    if (this.setup.hasOutputDirectory() && this.setup.hasKey()) {
                        try {
                            List<String> parsedTextsToDecrypt = this.parser.parse(this.setup.getOutputDirectoryPath());

                            for (String text : parsedTextsToDecrypt) {
                                String decryptedText = this.cypher.decrypt(text);

                                // Display decrypted text
                                System.out.println("\nParsed Text: " + text);
                                System.out.println("Decrypted Text: " + decryptedText);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (!this.setup.hasOutputDirectory())  {
                        System.out.println(NOT_SETUP_OUTPUT);
                    } else if (!this.setup.hasKey())  {
                        System.out.println(NOT_SETUP_KEY);
                    }

                    break;

                // Encrypt Interactive
                case 6:
                    this.cypher.encryptInteractive();

                    break;

                // Decrypt Interactive
                case 7:
                    this.cypher.decryptInteractive();

                    break;

                // Quit
                case 8:
                    System.out.println(EXIT_MESSAGE);
                    quit = true; // exit the loop

                    break;
                // Invalid option
                default:
                    System.out.println(INVALID_OPTION_MESSAGE);
            }

            if (!quit)  {
                Communicator.pause();
            }
        } while (!quit);
    }

    /**
     * Helper methods
     */

    /**
     * Displays the menu with options
     */
    private void displayMenu() {
        System.out.println(ConsoleColour.CYAN); // Set console colour for the menu.
        System.out.println("  /\\  /\\  /\\  /\\  /\\  /\\  /\\  /\\  /\\ ");
        System.out.println(" /  \\/  \\/  \\/  \\/  \\/  \\/  \\/  \\/  \\");
        System.out.println("|                                      |");
        System.out.println("| ATU - Dept. Computer Science &       |");
        System.out.println("| Applied Physics                      |");
        System.out.println("|                                      |");
        System.out.println("| ADFGVX File Encryption               |");
        System.out.println("|                                      |");
        System.out.println(" \\  /\\  /\\  /\\  /\\  /\\  /\\  /\\  /\\  /");
        System.out.println("  \\/  \\/  \\/  \\/  \\/  \\/  \\/  \\/  \\/ ");
        System.out.println();
        System.out.println("Please choose an option from the list:");
        System.out.println("(1) Specify Input File Directory " +
                (this.setup.getInputDirectoryPath().isEmpty() ? "()" : "(" + this.setup.getInputDirectoryPath() + ")"));
        System.out.println("(2) Specify Output File Directory "+
                (this.setup.getOutputDirectoryPath().isEmpty() ? "()" : "(" + this.setup.getOutputDirectoryPath() + ")"));
        System.out.println("(3) Create Key " + "(" +
                (this.createKey.hasKey() ? this.createKey.getKey() : "") + ")");
        System.out.println("(4) Encrypt All Files");
        System.out.println("(5) Decrypt All Files");

        System.out.println("(6) Encrypt Interactive");
        System.out.println("(7) Decrypt Interactive");

        System.out.println("(8) Quit");
        System.out.println();
        System.out.print(ConsoleColour.CYAN);
    }

    /* Directories */

    /**
     * Prompts the user for an input directory.
     *
     * @return the input directory.
     */
    private String getInputDirectoryFromUser()  {
        return getDirectoryFromUser("Please specify the input directory: ", setup.hasOutputDirectory(),
                DUPLICATE_INPUT_MESSAGE, this.setup.getOutputDirectoryPath());
    }

    /**
     * Prompts the user for an output directory.
     *
     * @return the output directory.
     */
    private String getOutputDirectoryFromUser()  {
        return getDirectoryFromUser("Please specify the output directory: ", setup.hasInputDirectory(),
                DUPLICATE_OUTPUT_MESSAGE, this.setup.getInputDirectoryPath());
    }

    /**
     * Prompts a user for a directory.
     *
     * @param message the prompt message
     * @param checkDuplicate true if should check if the provided input is a duplicate
     * of the given String.
     * @param errorDuplicate an error to display if the user provides a duplicate String
     * @param original the original String to compare user input to
     *
     * @return the directory.
     */
    private String getDirectoryFromUser(String message, boolean checkDuplicate,
                                        String errorDuplicate, String original)  {
        String result;

        do {
            if (checkDuplicate) {
                result = Communicator.getNotTheSame(message, EMPTY_MESSAGE,
                        errorDuplicate, original, false);
            } else {
                result = Communicator.getNotEmpty(message, EMPTY_MESSAGE);
            }

            if (FileManager.directoryExists(result))  {
                return result;
            }

            System.out.println(BAD_DIRECTORY_MESSAGE);
        } while (true);
    }


    /**
     * Gets an option from the user.
     * <p>
     * Keeps asking for an option until a valid option is provided.
     *
     * @return the correct option.
     */
    private int getOption() {
        return Communicator.getNumberInRange(MIN_OPTION, MAX_OPTION, OPTION_MESSAGE, RANGE_ERROR_MESSAGE, TYPE_ERROR_MESSAGE);
    }


    /**
     * This method is used to test the Menu class.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.show();
    }
}
