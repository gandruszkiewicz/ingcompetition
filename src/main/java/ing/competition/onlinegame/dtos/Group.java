package ing.competition.onlinegame.dtos;

import ing.competition.onlinegame.comparators.Comparators;

import java.util.*;

public class Group extends AbstractQueue<Clan> {
    private final LinkedList<Clan> elements = new LinkedList<>();

    public Group(List<Clan> list) {
        this.elements.addAll(list);
    }

    public Group() {
    }

    @Override
    public Iterator<Clan> iterator() {
        return this.elements.iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    public int getNumberOfPlayers() {
        var result = elements.stream().map(Clan::getNumberOfPlayers).mapToInt(p -> p).sum();
        return result;
    }

    public int getNumberOfPlayersReversed() {
        return this.getNumberOfPlayers() * (-1);
    }

    @Override
    public boolean offer(Clan t) {
        if (t == null) return false;
        this.elements.add(t);
        this.elements.sort(Comparators.sortClanByClanFactorDesc());
        return true;
    }

    public Clan get(int index) {
        return this.elements.get(index);
    }

    @Override
    public Clan poll() {
        Iterator<Clan> iter = elements.iterator();
        Clan t = null;
        if (iter.hasNext()) t = iter.next();
        if (t != null) {
            iter.remove();
            return t;
        }
        return null;
    }

    @Override
    public Clan peek() {
        return this.elements.getLast();
    }

    /**
     * Get number of players in whole queue.
     *
     * @return number of players in queue.
     */
    public int getOccupancy() {
        return this.elements.stream().map(Clan::getNumberOfPlayers)
                .mapToInt(np -> np)
                .sum();
    }

    public List<Clan> getClanList() {
        return this.elements.stream().toList();
    }

    public double getGroupFactor() {
        double numberOfPlayers = Double.parseDouble(String.valueOf(this.getNumberOfPlayers()));
        double groupPoints = Double.parseDouble(String.valueOf(this.getGroupPoints()));
        return groupPoints / numberOfPlayers;
    }

    public double getGroupFactorReversed() {
        return this.getGroupFactor() * (-1);
    }

    public List<Clan> getByNumerOfPlayers(int numberOfPlayers) {
        int sumOfSelectedPlayers = 0;
        ArrayList<Clan> clansToGet = new ArrayList<>();
        for (Clan clan : this.elements) {
            int clanNumberOfPlayers = clan.getNumberOfPlayers();
            if ((sumOfSelectedPlayers + clanNumberOfPlayers) <= numberOfPlayers) {
                sumOfSelectedPlayers += clan.getNumberOfPlayers();
                clansToGet.add(clan);
            }
        }
        this.elements.removeAll(clansToGet);
        Collections.reverse(clansToGet);
        return clansToGet;
    }

    private double getGroupPoints() {
        return this.elements.stream()
                .map(Clan::getPoints)
                .mapToDouble(p -> p)
                .sum();
    }
}
