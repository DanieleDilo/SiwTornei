package it.uniroma3.siw.siw_tornei.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Squadra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private Integer annoFondazione;
    private String citta;

    // Una squadra partecipa a molti tornei, e un torneo ha molte squadre
    @ManyToMany
    private List<Torneo> tornei;

    @OneToMany(mappedBy = "squadra") //
    private List<Giocatore> giocatori;

    public List<Giocatore> getGiocatori() {
        return giocatori;
    }

    public void setGiocatori(List<Giocatore> giocatori) {
        this.giocatori = giocatori;
    }

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

    public Integer getAnnoFondazione() {
        return annoFondazione;
    }

    public void setAnnoFondazione(Integer annoFondazione) {
        this.annoFondazione = annoFondazione;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public List<Torneo> getTornei() {
        return tornei;
    }

    public void setTornei(List<Torneo> tornei) {
        this.tornei = tornei;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Squadra squadra = (Squadra) o;
        return Objects.equals(nome, squadra.nome) && Objects.equals(citta, squadra.citta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, citta);
    }
}
