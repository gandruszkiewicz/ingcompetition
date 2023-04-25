package ing.competition.onlinegame;

import ing.competition.onlinegame.dtos.Clan;

import java.util.Comparator;

public class Comparators {
    public static Comparator<Clan> sortByClanFactor(){
        return Comparator.comparingDouble(Comparators::calculateClanFactor)
                .thenComparingDouble(Comparators::getNumberOfPlayersReversed);
    }
    public static Comparator<Clan> sortByNumberOfPlayers(){
        return Comparator.comparing(Clan::getNumberOfPlayers)
                .reversed();
    }
    public static double calculateClanFactor(Clan c){
        return (double) (c.getPoints() / c.getNumberOfPlayers()) * (-1);
    }
    public static double getNumberOfPlayersReversed(Clan c){
        return c.getNumberOfPlayers() * (-1);
    }
}

