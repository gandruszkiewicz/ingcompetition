package ing.competition.onlinegame.service;

import ing.competition.onlinegame.Utils;
import ing.competition.onlinegame.dtos.*;
import ing.competition.onlinegame.exceptions.RestGroupInsertException;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@ApplicationScoped
public class OnlineGameServiceImpl implements OnlineGameService {
    public Clan[][] calculateOrder(Players players){
        final int groupCount = players.getGroupCount();
        final ClanStats[] clanStats = Utils.sortByClanFactor(
                Utils.getClanStats(players.getClans())
        );
        List<Group> groups = new ArrayList<>();
        int index = 0;
        while(Utils.isSomeNonInserted(Arrays.stream(clanStats).toList())) {

            ClanStats clanStat = clanStats[index];
            if(groups.size() == 0 || Utils.getGroupAvailableSlots(groups.get(groups.size() -1).getClans(),groupCount) == 0){
                groups.add(new Group());
            }
            int groupMaxIndex = groups.size() - 1;
            Group group = groups.get(groupMaxIndex);
            int availableSlots = Utils.getGroupAvailableSlots(group.getClans(), groupCount);
            int clanNumberOfPlayers = clanStat.getNumberOfPlayers();
            List<Clan> groupClans = group.getClans();

            if(clanNumberOfPlayers <= availableSlots){
                clanStat.setInserted(true);
                groupClans.add(clanStat);
            }else if(clanNumberOfPlayers > availableSlots && availableSlots > 0){
                try{
                    groupClans = this.addRest(
                            groupClans,
                            Arrays.copyOfRange(clanStats,index +1,clanStats.length),
                            availableSlots,
                            groupCount

                    );
                }catch (RestGroupInsertException ex){
                    if(index == groupClans.size() -1){
//                        clanStat.setInserted(true);
//                        groups.add(new Group(clanStat));
                    }else{

                    }
                }
            }
            group.setClans(groupClans);
            index++;
        }
        var clans = groups.stream().map(Group::getClans).toArray(Clan[][]::new);
        return clans;
    }

    private List<Clan> addRest(List<Clan> groupClans, ClanStats[] clanStats, int availableSlots, int playerLimit) throws RestGroupInsertException {
        int stillAvailableSlots = availableSlots;
        int index = 0;
        int maxIndex = clanStats.length - 1;
        List<Clan> newGroupClans = groupClans;
        while (stillAvailableSlots > 0 && maxIndex != index){
            ClanStats clanStat = clanStats[index];
            int clanNumberOfPlayers = clanStat.getNumberOfPlayers();
            if(clanNumberOfPlayers <= availableSlots){
                newGroupClans.add(clanStat);
            }
            stillAvailableSlots = Utils.getGroupAvailableSlots(newGroupClans.stream().toList(),playerLimit);
            index++;
        }
        if(newGroupClans.size() == groupClans.size()){
            throw new RestGroupInsertException();
        }
        return groupClans;
    }
}
