package Infrastructure;


import java.util.ArrayList;

public class Sloth {

    public String fileName;
    public int numExtDeps = 0;
    public ArrayList<Sloth> extDepsList = new ArrayList<>();
    public ArrayList<Sloth> intDepsList = new ArrayList<>();
    public int impactScore = 0;
    public boolean impactCalculated = false;

    public Sloth(String fileName, ArrayList<Sloth> intDepsList){
        this.fileName = fileName;
        this.intDepsList = intDepsList;
    }

    public void setExtDepsList(ArrayList<Sloth> extDepsList) {
        this.extDepsList = extDepsList;
        this.numExtDeps = extDepsList.size();
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

    public String getFileName() {
        return fileName;
    }
}
