package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Credentials;
import it.uniroma3.siw.siw_tornei.model.User;
import it.uniroma3.siw.siw_tornei.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CredentialsService {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuovo utente con credenziali locali.
     * Valida email e password, poi salva Credentials + User collegati.
     *
     * @return null se la registrazione va a buon fine, altrimenti un messaggio di errore
     */
    @Transactional
    public String register(String nome, String cognome, String email, String password) {
        // --- Validazione email ---
        if (email == null || email.isBlank()) {
            return "L'email è obbligatoria.";
        }
        // Controllo formato email con regex semplice
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return "Il formato dell'email non è valido.";
        }
        // Controllo unicità
        if (credentialsRepository.existsByUsername(email)) {
            return "Questa email è già registrata. Prova ad accedere.";
        }

        // --- Validazione nome e cognome ---
        if (nome == null || nome.isBlank()) {
            return "Il nome è obbligatorio.";
        }
        if (cognome == null || cognome.isBlank()) {
            return "Il cognome è obbligatorio.";
        }

        // --- Validazione password ---
        if (password == null || password.length() < 8) {
            return "La password deve contenere almeno 8 caratteri.";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "La password deve contenere almeno una lettera maiuscola.";
        }
        if (!password.matches(".*[0-9].*")) {
            return "La password deve contenere almeno un numero.";
        }

        // --- Creazione User (dati anagrafici) ---
        User user = new User();
        user.setNome(nome.trim());
        user.setCognome(cognome.trim());

        // --- Creazione Credentials (dati di autenticazione) ---
        Credentials credentials = new Credentials();
        credentials.setUsername(email.trim().toLowerCase());
        credentials.setPassword(passwordEncoder.encode(password));
        credentials.setRole(Credentials.ROLE_USER);
        credentials.setProvider(Credentials.PROVIDER_LOCAL);
        credentials.setUser(user); // Collega User a Credentials (cascade salva anche User)

        credentialsRepository.save(credentials);

        return null; // Successo
    }

    /**
     * Aggiorna nome e cognome dell'utente.
     */
    @Transactional
    public void updateProfile(Credentials credentials, String nome, String cognome) {
        User user = credentials.getUser();
        if (user == null) {
            user = new User();
            credentials.setUser(user);
        }
        user.setNome(nome.trim());
        user.setCognome(cognome.trim());
        credentialsRepository.save(credentials);
    }

    /**
     * Trova le credenziali per username (email).
     */
    @Transactional(readOnly = true)
    public Optional<Credentials> findByUsername(String username) {
        return credentialsRepository.findByUsername(username);
    }

    /**
     * Controlla se esiste un utente con questo username (email).
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return credentialsRepository.existsByUsername(username);
    }
}
