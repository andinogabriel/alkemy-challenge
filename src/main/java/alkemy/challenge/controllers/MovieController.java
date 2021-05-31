package alkemy.challenge.controllers;

import alkemy.challenge.dtos.MovieCreationDto;
import alkemy.challenge.dtos.MovieDetailCreationDto;
import alkemy.challenge.dtos.MovieDetailDto;
import alkemy.challenge.dtos.MovieDto;
import alkemy.challenge.models.requests.MovieDetailRequest;
import alkemy.challenge.models.requests.MovieRequest;
import alkemy.challenge.models.responses.MovieDetailResponse;
import alkemy.challenge.models.responses.MovieResponse;
import alkemy.challenge.models.responses.OperationStatusModel;
import alkemy.challenge.services.MovieDetailService;
import alkemy.challenge.services.MovieService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @Autowired
    MovieService movieService;

    @Autowired
    MovieDetailService movieDetailService;

    @Autowired
    ModelMapper mapper;

    @GetMapping
    public Page<MovieResponse> getMovies(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "genre", required = false) Long genreId, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value="limit", defaultValue = "5") int limit, @RequestParam(value = "sortBy", defaultValue = "title") String sortBy, @RequestParam(value = "order", required = false, defaultValue = "asc") String sortDir) {

        Page<MovieDto> movies = null;
        if(name == null && genreId == null) {
            movies = movieService.getMovies(page, limit, sortBy, sortDir);
        } else if (name != null && genreId == null) {
            movies = movieService.getMoviesByTitle(name, page, limit, sortBy, sortDir);
        } else if (name == null && genreId != null) {
            long genre = genreId.longValue();
            movies = movieService.getMoviesByGenre(genre, page, limit, sortBy, sortDir);
        } else if (name == null && genreId == null && sortDir != null)  {
            movies = movieService.getMoviesByOrderCreationDate(page, limit, sortDir);
        } else {
            movies = movieService.getSearchMovies(page, limit, name, genreId, sortDir);
        }

        return mapper.map(movies, Page.class);
    }


    @PostMapping
    public MovieResponse createMovie(@Valid @RequestBody MovieRequest movieRequest) {
        MovieDto movieDto = movieService.createMovie(mapper.map(movieRequest, MovieCreationDto.class));
        return mapper.map(movieDto, MovieResponse.class);
    }

    @PostMapping(path = "/detail")
    public MovieDetailResponse createMovieDetail(@Valid @RequestBody MovieDetailRequest movieDetailRequest) {
        MovieDetailDto movieDetailDto = movieDetailService.createMovieDetail(mapper.map(movieDetailRequest, MovieDetailCreationDto.class));
        return mapper.map(movieDetailDto, MovieDetailResponse.class);
    }

    @PutMapping(path = "/{id}")
    public MovieResponse updateMovie(@PathVariable long id, @Valid @RequestBody MovieRequest movieRequest) {
        MovieDto movieDto = movieService.updateMovie(id, mapper.map(movieRequest, MovieCreationDto.class));
        return mapper.map(movieDto, MovieResponse.class);
    }

    @DeleteMapping(path = "/{id}")
    public OperationStatusModel deleteMovie(@PathVariable long id) {
        OperationStatusModel operationStatusModel = new OperationStatusModel();
        operationStatusModel.setName("DELETE");
        movieService.deleteMovie(id);
        operationStatusModel.setResult("SUCCESS");
        return operationStatusModel;
    }

}
