package it.uniroma3.siw.siw_tornei.repository;

import it.uniroma3.siw.siw_tornei.model.Arbitro;
import org.springframework.data.repository.CrudRepository;

public interface ArbitroRepository extends CrudRepository<Arbitro, Long> {
    // Potremo aggiungere metodi come findByCognome o findByCodiceArbitrale
}