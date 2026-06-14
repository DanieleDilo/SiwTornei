package it.uniroma3.siw.siw_tornei.repository;

import it.uniroma3.siw.siw_tornei.model.Partita;
import it.uniroma3.siw.siw_tornei.model.Torneo;
import it.uniroma3.siw.siw_tornei.model.Squadra;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface PartitaRepository extends CrudRepository<Partita, Long> {
    List<Partita> findByTorneo(Torneo torneo);

    @Query("SELECT p FROM Partita p WHERE p.squadraCasa = :squadra OR p.squadraTrasferta = :squadra")
    List<Partita> findBySquadra(@Param("squadra") Squadra squadra);
}