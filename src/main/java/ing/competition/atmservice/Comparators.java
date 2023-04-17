package ing.competition.atmservice;

import ing.competition.atmservice.dtos.Task;

import java.util.Collections;
import java.util.Comparator;

public class Comparators {
    public static Comparator<Task> sortByRegionAtmId(){
        return Comparator.comparing(Task::getRegion);
    }
}
