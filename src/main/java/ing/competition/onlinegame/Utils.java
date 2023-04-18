package ing.competition.onlinegame;

import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.dtos.ClanStats;
import ing.competition.onlinegame.dtos.Group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Utils {
    public static ClanStats[] getClanStats(Clan[] clans){
        return Arrays.stream(clans)
                .map(ClanStats::new)
                .toArray(c -> new ClanStats[clans.length]);
    }
    public static ClanStats[] sortByClanFactor(ClanStats[] clanStats){
        return Arrays.stream(clanStats)
                .sorted(Comparators.sortByClanFactor())
                .toArray(c -> new ClanStats[clanStats.length]);
    }
    public static int getGroupAvailableSlots(List<Clan> groupClans, int playerLimit){
        final int occupancy = groupClans.stream()
                .map(Clan::getNumberOfPlayers)
                .mapToInt(np -> np)
                .sum();
        int result = playerLimit - occupancy;
        if(result < 0){
            throw new RuntimeException("Group is overloaded !!");
        }
        return result;
    }
    public static boolean isSomeNonInserted(List<ClanStats> clanStats){
        return clanStats.stream().anyMatch(c -> !c.isInserted());
    }
}
