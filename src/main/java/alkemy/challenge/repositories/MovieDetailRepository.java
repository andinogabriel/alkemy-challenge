package alkemy.challenge.repositories;

import alkemy.challenge.dtos.MovieCharactersDto;
import alkemy.challenge.entities.MovieDetailEntity;
import alkemy.challenge.entities.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieDetailRepository extends JpaRepository<MovieDetailEntity, Long> {

    MovieDetailEntity findById(long id);

    Page<MovieCharactersDto> findByMovie(Pageable pageable, MovieEntity movie);

}
