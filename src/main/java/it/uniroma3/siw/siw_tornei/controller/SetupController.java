package it.uniroma3.siw.siw_tornei.controller;

import it.uniroma3.siw.siw_tornei.model.Credentials;
import it.uniroma3.siw.siw_tornei.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SetupController {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/setup-admin")
    @ResponseBody // Ritorna una semplice scritta a schermo invece di una pagina HTML
    public String setupAdmin() {
        // Controlla se l'admin esiste già per evitare duplicati
        if (credentialsRepository.findByUsername("admin").isEmpty()) {
            
            Credentials admin = new Credentials();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Cripta la password!
            admin.setRole("ADMIN");
            
            credentialsRepository.save(admin);
            
            return "✅ Admin creato con successo! Vai su <a href='/login'>/login</a> e accedi con: admin / admin123";
        }
        
        return "⚠️ L'utente admin esiste già nel database!";
    }
}