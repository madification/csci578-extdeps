package FileManipulator;

import java.util.HashMap;
import java.util.Set;

public class InputInfo {

    public String inputFilePath = new String();
    public HashMap<String, Set<String>> intDepsMap = new HashMap<>();
    public HashMap<String, Set<String>> extDepsMap = new HashMap<>();

        public InputInfo() {
        }

        public void setInputFilePath(String inputFilePath){
            this.inputFilePath = inputFilePath;
        }
    }


