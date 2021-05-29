package alkemy.challenge.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class CharacterDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String image;
    private String name;
    private Integer age;
    private BigDecimal weight;
    private String story;
    private List<MovieDetailDto> movies;

}
