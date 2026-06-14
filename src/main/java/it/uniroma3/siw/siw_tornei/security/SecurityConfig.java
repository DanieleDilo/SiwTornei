package it.uniroma3.siw.siw_tornei.security;

// Assicurati di importare il tuo servizio, aggiusta il pacchetto in base a dove l'hai salvato!
import it.uniroma3.siw.siw_tornei.configuration.CustomOAuth2UserService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Iniettiamo il servizio che gestisce gli utenti Google creato in precedenza
   @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disabilitiamo il CSRF per le form
            .csrf(csrf -> csrf.disable())
            
            .authorizeHttpRequests(auth -> auth
                // 1. L'area admin è accessibile SOLO a chi ha ruolo ADMIN
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                
                // 2. Le funzionalità per utenti registrati (es. inserire un commento)
                // richiedono semplicemente che l'utente abbia fatto l'accesso (Google o Manuale)
                .requestMatchers("/utente/**", "/commento/**").authenticated()
                
                // 3. TUTTO il resto (home, tornei, login, setup-admin, registrazione) è pubblico!
                .anyRequest().permitAll()
            )
            
            // --- LOGIN CLASSICO (Email/Password) ---
            .formLogin(form -> form
                .loginPage("/login")
                // Mandiamo tutti alla home dopo il login (oppure alla pagina che stavano visitando)
                .defaultSuccessUrl("/", false) 
                .permitAll()
            )
            
            // --- LOGIN TRAMITE GOOGLE (OAuth2) ---
              .oauth2Login(oauth2 -> oauth2
                .loginPage("/login") // Usa la nostra stessa bellissima pagina custom
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService) // Colleghiamo il nostro salvataggio su DB
                )
                .defaultSuccessUrl("/", false) // Anche gli utenti Google vanno alla home
            )
            
            // --- LOGOUT ---
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}