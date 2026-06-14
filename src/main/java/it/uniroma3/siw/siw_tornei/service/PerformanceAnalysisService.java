package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.Giocatore;
import it.uniroma3.siw.siw_tornei.model.Squadra;
import it.uniroma3.siw.siw_tornei.model.Torneo;
import it.uniroma3.siw.siw_tornei.repository.GiocatoreRepository;
import it.uniroma3.siw.siw_tornei.repository.SquadraRepository;
import it.uniroma3.siw.siw_tornei.repository.TorneoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PerformanceAnalysisService {

    @Autowired
    private TorneoRepository torneoRepository;

    @Autowired
    private SquadraRepository squadraRepository;

    @Autowired
    private GiocatoreRepository giocatoreRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Genera dati di test fittizi per l'analisi delle performance.
     * Crea 1 Torneo, 5 Squadre, e 15 Giocatori per squadra (75 giocatori totali).
     */
    @Transactional
    public Long generaDatiDiTest() {
        // Creazione Torneo
        Torneo torneo = new Torneo();
        torneo.setNome("Torneo Benchmark " + (System.currentTimeMillis() % 1000));
        torneo.setAnno(2026);
        torneo.setDescrizione("Torneo creato automaticamente per l'analisi sperimentale delle performance delle strategie di fetch.");
        torneoRepository.save(torneo);

        List<Squadra> squadre = new ArrayList<>();
        // Creazione di 5 squadre, ciascuna con 15 giocatori
        for (int i = 1; i <= 5; i++) {
            Squadra squadra = new Squadra();
            squadra.setNome("Team Benchmark " + i);
            squadra.setCitta("Città " + i);
            squadra.setAnnoFondazione(2000 + i);
            squadra.setTornei(new ArrayList<>());
            squadra.getTornei().add(torneo);
            squadraRepository.save(squadra);

            List<Giocatore> giocatori = new ArrayList<>();
            for (int j = 1; j <= 15; j++) {
                Giocatore g = new Giocatore();
                g.setNome("Nome" + j);
                g.setCognome("Cognome" + j + " (Team " + i + ")");
                g.setDataNascita(LocalDate.of(1995 + (j % 10), 1 + (j % 12), 1 + (j % 28)));
                g.setRuolo("ATTACCANTE");
                g.setAltezza(1.75f + (j * 0.01f));
                g.setSquadra(squadra);
                giocatoreRepository.save(g);
                giocatori.add(g);
            }
            squadra.setGiocatori(giocatori);
            squadre.add(squadra);
        }
        torneo.setSquadre(squadre);
        torneoRepository.save(torneo);

        return torneo.getId();
    }

    /**
     * Esegue il benchmark e ritorna i risultati in una mappa.
     */
    @Transactional
    public Map<String, Object> eseguiBenchmark(Long torneoId) {
        Map<String, Object> risultati = new HashMap<>();

        // 1. Reset/Clear EntityManager per evitare cache di primo livello
        entityManager.clear();

        // 2. Esegui caricamento con strategia LAZY (N+1 query)
        long startLazy = System.nanoTime();
        Torneo torneoLazy = torneoRepository.findById(torneoId).orElse(null);
        int numSquadre = 0;
        int numGiocatori = 0;
        if (torneoLazy != null) {
            List<Squadra> squadre = torneoLazy.getSquadre();
            if (squadre != null) {
                numSquadre = squadre.size(); // Forza caricamento squadre
                for (Squadra s : squadre) {
                    List<Giocatore> giocatori = s.getGiocatori();
                    if (giocatori != null) {
                        numGiocatori += giocatori.size(); // Forza caricamento giocatori (Query N+1)
                    }
                }
            }
        }
        long endLazy = System.nanoTime();
        double tempoLazyMs = (endLazy - startLazy) / 1_000_000.0;

        // 3. Reset/Clear EntityManager di nuovo
        entityManager.clear();

        // 4. Esegui caricamento con strategia ottimizzata JOIN FETCH (2 query totali per evitare MultipleBagFetchException)
        long startJoin = System.nanoTime();
        Torneo torneoJoin = torneoRepository.findByIdWithSquadre(torneoId).orElse(null);
        if (torneoJoin != null) {
            List<Squadra> squadre = torneoJoin.getSquadre();
            if (squadre != null && !squadre.isEmpty()) {
                torneoRepository.fetchSquadreWithGiocatori(squadre); // Carica tutti i giocatori in una sola query
                for (Squadra s : squadre) {
                    List<Giocatore> giocatori = s.getGiocatori();
                    if (giocatori != null) {
                        giocatori.size(); // Già in memoria
                    }
                }
            }
        }
        long endJoin = System.nanoTime();
        double tempoJoinMs = (endJoin - startJoin) / 1_000_000.0;

        // Popola i risultati
        risultati.put("torneoNome", torneoLazy != null ? torneoLazy.getNome() : "Nessuno");
        risultati.put("numSquadre", numSquadre);
        risultati.put("numGiocatori", numGiocatori);
        
        risultati.put("tempoLazy", String.format("%.2f", tempoLazyMs));
        risultati.put("queriesLazy", 1 + 1 + numSquadre); // 1 Torneo, 1 Associazione, N Squadre

        risultati.put("tempoJoin", String.format("%.2f", tempoJoinMs));
        risultati.put("queriesJoin", 2); // 1 Torneo + Squadre, 1 Squadre + Giocatori

        double miglioramento = ((tempoLazyMs - tempoJoinMs) / tempoLazyMs) * 100;
        risultati.put("miglioramento", String.format("%.1f", miglioramento > 0 ? miglioramento : 0.0));

        return risultati;
    }
}
