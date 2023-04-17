package ing.competition;

import lombok.Getter;

@Getter
public class CompetitionException extends RuntimeException{
    public CompetitionException(String message){
        super(message);
    }
}
