package it.uniroma3.siw.siw_tornei.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Giocatore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome; // [cite: 35]
    private String cognome; // [cite: 36]
    private LocalDate dataNascita; // [cite: 37]
    private String ruolo; // [cite: 38]
    private Float altezza; // [cite: 39]

    @ManyToOne // 
    private Squadra squadra;

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public LocalDate getDataNascita() { return dataNascita; }
    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }

    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }

    public Float getAltezza() { return altezza; }
    public void setAltezza(Float altezza) { this.altezza = altezza; }

    public Squadra getSquadra() { return squadra; }
    public void setSquadra(Squadra squadra) { this.squadra = squadra; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Giocatore giocatore = (Giocatore) o;
        return Objects.equals(nome, giocatore.nome) && 
               Objects.equals(cognome, giocatore.cognome) && 
               Objects.equals(dataNascita, giocatore.dataNascita);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, cognome, dataNascita);
    }
}