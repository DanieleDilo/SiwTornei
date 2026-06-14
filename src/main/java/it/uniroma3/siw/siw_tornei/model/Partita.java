package it.uniroma3.siw.siw_tornei.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Partita {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "La data e l'ora della partita sono obbligatorie")
    private LocalDateTime dataOra; 

    @NotBlank(message = "Il luogo della partita è obbligatorio")
    private String luogo; 
    
    @Min(value = 0, message = "I gol non possono essere negativi")
    private Integer goalsHome;

    @Min(value = 0, message = "I gol non possono essere negativi")
    private Integer goalsAway; 

    @Enumerated(EnumType.STRING)
    private StatoPartita stato; 

    @ManyToOne
    @NotNull(message = "Il torneo è obbligatorio")
    private Torneo torneo;

    @ManyToOne
    @NotNull(message = "La squadra di casa è obbligatoria")
    private Squadra squadraCasa;

    @ManyToOne
    @NotNull(message = "La squadra in trasferta è obbligatoria")
    private Squadra squadraTrasferta;

    @ManyToOne
    @NotNull(message = "L'arbitro è obbligatorio")
    private Arbitro arbitro;

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public String getLuogo() { return luogo; }
    public void setLuogo(String luogo) { this.luogo = luogo; }

    public Integer getGoalsHome() { return goalsHome; }
    public void setGoalsHome(Integer goalsHome) { this.goalsHome = goalsHome; }

    public Integer getGoalsAway() { return goalsAway; }
    public void setGoalsAway(Integer goalsAway) { this.goalsAway = goalsAway; }

    public StatoPartita getStato() { return stato; }
    public void setStato(StatoPartita stato) { this.stato = stato; }

    public Torneo getTorneo() { return torneo; }
    public void setTorneo(Torneo torneo) { this.torneo = torneo; }

    public Squadra getSquadraCasa() { return squadraCasa; }
    public void setSquadraCasa(Squadra squadraCasa) { this.squadraCasa = squadraCasa; }

    public Squadra getSquadraTrasferta() { return squadraTrasferta; }
    public void setSquadraTrasferta(Squadra squadraTrasferta) { this.squadraTrasferta = squadraTrasferta; }

    public Arbitro getArbitro() { return arbitro; }
    public void setArbitro(Arbitro arbitro) { this.arbitro = arbitro; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partita partita = (Partita) o;
        return Objects.equals(dataOra, partita.dataOra) && 
               Objects.equals(squadraCasa, partita.squadraCasa) && 
               Objects.equals(squadraTrasferta, partita.squadraTrasferta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataOra, squadraCasa, squadraTrasferta);
    }
}