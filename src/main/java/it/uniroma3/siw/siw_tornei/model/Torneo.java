package it.uniroma3.siw.siw_tornei.model;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.List;

@Entity
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private Integer anno;

    @Column(length = 500)
    private String descrizione;

    // Aggiungi questo dentro la classe Torneo
    @ManyToMany(mappedBy = "tornei")
    private List<Squadra> squadre;

    public List<Squadra> getSquadre() {
        return squadre;
    }

    public void setSquadre(List<Squadra> squadre) {
        this.squadre = squadre;
    }

    // Qui in futuro aggiungeremo le relazioni con Squadra e Partita

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    // Override di equals e hashCode (buona pratica per JPA)
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Torneo torneo = (Torneo) o;
        return Objects.equals(nome, torneo.nome) && Objects.equals(anno, torneo.anno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, anno);
    }
}