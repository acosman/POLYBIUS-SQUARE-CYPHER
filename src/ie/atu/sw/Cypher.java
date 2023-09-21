package ie.atu.sw;

import static java.lang.System.out;

// given

/**
 * This class contains the functionality for encrypting and
 * decrypting text using ADFGVX cypher.
 */
public class Cypher {
    // Constants
    private static final String EMPTY_ERROR = "[Error] Can't be empty!";

    private final char[][] square = {

            {'A', 'A', 'D', 'F', 'G', 'V', 'X'},
            {'A', 'P', 'H', '0', 'Q', 'G', '6'},
            {'D', '4', 'M', 'E', 'A', '1', 'Y'},
            {'F', 'L', '2', 'N', 'O', 'F', 'D'},
            {'G', 'X', 'K', 'R', '3', 'C', 'V'},
            {'V', 'S', '5', 'Z', 'W', '7', 'B'},
            {'X', 'J', '9', 'U', 'T', 'I', '8'},

    };

    private String secret;

    /**
     * Creates a default cypher.
     *
     * Key 'java' is used by default.
     */
    public Cypher() {
        this("java");
    }

    /**
     * Creates a cypher with the given key.
     *
     * @param secret the key.
     */
    public Cypher(String secret) {
        this.setKey("java");
    }

    /**
     * Asks the user for input and a key,
     * and decrypts the provided input.
     */
    public void decryptInteractive() {
        String encryptedText = Communicator.getNotEmpty("Enter the Encrypted Text: ", EMPTY_ERROR);
        String key = Communicator.getNotEmpty("Enter the key: ", EMPTY_ERROR);

        String result = this.decrypt(encryptedText, key);
        out.println(result);
        out.println();
    }

    /**
     * Asks the user for input and a key,
     * and encrypts the provided input.
     */
    public void encryptInteractive()  {
        String text = Communicator.getNotEmpty("Enter the text to Encrypt: ", EMPTY_ERROR);
        String key = Communicator.getNotEmpty("Enter the key: ", EMPTY_ERROR);

        String result = this.encrypt(text, key);
        out.println(result);
        out.println();
    }

    //Have a way to set the key

    /**
     * Encrypts the provided text.
     * The default key is used.
     *
     * @param text the text.
     *
     * @return the encrypted text.
     */
    public String encrypt(String text) {
        text = text.trim();

        if (text.isEmpty())  {
            out.println("[Error] Can't encrypt empty text!");
        }

        return this.encrypt(text, this.secret);
    }

    /**
     * Encrypts the provided text.
     *
     * @param text the text.
     * @param secret the key to use.
     *
     * @return the encrypted text.
     */
    public String encrypt(String text, String secret) {
        out.println("**** Encrypting " + text + " with key " + secret + " ****");

        String result = encryptReplace(text);
        result = encryptRearrange(secret, result);

        return result;
    }

    /*
    Helper methods for the encrypting functionality
     */

    /**
     * Replaces each letter in the given text
     * with a cypher equivalent from the table.
     *
     * @param s the text.
     *
     * @return the text with all of its letters replaced.
     */
    private String encryptReplace(String s) {
        String result = "";

        for (char letter : s.toCharArray()) {
            String encrypted = encryptLetter(letter);
            result += encrypted;
        }

        return result;
    }

    /**
     * Determines the two symbols from the Polybius square
     * that the given letter corresponds to.
     *
     * @param letter the letter.
     *
     * @return the two matching symbols,
     * an empty String if none were found.
     */
    private String encryptLetter(char letter) {
        for (int i = 1; i < square.length; i++) {
            for (int j = 1; j < square[i].length; j++) {
                if (Character.toUpperCase(letter) == square[i][j]) {
                    return String.valueOf(square[i][0]) + square[0][j];
                }
            }
        }

        return "";
    }

