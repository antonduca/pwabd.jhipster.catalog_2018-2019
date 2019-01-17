package pwabd.web.rest;

import pwabd.CatalogApp;

import pwabd.domain.Disciplina;
import pwabd.repository.DisciplinaRepository;
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
import java.util.List;


import static pwabd.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the DisciplinaResource REST controller.
 *
 * @see DisciplinaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CatalogApp.class)
public class DisciplinaResourceIntTest {

    private static final String DEFAULT_DENUMIRE = "AAAAAAAAAA";
    private static final String UPDATED_DENUMIRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIERE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_DESCRIERE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_PUNCTE_CREDIT = 2;
    private static final Integer UPDATED_PUNCTE_CREDIT = 3;

    private static final Integer DEFAULT_AN_DE_STUDIU = 1;
    private static final Integer UPDATED_AN_DE_STUDIU = 2;

    @Autowired
    private DisciplinaRepository disciplinaRepository;

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

    private MockMvc restDisciplinaMockMvc;

    private Disciplina disciplina;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DisciplinaResource disciplinaResource = new DisciplinaResource(disciplinaRepository);
        this.restDisciplinaMockMvc = MockMvcBuilders.standaloneSetup(disciplinaResource)
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
    public static Disciplina createEntity(EntityManager em) {
        Disciplina disciplina = new Disciplina()
            .denumire(DEFAULT_DENUMIRE)
            .descriere(DEFAULT_DESCRIERE)
            .puncteCredit(DEFAULT_PUNCTE_CREDIT)
            .anDeStudiu(DEFAULT_AN_DE_STUDIU);
        return disciplina;
    }

    @Before
    public void initTest() {
        disciplina = createEntity(em);
    }

