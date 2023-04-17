package ing.competition.atmservice.services;

import ing.competition.atmservice.Comparators;
import ing.competition.atmservice.dtos.ATM;
import ing.competition.atmservice.dtos.Task;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class AtmServiceImpl implements AtmService{
    public ATM[] calculateOrder(Task[] st){
        Set<ATM> set = new HashSet<>(Arrays.stream(st)
                .map(t -> new ATM(t.getRegion(), t.getAtmId())).toList());
        return set.stream()
                .sorted(Comparators.sortByRegionAtmId())
                .toArray(a -> new ATM[set.size()]);
    }
}
