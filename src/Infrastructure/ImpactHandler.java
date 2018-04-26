package Infrastructure;

import FileManipulator.InputInfo;

import java.util.HashSet;
import java.util.Set;

public class ImpactHandler {
    InputInfo inputInfo = new InputInfo();
    public ImpactHandler(InputInfo inputInfo) {
        this.inputInfo = inputInfo;
    }

    public int calCascadingExtDeps(Sloth sloth) {
        Set<String> toAdd = new HashSet<>();
        int total = sloth.usageScore;
        // for every sloth in extDepsList, add the usageScore
        sloth.extDepsList.forEach(s -> toAdd.);

        // for every sloth in toAdd, add the usage score to total
        toAdd.forEach(s -> total += inputInfo.allSloths.get(s).usageScore);
//        total += inputInfo.allSloths.get(s).usageScore;

        return total;
    }
}
