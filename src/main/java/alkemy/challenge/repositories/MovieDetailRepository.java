package alkemy.challenge.repositories;

import alkemy.challenge.entities.MovieDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieDetailRepository extends JpaRepository<MovieDetailEntity, Long> {

    MovieDetailEntity findById(long id);

}
