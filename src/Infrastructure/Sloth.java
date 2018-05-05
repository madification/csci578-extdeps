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
    public float impactScore = 0;
    public float spaghettiScore = 0;
    public float usageRatio = 0;
    public float uniqueUsages = 0;
    public float totalUsages = 0;
    public float immediateUsages = 0;
    private boolean uniqueSet = false;
    private boolean totalSet = false;
    public boolean impactCalculated = false;
    public boolean levelCalculated = false;

    // gui settings
    public int radius;
    public int xpos;
    public int ypos;

    /**
     * Constructor
     * @param fileName string name of the file
     * @param intDepsList list of dependencies: files used by the file
     */
    public Sloth(String fileName, ArrayList<String> intDepsList){
        this.fileName = fileName;
        this.intDepsList = intDepsList;
    }


    // This is a list of files which directly use the file of interest. They are immediate usages.
    public void setExtDepsList(ArrayList<String> extDepsList) {
        this.extDepsList = extDepsList;
        this.setImmediateUsages();
    }

    // Unique usages are cascading usages but w/o repeats. Duplicates on different branches will not come through.
    // Example: a is used by b and c; b is used by c and d; c is used by d, e, and f, but a's unique usages = 5: b, c, d, e, f
    public void setUniqueUsages(float uniqueUsages) {
        this.uniqueUsages = uniqueUsages;
        uniqueSet = true;
    }

    // Total usages counts every node in the tree
    public void setTotalUsages(float totalUsages) {
        this.totalUsages = totalUsages;
        totalSet = true;
    }

    /**
     * Consider this a slothBuilder.
     *
     * @param fileName String file name
     * @param intDepsSet Set of internal dependencies (what does the file use)
     * @param extDepsSet Set of external dependencies (what uses the file)
     * @return a sloth object containing all file information
     */
    public static Sloth createSloth(String fileName, Set<String> intDepsSet, Set<String> extDepsSet){
        ArrayList<String> intDList = new ArrayList<>();
        ArrayList<String> extDList = new ArrayList<>();
        if (intDepsSet != null && !intDepsSet.isEmpty()){
            intDList.addAll(intDepsSet);
        }
//        else System.out.println(fileName + " does not use any other files");
        if (extDepsSet != null && !extDepsSet.isEmpty()){
            extDList.addAll(extDepsSet);
        }
//        else System.out.println(fileName + " is not used by any other file.");

        Sloth newSloth = new Sloth(fileName, intDList);
        newSloth.setExtDepsList(extDList);
        return newSloth;
    }


    // Immediate usages are like immediate relatives. These files directly import the file of interest
    private void setImmediateUsages() {
        this.immediateUsages = this.extDepsList.size();
    }


    /**
     * setScores calculates the information used to determine the potential impact a change to the file
     * of interest could have on the system.
     *
     * @param maxUsagesInSystem The largest 'totalUsage' value of all sloths/files
     * @param numFilesInSystem The total number of unique files in the system
     */
    public void setScores(float maxUsagesInSystem, float numFilesInSystem) {
        if(dataAcquired()) {
            // ratio of number of usages of "this" vs the most used file in the system
            this.usageRatio = maxUsagesInSystem == 0 ? 0 : 100 * (this.totalUsages / maxUsagesInSystem);
            // Spaghetti score is the ratio of how many uniqueUsages vs totalUsages.
            // This shows "you have this many unique files, but this many total usages"
            // aka how many connections between each of the unique files thus making a giant spaghetti diagram!
            this.spaghettiScore = this.totalUsages == 0 ? 0 : 100*(this.uniqueUsages / this.totalUsages);
            // score percentage of unique files in the system which use "this"
            this.impactScore = 100 * (this.uniqueUsages / numFilesInSystem);

            // We've now calculated the potential impact a change to 'this' could have on the system
            this.impactCalculated = true;
        }
    }

    public void prepareForPlotting(float xscale, float yscale){

            // this way every circle has a minimum radius of at least 5
            this.radius = (int)this.immediateUsages + 20;
            this.xpos = Math.round(xscale * this.impactScore);
            this.ypos = Math.round(yscale * this.spaghettiScore);
        }


    private boolean dataAcquired(){
        if(totalSet && uniqueSet) return true;

        return false;
    }

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