    /**
     * Creates a matrix for the given text and key,
     * and performs a columnar transposition on it.
     *
     * @param secret the key.
     * @param replaced the text with all letters replaced with two encoded symbols.
     *
     * @return the text from the transposed matrix.
     */
    private String encryptRearrange(String secret, String replaced) {
        int rows = calculateRows(secret, replaced);
        int columns = calculateColumnsBySecret(secret);

        char[][] matrix = new char[rows][columns];
        fillMatrixByRows(replaced, matrix);

        encryptTransposition(secret, matrix);

        return encryptColumns(matrix);
    }

    /**
     * Performs a columnar transpositon on the given matrix.
     *
     * @param secret the key.
     * @param matrix the matrix to transpose.
     */
    private void encryptTransposition(String secret, char[][] matrix) {
        int[] sortedIndexes = calculateSortedIndexes(secret);

        int[] oldIndexes = new int[secret.length()];
        for (int i = 0; i < oldIndexes.length; i++) {
            oldIndexes[i] = i;
        }
        rearrangeMatrix(matrix, oldIndexes, sortedIndexes);
    }

    /**
     * Reads the encrypted text from the transposed matrix,
     * column by column.
     *
     * @param matrix the matrix.
     *
     * @return the encrypted text.
     */
    private String encryptColumns(char[][] matrix) {
        String result = "";

        char[] empty = new char[1];
        for (int column = 0; column < matrix[0].length; column++) {
            for (int row = 0; row < matrix.length; row++) {
                if (matrix[row][column] != empty[0]) {
                    result += matrix[row][column];
                }
            }
        }

        return result;
    }

    /* Decrypt */

    /**
     * Decrypts the given text.
     * Default key is used.
     *
     * @param s the text.
     *
     * @return the original text.
     */
    public String decrypt(String s) {
        s = s.trim();
        if (s.isEmpty())  {
            out.println("[Error] Can't encrypt empty text!");
        }

        return this.decrypt(s, this.secret);
    }

    /**
     * Decrypts the given text using the given key.
     *
     * @param text the text.
     * @param secret the key.
     *
     * @return the original text.
     */
    public String decrypt(String text, String secret) {
        out.println("**** Decrypting " + text + " with key " + secret + " ****");

        char[][] matrix = decryptToMatrix(text, secret);
        String content = matrixToContentByRows(matrix);
        if (content.length() % 2 == 0)  {
            String result = "";

            for (int i = 0; i < content.length(); i += 2) {
                String pair = content.substring(i, i + 2);
                if (pair.length() < 2) {
                    result += " ";
                } else {
                    result += decryptPair(pair.charAt(0), pair.charAt(1));
                }
            }

            return result;
        }

        out.println("[Error] Invalid key was used to decrypt!");
        return "";
    }

    /**
     * Decrypts the given pair of characters.
     * Determines which letter matches to these two characters
     * in the Polybius square.
     *
     * @param fist the first character
     * @param second the second character
     *
     * @return the letter, if found, a space otherwise.
     */
    private char decryptPair(char fist, char second) {
        for (int i = 1; i < square.length; i++) {
            if (square[i][0] == fist) {
                for (int j = 1; j < square[0].length; j++) {
                    if (square[0][j] == second) {
                        return square[i][j];
                    }
                }

                return ' ';
            }
        }

        return ' ';
    }

    /**
     * Creates a transposed matrix from the given encrypted text.
     *
     * @param s the text to decrypt
     * @param secret the key
     *
     * @return the matrix.
     */
    private char[][] decryptToMatrix(String s, String secret) {
        int rows = calculateRows(secret, s);
        int columns = calculateColumnsBySecret(secret);

        int remainingEmpty = rows * columns - s.length();

        char[][] matrix = new char[rows][columns];
        int[] sortedIndexes = calculateSortedIndexes(secret);

        if (remainingEmpty == 0) {
            fillMatrixByColumns(s, matrix);
        } else {
            fillMatrixByColumns(s, matrix, remainingEmpty, sortedIndexes);
        }
        int[] newIndexes = new int[secret.length()];
        for (int i = 0; i < newIndexes.length; i++) {
            newIndexes[i] = i;
        }
        rearrangeMatrix(matrix, sortedIndexes, newIndexes);

        return matrix;
    }

