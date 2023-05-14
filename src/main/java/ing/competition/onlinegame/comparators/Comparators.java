package ing.competition.onlinegame.comparators;

import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.dtos.Group;
import ing.competition.onlinegame.utils.ClanUtils;

import java.util.Comparator;

public class Comparators {
    private Comparators(){}
    public static Comparator<Clan> sortClanByClanFactorDesc() {
        return Comparator.comparingDouble(ClanUtils::calculateClanFactorReversed);
    }

    public static Comparator<Group> sortGroupByNumberOfPlayersDesc() {
        return Comparator.comparingInt(Group::getNumberOfPlayersReversed)
                .thenComparingDouble(Group::getGroupFactorReversed);
    }
}

