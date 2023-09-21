package ie.atu.sw;

/**
 * Is used to setup the app before allowing
 * the user to encrypt or decrypt the files.
 */
public class Setup {
    private String inputDirectoryPath;
    private String outputDirectoryPath;
    private boolean hasKey;

    /**
     * Creates an empty setup.
     */
    public Setup()  {
        this.inputDirectoryPath = "";
        this.outputDirectoryPath = "";
        this.hasKey = false;
    }

    /**
     * Returns true if the user is now allowed to encrypt and decrypt files.
     *
     * @return true if the setup has been finished.
     */
    public boolean isSetup()  {
        return hasInputDirectory() && hasOutputDirectory() && this.hasKey();
    }

    /**
     * Returns true if the key has been configured.
     *
     * @return true if the key has been configured, false otherwise.
     */
    public boolean hasKey() {
        return hasKey;
    }

    /**
     * Updates the key status.
     *
     * @param hasKey the new status.
     */
    public void setHasKey(boolean hasKey) {
        this.hasKey = hasKey;
    }

    /**
     * Returns true if the input directory has been configured.
     *
     * @return true if the input directory has been configured, false otherwise.
     */
    public boolean hasInputDirectory()  {
        return this.inputDirectoryPath != null &&
                !this.inputDirectoryPath.isEmpty();
    }

    /**
     * Returns true if the output directory has been configured.
     *
     * @return true if the output directory has been configured, false otherwise.
     */
    public boolean hasOutputDirectory()  {
        return this.outputDirectoryPath != null &&
                !this.outputDirectoryPath.isEmpty();
    }

    /**
     * Returns the input directory.
     *
     * @return the input directory.
     */
    public String getInputDirectoryPath() {
        return inputDirectoryPath;
    }

    /**
     * Returns the output directory.
     *
     * @return the output directory.
     */
    public String getOutputDirectoryPath() {
        return outputDirectoryPath;
    }


    /**
     * Updates the input directory.
     *
     * @param inputDirectoryPath the new input directory.
     */
    public void setInputDirectoryPath(String inputDirectoryPath) {
        this.inputDirectoryPath = inputDirectoryPath;
    }

    /**
     * Updates the output directory.
     *
     * @param outputDirectoryPath the new output directory.
     */
    public void setOutputDirectoryPath(String outputDirectoryPath) {
        this.outputDirectoryPath = outputDirectoryPath;
    }
}
