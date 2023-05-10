package ing.competition.onlinegame.comparators;

import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.utils.ClanUtils;

import java.util.Comparator;

public class Comparators {
    public static Comparator<Clan> sortByClanFactorPlayersDesc(){
        return Comparator.comparingDouble(ClanUtils::calculateClanFactor)
                .thenComparingDouble(ClanUtils::getNumberOfPlayersReversed);
    }
    public static Comparator<Clan> sortByNumberOfPlayersDesc(){
        return Comparator.comparingDouble(ClanUtils::getNumberOfPlayersReversed);
    }
}

