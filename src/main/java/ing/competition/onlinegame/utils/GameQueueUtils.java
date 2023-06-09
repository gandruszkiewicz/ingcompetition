package ing.competition.onlinegame.utils;

import ing.competition.exceptions.CustomRuntimeException;
import ing.competition.onlinegame.dtos.Group;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameQueueUtils {
    private GameQueueUtils(){}
    public static int getGroupAvailableSlots(Group clansQ, int playerLimit) {
        final int occupancy = clansQ.getOccupancy();
        int result = playerLimit - occupancy;
        if (result < 0) {
            throw new CustomRuntimeException("Group is overloaded !!");
        }
        return result;
    }
}
