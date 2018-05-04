package Infrastructure;

import FileManipulator.InputInfo;
import javafx.util.Pair;

import java.util.*;

public class ImpactHandler {
    InputInfo inputInfo = new InputInfo();
    int maxUsageTotal = 0;

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

    public int findDeepestLevel(ArrayList<Pair<String, Integer>> list) {
        Set<Integer> levelNums = new HashSet<>();
        list.forEach(p -> levelNums.add(p.getValue()));

        if(levelNums.isEmpty()) return 0;
        return levelNums.stream().max(Comparator.comparingInt(Integer::intValue)).get();
    }

    public int getMaxUsageTotal() {
        return maxUsageTotal;
    }

    /**
     * Calculate the total number of usages as cascaded through system
     * Example:  a is used by b and c and b is used by d and c is used by e, f, g means a has 6 total cascadingExtDeps
     *
     * @param ogSloth sloth to calculate cascadingExtDeps on
     * @return total cascadingExtDeps
     */
    public Pair<Integer, Integer> calCascadingExtDeps(Sloth ogSloth) {
        Set<String> discovered = new HashSet<>();
        ArrayList<Sloth> xDSlothList = new ArrayList<>();

        // first get the usage score (number of files which use it) of the original sloth (ogSloth)
        int total = ogSloth.totalUsages;

        // create a list of all the usages without any duplicates (remove circular dependencies)
        // go through the list of external dependencies (file name strings) for each file (sloth) and go get the sloth
        // which matches that string file name
        // iterate through the extDeps list of the input sloth, retrieve sloth objects, and place in this list
        ogSloth.extDepsList.forEach(xDfile -> xDSlothList.add(inputInfo.allSloths.get(xDfile)));
//        ogSloth.extDepsList.forEach(xDfile -> slothQueue.add(inputInfo.allSloths.get(xDfile)));

        while (!xDSlothList.isEmpty()) {
            //pull the first sloth off the queue
            Sloth sloth = xDSlothList.remove(0);
            // add it's usage score
            total += sloth.totalUsages;
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
        }

//        // use this while loop to make sure we get all the way back through the whole spiderweb of ext dependencies
//        while (!xDSlothList.isEmpty()) {
//            //for each sloth of the extdepslist, add the usage score to the total
//            for (int s = 0; s < xDSlothList.size(); s++){
//                Sloth nextSloth = xDSlothList.get(s);
//                total += nextSloth.totalUsages;
//
//                // add the extDeps of this sloth we just retrieved (this sloth was an extDep itself)
//                // We can't have duplicates in this list or we'll get infinite loops
//                // for each filename listed in the extDepsList for the current sloth, see if xDSlothList already contains it and if not retrieve the sloth object
//                for(int i = 0; i < nextSloth.extDepsList.size(); i++){
//                    if(!xDSlothList.contains(inputInfo.allSloths.get(nextSloth.extDepsList.get(i))) && !discovered.contains(nextSloth.extDepsList.get(i))){
//                        xDSlothList.add(inputInfo.allSloths.get(nextSloth.extDepsList.get(i)));
//                        discovered.add(nextSloth.extDepsList.get(i));
//                    }
//                }
//                xDSlothList.remove(s);
//            }
//        }

        //when we've explored all the sloths that cascading-ly use our og sloth, we will exit the while loop
        return new Pair<>(total, discovered.size());
    }

    /**
     * Calculate the levels of cascading extDeps
     * Example:  a is used by b and c and b is used by d and c is used by e, f, g means a has 2 levels of extDepsLevels
     *
     * @param ogSloth
     * @return
     */
    public int getDepth(Sloth ogSloth, ArrayList<Sloth> discovered){
        int depth = 0;
        if(ogSloth.extDepsList.isEmpty()) return 1;

        if(discovered.contains(ogSloth)) return 0;

        for(String child : ogSloth.extDepsList){
            ArrayList<Sloth> updatedDiscovered = new ArrayList<>(discovered);
            updatedDiscovered.add(ogSloth);
            depth = Math.max(depth, getDepth(inputInfo.allSloths.get(child), updatedDiscovered));
        }

        System.out.println(depth + " : " + ogSloth.getFileName());

        return depth+1;
    }


    public void calcExtDepsLevels(Sloth ogSloth) {
        int maxLevel = 0;
        // create a list that has the filename and level
        ArrayList<Pair<String, Integer>> levelNames = new ArrayList<>();
        // keep track of those we've discovered
        Set<Pair<String, Integer>> discovered = new HashSet<>();
        ArrayList<Pair<String, Integer>> all = new ArrayList<>();
        // put all the extDeps of og sloth, which area all at level 1
        ogSloth.extDepsList.forEach(n -> levelNames.add(new Pair<>(n, 1)));

        while (!levelNames.isEmpty()) {
            //pull the first sloth off the queue
            Pair<String, Integer> pair = levelNames.remove(0);

            // add it to our list of discovered sloths so we don't loop
//            discovered.add(pair);
            all.add(pair);

            // for each of it's external dependencies, check if we've discovered them already
            // else add to our queue to explore
            inputInfo.allSloths.get(pair.getKey()).extDepsList.forEach(s -> {
//                if(!discovered.contains(s)){
                int newLevel = pair.getValue() + 1;
                levelNames.add(new Pair<>(s, newLevel));

//                }
//                else System.out.println("Circular dependency discovered.");
            });

            //loop until we pop the last sloth off the queue
        }
        maxLevel = findDeepestLevel(all);
        ogSloth.setCascadeLevel(maxLevel);
    }


//        int deepestLevel = 0;
//        int currLevel = 0;
//        while (!levelNames.isEmpty()){
//            //TODO can I use DFS for this? Just the max search through all the nodes...
//
//            for(int i = 0; i < levelNames.size(); i++) {
////                if(deepestLevel < currLevel){
////                    deepestLevel = currLevel; //TODO this isn't going to work because Java is by reference not value.
////                }
//                Sloth nextSloth = inputInfo.allSloths.get(levelNames.get(i));
//                if (!nextSloth.extDepsList.isEmpty()) {
//                    deepestLevel++;
//                    // TODO the problem with this is that you could go down a shorter branch. The next sloth in this level of the list could go further back
////                    levelNames.removeAll(levelNames);
//                    levelNames.remove(i);
//                    levelNames.addAll(nextSloth.extDepsList);
//                    break;
//                }
//            }
//            levels += deepestLevel;
//
//        }
//
//        return levels;
//    }
//
//
//    public int getLevelCount(Sloth sloth){
//        int numLevels = 0;
//
//        while(!sloth.extDepsList.isEmpty()){
//            numLevels++;
//        }
//        return numLevels;
//    }

}
