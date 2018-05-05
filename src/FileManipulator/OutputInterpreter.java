package FileManipulator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OutputInterpreter {

    private InputInfo inputInfo = new InputInfo();

    public OutputInterpreter(InputInfo inputInfo) {
        this.inputInfo = inputInfo;
    }

    public void generateUsageText(String fileName) {

        // edit file name for output
        String outputName = fileName.replaceAll("deps", "externalDeps");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputName));

            // set up a header
            writer.write("--------- External Dependencies of Each File ---------");
            writer.newLine();
            writer.write("Date created: " + dateFormat.format(date));
            writer.newLine();
            writer.write("File Read:  " + fileName);
            writer.newLine();
            writer.newLine();
            writer.newLine();
            writer.write("How to use this file: ");
            writer.newLine();
            writer.write("file_a is used by file_b means file_b imports or includes file_a; file_b depends on file_a");
            writer.newLine();

            // now write the data
            this.inputInfo.extDepsMap.forEach((k, s) -> {
                try {
                    writer.write("------------------------------------------");
                    writer.newLine();
                    writer.write(k);
                    writer.newLine();
                    writer.write("    is used by:");
                    writer.newLine();
                    s.stream().forEach(ss -> {
                        try {
                            writer.write("       " + ss);
                            writer.newLine();
                        } catch (IOException e) {
                            System.out.println(e.toString() + "Error in creation of calibration file.");

                        }
                    });
                } catch (IOException e) {
                    System.out.println(e.toString() + "Error in creation of calibration file.");
                }
            });


            writer.newLine();


            writer.close();


        } catch (IOException e) {
            System.out.println(e.toString() + "Error in creation of calibration file.");
        }
    }

}
