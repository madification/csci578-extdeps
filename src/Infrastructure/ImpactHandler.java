package Infrastructure;

import FileManipulator.InputInfo;
import javafx.util.Pair;

import java.util.*;

public class ImpactHandler {
    private InputInfo inputInfo = new InputInfo();
    private float maxUsageTotal = 0;

    private ArrayList<Sloth> impactList = new ArrayList<>();
    private ArrayList<Sloth> immediateUsageList = new ArrayList<>();

    public ImpactHandler(InputInfo inputInfo) {
        this.inputInfo = inputInfo;
    }

    public ArrayList<Sloth> getImpactList() {
        return impactList;
    }

    public ArrayList<Sloth> getImmediateUsageList() {
        return immediateUsageList;
    }

    public void findMaxUsedFileTotal() {

        // find the file with the highest "total usage"
        inputInfo.allSloths.forEach((fileName, sloth) -> {
            if (sloth.totalUsages > this.maxUsageTotal) {
                this.maxUsageTotal = sloth.totalUsages;
            }
        });
    }


    public float getMaxUsageTotal() {
        return maxUsageTotal;
    }

    /**
     * Calculate the total number of usages as cascaded through system
     * Example:  a is used by b and c and b is used by d and c is used by e, f, g means a has 6 total cascadingExtDeps
     *
     * @param ogSloth sloth to calculate cascadingExtDeps on
     * @return total cascadingExtDeps
     */
    public Pair<Float, Float> calCascadingExtDeps(Sloth ogSloth) {
        Set<String> discovered = new HashSet<>();
        ArrayList<Sloth> xDSlothList = new ArrayList<>();

        // first get the usage score (number of files which use it) of the original sloth (ogSloth)
        float total = ogSloth.immediateUsages;

        // create a list of all the usages without any duplicates (remove circular dependencies)
        // go through the list of external dependencies (file name strings) for each file (sloth) and go get the sloth
        // which matches that string file name
        // iterate through the extDeps list of the input sloth, retrieve sloth objects, and place in this list
        ogSloth.extDepsList.forEach(xDfile -> xDSlothList.add(inputInfo.allSloths.get(xDfile)));

        while (!xDSlothList.isEmpty()) {
            //pull the first sloth off the queue
            Sloth sloth = xDSlothList.remove(0);
            // add it's usage score
            total += sloth.immediateUsages;
            // add it to our list of discovered sloths so we don't loop
            discovered.add(sloth.fileName);

            // for each of it's external dependencies, check if we've discovered them already
            // else add to our queue to explore
            sloth.extDepsList.forEach(s -> {
                if (!discovered.contains(s)) {
                    xDSlothList.add(inputInfo.allSloths.get(s));
                }
            });
            //loop until we pop the last sloth off the queue

        } //end while

        //when we've explored all the sloths that cascading-ly use our og sloth, we will exit the while loop
        // return totalUsages and unique usages for this sloth
        return new Pair<>(total, (float) discovered.size());
    }


    public void getSortedScoreLists(){

        this.immediateUsageList.addAll(inputInfo.allSloths.values());
        this.immediateUsageList.sort(Comparator.comparing(sloth -> sloth.immediateUsages));

        this.impactList.addAll(inputInfo.allSloths.values());
        this.impactList.sort(Comparator.comparing(sloth -> sloth.impactScore));

    }


}
