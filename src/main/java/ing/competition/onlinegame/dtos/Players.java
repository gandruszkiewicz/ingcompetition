package ing.competition.onlinegame.dtos;

import ing.competition.onlinegame.comparators.Comparators;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Players {
    private int groupCount;
    private List<Clan> clans;
    public void sortByClanFactor(){
        this.setClans(
                this.clans.stream().sorted(Comparators.sortClanByClanFactorDesc())
                        .toList()
        );
    }
}
