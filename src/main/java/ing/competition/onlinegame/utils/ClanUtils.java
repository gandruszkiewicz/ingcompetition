package ing.competition.onlinegame.utils;

import ing.competition.onlinegame.dtos.Clan;

public class ClanUtils {
    public static double calculateClanFactorReversed(Clan c) {
        double points = Double.parseDouble(String.valueOf(c.getPoints()));
        double numberOfPlayers = Double.parseDouble(String.valueOf(c.getNumberOfPlayers()));
        return (points / numberOfPlayers) * (-1);
    }

    public static double getNumberOfPlayersReversed(Clan c) {
        return c.getNumberOfPlayers() * (-1);
    }
}
