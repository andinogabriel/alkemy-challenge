package alkemy.challenge.repositories;

import alkemy.challenge.entities.GenreEntity;
import alkemy.challenge.entities.MovieEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {

    MovieEntity findById(long id);

    Page<MovieEntity> findByTitleIgnoreCaseContaining(Pageable pageable, String title);

    Page<MovieEntity> findByGenre(Pageable pageable ,GenreEntity genreEntity);

    @Query(value = "SELECT image, title, creation_date FROM movies", countQuery = "SELECT COUNT(title) FROM movies", nativeQuery = true)
    Page<Object[]> getMovies(Pageable pageable);

    @Query(value = "SELECT * FROM movies ORDER BY creation_date :ord", countQuery = "SELECT COUNT(*) FROM movies ORDER BY creation_date :ord", nativeQuery = true)
    Page<MovieEntity> getMoviesOrderBy(Pageable pageable, @Param("ord") String ord);

    @Query(value = "SELECT * FROM movies WHERE title LIKE %:movieTitle% and genre_id =:genreId ORDER BY creation_date :ord", countQuery = "SELECT COUNT(*) FROM movies WHERE title LIKE %:movieTitle% and genre_id =:genreId ORDER BY creation_date :ord", nativeQuery = true)
    Page<MovieEntity> getSearchMovies(Pageable pageable, @Param("movieTitle") String movieTitle, @Param("genreId") long genreId, @Param("ord") String ord);

}
