package ie.atu.sw;

/**
 * This class is used to get a key from the user.
 */
public class CreateKey {
    // Constants

    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 20;

    private static final String ENTER_KEY_MESSAGE = "Enter the key: ";
    private static final String EMPTY_KEY_MESSAGE = "[Error] Secret key can't be empty!";
    private static final String INVALID_LENGTH_MESSAGE = "[Error] Key length entered is not between 8 and 20 characters!";
    private static final String REPEAT_ENTER_KEY_MESSAGE = "Please enter a valid key: ";

    // Instance fields

    private char[] keyText;
    private boolean hasKey;

    /**
     * The default constructor.
     * The key has not been retrieved yet.
     */
    public CreateKey() {
        // call constructor to use in Menu Class
        this.hasKey = false;
    }

    /**
     * Returns the key as a character array.
     *
     * @return the key as a character array.
     */
    public char[] getKeyText() {
        return keyText;
    }

    /**
     * Returns the retrieved key.
     *
     * @return the key.
     */
    public String getKey()  {
        return new String(this.keyText);
    }

    /**
     * Returns true if the key has been retrieved, false otherwise.
     *
     * @return true if the key has been retrieved, false otherwise.
     */
    public boolean hasKey()  {
        return this.hasKey;
    }

    /**
     * Displays the choose key menu.
     */
    public void chooseKey() {
        System.out.println(ConsoleColour.CYAN); // Set console colour for the menu.
        System.out.println("  /\\  /\\  /\\  /\\  /\\  /\\  /\\  /\\  /\\ ");
        System.out.println(" /  \\/  \\/  \\/  \\/  \\/  \\/  \\/  \\/  \\");
        System.out.println("|                                      |");
        System.out.println("| Welcome to the Cipher Encryptor!     |");
        System.out.println("|                                      |");
        System.out.println("| To encrypt your message, you need to |");
        System.out.println("| enter a cipher key of your choice.   |");
        System.out.println("|                                      |");
        System.out.println("| The key should be between " + MIN_LENGTH +
                " to " + MAX_LENGTH + "    |");
        System.out.println("| letters from the Latin alphabet (A-Z)|");
        System.out.println("|                                      |");
        System.out.println("| Please enter your cipher key below > |");
        System.out.println("|                                      |");
        System.out.println(" \\  /\\  /\\  /\\  /\\  /\\  /\\  /\\  /\\  /");
        System.out.println("  \\/  \\/  \\/  \\/  \\/  \\/  \\/  \\/  \\/ ");
        System.out.println();
        System.out.println("Thank you for using the Cipher Encryptor!");

        // continue flow of control to the process method
        process();
    }

    /**
     * Processes user input and returns it as a char array.
     *
     * @return the key.
     */
    private char[] process() {
        // read in user input and assign to variable keyChoice
        String keyChoice = Communicator.getNotEmpty(ENTER_KEY_MESSAGE, EMPTY_KEY_MESSAGE);

        boolean isValidLength = isValidLength(keyChoice.length());

        // use if statement to set range for keyChoice
        if (isValidLength) {
            // pass keyChoice to method formatKey, assign it to instance variable keyText
            keyChoice = formatKey(keyChoice);

            // check the length again
            isValidLength = isValidLength(keyChoice.length());
        }

        if (isValidLength)  {
            // save the key
            this.keyText = keyChoice.toCharArray();
            this.hasKey = true;

            // print the key
            System.out.println(keyChoice);
        } else  {
            // inform user of incorrect length
            System.out.println(INVALID_LENGTH_MESSAGE);

            // instruct user to enter a valid key
            System.out.print(REPEAT_ENTER_KEY_MESSAGE);

            // call the method process (recursion) on itself so that user can enter input again
            process();
        }

        // the process method returns the char array keyText
        return keyText;
    }

    /**
     * Checks whether the key length is valid.
     *
     * @param length the length.
     *
     * @return true if valid, false otherwise.
     */
    private boolean isValidLength(int length)  {
        return length >= MIN_LENGTH && length <= MAX_LENGTH;
    }

    /**
     * Removes characters that are not letters.
     *
     * @param key the key.
     *
     * @return alphabetic key.
     */
    private String formatKey(String key) {
        // it also converts the letters to uppercase and returns a char array
        return key.trim().replaceAll("[^a-zA-Z]", "").toUpperCase();
    }

    /**
     * Is used to test the CreateKey class.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        CreateKey ck = new CreateKey();
        ck.chooseKey();
    }
}
