package alkemy.challenge.repositories;

import alkemy.challenge.entities.CharacterEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity, Long> {

    CharacterEntity findById(long id);

    Page<CharacterEntity> findByNameIgnoreCaseContaining(Pageable pageable, String name);

    Page<CharacterEntity> findByAge(Pageable pageable, Integer age);

    @Query(value = "SELECT name, image FROM characters", countQuery = "SELECT COUNT(name) FROM characters", nativeQuery = true)
    Page<Object[]> getCharacters(Pageable pageable);

}
