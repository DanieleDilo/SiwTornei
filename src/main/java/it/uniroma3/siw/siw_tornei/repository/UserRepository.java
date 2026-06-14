package it.uniroma3.siw.siw_tornei.repository;

import it.uniroma3.siw.siw_tornei.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    // User è ora puramente anagrafico (nome, cognome).
    // La ricerca per email/username avviene tramite CredentialsRepository.
}