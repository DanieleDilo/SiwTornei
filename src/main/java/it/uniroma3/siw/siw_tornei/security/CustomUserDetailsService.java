package it.uniroma3.siw.siw_tornei.security;

import it.uniroma3.siw.siw_tornei.model.Credentials;
import it.uniroma3.siw.siw_tornei.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cerchiamo le credenziali nel nostro database
        Optional<Credentials> credenziali = credentialsRepository.findByUsername(username);

        if (credenziali.isEmpty()) {
            throw new UsernameNotFoundException("Utente non trovato");
        }

        Credentials cred = credenziali.get();

        // Trasformiamo il ruolo del nostro DB in un'autorità che Spring può capire
        GrantedAuthority authority = new SimpleGrantedAuthority(cred.getRole());

        // Restituiamo un oggetto User che Spring Security sa gestire
        return new User(
                cred.getUsername(),
                cred.getPassword(),
                Collections.singletonList(authority)
        );
    }
}