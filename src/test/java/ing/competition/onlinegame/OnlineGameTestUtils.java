package ing.competition.onlinegame;

import ing.competition.onlinegame.comparators.Comparators;
import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.dtos.Group;

import java.util.List;

public class OnlineGameTestUtils {
    public static boolean evaluateGroup(Group group){
        List<Clan> groupClans = group.getClanList();
        List<Clan> clansOrdered = group.stream()
                .sorted(Comparators.sortClanByClanFactorDesc())
                .toList();
        boolean isPositive = true;
        for (int index = 0; index < clansOrdered.size(); index++) {
            Clan clanOrdered = clansOrdered.get(index);
            Clan groupClan = groupClans.get(index);
            if(!clanOrdered.equals(groupClan)){
                isPositive = false;
                break;
            }
        }
        return isPositive;
    }
}
