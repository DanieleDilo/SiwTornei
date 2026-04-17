package it.uniroma3.siw.siw_tornei.model;

import jakarta.persistence.*;
import java.util.List;

@SuppressWarnings("unused")
@Entity
public class Torneo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private Integer anno;
    private String descrizione;

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

    // Qui andranno le relazioni con Squadra e Partita [cite: 50]

}