package pwabd.web.rest;

import pwabd.CatalogApp;

import pwabd.domain.Nota;
import pwabd.repository.NotaRepository;
import pwabd.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static pwabd.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the NotaResource REST controller.
 *
 * @see NotaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatalogApp.class)
public class NotaResourceIntTest {

    private static final Integer DEFAULT_NUMAR_PUNCTE = 1;
    private static final Integer UPDATED_NUMAR_PUNCTE = 2;

    private static final Integer DEFAULT_NOTA = 1;
    private static final Integer UPDATED_NOTA = 2;

    private static final Instant DEFAULT_DATA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restNotaMockMvc;

    private Nota nota;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotaResource notaResource = new NotaResource(notaRepository);
        this.restNotaMockMvc = MockMvcBuilders.standaloneSetup(notaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Nota createEntity(EntityManager em) {
        Nota nota = new Nota()
            .numarPuncte(DEFAULT_NUMAR_PUNCTE)
            .nota(DEFAULT_NOTA)
            .data(DEFAULT_DATA);
        return nota;
    }

    @Before
    public void initTest() {
        nota = createEntity(em);
    }

    @Test
    @Transactional
    public void createNota() throws Exception {
        int databaseSizeBeforeCreate = notaRepository.findAll().size();

        // Create the Nota
        restNotaMockMvc.perform(post("/api/notas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nota)))
            .andExpect(status().isCreated());

        // Validate the Nota in the database
        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeCreate + 1);
        Nota testNota = notaList.get(notaList.size() - 1);
        assertThat(testNota.getNumarPuncte()).isEqualTo(DEFAULT_NUMAR_PUNCTE);
        assertThat(testNota.getNota()).isEqualTo(DEFAULT_NOTA);
        assertThat(testNota.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    public void createNotaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notaRepository.findAll().size();

        // Create the Nota with an existing ID
        nota.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotaMockMvc.perform(post("/api/notas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nota)))
            .andExpect(status().isBadRequest());

        // Validate the Nota in the database
        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumarPuncteIsRequired() throws Exception {
        int databaseSizeBeforeTest = notaRepository.findAll().size();
        // set the field null
        nota.setNumarPuncte(null);

        // Create the Nota, which fails.

        restNotaMockMvc.perform(post("/api/notas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nota)))
            .andExpect(status().isBadRequest());

        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNotaIsRequired() throws Exception {
        int databaseSizeBeforeTest = notaRepository.findAll().size();
        // set the field null
        nota.setNota(null);

        // Create the Nota, which fails.

        restNotaMockMvc.perform(post("/api/notas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nota)))
            .andExpect(status().isBadRequest());

        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = notaRepository.findAll().size();
        // set the field null
        nota.setData(null);

        // Create the Nota, which fails.

        restNotaMockMvc.perform(post("/api/notas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nota)))
            .andExpect(status().isBadRequest());

        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotas() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get all the notaList
        restNotaMockMvc.perform(get("/api/notas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nota.getId().intValue())))
            .andExpect(jsonPath("$.[*].numarPuncte").value(hasItem(DEFAULT_NUMAR_PUNCTE)))
            .andExpect(jsonPath("$.[*].nota").value(hasItem(DEFAULT_NOTA)))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }
    
    @Test
    @Transactional
    public void getNota() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        // Get the nota
        restNotaMockMvc.perform(get("/api/notas/{id}", nota.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(nota.getId().intValue()))
            .andExpect(jsonPath("$.numarPuncte").value(DEFAULT_NUMAR_PUNCTE))
            .andExpect(jsonPath("$.nota").value(DEFAULT_NOTA))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNota() throws Exception {
        // Get the nota
        restNotaMockMvc.perform(get("/api/notas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNota() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        int databaseSizeBeforeUpdate = notaRepository.findAll().size();

        // Update the nota
        Nota updatedNota = notaRepository.findById(nota.getId()).get();
        // Disconnect from session so that the updates on updatedNota are not directly saved in db
        em.detach(updatedNota);
        updatedNota
            .numarPuncte(UPDATED_NUMAR_PUNCTE)
            .nota(UPDATED_NOTA)
            .data(UPDATED_DATA);

        restNotaMockMvc.perform(put("/api/notas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNota)))
            .andExpect(status().isOk());

        // Validate the Nota in the database
        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeUpdate);
        Nota testNota = notaList.get(notaList.size() - 1);
        assertThat(testNota.getNumarPuncte()).isEqualTo(UPDATED_NUMAR_PUNCTE);
        assertThat(testNota.getNota()).isEqualTo(UPDATED_NOTA);
        assertThat(testNota.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    public void updateNonExistingNota() throws Exception {
        int databaseSizeBeforeUpdate = notaRepository.findAll().size();

        // Create the Nota

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotaMockMvc.perform(put("/api/notas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nota)))
            .andExpect(status().isBadRequest());

        // Validate the Nota in the database
        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNota() throws Exception {
        // Initialize the database
        notaRepository.saveAndFlush(nota);

        int databaseSizeBeforeDelete = notaRepository.findAll().size();

        // Get the nota
        restNotaMockMvc.perform(delete("/api/notas/{id}", nota.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Nota> notaList = notaRepository.findAll();
        assertThat(notaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Nota.class);
        Nota nota1 = new Nota();
        nota1.setId(1L);
        Nota nota2 = new Nota();
        nota2.setId(nota1.getId());
        assertThat(nota1).isEqualTo(nota2);
        nota2.setId(2L);
        assertThat(nota1).isNotEqualTo(nota2);
        nota1.setId(null);
        assertThat(nota1).isNotEqualTo(nota2);
    }
}
