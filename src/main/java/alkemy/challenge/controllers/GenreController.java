package alkemy.challenge.controllers;

import alkemy.challenge.dtos.GenreDto;
import alkemy.challenge.models.requests.GenreRequest;
import alkemy.challenge.models.responses.GenreResponse;
import alkemy.challenge.models.responses.OperationStatusModel;
import alkemy.challenge.services.GenreService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreController {

    @Autowired
    GenreService genreService;

    @Autowired
    ModelMapper mapper;

    @GetMapping
    public List<GenreResponse> getGenres() {
        List<GenreDto> genresDto = genreService.getGenres();
        List<GenreResponse> genresResponse = new ArrayList<>();
        genresDto.forEach(g -> genresResponse.add(mapper.map(g, GenreResponse.class)));
        return genresResponse;
    }

    @GetMapping(path = "/{id}")
    public GenreResponse getGenreById(@PathVariable long id) {
        GenreDto genreDto = genreService.getGenreById(id);
        return mapper.map(genreDto, GenreResponse.class);
    }

    @PostMapping
    public GenreResponse createGenre(@Valid @RequestBody GenreRequest genreRequest) {
        GenreDto genreDto = genreService.createGenre(mapper.map(genreRequest, GenreDto.class));
        return mapper.map(genreDto, GenreResponse.class);
    }

    @PutMapping(path = "/{id}")
    public GenreResponse updateGenre(@PathVariable long id, @Valid @RequestBody GenreRequest genreRequest) {
        GenreDto genreDto = genreService.updateGenre(id, mapper.map(genreRequest, GenreDto.class));
        return mapper.map(genreDto, GenreResponse.class);
    }

    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deleteGenre(@PathVariable long id) {
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setName("DELETE");
        genreService.deleteGenre(id);
        operationStatusModel.setResult("SUCCESS");
        return operationStatusModel;
    }

}
