package alkemy.challenge.dtos;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface MovieByGenreDto {

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

    List<MovieDetailEntity> getMovieDetails();

    interface MovieDetailEntity {
        CharacterEntity getCharacter();
        interface CharacterEntity {
            Long getId();
            String getName();
            String getImageLink();
            BigDecimal getWeight();
            Integer getAge();
            String getStory();
        }
    }

}
