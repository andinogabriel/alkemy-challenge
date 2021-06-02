package alkemy.challenge.services;

import alkemy.challenge.buckets.BucketName;
import alkemy.challenge.dtos.*;
import alkemy.challenge.entities.CharacterEntity;
import alkemy.challenge.entities.MovieEntity;
import alkemy.challenge.repositories.CharacterRepository;
import alkemy.challenge.repositories.MovieDetailRepository;
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
import java.util.*;


@Service
public class CharacterService {

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    MovieDetailRepository movieDetailRepository;

    @Autowired
    FileStore fileStore;

    @Autowired
    UploadImageService imageService;

    @Autowired
    ModelMapper mapper;

    public CharacterDto createCharacter(CharacterDto character) {
        CharacterEntity characterEntity = new CharacterEntity();
        characterEntity.setAge(character.getAge());
        //characterEntity.setImageLink(character.getImageLink());
        characterEntity.setName(character.getName());
        characterEntity.setWeight(character.getWeight());
        characterEntity.setStory(character.getStory());
        CharacterEntity characterSaved = characterRepository.save(characterEntity);
        return mapper.map(characterSaved, CharacterDto.class);
    }

    public Page<CharacterDto> getCharacters(int page, int limit, String sortBy, String sortDir) {
        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<Object[]> characters = characterRepository.getCharacters(pageable);
        return mapper.map(characters, Page.class);
    }

    public CharacterDto updateCharacter(long id, CharacterDto characterDto) {
        CharacterEntity characterEntity = characterRepository.findById(id);
        if(characterEntity == null) throw new RuntimeException("Character doesn't exist.");

        characterEntity.setStory(characterDto.getStory());
        characterEntity.setName(characterEntity.getName());
        //characterEntity.setImageLink(characterDto.getImageLink());
        characterEntity.setWeight(characterDto.getWeight());
        characterEntity.setAge(characterDto.getAge());
        CharacterEntity updatedCharacter = characterRepository.save(characterEntity);
        return mapper.map(updatedCharacter, CharacterDto.class);
    }

    public void deleteCharacter(long id) {
        CharacterEntity characterEntity = characterRepository.findById(id);
        if(characterEntity == null) throw new RuntimeException("Character doesn't exist.");
        characterRepository.delete(characterEntity);
    }

    public CharacterDto getCharacterById(long id) {
        CharacterEntity characterEntity = characterRepository.findById(id);
        if(characterEntity == null) throw new RuntimeException("Character doesn't exist.");
        return mapper.map(characterEntity, CharacterDto.class);
    }

    public Page<CharacterDto> getCharactersByName(String name, int page, int limit, String sortBy, String sortDir) {
        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<CharacterToShowDto> characterEntities = characterRepository.findByNameIgnoreCaseContaining(pageable, name);
        return mapper.map(characterEntities, Page.class);
    }

    public Page<CharacterDto> getCharactersByAge(Integer age, int page, int limit, String sortBy, String sortDir) {
        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );

        Page<CharacterToShowDto> characterEntities = characterRepository.findByAge(pageable, age);
        return mapper.map(characterEntities, Page.class);
    }

    public Page<MovieCharactersDto> getCharactersByMovie(long id, int page, int limit, String sortBy, String sortDir) {
        MovieEntity movieEntity = movieRepository.findById(id);
        if (movieEntity == null) throw new RuntimeException("Movie doesn't exist.");

        if (page > 0) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(
                page, limit,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
        );


        Page<MovieCharactersDto> characters = movieDetailRepository.findByMovie(pageable, movieEntity);
        return characters;
    }

    public void uploadCharacterImage(long id, MultipartFile file) {
        //1. Check if image isn't empty
        imageService.fileIsEmpty(file);

        //2. Check if the file is a valid image
        imageService.isAnImage(file);

        //3. Check if character exists in the DB
        CharacterEntity character = mapper.map(getCharacterById(id), CharacterEntity.class);

        //4. Save file metadata
        Map<String, String> metadata = imageService.exctractMetadata(file);

        //5. Store the image in s3 and update Character image link with the image link from s3
        //Path -> bucket name + folder name, the folder name gonna be equal to character id - character name
        String path = String.format("%s/%s", BucketName.ITEM_IMAGE.getBucketName(), character.getId() + "-" + character.getName());
        String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
            character.setImageLink(filename);
            characterRepository.save(character);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public byte[] downloadCharacterImage(long id) {
        CharacterDto character = getCharacterById(id);
        String path = String.format("%s/%s", BucketName.ITEM_IMAGE.getBucketName(), character.getId() + "-" + character.getName());
        return fileStore.download(path, character.getImageLink());
    }

}
