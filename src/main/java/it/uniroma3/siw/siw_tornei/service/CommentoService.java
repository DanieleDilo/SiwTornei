package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Commento;
import it.uniroma3.siw.siw_tornei.model.Credentials;
import it.uniroma3.siw.siw_tornei.model.Partita;
import it.uniroma3.siw.siw_tornei.repository.CommentoRepository;
import it.uniroma3.siw.siw_tornei.repository.CredentialsRepository;
import it.uniroma3.siw.siw_tornei.repository.PartitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentoService {

    @Autowired
    private CommentoRepository commentoRepository;

    @Autowired
    private PartitaRepository partitaRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;

    /**
     * Restituisce tutti i commenti di una partita, ordinati dal più recente.
     */
    @Transactional(readOnly = true)
    public List<Commento> findByPartitaId(Long partitaId) {
        return commentoRepository.findByPartitaIdOrderByDataCreazioneDesc(partitaId);
    }

    /**
     * Trova un commento per ID.
     */
    @Transactional(readOnly = true)
    public Commento findById(Long id) {
        return commentoRepository.findById(id).orElse(null);
    }

    /**
     * Aggiunge un nuovo commento a una partita.
     */
    @Transactional
    public void aggiungiCommento(Long partitaId, String testo, String usernameAutore) {
        Partita partita = partitaRepository.findById(partitaId).orElse(null);
        Optional<Credentials> autore = credentialsRepository.findByUsername(usernameAutore);

        if (partita != null && autore.isPresent() && testo != null && !testo.isBlank()) {
            Commento commento = new Commento();
            commento.setTesto(testo.trim());
            commento.setPartita(partita);
            commento.setAutore(autore.get());
            commentoRepository.save(commento);
        }
    }

    /**
     * Modifica un commento esistente, ma solo se l'autore corrisponde.
     * @return true se la modifica è avvenuta, false se non autorizzato
     */
    @Transactional
    public boolean modificaCommento(Long commentoId, String nuovoTesto, String usernameAutore) {
        Commento commento = commentoRepository.findById(commentoId).orElse(null);

        if (commento == null) return false;

        // Controllo che l'utente sia effettivamente l'autore del commento
        if (!commento.getAutore().getUsername().equals(usernameAutore)) {
            return false;
        }

        commento.setTesto(nuovoTesto.trim());
        commento.setDataModifica(LocalDateTime.now());
        commentoRepository.save(commento);
        return true;
    }

    /**
     * Elimina un commento, ma solo se l'autore corrisponde.
     */
    @Transactional
    public boolean eliminaCommento(Long commentoId, String usernameAutore) {
        Commento commento = commentoRepository.findById(commentoId).orElse(null);

        if (commento == null) return false;

        if (!commento.getAutore().getUsername().equals(usernameAutore)) {
            return false;
        }

        commentoRepository.delete(commento);
        return true;
    }
}
