package ing.competition.onlinegame.service;

import ing.competition.onlinegame.Utils;
import ing.competition.onlinegame.dtos.*;
import ing.competition.onlinegame.queue.GameQueue;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class OnlineGameServiceImpl implements OnlineGameService {
    public List<List<Clan>> calculateOrder(Players players){
        final int groupCount = players.getGroupCount();
        final Clan[] clanStatsByFactor = Utils.sortByClanFactor(
                players.getClans()
        );
        final GameQueue<Clan> clansQueue = new GameQueue<>(Arrays.stream(clanStatsByFactor).toList());
//        ClansQueue clansQueue = new ClansQueue(clanStatsByFactor, Utils.sortByNumberOfPlayers(
//                clanStatsByFactor
//        ));
        List<Group> groups = new ArrayList<>();
        groups.add(new Group());
        while(!clansQueue.isEmpty()) {
            Clan clanStat = clansQueue.peek();
            int currentGroupAvailableSlots = Utils.getGroupAvailableSlots(groups.get(groups.size() -1).getClans(),groupCount);
            if( currentGroupAvailableSlots == 0 || currentGroupAvailableSlots < clanStat.getNumberOfPlayers()){
                groups.add(new Group());
            }
            int groupMaxIndex = groups.size() - 1;
            Group group = groups.get(groupMaxIndex);
            int availableSlots = Utils.getGroupAvailableSlots(group.getClans(), groupCount);
            int clanNumberOfPlayers = clanStat.getNumberOfPlayers();
            List<Clan> groupClans = new ArrayList<>(group.getClans());
            groupClans.add(clanStat);
            availableSlots = Utils.getGroupAvailableSlots(groupClans,groupCount);
            GameQueue<Clan> clansToRemoveQ =this.fullfillGroup(
                    groupClans,
                    clansQueue,
                    availableSlots,
                    groupCount);
            while (clansToRemoveQ.size() > 0){
                Clan recentlyAdded = clansToRemoveQ.poll();
                clansQueue.remove(recentlyAdded);
            }

            group.setClans(groupClans);
        }
        var clanList = groups.stream().map(Group::getClans).toList();
        return clanList;
    }

    private GameQueue<Clan> fullfillGroup(List<Clan> groupClans,GameQueue<Clan> clanStatsQ,
                                     int availableSlots, int playerLimit)  {
        int stillAvailableSlots = availableSlots;
        GameQueue<Clan> recentlyAddedQ = new GameQueue<>(groupClans.get(groupClans.size() - 1));
        Iterator<Clan> iterator = clanStatsQ.iterator();
        while (stillAvailableSlots > 0 && groupClans.size() < playerLimit){
            boolean hasNext = iterator.hasNext();
            if(!hasNext){
                var polled = recentlyAddedQ.poll();
                 groupClans.remove(polled);
            }
            Optional<Clan> clanStatOpt = Utils.getElementByNumberOfPlayers(clanStatsQ,stillAvailableSlots);
            boolean hasMaxClan = Utils.hasMaxClan(clanStatsQ, playerLimit);
            if(clanStatOpt.isEmpty() && stillAvailableSlots < playerLimit && recentlyAddedQ.size() > 0
                    && availableSlots < playerLimit && hasMaxClan){
                Clan lastAdded = groupClans.size() > 0 ? groupClans.get(groupClans.size() - 1) : null;
                groupClans.remove(lastAdded);
                recentlyAddedQ.remove(lastAdded);
                stillAvailableSlots = Utils.getGroupAvailableSlots(groupClans.stream().toList(),playerLimit);
                availableSlots = Utils.getGroupAvailableSlots(groupClans.stream().toList(),playerLimit);
                continue;
            }
            if(clanStatOpt.isEmpty()){
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
