package alkemy.challege.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "genres")
@Getter @Setter
public class GenreEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private String image;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "genre")
    private List<MovieEntity> movies;
}
