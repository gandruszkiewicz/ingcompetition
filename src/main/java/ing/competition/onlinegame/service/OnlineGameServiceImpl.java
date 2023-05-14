package ing.competition.onlinegame.service;

import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.dtos.Group;
import ing.competition.onlinegame.handlers.OrderHandler;
import ing.competition.onlinegame.dtos.Players;
import ing.competition.onlinegame.utils.GameQueueUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ApplicationScoped
public class OnlineGameServiceImpl implements OnlineGameService {
    private final Random random = new Random();
    public OrderHandler calculateOrder(Players players) {
        final int playersLimit = players.getGroupCount();
        // 1. Sort desc collection of clans by factor - points divide on number of players.
        // This give us information of clan strength.
        // Then by number of players desc. This will help to fulfill maximum number of groups.
        players.sortByClanFactor();
        // 2. Create queues, first is input queue from which will be populated order queue.
        final Group clansInputQ = new Group(players.getClans());
        final OrderHandler orderHandler = new OrderHandler(new Group(), playersLimit);
        while (!clansInputQ.isEmpty()) {
            Group currentGroup = orderHandler.peek();
            Clan clanToAdd = clansInputQ.poll();
            int availableSlots = GameQueueUtils.getGroupAvailableSlots(currentGroup, playersLimit);
            if (clanToAdd.getNumberOfPlayers() <= availableSlots) {
                currentGroup.offer(clanToAdd);
            } else {
                Group newGroup = new Group();
                newGroup.offer(clanToAdd);
                orderHandler.offer(newGroup);
            }
        }
        orderHandler.compareLastTwoAddedGroups();
        orderHandler.sortByNumberOfPlayersDesc();
        return orderHandler;
    }

    public Players generatePlayers(int groupCount, int numberOfClans, int maxPoints) {
        final Players players = new Players();
        final List<Clan> clans = new ArrayList<>();
        players.setGroupCount(groupCount);
        for (int index = 0; index < numberOfClans; index++) {
            Clan clan = new Clan();
            int points = random.nextInt(maxPoints - 1) + 1;
            int numberOfPlayers = random.nextInt(groupCount);
            if (points == 0 || numberOfPlayers == 0) {
                continue;
            }
            clan.setPoints(points);
            clan.setNumberOfPlayers(numberOfPlayers);
            clans.add(clan);
        }
        players.setClans(clans);
        return players;
    }
}
