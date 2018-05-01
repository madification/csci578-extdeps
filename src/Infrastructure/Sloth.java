package Infrastructure;


import java.util.ArrayList;
import java.util.Set;

public class Sloth {

    public String fileName;
    // files which use this file
    public ArrayList<String> extDepsList = new ArrayList<>();
    // files which this file uses/imports/depends upon
    public ArrayList<String> intDepsList = new ArrayList<>();
    // level of potential impact change to this file could have
    public int impactScore = 0;
    public boolean impactCalculated = false;
    //number of external dependencies
    public int usageScore = 0;

    public Sloth(String fileName, ArrayList<String> intDepsList){
        this.fileName = fileName;
        this.intDepsList = intDepsList;
    }

    public static Sloth createSloth(String fileName, Set<String> intDepsSet, Set<String> extDepsSet){
        ArrayList<String> intDList = new ArrayList<>();
        ArrayList<String> extDList = new ArrayList<>();
        if (intDepsSet != null && !intDepsSet.isEmpty()){
            intDepsSet.forEach(s -> intDList.add(s));
        }
        else System.out.println(fileName + " does not use any other files");
        if (extDepsSet != null && !extDepsSet.isEmpty()){
            extDepsSet.forEach(s -> extDList.add(s));
        }
        else System.out.println(fileName + " is not used by any other file.");

        Sloth newSloth = new Sloth(fileName, intDList);
        newSloth.setExtDepsList(extDList);
        return newSloth;
    }

    public void setExtDepsList(ArrayList<String> extDepsList) {
        this.extDepsList = extDepsList;
        this.calculateUsageScore();
    }

    public boolean isImpactCalculated() {
        return impactCalculated;
    }

    public void setImpactScore(int score) {
        this.impactScore = score;
        impactIsCalculated();
    }

    private void impactIsCalculated(){
        this.impactCalculated = true;
    }

    private void calculateUsageScore(){
        this.usageScore = extDepsList.size();
    }

    public String getFileName() {
        return fileName;
    }
}
