package alkemy.challenge.repositories;

import alkemy.challenge.entities.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

    GenreEntity findById(long id);

    List<GenreEntity> findAllByOrderByName();

}
