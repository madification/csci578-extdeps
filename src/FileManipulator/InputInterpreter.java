package FileManipulator;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class InputInterpreter {

    public static InputInfo inputFiles = new InputInfo();
    private static String skipDepends = "depends";
    private static String[] lineSegments;
    public static HashMap<String, Set<String>> intDepsMap = new HashMap<>();
    public static HashMap<String, Set<String>> extDepsMap = new HashMap<>();


        public static InputInfo readInput(String fileName) throws IOException {
            // read in file line by line
            try {
                File file = new File(fileName);
                BufferedReader reader = new BufferedReader(new FileReader(file));

                //put lines in a list
                List<String> lines = reader.lines().collect(Collectors.toList());

                lines.size();
                // process each line
                for(int lineNum = 0; lineNum < lines.size(); lineNum++) {

                    // extract segments by space
                    lineSegments = lines.get(lineNum).split(" ");

                    // pull out 'depends ' and discard.
                    if (!lineSegments[0].equals(skipDepends)) {
                        throw new IOException("First 8 characters were not 'depends '");
                    }

                    // go through the line segments we just extracted and identify the internal deps
                    // start by pulling the first file as the base, the following are what it depends upon
                    // the key is the base, the set is the base's dependencies
                    // start at index 1 because we don't want 'depends' prefix from the input file
                    intDepsMap.computeIfAbsent(lineSegments[1], o->new HashSet<String>()).add(lineSegments[2]);

                    // Now do the same to get the external dependencies
                    // the second file listed is utilized by the first; the second is depended upon by the first
                    // the key is the file being utilized, the set is the list of files utilizing/importing the key
                    extDepsMap.computeIfAbsent(lineSegments[2], o->new HashSet<String>()).add(lineSegments[1]);

                    inputFiles.extDepsMap = extDepsMap;
                    inputFiles.intDepsMap = intDepsMap;

                }
                reader.close();
            }
            catch (IOException e) {
                System.out.println(e.toString() + " from InputInterpreter or below");
            }

            inputFiles.populateSlothList();

            return inputFiles;
        }
}
