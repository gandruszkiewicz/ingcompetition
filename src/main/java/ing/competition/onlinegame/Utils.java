package ing.competition.onlinegame;

import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.queue.GameQueue;

import java.util.*;

public class Utils {
    public static Clan[] sortByClanFactor(Clan[] clans){
        return Arrays.stream(clans)
                .sorted(Comparators.sortByClanFactor())
                .toArray(Clan[]::new);
    }
    public static Clan[] sortByNumberOfPlayers(Clan[] clanStats){
        return Arrays.stream(clanStats)
                .sorted(Comparators.sortByNumberOfPlayers())
                .toArray(c -> new Clan[clanStats.length]);
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
    public static Optional<Clan> getElementByNumberOfPlayers(GameQueue<Clan> clanStatsQ, GameQueue<Clan> recentlyAddedQ,
                                                             int numberOfPlayers){
        return clanStatsQ.stream().filter(e -> e.getNumberOfPlayers() == numberOfPlayers
                && !recentlyAddedQ.contains(e)).findFirst();
    }
    public static boolean hasBiggerClan(GameQueue<Clan> clanStatsQ, int numberOfPlayers){
        return clanStatsQ.stream().anyMatch(e -> e.getNumberOfPlayers() >= numberOfPlayers);
    }
    public static boolean hasMaxClan(GameQueue<Clan> clanStatsQ, int numberOfPlayers){
        return clanStatsQ.stream().anyMatch(e -> e.getNumberOfPlayers() == numberOfPlayers);
    }
}
