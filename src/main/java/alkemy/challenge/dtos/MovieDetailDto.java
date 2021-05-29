package alkemy.challenge.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class MovieDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private long id;
    private MovieDto movie;
    private CharacterDto character;

}
