package alkemy.challege.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

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

    @CreatedDate
    private Date creationDate;

    private Integer qualification;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;

    @ManyToMany(mappedBy = "characterMovies")
    private List<CharacterEntity> movieCharacters;


}
