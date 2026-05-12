package it.uniroma3.siw.siw_tornei.model;

public class RigaClassifica implements Comparable<RigaClassifica> {
    
    private Squadra squadra;
    private int punti = 0;
    private int partiteGiocate = 0;
    private int vittorie = 0;
    private int pareggi = 0;
    private int sconfitte = 0;
    private int golFatti = 0;
    private int golSubiti = 0;

    public RigaClassifica(Squadra squadra) {
        this.squadra = squadra;
    }

    // Metodo che aggiorna i punti della squadra dopo ogni partita letta
    public void aggiungiRisultato(int golFatti, int golSubiti) {
        this.partiteGiocate++;
        this.golFatti += golFatti;
        this.golSubiti += golSubiti;
        
        if (golFatti > golSubiti) {
            this.vittorie++;
            this.punti += 3; // Vittoria
        } else if (golFatti == golSubiti) {
            this.pareggi++;
            this.punti += 1; // Pareggio
        } else {
            this.sconfitte++; // Sconfitta (0 punti)
        }
    }

    public int getDifferenzaReti() {
        return this.golFatti - this.golSubiti;
    }

    // --- GETTER NECESSARI PER L'HTML ---
    public Squadra getSquadra() { return squadra; }
    public int getPunti() { return punti; }
    public int getPartiteGiocate() { return partiteGiocate; }
    public int getVittorie() { return vittorie; }
    public int getPareggi() { return pareggi; }
    public int getSconfitte() { return sconfitte; }
    public int getGolFatti() { return golFatti; }
    public int getGolSubiti() { return golSubiti; }

    // Regola di ordinamento: prima i punti, poi la differenza reti
    @Override
    public int compareTo(RigaClassifica altra) {
        if (this.punti != altra.punti) {
            return Integer.compare(altra.punti, this.punti); // Ordine decrescente
        }
        return Integer.compare(altra.getDifferenzaReti(), this.getDifferenzaReti());
    }
}