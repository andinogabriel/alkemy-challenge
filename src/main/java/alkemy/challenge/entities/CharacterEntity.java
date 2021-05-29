package alkemy.challenge.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity(name = "characters")
@Getter @Setter
public class CharacterEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Digits(integer = 8, fraction = 2)
    private BigDecimal weight;

    private String story;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "character")
    private List<MovieDetailEntity> movies;

}
