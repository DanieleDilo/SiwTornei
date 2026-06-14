package it.uniroma3.siw.siw_tornei.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.siw_tornei.model.User;
import it.uniroma3.siw.siw_tornei.repository.UserRepository;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // Estrapoliamo l'email fornita da Google
        String email = oauth2User.getAttribute("email");

        // Controlliamo se l'utente esiste già nel nostro DB
        Optional<User> userOptional = userRepository.findByUsername(email);

        if (userOptional.isEmpty()) {
            // Se non esiste, lo registriamo automaticamente
            User newUser = new User();
            newUser.setUsername(email);
            // newUser.setNome(oauth2User.getAttribute("given_name")); // Se hai questo
            // campo
            // newUser.setCognome(oauth2User.getAttribute("family_name")); // Se hai questo
            // campo
            newUser.setRuolo("USER"); // Ruolo di base
            newUser.setProvider("GOOGLE");

            userRepository.save(newUser);
        }

        return oauth2User;
    }
}