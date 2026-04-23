package it.uniroma3.siw.siw_tornei.repository;

import it.uniroma3.siw.siw_tornei.model.Giocatore;
import org.springframework.data.repository.CrudRepository;

public interface GiocatoreRepository extends CrudRepository<Giocatore, Long> {
}