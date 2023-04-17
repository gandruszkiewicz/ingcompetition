package ing.competition.atmservice.dtos;


import lombok.Getter;

@Getter
public class ATM{
    private int region;
    private int atmId;
    public ATM(int region, int atmId) {
        this.region = region;
        this.atmId = atmId;
    }
    @Override
    public int hashCode(){
        return String.valueOf(this.region).hashCode() + String.valueOf(this.atmId).hashCode();
    }
    @Override
    public boolean equals(Object obj){
        if(obj instanceof ATM){
            ATM temp = (ATM)obj;
            return this.getRegion() == temp.getRegion() && this.getAtmId() == temp.getAtmId();
        }
        return false;
    }
}
