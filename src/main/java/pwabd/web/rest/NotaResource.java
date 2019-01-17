package pwabd.web.rest;

import com.codahale.metrics.annotation.Timed;
import pwabd.domain.Nota;
import pwabd.repository.NotaRepository;
import pwabd.security.AuthoritiesConstants;
import pwabd.security.SecurityUtils;
import pwabd.web.rest.errors.BadRequestAlertException;
import pwabd.web.rest.util.HeaderUtil;
import pwabd.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Nota.
 */
@RestController
@RequestMapping("/api")
public class NotaResource {

    private final Logger log = LoggerFactory.getLogger(NotaResource.class);

    private static final String ENTITY_NAME = "nota";

    private final NotaRepository notaRepository;

    public NotaResource(NotaRepository notaRepository) {
        this.notaRepository = notaRepository;
    }

    /**
     * POST /notas : Create a new nota.
     *
     * @param nota the nota to create
     * @return the ResponseEntity with status 201 (Created) and with body the new
     *         nota, or with status 400 (Bad Request) if the nota has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/notas")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Nota> createNota(@Valid @RequestBody Nota nota) throws URISyntaxException {
        log.debug("REST request to save Nota : {}", nota);
        if (nota.getId() != null) {
            throw new BadRequestAlertException("A new nota cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Nota result = notaRepository.save(nota);
        return ResponseEntity.created(new URI("/api/notas/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
    }

    /**
     * PUT /notas : Updates an existing nota.
     *
     * @param nota the nota to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     *         nota, or with status 400 (Bad Request) if the nota is not valid, or
     *         with status 500 (Internal Server Error) if the nota couldn't be
     *         updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/notas")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Nota> updateNota(@Valid @RequestBody Nota nota) throws URISyntaxException {
        log.debug("REST request to update Nota : {}", nota);
        if (nota.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Nota result = notaRepository.save(nota);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, nota.getId().toString()))
                .body(result);
    }

    /**
     * GET /notas : get all the notas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notas in body
     */
    @GetMapping("/notas")
    @Timed
    public ResponseEntity<List<Nota>> getAllNotas(Pageable pageable) {
        log.debug("REST request to get a page of Notas");

        if (SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            Page<Nota> page = notaRepository.findAll(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notas");
            return ResponseEntity.ok().headers(headers).body(page.getContent());

        } else {
            // Page<Nota> page = notaRepository.findAll(pageable);
            Page<Nota> page = notaRepository.findByUserIsCurrentUser(pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notas");
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
    }

    /**
     * GET /notas/:id : get the "id" nota.
     *
     * @param id the id of the nota to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the nota, or
     *         with status 404 (Not Found)
     */
    @GetMapping("/notas/{id}")
    @Timed
    public ResponseEntity<?> getNota(@PathVariable Long id) {
        log.debug("REST request to get Nota : {}", id);
        Optional<Nota> nota = notaRepository.findById(id);
        if (nota.isPresent() && nota.get().getUser() != null
                && !nota.get().getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))
                && !SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            return new ResponseEntity<>("error.http.403", HttpStatus.FORBIDDEN);
        }
        return ResponseUtil.wrapOrNotFound(nota);
    }

    /**
     * DELETE /notas/:id : delete the "id" nota.
     *
     * @param id the id of the nota to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/notas/{id}")
    @Timed
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteNota(@PathVariable Long id) {
        log.debug("REST request to delete Nota : {}", id);

        notaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
