import FileManipulator.InputInfo;
import FileManipulator.InputInterpreter;
import FileManipulator.OutputInterpreter;

import java.io.IOException;

public class Main {

    /**
     *
     *
     * @param args
     */
    public static void main(String args[]) {
        // inputfile must be passed in as first argument
        String inputFile = args[0];
        try {
            InputInfo fileLists = InputInterpreter.readInput(inputFile);
            fileLists.setInputFilePath(args[0]);

            OutputInterpreter out = new OutputInterpreter(fileLists);
            out.generateTxt("output.txt"); //TODO figure out how to use the file path in inputInfo to select the location to save output

        } catch (IOException e) {
            System.out.println(e.getMessage());

        }


    }
}