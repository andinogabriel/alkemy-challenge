package alkemy.challenge.services;

import alkemy.challenge.buckets.BucketName;
import alkemy.challenge.dtos.MovieByGenreDto;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    FileStore fileStore;

    @Autowired
    UploadImageService imageService;

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

    public MovieDto getMovieById(long id) {
        MovieEntity movieEntity = movieRepository.findById(id);
        if (movieEntity == null) throw new RuntimeException("Movie id doesn't exist.");
        return mapper.map(movieEntity, MovieDto.class);
    }

    public MovieDto createMovie(MovieCreationDto movieCreationDto) {
        MovieEntity movieEntity = new MovieEntity();
        GenreEntity genreEntity = genreRepository.findById(movieCreationDto.getGenre());
        if (genreEntity == null) throw new RuntimeException("Genre movie doesn't exist.");

        movieEntity.setCreationDate(movieCreationDto.getCreationDate());
        movieEntity.setGenre(genreEntity);
        movieEntity.setImageLink(movieCreationDto.getImageLink());
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
        movieEntity.setImageLink(movieCreationDto.getImageLink());
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

        Page<MovieByGenreDto> movieEntities = movieRepository.findByTitleIgnoreCaseContaining(pageable, title);
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

        Page<MovieByGenreDto> movieEntities = movieRepository.findByGenre(pageable, genreEntity);
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

    public void uploadMovieImage(long id, MultipartFile file) {
        //1. Check if image isn't empty
        imageService.fileIsEmpty(file);

        //2. Check if the file is a valid image
        imageService.isAnImage(file);

        //3. Check if genre exists in the DB
        MovieEntity movieEntity = mapper.map(getMovieById(id), MovieEntity.class);

        //4. Save file metadata
        Map<String, String> metadata = imageService.exctractMetadata(file);

        //5. Store the image in s3 and update Movie image link with the image link from s3
        //Path -> bucket name + folder name, the folder name gonna be equal to movie id - movie title
        String path = String.format("%s/%s", BucketName.ITEM_IMAGE.getBucketName(), movieEntity.getId() + "-" + movieEntity.getTitle());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            movieEntity.setImageLink(filename);
            movieRepository.save(movieEntity);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] downloadMovieImage(long id) {
        MovieDto movieDto = getMovieById(id);
        String path = String.format("%s/%s", BucketName.ITEM_IMAGE.getBucketName(), movieDto.getId() + "-" + movieDto.getTitle());
        return fileStore.download(path, movieDto.getImageLink());
    }

}
