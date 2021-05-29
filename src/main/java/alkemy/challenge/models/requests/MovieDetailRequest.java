package alkemy.challenge.models.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class MovieDetailRequest {

    @NotNull(message = "Movie is required.")
    private long movie;

    @NotNull(message = "Character is required.")
    private long character;

}
