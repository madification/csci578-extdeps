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
    public int cascadeLevels = 0;
    public boolean impactCalculated = false;
    public boolean levelsCalculated = false;
    //number of external dependencies
    public int usageScore = 0;

    /**
     * Constructor
     * @param fileName string name of the file
     * @param intDepsList list of dependencies: files used by the file
     */
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
        if(levelsCalculated && impactCalculated){
            return true;
        }
        else return false;
    }

    public void setImpactScore(int totalUsage, int numSystemFiles) {
        // score = percentage of files in the whole system which use "this"
        this.impactScore = 100*(totalUsage/numSystemFiles);
        impactIsCalculated();
    }

    public void setCascadeLevels(int levels){
        this.cascadeLevels = levels;
        levelsAreCalculated();
    }

    private void levelsAreCalculated() {this.levelsCalculated = true;}

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
