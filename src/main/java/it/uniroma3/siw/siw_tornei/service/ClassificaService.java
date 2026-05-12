package it.uniroma3.siw.siw_tornei.service;

import it.uniroma3.siw.siw_tornei.model.*;
import it.uniroma3.siw.siw_tornei.repository.PartitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClassificaService {

    @Autowired
    private PartitaRepository partitaRepository;

    public List<RigaClassifica> generaClassifica(Torneo torneo) {
        // Usiamo una mappa per trovare subito la squadra tramite il suo ID
        Map<Long, RigaClassifica> mappaClassifica = new HashMap<>();

        // 1. Inizializza la classifica: tutte le squadre del torneo partono da 0 punti
        if (torneo.getSquadre() != null) {
            for (Squadra s : torneo.getSquadre()) {
                mappaClassifica.put(s.getId(), new RigaClassifica(s));
            }
        }

        // 2. Leggi le partite e assegna i punti
        for (Partita p : partitaRepository.findAll()) {
            // Se la partita appartiene a questo torneo ed è già stata GIOCATA...
            if (p.getTorneo().getId().equals(torneo.getId()) && "PLAYED".equals(p.getStato().name())) {
                
                RigaClassifica rigaCasa = mappaClassifica.get(p.getSquadraCasa().getId());
                RigaClassifica rigaTrasf = mappaClassifica.get(p.getSquadraTrasferta().getId());

                if (rigaCasa != null) rigaCasa.aggiungiRisultato(p.getGoalsHome(), p.getGoalsAway());
                if (rigaTrasf != null) rigaTrasf.aggiungiRisultato(p.getGoalsAway(), p.getGoalsHome());
            }
        }

        // 3. Converti la mappa in lista e ordinala (scatterà il metodo compareTo)
        List<RigaClassifica> classificaOrdinata = new ArrayList<>(mappaClassifica.values());
        Collections.sort(classificaOrdinata);
        
        return classificaOrdinata;
    }
}