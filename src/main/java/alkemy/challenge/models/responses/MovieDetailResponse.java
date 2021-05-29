package alkemy.challenge.models.responses;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MovieDetailResponse {

    private long id;
    private MovieResponse movie;
    private CharacterResponse character;

}
