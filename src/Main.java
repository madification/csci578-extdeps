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
//            fileLists.extDepsMap.forEach((k, s)-> {
//                System.out.println("\n" + "key: " + k);
//                s.stream().forEach(ss->System.out.println("      val: " + ss));
//            });

            OutputInterpreter out = new OutputInterpreter(fileLists);
            out.generateTxt("output.txt");

        } catch (IOException e) {
            System.out.println(e.getMessage());

        }


    }
}