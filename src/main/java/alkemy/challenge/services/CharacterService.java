package alkemy.challenge.services;

import alkemy.challenge.dtos.CharacterDto;
import alkemy.challenge.entities.CharacterEntity;
import alkemy.challenge.repositories.CharacterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CharacterService {

    @Autowired
    CharacterRepository characterRepository;

    @Autowired
    ModelMapper mapper;

    public CharacterDto createCharacter(CharacterDto character) {
        CharacterEntity characterEntity = new CharacterEntity();

        characterEntity.setAge(character.getAge());
        characterEntity.setImage(character.getImage());
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
        characterEntity.setImage(characterDto.getImage());
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

        Page<CharacterEntity> characterEntities = characterRepository.findByNameIgnoreCaseContaining(pageable, name);
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

        Page<CharacterEntity> characterEntities = characterRepository.findByAge(pageable, age);
        return mapper.map(characterEntities, Page.class);
    }

}
