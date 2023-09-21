package ie.atu.sw;

import java.util.Scanner;
import static java.lang.System.out;

/**
 * This class is used to communicate with the user.
 */
public class Communicator {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String PAUSE_MESSAGE = "\n[Message] Press ENTER to continue!";

    /**
     * Prompts the user to hit ENTER to continue.
     * Is used in order to pause execution to give the user
     * an opportunity to read the information that is currently on the screen.
     */
    public static void pause()  {
        out.println(PAUSE_MESSAGE);
        scanner.nextLine();
    }

    /**
     * Asks the user for input, and makes sure that the provided input is not empty
     * and is not a duplicate of the given String.
     *
     * @param message the message
     * @param errorEmpty the error message if the input is empty
     * @param errorDuplicate the error message if the input is duplicate
     * @param original the original String to compare with.
     * @param caseSensitive true if the comparison should be case-sensitive, false otherwise.
     *
     * @return the input.
     */
    public static String getNotTheSame(String message, String errorEmpty, String errorDuplicate,
                                       String original, boolean caseSensitive)  {
        do  {
            String input = getNotEmpty(message, errorEmpty);

            boolean isDuplicate = (caseSensitive && input.equals(original) || (!caseSensitive && input.equalsIgnoreCase(original)));
            if (isDuplicate)  {
                out.println(errorDuplicate);
            } else  {
                return input;
            }

        } while (true);
    }

    /**
     * Asks the user for input, and makes sure that the provided input is not empty.
     *
     * @param message the prompt message.
     * @param error the error message, if the input is empty.
     *
     * @return the input.
     */
    public static String getNotEmpty(String message, String error) {
        do {
            System.out.print(message);

            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                out.println(error);
            } else {
                return input;
            }
        } while (true);
    }

    /**
     * Asks the user for a numeric input within a given range.
     *
     * @param min the lower bound
     * @param max the upper bound
     * @param message the message
     * @param rangeError the error message if the input is out of range
     * @param typeError the error message if the input is not numeric
     *
     * @return the input, converted to a number
     */
    public static int getNumberInRange(int min, int max,
                                       String message, String rangeError, String typeError) {
        do {
            out.print(message);

            String choice = scanner.nextLine().trim();

            try {
                int option = Integer.parseInt(choice);

                if (option >= min && option <= max) {
                    return option;
                } else {
                    System.out.println(rangeError);
                }
            } catch (NumberFormatException e) {
                System.out.println(typeError);
            }
        } while (true);
    }
}
