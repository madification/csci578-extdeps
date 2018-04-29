package Infrastructure;

import FileManipulator.InputInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class ImpactHandler {
    InputInfo inputInfo = new InputInfo();

    public ImpactHandler(InputInfo inputInfo) {
        this.inputInfo = inputInfo;
    }

    public int calCascadingExtDeps(Sloth ogSloth) {
        Set<String> toAdd = new HashSet<>();
        ArrayList<Sloth> xDSlothList = new ArrayList<>();
        PriorityQueue<Sloth> slothQueue = new PriorityQueue<>();
        // first get the usage score (number of files which use it) of the original sloth (ogSloth)
        int total = ogSloth.usageScore;

        // create a list of all the usages without any duplicates (remove circular dependencies)
        // go through the list of external dependencies (file name strings) for each file (sloth) and go get the sloth
        // which matches that string file name
        ogSloth.extDepsList.forEach(xDfile -> toAdd.add(inputInfo.allSloths.get(xDfile).fileName));
        // iterate through the extDeps list of the input sloth, retrieve sloth objects, and place in this list
        ogSloth.extDepsList.forEach(xDfile -> xDSlothList.add(inputInfo.allSloths.get(xDfile)));
        // a queue of sloths of all the ext dependencies of the original sloth
        ogSloth.extDepsList.forEach(xDfile -> slothQueue.add(inputInfo.allSloths.get(xDfile)));


        // use this while loop to make sure we get all the way back through the whole spiderweb of ext dependencies
        while (!xDSlothList.isEmpty()) {
            //for each sloth of the extdepslist, add the usage score to the total
            for (int s = 0; s < xDSlothList.size(); s++){
                Sloth nextSloth = xDSlothList.remove(s);
                total += nextSloth.usageScore;

                // add the extDeps of this sloth we just retrieved (this sloth was an extDep itself)
                //TODO the problem with this is we only want to add the file to the xDSlothList if it is not already contained. We can't have duplicates in this list or we'll get infinite loops
//                nextSloth.extDepsList.forEach(xDfile-> xDSlothList.add(inputInfo.allSloths.get(xDfile)));
                // for each filename listed in the extDepsList for the current sloth, see if xDSlothList already contains it and if not retrieve the sloth object
                for(int i = 0; i < nextSloth.extDepsList.size(); i++){
                    if(!xDSlothList.contains(inputInfo.allSloths.get(nextSloth.extDepsList.get(i)))){
                        xDSlothList.add(inputInfo.allSloths.get(nextSloth.extDepsList.get(i)));
                    }
                }
            }
        }

//            // grab the first sloth off the queue
//            Sloth nextSloth = slothQueue.remove();
//            // add each of the sloth's ext deps if they're not already in the queue
//
//            if (!toAdd.contains(nextSloth)) {
//                nextSloth.extDepsList.forEach(xDfile -> toAdd.add(inputInfo.allSloths.get(xDfile).fileName));
//            }

        // for every sloth in extDepsList, add the usageScore


        // for every sloth in toAdd, add the usage score to total
//        toAdd.forEach(s -> total += inputInfo.allSloths.get(s).usageScore);
//        total += inputInfo.allSloths.get(s).usageScore;

        return total;
    }
}
