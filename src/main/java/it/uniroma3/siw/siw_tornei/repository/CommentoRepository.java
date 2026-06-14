package it.uniroma3.siw.siw_tornei.repository;

import it.uniroma3.siw.siw_tornei.model.Commento;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentoRepository extends CrudRepository<Commento, Long> {

    // Tutti i commenti di una partita, ordinati dal più recente
    List<Commento> findByPartitaIdOrderByDataCreazioneDesc(Long partitaId);

    // Tutti i commenti scritti da un certo utente per una partita
    List<Commento> findByPartitaIdAndAutoreId(Long partitaId, Long autoreId);
}
