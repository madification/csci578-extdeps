package FileManipulator;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class InputInterpreter {

    public static InputInfo inputFiles = new InputInfo();
    private static String skipDepends = "depends";
    private static String lineRead;
    private static String[] lineSegments;
    public static HashMap<String, Set<String>> intDepsMap = new HashMap<>();
    public static HashMap<String, Set<String>> extDepsMap = new HashMap<>();


        public static InputInfo readInput(String fileName) throws IOException {


            try {
                File file = new File(fileName);
                BufferedReader reader = new BufferedReader(new FileReader(file));

                List<String> lines = reader.lines().collect(Collectors.toList());

                lines.size();
                // process deps.rsf file line by line
                for(int lineNum = 0; lineNum < lines.size(); lineNum++) {
                    // read full line
//                    lineRead = reader.readLine();
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
            //TODO figure out how the exception thrown in if above could affect this
            catch (IOException e) {
                System.out.println(e.toString() );
            }

            return inputFiles;

        }

        public static double[] readCalibration(String fileName) {
            double[] timesArray = new double[26];

            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));

                for(int i = 0; i < 26; i++){
                    timesArray[i] = Double.parseDouble(reader.readLine());
                }

                reader.close();

            } catch (IOException e) {
                System.out.println(e.toString() + "Could not find calibration.txt file.");
            }

            return timesArray;
        }



//        public static void createOutput(OutputInfo outputInfo, String fileName) {
//
//            try {
//                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
//
//                writer.write(convertColumn(outputInfo.selectedLocation.columnLocation+1));
//                writer.write(Integer.toString(outputInfo.selectedLocation.rowLocation+1));
//                writer.newLine();
//                for (int row = 0; row < outputInfo.boardDimensions; row++) {
//                    for (int column = 0; column < outputInfo.boardDimensions; column++) {
//                        if (outputInfo.outputArrangement[row][column] == -1) {
//                            // cell was empty, convert back to * for HW conventions
//                            writer.write("*");
//                        }
//                        else {
//                            // Place in file
//                            writer.write(Integer.toString(outputInfo.outputArrangement[row][column]));
//                        }
//                    }
//                    writer.newLine();
//                }
//
//                writer.close();
//
//
//            } catch (IOException e) {
//                System.out.println(e.toString() + "Error in creation of output file.");
//            }
//        }
//
//
//    }

}
