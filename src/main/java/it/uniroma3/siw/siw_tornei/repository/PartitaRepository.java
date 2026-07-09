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

    List<Partita> findAllByOrderByDataOraDesc();

    @Query("SELECT p FROM Partita p WHERE p.stato = 'PLAYED' ORDER BY p.dataOra DESC")
    List<Partita> findAllPlayedByOrderByDataOraDesc();

    @Query(value = "SELECT * FROM partita ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Partita> findRandomPartite(@Param("limit") long limit);
    
    @Query("SELECT (sum(p.goalsHome + p.goalsAway), 0) FROM Partita p WHERE p.torneo.id = :torneoId")
    Long countGoalsByTorneoId(@Param("torneoId") Long torneoId);
}