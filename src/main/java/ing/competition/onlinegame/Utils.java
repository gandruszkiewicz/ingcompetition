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
    public static int getGroupAvailableSlots(GameQueue<Clan> groupClans, int playerLimit){
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
    public static Optional<Clan> getElementByNumberOfPlayers(GameQueue<Clan> clanQ, GameQueue<Clan> recentlyAddedQ,
                                                          int maxNumberOfPlayers){
        return clanQ.stream().filter(c -> c.getNumberOfPlayers() == maxNumberOfPlayers && !recentlyAddedQ.contains(c))
                .findFirst();
    }
    public static List<Clan> getElementsByNumberOfPlayers(GameQueue<Clan> clanQ, GameQueue<Clan> recentlyAddedQ,
                                                          int maxNumberOfPlayers){
        List<Clan> result = new ArrayList<>();
        Optional<Clan> maxClan = getElementByNumberOfPlayers(clanQ, recentlyAddedQ, maxNumberOfPlayers);
        if(maxClan.isPresent()){
            result.add(maxClan.get());
            return result;
        }
        int resultPlayerSum = 0;
        for (Clan clan : clanQ) {
            int clanNumberOfPlayers = clan.getNumberOfPlayers();
            boolean isRecentlyAdded = recentlyAddedQ.contains(clan);
            if(isRecentlyAdded){
                continue;
            }
            if(clanNumberOfPlayers == maxNumberOfPlayers && resultPlayerSum == 0 ){
                result.add(clan);
                break;
            }else if((resultPlayerSum + clanNumberOfPlayers) <= maxNumberOfPlayers) {
                result.add(clan);
                resultPlayerSum = resultPlayerSum + clanNumberOfPlayers;
            }
            if(maxNumberOfPlayers == resultPlayerSum){
                break;
            }
        }
        return result;
    }
    public static boolean hasBiggerClan(GameQueue<Clan> clanStatsQ, int numberOfPlayers){
        return clanStatsQ.stream().anyMatch(e -> e.getNumberOfPlayers() >= numberOfPlayers);
    }
    public static boolean hasMaxClan(GameQueue<Clan> clanStatsQ, int numberOfPlayers){
        return clanStatsQ.stream().anyMatch(e -> e.getNumberOfPlayers() == numberOfPlayers);
    }
}
