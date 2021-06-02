package alkemy.challenge.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "users")
@Table(indexes = { @Index(columnList = "email", name = "index_email", unique = true) })
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @CreatedDate
    private Date startDate;

    private boolean isEnabled;


}
