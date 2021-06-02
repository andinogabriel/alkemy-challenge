package alkemy.challenge.dtos;

import java.math.BigDecimal;

public interface MovieCharactersDto {

    CharacterEntity getCharacter();

    interface CharacterEntity {
        Long getId();
        String getName();
        String getImageLink();
        BigDecimal getWeight();
        Integer getAge();
        String getStory();
    }

}
