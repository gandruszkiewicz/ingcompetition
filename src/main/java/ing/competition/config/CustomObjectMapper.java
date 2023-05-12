package ing.competition.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Singleton
public class CustomObjectMapper{
    @Produces
    @Singleton
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
