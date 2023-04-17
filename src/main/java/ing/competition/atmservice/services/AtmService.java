package ing.competition.atmservice.services;

import ing.competition.atmservice.dtos.ATM;
import ing.competition.atmservice.dtos.Task;

import java.util.List;

public interface AtmService {
    ATM[] calculateOrder(Task[] st);
}
