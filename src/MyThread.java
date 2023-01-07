import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Class: MyThread
// Description: This class is used to create a thread that will read a file
// and count the number of words in the file.
public class MyThread extends Thread {
    private String fileName;// The name of the file to be read
    private int numOfLines;// The number of lines in the file


    // Method: MyThread
    // Description: This is the constructor for the MyThread class.
    // Parameters: String fileName - the name of the file to be read.
    // Returns: None.
    public MyThread(String fileName) {
        this.fileName = fileName;
    }


    /*
        * Method: getFileName
        * Description: This method returns the name of the file.
        * Parameters: None.
        * Returns: String - the name of the file.
     */
    public String getFileName() {
        return fileName;
    }

    @Override
    /*
     * Method: run
     * Description: This method is used to read the file and count the number of words in the file.
     * Parameters: None.
     * Returns: None.
     */
    public void run() {
        try (Scanner in = new Scanner(new File(fileName))) {
            while (in.hasNextLine()) {
                in.nextLine();
                numOfLines++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*
     * Method: getNumOfLines
     * Description: This method returns the number of lines in the file.
     * Parameters: None.
     * Returns: int - the number of lines in the file.
     */
    public int NumberOfLines() {
        return numOfLines;
    }


}
