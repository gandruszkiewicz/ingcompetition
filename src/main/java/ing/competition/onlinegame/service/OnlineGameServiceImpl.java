package ing.competition.onlinegame.service;

import ing.competition.onlinegame.Utils;
import ing.competition.onlinegame.dtos.*;
import ing.competition.onlinegame.queue.GameQueue;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class OnlineGameServiceImpl implements OnlineGameService {
    public GameQueue<GameQueue<Clan>> calculateOrder(Players players){
        final int playersLimit = players.getGroupCount();
        final Clan[] clanStatsByFactor = Utils.sortByClanFactor(
                players.getClans()
        );
        final GameQueue<Clan> clansQueue = new GameQueue<>(Arrays.stream(clanStatsByFactor).toList());
        GameQueue<GameQueue<Clan>> groups = new GameQueue<>();
        while(!clansQueue.isEmpty()) {
            Clan clanStat = clansQueue.peek();
            GameQueue<Clan> newGroup = new GameQueue<>();
            GameQueue<Clan> clansToAddQ =this.fullFillGroup(
                    clanStat,
                    clansQueue,
                    playersLimit);
            while (clansToAddQ.size() > 0){
                Clan recentlyAdded = clansToAddQ.poll();
                clansQueue.remove(recentlyAdded);
                newGroup.offer(recentlyAdded);
            }
            groups.offer(newGroup);

        }
        return groups;
    }

    private GameQueue<Clan> fullFillGroup(Clan groupClan,GameQueue<Clan> clanStatsQ, int playerLimit)  {
        GameQueue<Clan> recentlyAddedQ = new GameQueue<>(groupClan);
        int stillAvailableSlots = Utils.getGroupAvailableSlots(recentlyAddedQ,playerLimit);
        if(stillAvailableSlots == 0){
            return recentlyAddedQ;
        }
        boolean hasMaxClan = Utils.hasMaxClan(clanStatsQ, playerLimit);
        List<Clan> clansToAdd = Utils.getElementsByNumberOfPlayers(clanStatsQ, recentlyAddedQ, stillAvailableSlots);
        recentlyAddedQ.bulkOffer(clansToAdd);
        stillAvailableSlots = Utils.getGroupAvailableSlots(recentlyAddedQ,playerLimit);
        if(stillAvailableSlots != 0 && hasMaxClan){
            Optional<Clan> maxClan = Utils.getElementByNumberOfPlayers(clanStatsQ,recentlyAddedQ,playerLimit);
            if(maxClan.isPresent()){
                recentlyAddedQ.clear();
                recentlyAddedQ.offer(maxClan.get());
            }
        }
        return recentlyAddedQ;
    }
}
