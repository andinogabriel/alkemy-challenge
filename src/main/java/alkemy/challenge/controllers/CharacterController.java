package alkemy.challenge.controllers;

import alkemy.challenge.dtos.CharacterDto;
import alkemy.challenge.models.requests.CharacterRequest;
import alkemy.challenge.models.responses.CharacterResponse;
import alkemy.challenge.models.responses.OperationStatusModel;
import alkemy.challenge.services.CharacterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/characters")
public class CharacterController {

    @Autowired
    CharacterService characterService;

    @Autowired
    ModelMapper mapper;

    //List of characters
    @GetMapping
    public Page<CharacterResponse> getCharacters(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "age", required = false) Integer age, @RequestParam(value = "movies", required = false) Long idMovie, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value="limit", defaultValue = "5") int limit, @RequestParam(value = "sortBy", defaultValue = "name") String sortBy, @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        Page<CharacterDto> characters = null;
        if (name == null && age == null && idMovie == null) {
            characters = characterService.getCharacters(page, limit, sortBy, sortDir);
        } else if (name == null && age != null && idMovie == null) {
            characters = characterService.getCharactersByAge(age, page, limit, sortBy, sortDir);
        } else if (name == null && age == null & idMovie != null) {
            long movieId = idMovie.longValue();
        }
        return mapper.map(characters, Page.class);
    }

    @GetMapping(path = "/{id}")
    public CharacterResponse getCharacterById(@PathVariable long id) {
        CharacterDto characterDto = characterService.getCharacterById(id);
        return mapper.map(characterDto, CharacterResponse.class);
    }

    @PostMapping
    public CharacterResponse createCharacter(@Valid @RequestBody CharacterRequest characterRequest) {
        CharacterDto characterDto = mapper.map(characterRequest, CharacterDto.class);
        CharacterDto createdCharacter = characterService.createCharacter(characterDto);
        return mapper.map(createdCharacter, CharacterResponse.class);
    }

    @PutMapping(path = "/{id}")
    public CharacterResponse updateCharacter(@PathVariable long id, @Valid @RequestBody CharacterRequest characterRequest) {
        CharacterDto characterDto = characterService.updateCharacter(id, mapper.map(characterRequest, CharacterDto.class));
        return mapper.map(characterDto, CharacterResponse.class);
    }

    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deleteCharacter(@PathVariable long id) {
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setName("DELETE");
        characterService.deleteCharacter(id);
        operationStatusModel.setResult("SUCCESS");
        return operationStatusModel;
    }

    @GetMapping(path = "/name")
    public Page<CharacterResponse> getCharactersByName(@RequestParam(value = "name") String name, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value="limit", defaultValue = "5") int limit, @RequestParam(value = "sortBy", defaultValue = "name") String sortBy, @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        Page<CharacterDto> charactersDto = characterService.getCharactersByName(name, page, limit, sortBy, sortDir);
        return mapper.map(charactersDto, Page.class);
    }

    @GetMapping(path = "/age")
    public Page<CharacterResponse> getCharactersByAge(@RequestParam(value = "age") Integer age, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value="limit", defaultValue = "5") int limit, @RequestParam(value = "sortBy", defaultValue = "name") String sortBy, @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        Page<CharacterDto> charactersDto = characterService.getCharactersByAge(age, page, limit, sortBy, sortDir);
        return mapper.map(charactersDto, Page.class);
    }

}
