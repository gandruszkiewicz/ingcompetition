package ing.competition.onlinegame.service;

import ing.competition.onlinegame.dtos.*;
import ing.competition.onlinegame.queue.GameQueue;
import ing.competition.onlinegame.utils.GameQueueUtils;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class OnlineGameServiceImpl implements OnlineGameService {
    public GameQueue<GameQueue<Clan>> calculateOrder(Players players){
        final int playersLimit = players.getGroupCount();
        players.sortByClanFactorPlayersDesc();
        final GameQueue<Clan> clansInputQ = new GameQueue<>(players.getClans());
        final GameQueue<GameQueue<Clan>> order = new GameQueue<>();
        while(!clansInputQ.isEmpty()) {
            Clan clanIterPeek = clansInputQ.peek();
            final GameQueue<Clan> newGroup = new GameQueue<>();
            final GameQueue<Clan> clansToAddQ =this.getRestForTheGroup(
                    clanIterPeek,
                    clansInputQ,
                    playersLimit);
            while (clansToAddQ.size() > 0){
                Clan recentlyAdded = clansToAddQ.poll();
                clansInputQ.remove(recentlyAdded);
                newGroup.offer(recentlyAdded);
            }
            order.offer(newGroup);

        }
        return order;
    }

    private GameQueue<Clan> getRestForTheGroup(Clan groupClan, GameQueue<Clan> clanStatsQ, int playerLimit)  {
        final GameQueue<Clan> peekToAddQ = new GameQueue<>(groupClan);
        int availableSlots = GameQueueUtils.getGroupAvailableSlots(peekToAddQ,playerLimit);
        if(availableSlots == 0){
            return peekToAddQ;
        }
        List<Clan> clansToAdd = GameQueueUtils.getElementsByNumberOfPlayers(
                clanStatsQ, peekToAddQ, availableSlots
        );
        peekToAddQ.bulkOffer(clansToAdd);
        availableSlots = GameQueueUtils.getGroupAvailableSlots(peekToAddQ,playerLimit);
        boolean hasMaxClan = GameQueueUtils.hasMaxClan(clanStatsQ, playerLimit);
        if(availableSlots != 0 && hasMaxClan){
            Optional<Clan> maxClan = GameQueueUtils.getElementByNumberOfPlayers(
                    clanStatsQ,peekToAddQ,playerLimit
            );
            if(maxClan.isPresent()){
                peekToAddQ.clear();
                peekToAddQ.offer(maxClan.get());
            }
        }
        return peekToAddQ;
    }
}
