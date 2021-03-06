package alkemy.challenge;

import alkemy.challenge.dtos.CharacterDto;
import alkemy.challenge.dtos.MovieDto;
import alkemy.challenge.models.responses.CharacterResponse;
import alkemy.challenge.models.responses.MovieResponse;
import alkemy.challenge.security.AppProperties;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class AndinochallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AndinochallengeApplication.class, args);
	}

	//Creamos una sola instancia de esta clase para poder utilizarlas en todas partes
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}

	@Bean(name = "AppProperties")
	public AppProperties getAppProperties() {
		return new AppProperties();
	}

	//Para tener una instancia ModelMapper global para no estar instanciando cada rato
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		//mapper.typeMap(MovieDto.class, MovieResponse.class).addMappings(m -> m.skip(MovieResponse::setMovieDetails));
		//mapper.typeMap(CharacterDto.class, CharacterResponse.class).addMappings(m -> m.skip(CharacterResponse::setMovies));
		return mapper;
	}

}
