package it.uniroma3.siw.siw_tornei.repository;

import it.uniroma3.siw.siw_tornei.model.Giocatore;
import it.uniroma3.siw.siw_tornei.model.Squadra;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface SquadraRepository extends CrudRepository<Squadra, Long> {

    List<Squadra> findAllByOrderByAnnoFondazioneAscNomeAsc();

    List<Squadra> findAllByOrderByNomeDesc();

    List<Squadra> findByCitta(String citta);

    List<Squadra> findAllByOrderByCittaDesc();

   

    


}