package Infrastructure;

import FileManipulator.InputInfo;
import javafx.util.Pair;

import java.util.*;

public class ImpactHandler {
    InputInfo inputInfo = new InputInfo();
    private float maxUsageTotal = 0;

    public ImpactHandler(InputInfo inputInfo) {
        this.inputInfo = inputInfo;
    }

    public void findMaxUsedFileTotal() {

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











    // We'll no longer be using these
//
//    @Deprecated
//    public int findDeepestLevel(ArrayList<Pair<String, Double>> list) {
//        Set<Double> levelNums = new HashSet<>();
//        list.forEach(p -> levelNums.add(p.getValue()));
//
//        if(levelNums.isEmpty()) return 0;
//        return levelNums.stream().max(Comparator.comparingInt(Double::doubleValue)).get();
//    }
//
//    /**
//     * Calculate the levels of cascading extDeps
//     * Example:  a is used by b and c and b is used by d and c is used by e, f, g means a has 2 levels of extDepsLevels
//     *
//     * @param ogSloth
//     * @return
//     */
//    @Deprecated
//    public int getDepth(Sloth ogSloth, ArrayList<Sloth> discovered){
//        int depth = 0;
//        if(ogSloth.extDepsList.isEmpty()) return 1;
//
//        if(discovered.contains(ogSloth)) return 0;
//
//        for(String child : ogSloth.extDepsList){
//            ArrayList<Sloth> updatedDiscovered = new ArrayList<>(discovered);
//            updatedDiscovered.add(ogSloth);
//            depth = Math.max(depth, getDepth(inputInfo.allSloths.get(child), updatedDiscovered));
//        }
//
//        System.out.println(depth + " : " + ogSloth.getFileName());
//
//        return depth+1;
//    }
//
//
//    @Deprecated
//    public void calcExtDepsLevels(Sloth ogSloth) {
//        int maxLevel = 0;
//        // create a list that has the filename and level
//        ArrayList<Pair<String, Double>> levelNames = new ArrayList<>();
//        // keep track of those we've discovered
//        Set<Pair<String, Double>> discovered = new HashSet<>();
//        ArrayList<Pair<String, Double>> all = new ArrayList<>();
//        // put all the extDeps of og sloth, which area all at level 1
//        ogSloth.extDepsList.forEach(n -> levelNames.add(new Pair<>(n, 1.0)));
//
//        while (!levelNames.isEmpty()) {
//            //pull the first sloth off the queue
//            Pair<String, Double> pair = levelNames.remove(0);
//
//            // add it to our list of discovered sloths so we don't loop
////            discovered.add(pair);
//            all.add(pair);
//
//            // for each of it's external dependencies, check if we've discovered them already
//            // else add to our queue to explore
//            inputInfo.allSloths.get(pair.getKey()).extDepsList.forEach(s -> {
////                if(!discovered.contains(s)){
//                double newLevel = pair.getValue() + 1;
//                levelNames.add(new Pair<>(s, newLevel));
//
////                }
////                else System.out.println("Circular dependency discovered.");
//            });
//
//            //loop until we pop the last sloth off the queue
//        }
//        maxLevel = findDeepestLevel(all);
//        ogSloth.setCascadeLevel(maxLevel);
//    }
}
