package ing.competition.onlinegame.service;

import ing.competition.onlinegame.dtos.*;
import ing.competition.onlinegame.queue.GameQueue;
import ing.competition.onlinegame.utils.GameQueueUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class OnlineGameServiceImpl implements OnlineGameService {
    public GameQueue<GameQueue<Clan>> calculateOrder(Players players){
        final int playersLimit = players.getGroupCount();
        // 1. Sort desc collection of clans by factor - points divide on number of players.
        // This give us information of clan strength.
        // Then by number of players desc. This will help to fulfill maximum number of groups.
        players.sortByClanFactorPlayersDesc();
        // 2. Create queues, first is input queue from which will be populated order queue.
        final GameQueue<Clan> clansInputQ = new GameQueue<>(players.getClans());
        final GameQueue<GameQueue<Clan>> order = new GameQueue<>();
        while(!clansInputQ.isEmpty()) {
            final GameQueue<Clan> newGroup = new GameQueue<>();
            // 4 Fulfill rest of free slots in the queue or replace with the largest.
            final GameQueue<Clan> clansToAddQ =this.getClansForGroup(
                    clansInputQ,
                    playersLimit);
            // 5. Add clan which was peek to add to.
            while (clansToAddQ.size() > 0){
                Clan recentlyAdded = clansToAddQ.poll();
                clansInputQ.remove(recentlyAdded);
                newGroup.offer(recentlyAdded);
            }
            // 6. Add fulfilled group to order.
            order.offer(newGroup);

        }
        return order;
    }

    public Players generatePlayers(int groupCount, int numberOfClans){
        final Players players = new Players();
        final List<Clan> clans = new ArrayList<>();
        int maxPoints = 100000;
        players.setGroupCount(groupCount);
        for (int index = 0; index < numberOfClans; index++) {
            Random random = new Random();
            Clan clan = new Clan();
            int points = random.nextInt(maxPoints-1) + 1;
            int numberOfPlayers = random.nextInt(groupCount);
            if(points == 0 || numberOfPlayers == 0){
                continue;
            }
            clan.setPoints(points);
            clan.setNumberOfPlayers(numberOfPlayers);
            clans.add(clan);
        }
        players.setClans(clans);
        return players;
    }

    private GameQueue<Clan> getClansForGroup(GameQueue<Clan> clansInputQ, int playerLimit)  {
        // 1. Get the strongest clan.
        Clan mostPowerFullClan = clansInputQ.peek();
        final GameQueue<Clan> peekToAddQ = new GameQueue<>(mostPowerFullClan);
        // 2. Calculate how many slots are still available.
        int availableSlots = GameQueueUtils.getGroupAvailableSlots(peekToAddQ,playerLimit);
        if(availableSlots == 0){
            return peekToAddQ;
        }
        // 3. Get rest for the group to fulfill slots.
        List<Clan> clansToAdd = GameQueueUtils.getElementsByNumberOfPlayers(
                clansInputQ, peekToAddQ, availableSlots
        );
        // 4. Add chosen clans to queue.
        peekToAddQ.bulkOffer(clansToAdd);
        // 5. Again calculate slots.
        availableSlots = GameQueueUtils.getGroupAvailableSlots(peekToAddQ,playerLimit);
        // 6. check if input queue have clan that has the same number of players as group limit.
        boolean hasMaxClan = GameQueueUtils.hasMaxClan(clansInputQ, playerLimit);
        // 7. If group has still available slot get that clan and replace it in the queue.
        if(availableSlots != 0 && hasMaxClan){
            Optional<Clan> maxClan = GameQueueUtils.geClanByNumberOfPlayers(
                    clansInputQ,peekToAddQ,playerLimit
            );
            if(maxClan.isPresent()){
                peekToAddQ.clear();
                peekToAddQ.offer(maxClan.get());
            }
        }
        return peekToAddQ;
    }
}
