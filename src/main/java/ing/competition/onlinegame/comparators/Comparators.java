package ing.competition.onlinegame.comparators;

import ing.competition.onlinegame.dtos.Clan;
import ing.competition.onlinegame.utils.ClanUtils;

import java.util.Comparator;

public class Comparators {
    public static Comparator<Clan> sortByClanFactorDesc(){
        return Comparator.comparingDouble(ClanUtils::calculateClanFactorReversed);
    }
}

