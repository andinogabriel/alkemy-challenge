package alkemy.challenge.services;

import alkemy.challenge.buckets.BucketName;
import alkemy.challenge.dtos.GenreDto;
import alkemy.challenge.entities.GenreEntity;
import alkemy.challenge.repositories.GenreRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class GenreService {

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    FileStore fileStore;

    @Autowired
    UploadImageService imageService;

    @Autowired
    ModelMapper mapper;

    public GenreDto createGenre(GenreDto genreDto) {
        GenreEntity genreEntity = new GenreEntity();
        genreEntity.setName(genreDto.getName());
        GenreEntity genreSaved = genreRepository.save(genreEntity);
        return mapper.map(genreSaved, GenreDto.class);
    }

    public GenreDto updateGenre(long id, GenreDto genreDto) {
        GenreEntity genreEntity = genreRepository.findById(id);
        if(genreEntity == null) throw new RuntimeException("Genre doesn't exist.");

        genreEntity.setName(genreDto.getName());
        genreEntity.setImageLink(genreDto.getImageLink());

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

    public void uploadGenreImage(long id, MultipartFile file) {
        //1. Check if image isn't empty
        imageService.fileIsEmpty(file);

        //2. Check if the file is a valid image
        imageService.isAnImage(file);

        //3. Check if genre exists in the DB
        GenreEntity genreEntity = mapper.map(getGenreById(id), GenreEntity.class);

        //4. Save file metadata
        Map<String, String> metadata = imageService.exctractMetadata(file);

        //5. Store the image in s3 and update Genre image link with the image link from s3
        //Path -> bucket name + folder name, the folder name gonna be equal to genre id - genre name
        String path = String.format("%s/%s", BucketName.ITEM_IMAGE.getBucketName(), genreEntity.getId() + "-" + genreEntity.getName());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            genreEntity.setImageLink(filename);
            genreRepository.save(genreEntity);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] downloadGenreImage(long id) {
        GenreDto genre = getGenreById(id);
        String path = String.format("%s/%s", BucketName.ITEM_IMAGE.getBucketName(), genre.getId() + "-" + genre.getName());
        return fileStore.download(path, genre.getImageLink());
    }

}
