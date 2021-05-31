package alkemy.challenge.services;

import alkemy.challenge.dtos.MovieCreationDto;
import alkemy.challenge.dtos.MovieDto;
import alkemy.challenge.entities.GenreEntity;
import alkemy.challenge.entities.MovieEntity;
import alkemy.challenge.repositories.GenreRepository;
import alkemy.challenge.repositories.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    ModelMapper mapper;

    public Page<MovieDto> getMovies(int page, int limit, String sortBy, String sortDir) {
        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<Object[]> movieEntities = movieRepository.getMovies(pageable);
        return mapper.map(movieEntities, Page.class);
    }

    public MovieDto createMovie(MovieCreationDto movieCreationDto) {
        MovieEntity movieEntity = new MovieEntity();
        GenreEntity genreEntity = genreRepository.findById(movieCreationDto.getGenre());
        if (genreEntity == null) throw new RuntimeException("Genre movie doesn't exist.");

        movieEntity.setCreationDate(movieCreationDto.getCreationDate());
        movieEntity.setGenre(genreEntity);
        movieEntity.setImage(movieCreationDto.getImage());
        movieEntity.setQualification(movieCreationDto.getQualification());
        movieEntity.setTitle(movieCreationDto.getTitle());

        MovieEntity movieCreated = movieRepository.save(movieEntity);
        return mapper.map(movieCreated, MovieDto.class);
    }

    public MovieDto updateMovie(long id, MovieCreationDto movieCreationDto) {
        MovieEntity movieEntity = movieRepository.findById(id);
        if (movieEntity == null) throw new RuntimeException("Movie doesn't exist.");
        GenreEntity genreEntity = genreRepository.findById(movieCreationDto.getGenre());
        if (genreEntity == null) throw new RuntimeException("Genre movie doesn't exist.");

        movieEntity.setCreationDate(movieCreationDto.getCreationDate());
        movieEntity.setGenre(genreEntity);
        movieEntity.setImage(movieCreationDto.getImage());
        movieEntity.setQualification(movieCreationDto.getQualification());
        movieEntity.setTitle(movieCreationDto.getTitle());

        MovieEntity movieCreated = movieRepository.save(movieEntity);
        return mapper.map(movieCreated, MovieDto.class);
    }

    public void deleteMovie(long id) {
        MovieEntity movieEntity = movieRepository.findById(id);
        if (movieEntity == null) throw new RuntimeException("Movie doesn't exist.");
        movieRepository.delete(movieEntity);
    }

    public Page<MovieDto> getMoviesByTitle(String title, int page, int limit, String sortBy, String sortDir) {
        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<MovieEntity> movieEntities = movieRepository.findByTitleIgnoreCaseContaining(pageable, title);
        return mapper.map(movieEntities, Page.class);
    }

    public Page<MovieDto> getMoviesByGenre(long id, int page, int limit, String sortBy, String sortDir) {
        GenreEntity genreEntity = genreRepository.findById(id);
        if (genreEntity == null) throw new RuntimeException("Genre movie doesn't exist.");

        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<MovieEntity> movieEntities = movieRepository.findByGenre(pageable, genreEntity);
        return mapper.map(movieEntities, Page.class);
    }

    public Page<MovieDto> getMoviesByOrderCreationDate(int page, int limit, String sortDir) {
        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit
        );

        Page<MovieEntity> movieEntities = movieRepository.getMoviesOrderBy(pageable, sortDir);
        return mapper.map(movieEntities, Page.class);
    }

    public Page<MovieDto> getSearchMovies(int page, int limit, String title, long genreId, String sortDir) {
        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit
        );

        Page<MovieEntity> movieEntities = movieRepository.getSearchMovies(pageable, title, genreId, sortDir);
        return mapper.map(movieEntities, Page.class);
    }

}
