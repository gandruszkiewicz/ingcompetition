package ing.competition.onlinegame.utils;

import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.queue.GameQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameQueueUtils {
    public static int getGroupAvailableSlots(GameQueue<Clan> clansQ, int playerLimit){
        final int occupancy = getOccupancy(clansQ);
        int result = playerLimit - occupancy;
        if(result < 0){
            throw new RuntimeException("Group is overloaded !!");
        }
        return result;
    }
    public static int getOccupancy(GameQueue<Clan> clanQ){
        return clanQ.stream()
                .map(Clan::getNumberOfPlayers)
                .mapToInt(np -> np)
                .sum();
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
    public static boolean hasMaxClan(GameQueue<Clan> clanStatsQ, int numberOfPlayers){
        return clanStatsQ.stream().anyMatch(e -> e.getNumberOfPlayers() == numberOfPlayers);
    }
}
