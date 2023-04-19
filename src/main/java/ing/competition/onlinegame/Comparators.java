package ing.competition.onlinegame;

import ing.competition.onlinegame.dtos.ClanStats;

import java.util.Comparator;

public class Comparators {
    public static Comparator<ClanStats> sortByClanFactor(){
        return Comparator.comparing(ClanStats::getClanFactor)
                .reversed();
    }
    public static Comparator<ClanStats> sortByNumberOfPlayers(){
        return Comparator.comparing(ClanStats::getNumberOfPlayers)
                .reversed();
    }
}

