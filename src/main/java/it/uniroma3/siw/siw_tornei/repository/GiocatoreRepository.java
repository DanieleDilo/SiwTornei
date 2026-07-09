package it.uniroma3.siw.siw_tornei.repository;

import it.uniroma3.siw.siw_tornei.model.Giocatore;
import it.uniroma3.siw.siw_tornei.model.Squadra;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface GiocatoreRepository extends CrudRepository<Giocatore, Long> {
    List<Giocatore> findByCognome(String cognome);

    List<Giocatore> findAll();

    @Query("SELECT g FROM Giocatore g WHERE " +
            "LOWER(g.nome) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(g.cognome) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "ORDER BY g.cognome ASC, g.nome ASC")
    List<Giocatore> searchByTerm(@Param("term") String term);

    List<Giocatore> findAllByOrderByCognomeDescNomeDesc();

    @Query("SELECT g FROM Giocatore g WHERE g.squadra.id = :squadraId ORDER BY g.cognome ASC, g.nome ASC")
    List<Giocatore> findBySquadraOrderByCognomeAscNomeAsc(@Param("squadraId") Long squadraId);

    List<Giocatore> findAllByOrderByDataNascitaAsc();
}