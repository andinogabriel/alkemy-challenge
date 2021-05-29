package alkemy.challenge.services;

import alkemy.challenge.dtos.GenreDto;
import alkemy.challenge.entities.GenreEntity;
import alkemy.challenge.repositories.GenreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreService {

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    ModelMapper mapper;

    public GenreDto createGenre(GenreDto genreDto) {
        GenreEntity genreEntity = new GenreEntity();

        genreEntity.setName(genreDto.getName());
        genreEntity.setImage(genreDto.getImage());

        GenreEntity genreSaved = genreRepository.save(genreEntity);
        return mapper.map(genreSaved, GenreDto.class);
    }

    public GenreDto updateGenre(long id, GenreDto genreDto) {
        GenreEntity genreEntity = genreRepository.findById(id);
        if(genreEntity == null) throw new RuntimeException("Genre doesn't exist.");

        genreEntity.setName(genreDto.getName());
        genreEntity.setImage(genreDto.getImage());

        GenreEntity genreUpdated = genreRepository.save(genreEntity);
        return mapper.map(genreUpdated, GenreDto.class);
    }

    public void deleteGenre(long id) {
        GenreEntity genreEntity = genreRepository.findById(id);
        if(genreEntity == null) throw new RuntimeException("Genre doesn't exist.");
        genreRepository.delete(genreEntity);
    }

    public List<GenreDto> getGenres() {
        List<GenreEntity> genreEntities = genreRepository.findAllByOrderByName();
        List<GenreDto> genresDto = new ArrayList<>();
        genreEntities.forEach(g -> genresDto.add(mapper.map(g, GenreDto.class)));
        return genresDto;
    }

    public GenreDto getGenreById(long id) {
        GenreEntity genreEntity = genreRepository.findById(id);
        if(genreEntity == null) throw new RuntimeException("Genre doesn't exist.");
        return mapper.map(genreEntity, GenreDto.class);
    }



}
