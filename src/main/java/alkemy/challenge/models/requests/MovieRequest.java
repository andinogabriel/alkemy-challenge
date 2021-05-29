package alkemy.challenge.models.requests;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;

@Getter @Setter
public class MovieRequest {

    @NotBlank(message = "Image is required.")
    private String image;

    @NotBlank(message = "Title is required.")
    private String title;

    @Positive
    @Range(min = 1, max = 5, message = "Qualification must be between 1 and 5.")
    private Integer qualification;

    @NotNull(message = "Genre is required.")
    private long genre;

}
