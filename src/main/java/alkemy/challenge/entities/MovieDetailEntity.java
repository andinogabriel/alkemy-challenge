package alkemy.challenge.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "movie_details")
@Getter @Setter
public class MovieDetailEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @JsonIgnoreProperties(value = {"movieDetails", "handler","hibernateLazyInitializer"}, allowSetters = true)
    private MovieEntity movie;

    @ManyToOne
    @JoinColumn(name = "character_id", nullable = false)
    @JsonIgnoreProperties(value = {"movies", "handler","hibernateLazyInitializer"}, allowSetters = true)
    private CharacterEntity character;
}
