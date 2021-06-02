package alkemy.challenge.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class CharacterDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String imageLink;
    private String name;
    private Integer age;
    private BigDecimal weight;
    private String story;
    private List<MovieDetailDto> movies;

}
