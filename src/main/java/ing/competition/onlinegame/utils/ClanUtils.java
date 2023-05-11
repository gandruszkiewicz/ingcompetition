package ing.competition.onlinegame.utils;

import ing.competition.onlinegame.dtos.Clan;

public class ClanUtils {
    public static double calculateClanFactorReversed(Clan c){
        return (double) (c.getPoints() / c.getNumberOfPlayers()) * (-1);
    }
    public static double getNumberOfPlayersReversed(Clan c){
        return c.getNumberOfPlayers() * (-1);
    }
}
