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
    public int usageRatio = 0;
    public int uniqueUsages = 0;
    public int cascadeLevel = 0;
    private boolean uniqueSet = false;
    private boolean totalSet = false;
    public boolean impactCalculated = false;
    public boolean levelCalculated = false;
    public int totalUsages = 0;
    public int immediateUsages = 0;

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
        uniqueSet = true;
    }

    public void setTotalUsages(int totalUsages) {
        this.totalUsages = totalUsages;
        totalSet = true;
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
        this.setImmediateUsages();
    }

    private void setImmediateUsages() {
        this.immediateUsages = this.extDepsList.size();
    }

    public void setScores(int maxUsagesInSystem, int numFilesInSystem) {
        if(dataAcquired()) {
            // ratio of number of usages of "this" vs the most used file in the system
            this.usageRatio = maxUsagesInSystem == 0 ? 0 : 100 * (this.totalUsages / maxUsagesInSystem);
            this.spaghettiScore = this.totalUsages == 0 ? 0 : this.uniqueUsages / this.totalUsages;
            // score percentage of unique files in the system which use "this"
            this.impactScore = this.uniqueUsages / numFilesInSystem;

            // We've now calculated the impact
            this.impactCalculated = true;
        }
    }


    public void setCascadeLevel(int level){
        this.cascadeLevel = level;
        this.levelCalculated = true;
    }

    private boolean dataAcquired(){
        if(totalSet && uniqueSet) return true;

        return false;
    }

    public boolean areLevelsCalculated() {return this.levelCalculated;}

    public boolean isImpactCalculated() {
        if(levelCalculated && impactCalculated){
            return true;
        }
        else return false;
    }
    public String getFileName() {
        return fileName;
    }

}
