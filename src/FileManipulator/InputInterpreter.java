package FileManipulator;

import java.io.*;

public class InputInterpreter {



        public static InputInfo readInput(String fileName) {
            InputInfo inputInfo = new InputInfo();

            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));

                inputInfo.boardDimensions = Integer.parseInt(reader.readLine());
                for (int row = 0; row < inputInfo.boardDimensions; row++) {
                    String[] lineRead = reader.readLine().split("");

                    for (int column = 0; column < inputInfo.boardDimensions; column++) {
                        if (lineRead[column].equals("*")) {
                            // Cell was empty; increment count and convert to usable -1
                            inputInfo.emptyCells++;

                        }
                        else {
                            System.out.println("placeholder");
                        }
                    }
                }


                reader.close();
            } catch (IOException e) {
                System.out.println(e.toString() + "Could not find input.txt file.");
            }

            return inputInfo;
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
