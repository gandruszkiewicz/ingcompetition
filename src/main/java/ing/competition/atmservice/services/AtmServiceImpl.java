package ing.competition.atmservice.services;

import ing.competition.atmservice.dtos.ATM;
import ing.competition.atmservice.dtos.RequestType;
import ing.competition.atmservice.dtos.Task;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
public class AtmServiceImpl implements AtmService{
    public ATM[] calculateOrder(Task[] st){
        Set<ATM> set = new HashSet<>(Arrays.stream(st)
                .map(t -> new ATM(t.getRegion(), t.getAtmId())).toList());
        ATM[] result = set.stream().sorted(Comparator.comparing(ATM::getRegion)
                .thenComparing(ATM::getAtmId)).toArray(a -> new ATM[set.size()]);
        return result;
    }
}
