package pwabd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Disciplina.
 */
@Entity
@Table(name = "disciplina")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Disciplina implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 15)
    @Column(name = "denumire", length = 15, nullable = false)
    private String denumire;

    @NotNull
    @Size(min = 30)
    @Column(name = "descriere", nullable = false)
    private String descriere;

    @NotNull
    @Min(value = 2)
    @Max(value = 6)
    @Column(name = "puncte_credit", nullable = false)
    private Integer puncteCredit;

    @NotNull
    @Min(value = 1)
    @Max(value = 4)
    @Column(name = "an_de_studiu", nullable = false)
    private Integer anDeStudiu;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenumire() {
        return denumire;
    }

    public Disciplina denumire(String denumire) {
        this.denumire = denumire;
        return this;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getDescriere() {
        return descriere;
    }

    public Disciplina descriere(String descriere) {
        this.descriere = descriere;
        return this;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    public Integer getPuncteCredit() {
        return puncteCredit;
    }

    public Disciplina puncteCredit(Integer puncteCredit) {
        this.puncteCredit = puncteCredit;
        return this;
    }

    public void setPuncteCredit(Integer puncteCredit) {
        this.puncteCredit = puncteCredit;
    }

    public Integer getAnDeStudiu() {
        return anDeStudiu;
    }

    public Disciplina anDeStudiu(Integer anDeStudiu) {
        this.anDeStudiu = anDeStudiu;
        return this;
    }

    public void setAnDeStudiu(Integer anDeStudiu) {
        this.anDeStudiu = anDeStudiu;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Disciplina disciplina = (Disciplina) o;
        if (disciplina.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), disciplina.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Disciplina{" +
            "id=" + getId() +
            ", denumire='" + getDenumire() + "'" +
            ", descriere='" + getDescriere() + "'" +
            ", puncteCredit=" + getPuncteCredit() +
            ", anDeStudiu=" + getAnDeStudiu() +
            "}";
    }
}
