package FileManipulator;

import Infrastructure.Sloth;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InputInterpreter {

    public static ArrayList<Sloth> inputFiles = new ArrayList<>();
    private static String skipDepends = "depends";
    private static String lineRead;
    private static String[] lineSegments;


        public static ArrayList<Sloth> readInput(String fileName) throws IOException {


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
                    // start at index 1 because we don't want 'depends '
                    ArrayList<Sloth> currList = new ArrayList<>();
                    Sloth currFile = new Sloth(lineSegments[1], currList);
                    for (int curr = 2; curr < lineSegments.length ; curr++) {
                        //TODO identify if nextFile has already been found and thus has a populated intDepsList
                        Sloth nextFile = new Sloth(lineSegments[curr], null);
                        currList.add(nextFile);
                    }

                    inputFiles.add(currFile);

                }
                reader.close();
            }
            //TODO figure out how the exception thrown in if above could affect this
            catch (IOException e) {
                System.out.println(e.toString() + "Could not find input.txt file.");
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
