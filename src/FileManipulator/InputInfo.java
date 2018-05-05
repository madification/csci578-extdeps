package FileManipulator;

import Infrastructure.Sloth;

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

        // get a hashmap of all sloths with the file name as key and sloth as value
        public void populateSlothList(){
            //for every file that is used by another, create a sloth and put it in a list
            this.extDepsMap.forEach((key,set) -> {
                Sloth sloth = Sloth.createSloth(key, intDepsMap.get(key), set);
                allSloths.put(key, sloth);
            });
            this.intDepsMap.forEach((key, set) -> {
                allSloths.computeIfAbsent(key, o-> allSloths.put(key, Sloth.createSloth(key, set, extDepsMap.get(key))));
            } );
        }
    }


