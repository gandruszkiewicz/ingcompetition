package ing.competition.onlinegame.service;

import ing.competition.onlinegame.Utils;
import ing.competition.onlinegame.dtos.*;
import ing.competition.onlinegame.queue.GameQueue;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class OnlineGameServiceImpl implements OnlineGameService {
    public List<List<Clan>> calculateOrder(Players players){
        final int playersLimit = players.getGroupCount();
        final Clan[] clanStatsByFactor = Utils.sortByClanFactor(
                players.getClans()
        );
        final GameQueue<Clan> clansQueue = new GameQueue<>(Arrays.stream(clanStatsByFactor).toList());
        List<Group> groups = new ArrayList<>();
        while(!clansQueue.isEmpty()) {
            Clan clanStat = clansQueue.peek();
            final Group newGroup = new Group();
            GameQueue<Clan> clansToAddQ =this.fullFillGroup(
                    clanStat,
                    clansQueue,
                    playersLimit);
            newGroup.setClans(clansToAddQ.stream().toList());
            groups.add(newGroup);
            while (clansToAddQ.size() > 0){
                Clan recentlyAdded = clansToAddQ.poll();
                clansQueue.remove(recentlyAdded);
            }

        }
        var clanList = groups.stream().map(Group::getClans).toList();
        return clanList;
    }

    private GameQueue<Clan> fullFillGroup(Clan groupClan,GameQueue<Clan> clanStatsQ, int playerLimit)  {
        GameQueue<Clan> recentlyAddedQ = new GameQueue<>(groupClan);
        int stillAvailableSlots = Utils.getGroupAvailableSlots(recentlyAddedQ,playerLimit);
        Iterator<Clan> iterator = clanStatsQ.iterator();
        while (stillAvailableSlots > 0 && recentlyAddedQ.size() < playerLimit){
            boolean hasNext = iterator.hasNext();
            if(!hasNext){
                recentlyAddedQ.poll();
            }
            boolean hasMaxClan = Utils.hasMaxClan(clanStatsQ, playerLimit);
            Optional<Clan> clanToAdd = Utils.getElementByNumberOfPlayers(clanStatsQ, recentlyAddedQ, stillAvailableSlots);
            if(clanToAdd.isEmpty() && stillAvailableSlots < playerLimit && recentlyAddedQ.size() > 0
                    && hasMaxClan){
                recentlyAddedQ.poll();
                stillAvailableSlots = Utils.getGroupAvailableSlots(recentlyAddedQ,playerLimit);
                continue;
            }else if(clanToAdd.isEmpty()){
                stillAvailableSlots--;
                continue;
            }
            Clan clanStat = clanToAdd.get();
            if(recentlyAddedQ.contains(clanStat)){
               stillAvailableSlots--;
               continue;
            }
            recentlyAddedQ.offer(clanStat);
            stillAvailableSlots = Utils.getGroupAvailableSlots(recentlyAddedQ,playerLimit);
        }
        return recentlyAddedQ;
    }
}