    /**
     * Sets the key for this cypher.
     *
     * @param s the key.
     *
     * @return the old key.
     */
    public String setKey(String s) {
        String old = this.secret;
        this.secret = s;
        return old;
    }

    /**
     * Additional helper methods
     */

    /**
     * Calculates the number of columns in a matrix,
     * based on the given key.
     *
     * @param secret the key.
     *
     * @return the number of columns.
     */
    private static int calculateColumnsBySecret(String secret) {
        return secret.length();
    }

    /**
     * Calculates the number of rows in a matrix,
     * based on the given key and text.
     *
     * @param secret the key.
     * @param replaced the text.
     *
     * @return the number of rows.
     */
    private static int calculateRows(String secret, String replaced) {
        int rows = replaced.length() / secret.length();
        if (replaced.length() % secret.length() != 0) {
            rows++;
        }
        return rows;
    }

    /**
     * Sorts the given key and stores the correct order of its letters
     * in the result array.
     *
     * @param secret the key.
     *
     * @return the array containing the indexes of the key
     * in an order that corresponds to the sorted letters.
     */
    private static int[] calculateSortedIndexes(String secret) {
        char[] sortedLetters = secret.toCharArray();

        int[] sortedIndexes = new int[secret.length()];
        for (int i = 0; i < secret.length(); i++) {
            sortedIndexes[i] = i;
        }

        boolean isSorted;
        do {
            isSorted = true;

            for (int i = 1; i < sortedLetters.length; i++) {
                char first = sortedLetters[i - 1];
                char second = sortedLetters[i];

                if (second < first) {
                    sortedLetters[i] = first;
                    sortedLetters[i - 1] = second;

                    isSorted = false;

                    int firstIndex = sortedIndexes[i - 1];
                    int secondIndex = sortedIndexes[i];

                    sortedIndexes[i] = firstIndex;
                    sortedIndexes[i - 1] = secondIndex;
                }
            }
        } while (!isSorted);

        return sortedIndexes;
    }

    /**
     * Performs a columnar transposition on the given matrix.
     * The columns are rearranged to match the index order in the given array.
     *
     * @param matrix the matrix
     * @param oldIndexes the old order of indexes - matches the current
     * order of the matrix columns.
     * @param sortedIndexes the indexes that the columns of the matrix should
     * match after the transposition.
     */
    private static void rearrangeMatrix(char[][] matrix, int[] oldIndexes, int[] sortedIndexes) {
        // what is the new index of content that was located at the old i-th index
        int[] contentIndexes = new int[oldIndexes.length];
        for (int i = 0; i < oldIndexes.length; i++) {
            // each oldIndexes[i]-th element is located at i-th position
            contentIndexes[oldIndexes[i]] = i;
        }

        // what was the old index of the content located at the new i-th index
        int[] positionIndexes = new int[oldIndexes.length];
        System.arraycopy(oldIndexes, 0, positionIndexes, 0, oldIndexes.length);

        for (int i = 0; i < sortedIndexes.length; i++) {
            // the true index of the element to be put at i-th position
            int trueSortedIndex = sortedIndexes[i];
            // the true index of i-th element was
            int trueIthIndex = positionIndexes[i];

            // the current index of the element to be put at i-th position
            int contentIndex = contentIndexes[trueSortedIndex];

            swapColumns(i, contentIndex, matrix);

            // true i-th element is now at contentIndex position
            contentIndexes[trueIthIndex] = contentIndex;
            // the trueSortedIndex element is now at i-th position
            contentIndexes[trueSortedIndex] = i;

            positionIndexes[i] = trueSortedIndex;
            positionIndexes[contentIndex] = trueIthIndex;
        }
    }

