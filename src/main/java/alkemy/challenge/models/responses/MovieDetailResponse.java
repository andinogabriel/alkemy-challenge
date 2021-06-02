package alkemy.challenge.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MovieDetailResponse {

    private long id;

    @JsonIgnoreProperties(value = {"movieDetails", "handler","hibernateLazyInitializer"}, allowSetters = true)
    private MovieResponse movie;

    @JsonIgnoreProperties(value = {"movies", "handler","hibernateLazyInitializer"}, allowSetters = true)
    private CharacterResponse character;

}
