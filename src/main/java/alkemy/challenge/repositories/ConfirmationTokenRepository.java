package alkemy.challenge.repositories;

import alkemy.challenge.entities.ConfirmationTokenEntity;
import alkemy.challenge.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, Long> {

    ConfirmationTokenEntity findByToken(String token);

    ConfirmationTokenEntity findByUser(UserEntity user);

}
