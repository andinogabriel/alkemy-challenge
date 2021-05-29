package alkemy.challenge.models.responses;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
public class CharacterResponse {

    private long id;
    private String image;
    private String name;
    private Integer age;
    private BigDecimal weight;
    private String story;
    private List<MovieDetailResponse> movies;

}
