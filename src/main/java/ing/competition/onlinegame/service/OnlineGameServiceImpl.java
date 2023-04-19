package ing.competition.onlinegame.service;

import ing.competition.onlinegame.Utils;
import ing.competition.onlinegame.dtos.*;
import ing.competition.onlinegame.queue.ClansQueue;
import ing.competition.onlinegame.queue.GameQueue;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ApplicationScoped
public class OnlineGameServiceImpl implements OnlineGameService {
    public List<List<Clan>> calculateOrder(Players players){
        final int groupCount = players.getGroupCount();
        final ClanStats[] clanStatsByFactor = Utils.sortByClanFactor(
                Utils.getClanStats(players.getClans())
        );
        ClansQueue clansQueue = new ClansQueue(clanStatsByFactor, Utils.sortByNumberOfPlayers(
                clanStatsByFactor
        ));
        List<Group> groups = new ArrayList<>();
        groups.add(new Group());
        while(!clansQueue.getSortedByFactor().isEmpty()) {
            ClanStats clanStat = clansQueue.poolBySortedByFactor();
            if(clanStat.getNumberOfPlayers() == 6){
                System.out.println();
            }
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
            this.fullfillGroup(
                    groupClans,
                    clansQueue,
                    availableSlots,
                    groupCount);

            group.setClans(groupClans);
        }
        var clanList = groups.stream().map(Group::getClans).toList();
        return clanList;
    }

    private List<Clan> fullfillGroup(List<Clan> groupClans, ClansQueue clansQueue,
                                     int availableSlots, int playerLimit)  {
        int stillAvailableSlots = availableSlots;
        List<Clan> newGroupClans = groupClans;
        Iterator<ClanStats> iterator = clansQueue.getSortedByPlayers().iterator();
        while (stillAvailableSlots > 0 && iterator.hasNext()){
            ClanStats clanStat = iterator.next();
            int clanNumberOfPlayers = clanStat.getNumberOfPlayers();
            if(clanNumberOfPlayers <= availableSlots){
                newGroupClans.add(clanStat);
                clansQueue.removeBySortedPlayers(clanStat);
            }
            stillAvailableSlots = Utils.getGroupAvailableSlots(newGroupClans.stream().toList(),playerLimit);
        }
        return groupClans;
    }
}
