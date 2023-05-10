package ing.competition.onlinegame.utils;

import ing.competition.onlinegame.comparators.Comparators;
import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.queue.GameQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class GameQueueUtils {
    public static int iterationNumbers = 0;
    public static ArrayList<Integer> iterList = new ArrayList<Integer>();
    public static int getGroupAvailableSlots(GameQueue<Clan> clansQ, int playerLimit){
        final int occupancy = getOccupancy(clansQ);
        int result = playerLimit - occupancy;
        if(result < 0){
            throw new RuntimeException("Group is overloaded !!");
        }
        return result;
    }

    /**
     * Get number of players in whole queue.
     * @param clanQ - queue from which occupancy will be calculated.
     * @return number of players in queue.
     */
    public static int getOccupancy(GameQueue<Clan> clanQ){
        return clanQ.stream()
                .map(Clan::getNumberOfPlayers)
                .mapToInt(np -> np)
                .sum();
    }

    public static Optional<Clan> geClanByNumberOfPlayers(GameQueue<Clan> clanQ, GameQueue<Clan> recentlyAddedQ,
                                                         int maxNumberOfPlayers){
        return clanQ.stream().filter(c -> c.getNumberOfPlayers() == maxNumberOfPlayers && !recentlyAddedQ.contains(c))
                .findFirst();
    }

    /**
     * Method returns clan list handling two strategies.
     * Returns clan that fulfill all slots or collection which of smaller clans from queue.
     * @param clanInputQ input queue of clans.
     * @param peekToAddQ clans chosen to new group.
     * @param maxNumberOfPlayers maximum number of players in group.
     * @return list of clans that maximize group by number of players and factor points / players.
     */
    public static List<Clan> getElementsByNumberOfPlayers(GameQueue<Clan> clanInputQ, GameQueue<Clan> peekToAddQ,
                                                          int maxNumberOfPlayers){
        List<Clan> clansToAdd = new ArrayList<>();
        Optional<Clan> maxClan = geClanByNumberOfPlayers(clanInputQ, peekToAddQ, maxNumberOfPlayers);
        if (maxClan.isPresent()) {
            clansToAdd.add(maxClan.get());
            return clansToAdd;
        }
        int totalNumberOfPlayers = peekToAddQ.stream()
                .map(Clan::getNumberOfPlayers).mapToInt(p -> p).sum();
        int iterations = 0;
        var sorted = clanInputQ.stream().sorted(Comparators.sortByNumberOfPlayersDesc()).toList();
        for (Clan clan : sorted.stream().filter(c -> c.getNumberOfPlayers() <= maxNumberOfPlayers).toList()) {
            iterations++;
            int numberOfPlayers = clan.getNumberOfPlayers();
            boolean isRecentlyAdded = peekToAddQ.contains(clan);
            if (isRecentlyAdded) {
                continue;
            }
            if (numberOfPlayers == maxNumberOfPlayers && totalNumberOfPlayers == 0) {
                clansToAdd.add(clan);
                break;
            } else if ((totalNumberOfPlayers + numberOfPlayers) <= maxNumberOfPlayers) {
                clansToAdd.add(clan);
                totalNumberOfPlayers += numberOfPlayers;
            }
            if (maxNumberOfPlayers == totalNumberOfPlayers || iterations >= clanInputQ.size()) {
                break;
            }
        }
        iterList.add(iterations);
        return clansToAdd;
    }

    public static boolean hasMaxClan(GameQueue<Clan> clanStatsQ, int numberOfPlayers){
        return clanStatsQ.stream().anyMatch(e -> e.getNumberOfPlayers() == numberOfPlayers);
    }
}
