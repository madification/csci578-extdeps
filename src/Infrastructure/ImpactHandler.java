package Infrastructure;

import FileManipulator.InputInfo;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class ImpactHandler {
    InputInfo inputInfo = new InputInfo();
    public ImpactHandler(InputInfo inputInfo) {
        this.inputInfo = inputInfo;
    }

    public int calCascadingExtDeps(Sloth sloth) {
        Set<String> toAdd = new HashSet<>();
        PriorityQueue<Sloth> slothQueue = new PriorityQueue<>();
        int total = sloth.usageScore;

        // create a list of all the usages without any duplicates (remove circular dependencies)
        // go through the list of external dependencies (file name strings) for each file (sloth) and go get the sloth
        // which matches that string file name
        sloth.extDepsList.forEach(xDfile -> toAdd.add(inputInfo.allSloths.get(xDfile).fileName));
        sloth.extDepsList.forEach(xDfile -> slothQueue.add(inputInfo.allSloths.get(xDfile));
        while(!slothQueue.isEmpty()){
            Sloth nextSloth = slothQueue.remove();
            if(!toAdd.contains(nextSloth)){
                nextSloth.extDepsList.forEach(xDfile -> toAdd.add(inputInfo.allSloths.get(xDfile).fileName));
            }

        }

        // for every sloth in extDepsList, add the usageScore


        // for every sloth in toAdd, add the usage score to total
        toAdd.forEach(s -> total += inputInfo.allSloths.get(s).usageScore);
//        total += inputInfo.allSloths.get(s).usageScore;

        return total;
    }
}
