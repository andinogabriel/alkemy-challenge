package alkemy.challenge.models.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MovieResponse {

    private long id;
    private String imageLink;
    private String title;
    private Integer qualification;
    private GenreResponse genre;
    private List<MovieDetailResponse> movieDetails;

}