    private static void fillMatrixByRows(String replaced, char[][] matrix) {
        int i = 0;
        int j = 0;
        for (char letter : replaced.toCharArray()) {
            matrix[i][j] = letter;

            j++;
            if (j >= matrix[i].length) {
                i++;
                j = 0;
            }
        }
    }

    /**
     * Fills the matrix column by column -  each character of the given text
     * is placed in a single cell in the matrix.
     *
     * @param encrypted the text to place inside the matrix.
     * @param matrix the matrix to fill.
     */
    private static void fillMatrixByColumns(String encrypted, char[][] matrix) {
        fillMatrixByColumns(encrypted, matrix, 0, null);
    }

    /**
     * Fills the matrix column by column -  each character of the given text
     * is placed in a single cell in the matrix.
     *
     * @param encrypted the text to place inside the matrix.
     * @param matrix the matrix to fill.
     * @param remainingEmpty the number of cells to leave empty (occurs when
     * the length of the text cannot be divided by the number of cells in the matrix
     * without a remained).
     * @param sortedIndexes is used to determine which matrix columns are going to be
     * moved to the end of the matrix, in order to leave them empty, if necessary.
     */
    private static void fillMatrixByColumns(String encrypted, char[][] matrix, int remainingEmpty, int[] sortedIndexes) {
        int row = 0;
        int column = 0;

        int startingEmptyColumn = matrix[0].length - remainingEmpty;

        char[] letters = encrypted.toCharArray();
        for (int i = 0; i < letters.length; i++) {
            char letter = letters[i];

            boolean putLetter = remainingEmpty == 0 ||
                    sortedIndexes[column] < startingEmptyColumn || row < matrix.length - 1;

            if (putLetter) {
                matrix[row][column] = letter;
            }

            row++;
            if (row >= matrix.length) {
                row = 0;
                column++;
            }

            if (!putLetter) {
                i--;
            }
        }
    }

    /**
     * Swaps two columns of the matrix.
     *
     * @param firstIndex the index of the first column
     * @param secondIndex the index of the second column
     * @param matrix the matrix
     */
    private static void swapColumns(int firstIndex, int secondIndex, char[][] matrix) {
        for (int row = 0; row < matrix.length; row++) {
            char first = matrix[row][firstIndex];
            char second = matrix[row][secondIndex];

            matrix[row][firstIndex] = second;
            matrix[row][secondIndex] = first;
        }
    }

    /**
     * Converts the content of the matrix into a String.
     * Reads characters from the matrix, row by row, appending each character
     * to the result String.
     *
     * @param matrix the matrix
     *
     * @return a String with matrix contents.
     */
    private static String matrixToContentByRows(char[][] matrix) {
        String result = "";

        char[] empty = new char[1];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == empty[0])  {
                    // the content has been read
                    break;
                }

                result += matrix[i][j];
            }
        }

        return result;
    }

    /**
     * Prints an information message.
     */
    public void add() {
        out.println("[INFO] Add a Student");
    }

    /**
     * Is used to test the Cypher class.
     *
     * @param s not used.
     */
    public static void main(String[] s) {
        String text = "didhdihdiudhi";
        String key = "TOPFHEMRING";
        int cols = key.length();
        text.length();

        System.out.println((double) text.length() / (double) key.length());
        System.out.println((double) text.length() % (double) key.length());

        Cypher cypher = new Cypher();

        String[] words = {"object", "monkey", "planes", "antelope",
                "gymnasts", "forest", "hello", "lion", "lions", "greatlion", "greatlions"};

        out.println("\n**** Encrypt ****\n");
        for (String word : words) {
            out.println(word + " = " + cypher.encrypt(word));
        }

        out.println("\n**** Decrypt ****\n");
        for (String word : words) {
            String encryped = cypher.encrypt(word);

            out.println(encryped + " = " + cypher.decrypt(encryped));
        }
    }
}
