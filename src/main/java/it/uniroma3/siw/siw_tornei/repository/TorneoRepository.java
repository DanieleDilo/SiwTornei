package it.uniroma3.siw.siw_tornei.repository;

import it.uniroma3.siw.siw_tornei.model.Torneo;
import org.springframework.data.repository.CrudRepository;

public interface TorneoRepository extends CrudRepository<Torneo, Long> {
    // Spring implementerà automaticamente i metodi per il CRUD (Save, Find, Delete) [cite: 91]
}