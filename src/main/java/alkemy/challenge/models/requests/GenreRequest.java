package alkemy.challege.models.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class GenreRequest {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Image is required.")
    private String image;

}