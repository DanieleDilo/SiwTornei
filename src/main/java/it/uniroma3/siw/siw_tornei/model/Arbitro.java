package it.uniroma3.siw.siw_tornei.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.List;

@Entity
public class Arbitro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    private String cognome;
    
    @Column(unique = true) // Il codice deve essere unico per ogni arbitro
    @NotBlank(message = "Il codice arbitrale è obbligatorio")
    private String codiceArbitrale;

    // Relazione con le partite: un arbitro può dirigere molte partite
    @OneToMany(mappedBy = "arbitro")
    private List<Partita> partiteDirette;

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getCodiceArbitrale() { return codiceArbitrale; }
    public void setCodiceArbitrale(String codiceArbitrale) { this.codiceArbitrale = codiceArbitrale; }

    public List<Partita> getPartiteDirette() { return partiteDirette; }
    public void setPartiteDirette(List<Partita> partiteDirette) { this.partiteDirette = partiteDirette; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arbitro arbitro = (Arbitro) o;
        return Objects.equals(codiceArbitrale, arbitro.codiceArbitrale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codiceArbitrale);
    }
}