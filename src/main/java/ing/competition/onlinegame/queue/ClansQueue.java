package ing.competition.onlinegame.queue;

import ing.competition.onlinegame.dtos.ClanStats;
import lombok.Getter;


@Getter
public class ClansQueue {
    private GameQueue<ClanStats> sortedByFactor;
    private GameQueue<ClanStats> sortedByPlayers;
    public ClansQueue(ClanStats[] sortedByFactor, ClanStats[] sortedByPlayers){
        this.sortedByFactor = new GameQueue<>(sortedByFactor);
        this.sortedByPlayers = new GameQueue<>(sortedByPlayers);
    }

    public ClanStats poolBySortedByFactor(){
        return this.poolByCollection(this.sortedByFactor, this.sortedByPlayers);
    }
    public ClanStats poolBySortedByPlayers(){
        return this.poolByCollection(this.sortedByPlayers, this.sortedByFactor);
    }
    public boolean removeBySortedPlayers(ClanStats obj){
        return this.removeByCollection(this.sortedByPlayers, this.sortedByFactor, obj);
    }
//    public boolean offerBySortedByPlayers(ClanStats clanStats){
//        return this.offerByCollection(this.sortedByPlayers, this.sortedByFactor, clanStats);
//    }
//    public boolean offerBySortedByFactor(ClanStats clanStats){
//        return this.offerByCollection(this.sortedByFactor, this.sortedByPlayers, clanStats);
//    }
    private ClanStats poolByCollection(GameQueue<ClanStats> pooled, GameQueue<ClanStats> second){
        ClanStats clanStats = pooled.poll();
        if(clanStats != null){
            second.remove(clanStats);
        }
        return clanStats;
    }
    private boolean removeByCollection(GameQueue<ClanStats> removed, GameQueue<ClanStats> second,
                                        ClanStats obj){
        boolean isOffered = removed.remove(obj);
        if(isOffered){
            return second.remove(obj);
        }
        return false;
    }
}
