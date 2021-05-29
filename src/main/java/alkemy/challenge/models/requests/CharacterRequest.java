package alkemy.challenge.models.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter @Setter
public class CharacterRequest {

    @NotBlank(message = "Image is required.")
    private String image;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotNull(message = "Age is required.")
    @Positive
    private Integer age;

    @Digits(integer=8, fraction=2, message = "Weight must only have two decimal.")
    @Positive
    private BigDecimal weight;

    private String story;

}
