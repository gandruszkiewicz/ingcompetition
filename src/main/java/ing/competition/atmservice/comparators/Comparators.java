package ing.competition.atmservice.comparators;

import ing.competition.atmservice.dtos.ATM;
import java.util.Comparator;

public class Comparators {
    public static Comparator<ATM> sortByRegionAtmId(){
        return Comparator.comparing(ATM::getRegion)
                .thenComparing(ATM::getAtmId);
    }
}
