package alkemy.challenge.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter @Setter
public class MovieCreationDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String imageLink;
    private String title;
    private Date creationDate;
    private Integer qualification;
    private long genre;
    private List<MovieDetailDto> movieDetails;

}
