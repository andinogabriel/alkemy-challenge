package alkemy.challenge.services;

import alkemy.challenge.dtos.MovieDetailCreationDto;
import alkemy.challenge.dtos.MovieDetailDto;
import alkemy.challenge.entities.CharacterEntity;
import alkemy.challenge.entities.MovieDetailEntity;
import alkemy.challenge.entities.MovieEntity;
import alkemy.challenge.repositories.CharacterRepository;
import alkemy.challenge.repositories.MovieDetailRepository;
import alkemy.challenge.repositories.MovieRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieDetailService {

    @Autowired
    MovieDetailRepository movieDetailRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    ModelMapper mapper;

    public MovieDetailDto createMovieDetail(MovieDetailCreationDto movieDetailCreationDto) {
        MovieEntity movieEntity = movieRepository.findById(movieDetailCreationDto.getMovie());
        if (movieEntity == null) throw new RuntimeException("Movie doesn't exist.");
        CharacterEntity characterEntity = characterRepository.findById(movieDetailCreationDto.getCharacter());
        if (characterEntity == null) throw new RuntimeException("Character doesn't exist");

        MovieDetailEntity movieDetailEntity = new MovieDetailEntity();
        movieDetailEntity.setMovie(movieEntity);
        movieDetailEntity.setCharacter(characterEntity);
        MovieDetailEntity movieDetailSaved = movieDetailRepository.save(movieDetailEntity);
        return mapper.map(movieDetailSaved, MovieDetailDto.class);
    }

    public MovieDetailDto updateMovieDetail(long id, MovieDetailCreationDto movieDetailCreationDto) {
        MovieDetailEntity movieDetailEntity = movieDetailRepository.findById(id);
        if (movieDetailEntity == null) throw new RuntimeException("Movie Detail doesn't exist.");
        MovieEntity movieEntity = movieRepository.findById(movieDetailCreationDto.getMovie());
        if (movieEntity == null) throw new RuntimeException("Movie doesn't exist.");
        CharacterEntity characterEntity = characterRepository.findById(movieDetailCreationDto.getCharacter());
        if (characterEntity == null) throw new RuntimeException("Character doesn't exist");

        movieDetailEntity.setMovie(movieEntity);
        movieDetailEntity.setCharacter(characterEntity);
        MovieDetailEntity movieDetailUpdated = movieDetailRepository.save(movieDetailEntity);
        return mapper.map(movieDetailUpdated, MovieDetailDto.class);
    }

    public void deleteMovieDetail(long id) {
        MovieDetailEntity movieDetailEntity = movieDetailRepository.findById(id);
        if (movieDetailEntity == null) throw new RuntimeException("Movie Detail doesn't exist.");
        movieDetailRepository.delete(movieDetailEntity);
    }

}
