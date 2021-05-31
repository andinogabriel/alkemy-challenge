package alkemy.challenge.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "movies")
@Getter @Setter
public class MovieEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private Date creationDate;

    private Integer qualification;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    @JsonIgnoreProperties(value = {"movies", "handler","hibernateLazyInitializer"}, allowSetters = true)
    private GenreEntity genre;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movie")
    private List<MovieDetailEntity> movieDetails;


}
