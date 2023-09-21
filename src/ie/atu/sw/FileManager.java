package ie.atu.sw;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the functionality of
 * reading files and writing to files.
 */
public class FileManager {
    private static final String BAD_DIRECTORY_MESSAGE = "[Error] Invalid directory!";
    private static final String NO_FILES_INVALID_DIRECTORY_MESSAGE = "[Error] No files found or invalid directory!";


    /* Writing */

    /**
     * Writes the given contents to all files in the given directory.
     *
     * @param contents the contents.
     * @param directoryPath the directory.
     *
     * @throws FileNotFoundException if the files could not be found.
     */
    public void write(List<String> contents, String directoryPath) throws FileNotFoundException {
        if (directoryPath == null || directoryPath.isEmpty()) {
            System.out.println(BAD_DIRECTORY_MESSAGE);
            return;
        }

        File directory = new File(directoryPath);
        File[] contentsOfDirectory = directory.listFiles();

        if (contentsOfDirectory == null) {
            System.out.println(NO_FILES_INVALID_DIRECTORY_MESSAGE);
        }

        for (File object : contentsOfDirectory) {
            if (object.isFile()) {
                String fileName = object.getName();
                System.out.println("\nWriting to file: " + fileName);

                PrintWriter writer = new PrintWriter(object);
            } else if (object.isDirectory()) {
                System.out.println("\nEncountered a directory. Skipping: " + object.getName());
            }
        }
    }

    /* Reading */

    /**
     * Parse the given directory path and returns the content of each file as a list of Strings.
     *
     * @param directoryPath The directory containing the files to be read.
     * @return A list of strings where each string is the content of a file.
     * @throws Exception If any error occurs during file reading.
     */
    public List<String> parse(String directoryPath) throws Exception {
        if (directoryPath == null || directoryPath.isEmpty()) {
            System.out.println(BAD_DIRECTORY_MESSAGE);
            return new ArrayList<>();
        }

        File directory = new File(directoryPath);
        File[] contentsOfDirectory = directory.listFiles();

        if (contentsOfDirectory == null) {
            System.out.println(NO_FILES_INVALID_DIRECTORY_MESSAGE);

            // return an empty list
            return new ArrayList<>();
        }

        List<String> fileContents = new ArrayList<>();

        for (File object : contentsOfDirectory) {
            if (object.isFile()) {
                String fileName = object.getName();
                System.out.println("\nReading file: " + fileName);

                String content = readFile(object.getAbsolutePath());
                fileContents.add(content);
            } else if (object.isDirectory()) {
                System.out.println("\nEncountered a directory. Skipping: " + object.getName());
            }
        }

        return fileContents;
    }

    /**
     * Read the content of the specified file.
     *
     * @param filePath The absolute path of the file to be read.
     * @return The content of the file as a string.
     * @throws Exception If any error occurs during file reading.
     */
    public String readFile(String filePath) throws Exception {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().replaceAll("[^a-zA-Z]", "").toUpperCase();
                sb.append(line).append("\n");
                System.out.println("Processed line: " + line);
            }
        }

        return sb.toString();
    }

    /**
     * Checks whether the given directory is valid.
     *
     * @param d the directory
     *
     * @return true if valid, false otherwise.
     */
    private boolean isValidDirectory(String d)  {
        return d != null && !d.isEmpty();
    }

    /* Read and Encrypt */

    /**
     * Reads the files from the given input directory,
     * encrypts them, and writes the encrypted contents
     * to a file with the corresponding name in the given output directory.
     *
     * @param inputDirectory the input directory.
     * @param outputDirectory the output directory.
     * @param cypher the cypher that is used for encrypting the files.
     *
     * @return the list with encrypted contents.
     *
     * @throws Exception is used in case of any input/output errors.
     */
    public List<String> readAndEncrypt(String inputDirectory, String outputDirectory, Cypher cypher) throws Exception {
        if (!isValidDirectory(inputDirectory)) {
            System.out.println(BAD_DIRECTORY_MESSAGE);
            return new ArrayList<>();
        }
        if (!isValidDirectory(outputDirectory)) {
            System.out.println(BAD_DIRECTORY_MESSAGE);
            return new ArrayList<>();
        }

        File inputDirectoryFile = new File(inputDirectory);
        File[] inputContents = inputDirectoryFile.listFiles();
        if (inputContents == null) {
            System.out.println(NO_FILES_INVALID_DIRECTORY_MESSAGE);

            // return an empty list
            return new ArrayList<>();
        }

        File outputDirectoryFile = new File(outputDirectory);
        //clearDirectory(outputDirectoryFile);

        List<String> fileContents = new ArrayList<>();

        for (File object : inputContents) {
            if (object.isFile()) {
                String fileName = object.getName();
                System.out.println("\nReading file: " + fileName);

                String content = readFile(object.getAbsolutePath());
                fileContents.add(content);

                String newFilePath = outputDirectoryFile.getAbsolutePath() + File.separator + object.getName();
                File newFile = new File(newFilePath);

                PrintWriter writer = new PrintWriter(newFile);
                writer.println(cypher.encrypt(content));
                writer.flush();

                writer.close();
            } else if (object.isDirectory()) {
                System.out.println("\nEncountered a directory. Skipping: " + object.getName());
            }
        }

        return fileContents;
    }

    /**
     * Checks whether the given directory exists.
     *
     * @param directory the directory.
     *
     * @return true if exists, false otherwise.
     */
    public static boolean directoryExists(String directory)  {
        File file = new File(directory);
        return file.exists() && file.isDirectory();
    }

    /**
     * Deletes all files from the given directory.
     *
     * Can be used by a readAndEncrypt method.
     *
     * @param directory the directory.
     */
    private void clearDirectory(File directory)  {
        File[] files = directory.listFiles();
        for (File f: files)  {
            f.delete();
        }
    }

    /**
     * Is used to test the FileManager class.
     *
     * @param args not used.
     *
     * @throws Exception is used in case of any input/output errors.
     */
    public static void main(String[] args) throws Exception {
        FileManager p = new FileManager();
        List<String> results = p.parse("");
        for (String content : results) {
            System.out.println("File content:");
            System.out.println(content);
        }
    }
}
