package FileManipulator;

import Infrastructure.Sloth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class InputInfo {

    public String inputFilePath = new String();
    public HashMap<String, Set<String>> intDepsMap = new HashMap<>();
    public HashMap<String, Set<String>> extDepsMap = new HashMap<>();
    public HashMap<String, Sloth> allSloths = new HashMap<>();

        public InputInfo() {
        }

        public void setInputFilePath(String inputFilePath){
            this.inputFilePath = inputFilePath;
        }

        //TODO how do I confirm that every file is in here exactly once?
        public void populateSlothList(){
            //for every file that is used by another, create a sloth and put it in a list
            this.extDepsMap.forEach((key,set) -> allSloths.put(key, Sloth.createSloth(key, intDepsMap.get(key), set)));
//            allSloths.computeIfAbsent(this.intDepsMap.forEach((k, s) -> allSloths.put(k, Sloth.createSloth(k, this.intDepsMap.get(k), null))) );
        } //TODO should add a computeIfAbsent on the intDepsList to make sure, if any files are not used by another, they still make it into this list
    }


