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
     * Crea 1 Torneo, 5 Squadre iscritte.
     */
    @Transactional
    public Long generaDatiDiTest() {
        Torneo torneo = new Torneo();
        torneo.setNome("Torneo Benchmark " + (System.currentTimeMillis() % 1000));
        torneo.setAnno(2026);
        torneo.setDescrizione("Torneo creato automaticamente per l'analisi sperimentale.");
        torneoRepository.save(torneo);

        List<Squadra> squadre = new ArrayList<>();
        String[] nomiSquadre = {"AC Roma", "Inter Benchmark", "Juventus Test", "SSC Napoli Mock", "Milan Stress"};
        String[] citta = {"Roma", "Milano", "Torino", "Napoli", "Milano"};

        for (int i = 0; i < 5; i++) {
            Squadra squadra = new Squadra();
            squadra.setNome(nomiSquadre[i] + " " + (System.currentTimeMillis() % 100));
            squadra.setCitta(citta[i]);
            squadra.setAnnoFondazione(1900 + i * 10);
            squadra.setTornei(new ArrayList<>());
            squadra.getTornei().add(torneo);
            squadraRepository.save(squadra);

            // Aggiungi giocatori
            for (int j = 1; j <= 5; j++) {
                Giocatore g = new Giocatore();
                g.setNome("Giocatore" + j);
                g.setCognome("Team" + (i + 1));
                g.setDataNascita(LocalDate.of(1990 + j, 1 + (j % 12), 1 + (j % 28)));
                g.setRuolo(j <= 1 ? "PORTIERE" : j <= 3 ? "DIFENSORE" : "ATTACCANTE");
                g.setAltezza(1.70f + (j * 0.03f));
                g.setSquadra(squadra);
                giocatoreRepository.save(g);
            }

            squadre.add(squadra);
        }

        return torneo.getId();
    }

    /**
     * Esegue il benchmark completo confrontando 3 strategie di fetch:
     * 1. LAZY (default) - problema N+1
     * 2. JOIN FETCH (JPQL custom)
     * 3. EntityGraph (@EntityGraph annotation)
     *
     * Ritorna una mappa con i risultati strutturati per la view.
     */
    @Transactional
    public Map<String, Object> eseguiBenchmark(Long torneoId) {
        Map<String, Object> risultati = new HashMap<>();

        // Conta tornei e squadre dal DB per i contatori
        Torneo torneoInfo = torneoRepository.findByIdWithSquadre(torneoId).orElse(null);
        int numTornei = 0;
        int numSquadre = 0;
        if (torneoInfo != null) {
            numTornei = 1; // Stiamo analizzando 1 torneo
            numSquadre = torneoInfo.getSquadre() != null ? torneoInfo.getSquadre().size() : 0;
        }

        risultati.put("numTornei", numTornei);
        risultati.put("numSquadre", numSquadre);

        // =============================================
        // STRATEGIA 1: LAZY (default) - N+1 Problem
        // =============================================
        entityManager.clear();

        long startLazy = System.nanoTime();
        Torneo torneoLazy = torneoRepository.findById(torneoId).orElse(null);
        if (torneoLazy != null && torneoLazy.getSquadre() != null) {
            for (Squadra s : torneoLazy.getSquadre()) {
                // Ogni accesso genera una query aggiuntiva (N+1)
                if (s.getNome() != null) {
                    s.getNome().length(); // Forza il caricamento
                }
            }
        }
        long endLazy = System.nanoTime();
        double tempoLazyMs = (endLazy - startLazy) / 1_000_000.0;

        // Query stimate LAZY: 1 (torneo) + 1 (collezione squadre caricata lazy) = potenzialmente N+1
        int queriesLazy = 1 + numSquadre; // 1 per il torneo + N per le squadre

        risultati.put("tempoLazy", String.format("%.0f", tempoLazyMs));
        risultati.put("queriesLazy", queriesLazy);
        risultati.put("torneiLazy", numTornei);
        risultati.put("squadreLazy", numSquadre);

        // =============================================
        // STRATEGIA 2: JOIN FETCH (JPQL custom)
        // =============================================
        entityManager.clear();

        long startJoin = System.nanoTime();
        Torneo torneoJoin = torneoRepository.findByIdWithSquadre(torneoId).orElse(null);
        if (torneoJoin != null && torneoJoin.getSquadre() != null) {
            for (Squadra s : torneoJoin.getSquadre()) {
                if (s.getNome() != null) {
                    s.getNome().length();
                }
            }
        }
        long endJoin = System.nanoTime();
        double tempoJoinMs = (endJoin - startJoin) / 1_000_000.0;

        risultati.put("tempoJoin", String.format("%.0f", tempoJoinMs));
        risultati.put("queriesJoin", 1); // Una singola query JPQL
        risultati.put("torneiJoin", numTornei);
        risultati.put("squadreJoin", numSquadre);

        // =============================================
        // STRATEGIA 3: EntityGraph (@EntityGraph annotation)
        // =============================================
        entityManager.clear();

        long startGraph = System.nanoTime();
        Torneo torneoGraph = torneoRepository.findByIdWithSquadreEntityGraph(torneoId).orElse(null);
        if (torneoGraph != null && torneoGraph.getSquadre() != null) {
            for (Squadra s : torneoGraph.getSquadre()) {
                if (s.getNome() != null) {
                    s.getNome().length();
                }
            }
        }
        long endGraph = System.nanoTime();
        double tempoGraphMs = (endGraph - startGraph) / 1_000_000.0;

        risultati.put("tempoGraph", String.format("%.0f", tempoGraphMs));
        risultati.put("queriesGraph", 1); // Una singola query generata dall'EntityGraph
        risultati.put("torneiGraph", numTornei);
        risultati.put("squadreGraph", numSquadre);

        // Calcola la percentuale massima per le barre
        double maxTempo = Math.max(tempoLazyMs, Math.max(tempoJoinMs, tempoGraphMs));
        if (maxTempo <= 0) maxTempo = 1;
        risultati.put("barLazy", 100);
        risultati.put("barJoin", Math.max(5, (int) ((tempoJoinMs / maxTempo) * 100)));
        risultati.put("barGraph", Math.max(5, (int) ((tempoGraphMs / maxTempo) * 100)));

        return risultati;
    }
}
