package alkemy.challenge.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter @Setter
public class MovieDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String image;
    private String title;
    private Date creationDate;
    private Integer qualification;
    private GenreDto genre;
    private List<MovieDetailDto> movieDetails;

}
