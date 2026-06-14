package it.uniroma3.siw.siw_tornei.configuration;

import it.uniroma3.siw.siw_tornei.model.Credentials;
import it.uniroma3.siw.siw_tornei.model.User;
import it.uniroma3.siw.siw_tornei.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // Estrapoliamo i dati forniti da Google
        String email = oauth2User.getAttribute("email");
        String nome = oauth2User.getAttribute("given_name");
        String cognome = oauth2User.getAttribute("family_name");

        // Controlliamo se esiste già un account con questa email
        Optional<Credentials> existingCredentials = credentialsRepository.findByUsername(email);

        Credentials credentials;

        if (existingCredentials.isPresent()) {
            // Account già esistente: aggiorniamo i dati da Google
            credentials = existingCredentials.get();

            // Se era un account locale, lo colleghiamo a Google (merge)
            if (Credentials.PROVIDER_LOCAL.equals(credentials.getProvider())) {
                credentials.setProvider(Credentials.PROVIDER_GOOGLE);
            }

            // Aggiorniamo nome e cognome dal profilo Google (se disponibili)
            User user = credentials.getUser();
            if (user != null) {
                if (nome != null && !nome.isBlank()) user.setNome(nome);
                if (cognome != null && !cognome.isBlank()) user.setCognome(cognome);
            }

            credentialsRepository.save(credentials);
        } else {
            // Nuovo utente: creiamo User + Credentials
            User newUser = new User();
            newUser.setNome(nome != null ? nome : "");
            newUser.setCognome(cognome != null ? cognome : "");

            credentials = new Credentials();
            credentials.setUsername(email);
            credentials.setPassword(""); // Nessuna password per utenti Google
            credentials.setRole(Credentials.ROLE_USER);
            credentials.setProvider(Credentials.PROVIDER_GOOGLE);
            credentials.setUser(newUser); // Cascade salva anche User

            credentialsRepository.save(credentials);
        }

        // Restituiamo un OAuth2User con l'autorità corretta dal nostro DB
        // Questo assicura che il ruolo in SecurityContext corrisponda al ruolo nel DB
        return new DefaultOAuth2User(
                Collections.singletonList(new SimpleGrantedAuthority(credentials.getRole())),
                oauth2User.getAttributes(),
                "email" // L'attributo da usare come "name" principal
        );
    }
}