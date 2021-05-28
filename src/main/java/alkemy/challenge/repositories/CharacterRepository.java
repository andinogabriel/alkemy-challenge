package alkemy.challege.repositories;

import alkemy.challege.entities.CharacterEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity, Long> {

    Page<CharacterEntity> findByName(Pageable pageable, String name);

    Page<CharacterEntity> findByAge(Pageable pageable, Integer age);


}
