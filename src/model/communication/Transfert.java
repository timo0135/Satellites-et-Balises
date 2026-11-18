package model.communication;

import model.balise.Balise;
import model.satellite.Satellite;

/**
 * Communication en cours entre une balise et un satellite.
 * <p>
 * Cycle : EN_ATTENTE (cree, satellite reserve) -&gt; EN_COURS (donnees en
 * transit, la progression augmente) -&gt; TERMINE (la memoire de la balise est
 * videe et les mesures sont remises au satellite). La duree depend du nombre
 * de mesures a transferer et du debit.
 */
public class Transfert {
    /** Nombre de mesures transferees par pas de temps. */
    public static final int DEBIT = 5;

    private final Balise balise;
    private final Satellite satellite;
    private EtatTransfert etat = EtatTransfert.EN_ATTENTE;
    private int progression; // pourcentage, 0 a 100
    private int pasNecessaires;
    private int pasEcoules;

    /** Reserve immediatement le satellite : il devient indisponible. */
    public Transfert(Balise balise, Satellite satellite) {
        this.balise = balise;
        this.satellite = satellite;
        satellite.setDisponible(false);
    }

    public void avancer() {
        switch (etat) {
            case EN_ATTENTE:
                int mesures = balise.getMemoire().taille();
                pasNecessaires = Math.max(1, (mesures + DEBIT - 1) / DEBIT);
                etat = EtatTransfert.EN_COURS;
                break;
            case EN_COURS:
                pasEcoules++;
                progression = Math.min(100, 100 * pasEcoules / pasNecessaires);
                if (pasEcoules >= pasNecessaires) {
                    terminer();
                }
                break;
            case TERMINE:
                break;
        }
    }

    private void terminer() {
        satellite.recevoir(balise.getMemoire().vider());
        satellite.setDisponible(true);
        progression = 100;
        etat = EtatTransfert.TERMINE;
    }

    /** Abandon avant la fin (communication perdue) : libere le satellite. */
    public void interrompre() {
        if (etat != EtatTransfert.TERMINE) {
            satellite.setDisponible(true);
        }
    }

    public boolean estTermine() {
        return etat == EtatTransfert.TERMINE;
    }

    public Balise getBalise() {
        return balise;
    }

    public Satellite getSatellite() {
        return satellite;
    }

    public EtatTransfert getEtat() {
        return etat;
    }

    public int getProgression() {
        return progression;
    }
}
