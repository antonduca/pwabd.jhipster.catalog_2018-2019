package pwabd.repository;

import pwabd.domain.Disciplina;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Disciplina entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

}
