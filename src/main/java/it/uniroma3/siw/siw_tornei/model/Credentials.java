package it.uniroma3.siw.siw_tornei.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Credentials {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role; // "ADMIN" o "USER"

    @OneToOne(cascade = CascadeType.ALL)
    private User user; // Relazione con i dati anagrafici

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}