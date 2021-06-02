package alkemy.challenge.models.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class GenreRequest {

    @NotBlank(message = "Name is required.")
    private String name;

    private String imageLink;

}
