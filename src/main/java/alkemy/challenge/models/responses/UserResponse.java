package alkemy.challenge.models.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class UserResponse {

    private String email;
    private String firstName;
    private String lastName;
    private Date startDate;
    private boolean isEnabled;

}
