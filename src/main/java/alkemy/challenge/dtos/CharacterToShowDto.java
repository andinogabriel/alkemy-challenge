package alkemy.challenge.dtos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CharacterToShowDto {

    long getId();
    String getName();
    String getImageLink();
    BigDecimal getWeight();
    Integer getAge();
    String getStory();
    List<MovieDetailEntity> getMovies();

    interface MovieDetailEntity {
        MovieEntity getMovie();
        interface MovieEntity {
            Long getId();
            String getImageLink();
            String getTitle();
            Date getCreationDate();
            Integer getQualification();
            GenreEntity getGenre();
            interface GenreEntity {
                Long getId();
                String getName();
                String getImageLink();
            }
        }
    }

}
