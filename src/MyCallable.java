import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Callable;

/*
    * Class: MyCallable
    * Description: This class is used to create a thread that will read a file
    * and count the number of words in the file.
    */
public class MyCallable implements Callable<Integer> {
    private String fileName;// The name of the file to be read


    /*
    *method: MyCallable
    * Description: This is the constructor for the MyCallable class.
    * Parameters: String fileName - the name of the file to be read.
    * Returns: None.
     */
    public MyCallable(String fileName) {
        this.fileName = fileName;
    }

    @Override
    /*
        * Method: call
        * Description: This method is used to read the file and count the number of words in the file.
        * Parameters: None.
        * Returns: Integer - the number of words in the file.

     */
    public Integer call() throws Exception {
        int numOfLines = 0;
        try (Scanner in = new Scanner(new File(fileName))) {
            while (in.hasNextLine()) {
                in.nextLine();
                numOfLines++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return numOfLines;
    }



}
