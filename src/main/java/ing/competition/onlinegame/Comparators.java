package ing.competition.onlinegame;

import ing.competition.onlinegame.dtos.Clan;

import java.util.Comparator;

public class Comparators {
    public static Comparator<Clan> sortByClanFactor(){
        return Comparator.comparingDouble(Comparators::calculateClanFactor)
                .reversed();
    }
    public static Comparator<Clan> sortByNumberOfPlayers(){
        return Comparator.comparing(Clan::getNumberOfPlayers)
                .reversed();
    }
    public static double calculateClanFactor(Clan c){
        return (double) c.getPoints() / c.getNumberOfPlayers();
    }
}

