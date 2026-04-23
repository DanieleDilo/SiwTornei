package it.uniroma3.siw.siw_tornei.repository;

import it.uniroma3.siw.siw_tornei.model.Credentials;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {
    Optional<Credentials> findByUsername(String username);
}