package alkemy.challenge.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class MovieDetailCreationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long movie;
    private long character;

}
