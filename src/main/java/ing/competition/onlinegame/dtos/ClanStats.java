package ing.competition.onlinegame.dtos;

import lombok.Getter;

@Getter
public class ClanStats extends Clan{
    private double clanFactor;
    private boolean isInserted;
    public ClanStats(Clan clan){
        this.setPoints(clan.getPoints());
        this.setNumberOfPlayers(clan.getNumberOfPlayers());
        this.setClanFactor(clan.getPoints(), clan.getNumberOfPlayers());
    }
    private void setClanFactor(int points, int numberOfPlayers){
        this.clanFactor = (double) points / numberOfPlayers;
    }

    public void setInserted(boolean value){
        this.isInserted = value;
    }
}
