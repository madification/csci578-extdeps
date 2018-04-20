package FileManipulator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OutputInterpreter {

    InputInfo inputInfo = new InputInfo();

    public OutputInterpreter(InputInfo inputInfo) {
        this.inputInfo = inputInfo;
    }

    public void generateTxt(String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            this.inputInfo.extDepsMap.forEach((k, s) -> {
                try {
                    writer.write("\n" + "key: " + k);
                    writer.newLine();
                    s.stream().forEach(ss -> {
                        try {
                            writer.write("      val: " + ss);
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
