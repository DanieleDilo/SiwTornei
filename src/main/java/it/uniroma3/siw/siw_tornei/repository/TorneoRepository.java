package it.uniroma3.siw.siw_tornei.repository;

import it.uniroma3.siw.siw_tornei.model.Torneo;
import it.uniroma3.siw.siw_tornei.model.Squadra;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

public interface TorneoRepository extends CrudRepository<Torneo, Long> {

    
    @Query("SELECT DISTINCT t FROM Torneo t LEFT JOIN FETCH t.squadre WHERE t.id = :id")
    Optional<Torneo> findByIdWithSquadre(@Param("id") Long id);

    @Query("SELECT DISTINCT s FROM Squadra s LEFT JOIN FETCH s.giocatori WHERE s IN :squadre")
    List<Squadra> fetchSquadreWithGiocatori(@Param("squadre") List<Squadra> squadre);

    @EntityGraph(attributePaths = {"squadre"})
    @Query("SELECT t FROM Torneo t WHERE t.id = :id")
    Optional<Torneo> findByIdWithSquadreEntityGraph(@Param("id") Long id);

    @Query("SELECT t FROM Torneo t") //lazy
    List<Torneo> findAll();

    List<Torneo> findByNomeContainingIgnoreCase(String nome);

    }