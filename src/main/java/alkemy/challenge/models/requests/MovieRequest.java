package alkemy.challenge.models.requests;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.util.Date;

@Getter @Setter
public class MovieRequest {

    private String imageLink;

    @NotBlank(message = "Title is required.")
    private String title;

    @Positive
    @Range(min = 1, max = 5, message = "Qualification must be between 1 and 5.")
    private Integer qualification;

    @NotNull(message = "Creation date is required.")
    private Date creationDate;

    @NotNull(message = "Genre is required.")
    private long genre;

}