    @Test
    @Transactional
    public void createDisciplina() throws Exception {
        int databaseSizeBeforeCreate = disciplinaRepository.findAll().size();

        // Create the Disciplina
        restDisciplinaMockMvc.perform(post("/api/disciplinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disciplina)))
            .andExpect(status().isCreated());

        // Validate the Disciplina in the database
        List<Disciplina> disciplinaList = disciplinaRepository.findAll();
        assertThat(disciplinaList).hasSize(databaseSizeBeforeCreate + 1);
        Disciplina testDisciplina = disciplinaList.get(disciplinaList.size() - 1);
        assertThat(testDisciplina.getDenumire()).isEqualTo(DEFAULT_DENUMIRE);
        assertThat(testDisciplina.getDescriere()).isEqualTo(DEFAULT_DESCRIERE);
        assertThat(testDisciplina.getPuncteCredit()).isEqualTo(DEFAULT_PUNCTE_CREDIT);
        assertThat(testDisciplina.getAnDeStudiu()).isEqualTo(DEFAULT_AN_DE_STUDIU);
    }

    @Test
    @Transactional
    public void createDisciplinaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = disciplinaRepository.findAll().size();

        // Create the Disciplina with an existing ID
        disciplina.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisciplinaMockMvc.perform(post("/api/disciplinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disciplina)))
            .andExpect(status().isBadRequest());

        // Validate the Disciplina in the database
        List<Disciplina> disciplinaList = disciplinaRepository.findAll();
        assertThat(disciplinaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDenumireIsRequired() throws Exception {
        int databaseSizeBeforeTest = disciplinaRepository.findAll().size();
        // set the field null
        disciplina.setDenumire(null);

        // Create the Disciplina, which fails.

        restDisciplinaMockMvc.perform(post("/api/disciplinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disciplina)))
            .andExpect(status().isBadRequest());

        List<Disciplina> disciplinaList = disciplinaRepository.findAll();
        assertThat(disciplinaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriereIsRequired() throws Exception {
        int databaseSizeBeforeTest = disciplinaRepository.findAll().size();
        // set the field null
        disciplina.setDescriere(null);

        // Create the Disciplina, which fails.

        restDisciplinaMockMvc.perform(post("/api/disciplinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disciplina)))
            .andExpect(status().isBadRequest());

        List<Disciplina> disciplinaList = disciplinaRepository.findAll();
        assertThat(disciplinaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPuncteCreditIsRequired() throws Exception {
        int databaseSizeBeforeTest = disciplinaRepository.findAll().size();
        // set the field null
        disciplina.setPuncteCredit(null);

        // Create the Disciplina, which fails.

        restDisciplinaMockMvc.perform(post("/api/disciplinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disciplina)))
            .andExpect(status().isBadRequest());

        List<Disciplina> disciplinaList = disciplinaRepository.findAll();
        assertThat(disciplinaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAnDeStudiuIsRequired() throws Exception {
        int databaseSizeBeforeTest = disciplinaRepository.findAll().size();
        // set the field null
        disciplina.setAnDeStudiu(null);

        // Create the Disciplina, which fails.

        restDisciplinaMockMvc.perform(post("/api/disciplinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disciplina)))
            .andExpect(status().isBadRequest());

        List<Disciplina> disciplinaList = disciplinaRepository.findAll();
        assertThat(disciplinaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDisciplinas() throws Exception {
        // Initialize the database
        disciplinaRepository.saveAndFlush(disciplina);

        // Get all the disciplinaList
        restDisciplinaMockMvc.perform(get("/api/disciplinas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disciplina.getId().intValue())))
            .andExpect(jsonPath("$.[*].denumire").value(hasItem(DEFAULT_DENUMIRE.toString())))
            .andExpect(jsonPath("$.[*].descriere").value(hasItem(DEFAULT_DESCRIERE.toString())))
            .andExpect(jsonPath("$.[*].puncteCredit").value(hasItem(DEFAULT_PUNCTE_CREDIT)))
            .andExpect(jsonPath("$.[*].anDeStudiu").value(hasItem(DEFAULT_AN_DE_STUDIU)));
    }
    
    @Test
    @Transactional
    public void getDisciplina() throws Exception {
        // Initialize the database
        disciplinaRepository.saveAndFlush(disciplina);

        // Get the disciplina
        restDisciplinaMockMvc.perform(get("/api/disciplinas/{id}", disciplina.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(disciplina.getId().intValue()))
            .andExpect(jsonPath("$.denumire").value(DEFAULT_DENUMIRE.toString()))
            .andExpect(jsonPath("$.descriere").value(DEFAULT_DESCRIERE.toString()))
            .andExpect(jsonPath("$.puncteCredit").value(DEFAULT_PUNCTE_CREDIT))
            .andExpect(jsonPath("$.anDeStudiu").value(DEFAULT_AN_DE_STUDIU));
    }

    @Test
    @Transactional
    public void getNonExistingDisciplina() throws Exception {
        // Get the disciplina
        restDisciplinaMockMvc.perform(get("/api/disciplinas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDisciplina() throws Exception {
        // Initialize the database
        disciplinaRepository.saveAndFlush(disciplina);

        int databaseSizeBeforeUpdate = disciplinaRepository.findAll().size();

        // Update the disciplina
        Disciplina updatedDisciplina = disciplinaRepository.findById(disciplina.getId()).get();
        // Disconnect from session so that the updates on updatedDisciplina are not directly saved in db
        em.detach(updatedDisciplina);
        updatedDisciplina
            .denumire(UPDATED_DENUMIRE)
            .descriere(UPDATED_DESCRIERE)
            .puncteCredit(UPDATED_PUNCTE_CREDIT)
            .anDeStudiu(UPDATED_AN_DE_STUDIU);

        restDisciplinaMockMvc.perform(put("/api/disciplinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDisciplina)))
            .andExpect(status().isOk());

        // Validate the Disciplina in the database
        List<Disciplina> disciplinaList = disciplinaRepository.findAll();
        assertThat(disciplinaList).hasSize(databaseSizeBeforeUpdate);
        Disciplina testDisciplina = disciplinaList.get(disciplinaList.size() - 1);
        assertThat(testDisciplina.getDenumire()).isEqualTo(UPDATED_DENUMIRE);
        assertThat(testDisciplina.getDescriere()).isEqualTo(UPDATED_DESCRIERE);
        assertThat(testDisciplina.getPuncteCredit()).isEqualTo(UPDATED_PUNCTE_CREDIT);
        assertThat(testDisciplina.getAnDeStudiu()).isEqualTo(UPDATED_AN_DE_STUDIU);
    }

    @Test
    @Transactional
    public void updateNonExistingDisciplina() throws Exception {
        int databaseSizeBeforeUpdate = disciplinaRepository.findAll().size();

        // Create the Disciplina

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisciplinaMockMvc.perform(put("/api/disciplinas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(disciplina)))
            .andExpect(status().isBadRequest());

        // Validate the Disciplina in the database
        List<Disciplina> disciplinaList = disciplinaRepository.findAll();
        assertThat(disciplinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDisciplina() throws Exception {
        // Initialize the database
        disciplinaRepository.saveAndFlush(disciplina);

        int databaseSizeBeforeDelete = disciplinaRepository.findAll().size();

        // Get the disciplina
        restDisciplinaMockMvc.perform(delete("/api/disciplinas/{id}", disciplina.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Disciplina> disciplinaList = disciplinaRepository.findAll();
        assertThat(disciplinaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Disciplina.class);
        Disciplina disciplina1 = new Disciplina();
        disciplina1.setId(1L);
        Disciplina disciplina2 = new Disciplina();
        disciplina2.setId(disciplina1.getId());
        assertThat(disciplina1).isEqualTo(disciplina2);
        disciplina2.setId(2L);
        assertThat(disciplina1).isNotEqualTo(disciplina2);
        disciplina1.setId(null);
        assertThat(disciplina1).isNotEqualTo(disciplina2);
    }
}
