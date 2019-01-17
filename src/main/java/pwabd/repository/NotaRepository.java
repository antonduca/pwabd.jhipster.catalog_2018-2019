package pwabd.repository;

import pwabd.domain.Nota;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Nota entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {

    @Query("select nota from Nota nota where nota.user.login = ?#{principal.username}")
    List<Nota> findByUserIsCurrentUser();

    @Query("select nota from Nota nota where nota.user.login = ?#{principal.username}")
    Page<Nota> findByUserIsCurrentUser(Pageable Pageable);

}
