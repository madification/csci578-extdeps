import FileManipulator.InputInterpreter;
import Infrastructure.Sloth;

import java.io.IOException;
import java.util.ArrayList;

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
            ArrayList<Sloth> fileList = InputInterpreter.readInput(inputFile);
            fileList.stream().map(Sloth::getFileName).forEach(System.out::println);
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }


    }
}