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
    private int spaghettiScore = 0;
    public int uniqueUsages = 0;
    public int cascadeLevels = 0;
    public boolean impactCalculated = false;
    public boolean levelsCalculated = false;
    //number of external dependencies
    public int totalUsages = 0;

    /**
     * Constructor
     * @param fileName string name of the file
     * @param intDepsList list of dependencies: files used by the file
     */
    public Sloth(String fileName, ArrayList<String> intDepsList){
        this.fileName = fileName;
        this.intDepsList = intDepsList;
    }


    public void setUniqueUsages(int uniqueUsages) {
        this.uniqueUsages = uniqueUsages;
    }

    public void setTotalUsages(int totalUsages) {
        this.totalUsages = totalUsages;
    }


    public static Sloth createSloth(String fileName, Set<String> intDepsSet, Set<String> extDepsSet){
        ArrayList<String> intDList = new ArrayList<>();
        ArrayList<String> extDList = new ArrayList<>();
        if (intDepsSet != null && !intDepsSet.isEmpty()){
            intDList.addAll(intDepsSet);
        }
        else System.out.println(fileName + " does not use any other files");
        if (extDepsSet != null && !extDepsSet.isEmpty()){
            extDList.addAll(extDepsSet);
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

    private void calculateUsageScore(){
        this.totalUsages = extDepsList.size();
    }

    public boolean isImpactCalculated() {
        if(levelsCalculated && impactCalculated){
            return true;
        }
        else return false;
    }

    public void setScores(int maxUsagesInSystem) {
        // score = percentage of files in the whole system which use "this"
        this.impactScore = maxUsagesInSystem == 0 ? 0 : 100*(this.totalUsages/maxUsagesInSystem);
        this.spaghettiScore = this.totalUsages == 0 ? 0 : uniqueUsages/totalUsages;
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

    public String getFileName() {
        return fileName;
    }

}
