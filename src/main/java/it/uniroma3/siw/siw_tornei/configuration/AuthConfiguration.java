package it.uniroma3.siw.siw_tornei.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .anyRequest().permitAll() // Permette l'accesso a tutte le pagine senza login
            )
            .csrf(csrf -> csrf.disable()) // Disabilita CSRF (utile in fase di sviluppo)
            .formLogin(form -> form.disable()); // Nasconde la form di login automatica
            
        return http.build();
    }
}