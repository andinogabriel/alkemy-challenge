package alkemy.challenge.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter @Setter
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String lastName;
    private String firstName;
    private String email;
    private String password;
    private String encryptedPassword;
    private Date startDate;
    private boolean isEnabled;

}
