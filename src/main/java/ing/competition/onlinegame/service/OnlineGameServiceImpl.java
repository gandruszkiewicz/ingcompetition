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
            groups.add(new Group());
            Group group = groups.get(groups.size() - 1);
            List<Clan> groupClans = new ArrayList<>();
            groupClans.add(clanStat);
            GameQueue<Clan> clansToRemoveQ =this.fullfillGroup(
                    groupClans,
                    clansQueue,
                    playersLimit);
            while (clansToRemoveQ.size() > 0){
                Clan recentlyAdded = clansToRemoveQ.poll();
                clansQueue.remove(recentlyAdded);
            }
            group.setClans(groupClans);
        }
        var clanList = groups.stream().map(Group::getClans).toList();
        return clanList;
    }

    private GameQueue<Clan> fullfillGroup(List<Clan> groupClans,GameQueue<Clan> clanStatsQ, int playerLimit)  {
        int stillAvailableSlots = Utils.getGroupAvailableSlots(groupClans.stream().toList(),playerLimit);
        GameQueue<Clan> recentlyAddedQ = new GameQueue<>(groupClans.get(groupClans.size() - 1));
        Iterator<Clan> iterator = clanStatsQ.iterator();
        while (stillAvailableSlots > 0 && groupClans.size() < playerLimit){
            boolean hasNext = iterator.hasNext();
            if(!hasNext){
                var polled = recentlyAddedQ.poll();
                 groupClans.remove(polled);
            }
            boolean hasMaxClan = Utils.hasMaxClan(clanStatsQ, playerLimit);
            Optional<Clan> clanStatOpt = Utils.getElementByNumberOfPlayers(clanStatsQ, recentlyAddedQ, stillAvailableSlots);
            if(clanStatOpt.isEmpty() && stillAvailableSlots < playerLimit && recentlyAddedQ.size() > 0
                    && hasMaxClan){
                Clan lastAdded = groupClans.size() > 0 ? groupClans.get(groupClans.size() - 1) : null;
                if(lastAdded != null){
                    groupClans.remove(lastAdded);
                    recentlyAddedQ.remove(lastAdded);
                }
                stillAvailableSlots = Utils.getGroupAvailableSlots(groupClans.stream().toList(),playerLimit);
                continue;
            }else if(clanStatOpt.isEmpty()){
                stillAvailableSlots--;
                continue;
            }
            Clan clanStat = clanStatOpt.get();
            if(recentlyAddedQ.contains(clanStat) || groupClans.contains(clanStat)){
               stillAvailableSlots--;
               continue;
            }
            groupClans.add(clanStat);
            recentlyAddedQ.offer(clanStat);
            stillAvailableSlots = Utils.getGroupAvailableSlots(groupClans.stream().toList(),playerLimit);
        }
        return recentlyAddedQ;
    }
}
