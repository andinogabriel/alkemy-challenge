package alkemy.challenge.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity(name = "confirmation_token")
@Getter @Setter
public class ConfirmationTokenEntity implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    private String token;

    private Timestamp expiryDate;

    @OneToOne(targetEntity = UserEntity.class, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;

    public ConfirmationTokenEntity () {}

    public ConfirmationTokenEntity(UserEntity userEntity, String token) {
        this.user = userEntity;
        this.token = token;
    }

}
