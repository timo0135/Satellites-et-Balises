package model.activite;

import model.Position;
import model.balise.Balise;
import model.balise.Memoire;
import model.instrument.Instrument;

/**
 * Exploration et collecte : la balise se deplace selon sa strategie et fait
 * mesurer chacun de ses instruments a chaque pas. Quand la memoire est pleine,
 * elle passe a {@link Remontee}.
 * <p>
 * Si la collecte reprend apres une transmission, la balise redescend d'abord
 * a la profondeur qu'elle avait quittee (sans mesurer pendant la descente).
 */
public class Collecte implements Activite {
    private final double profondeurCible;
    private final double vitesseDescente;
    private boolean enDescente;

    /** Collecte immediate, sans descente prealable. */
    public Collecte() {
        this.profondeurCible = 0;
        this.vitesseDescente = VITESSE_VERTICALE_PAR_DEFAUT;
        this.enDescente = false;
    }

    /** Collecte precedee d'une descente jusqu'a profondeurCible. */
    public Collecte(double profondeurCible, double vitesseDescente) {
        this.profondeurCible = profondeurCible;
        this.vitesseDescente = vitesseDescente;
        this.enDescente = true;
    }

    @Override
    public void agir(Balise balise) {
        Position p = balise.getPosition();
        if (enDescente) {
            if (p.getAltitude() > profondeurCible) {
                p.setAltitude(Math.max(profondeurCible, p.getAltitude() - vitesseDescente));
            }
            if (p.getAltitude() <= profondeurCible) {
                enDescente = false;
            }
            return;
        }
        balise.getDeplacementActuel().deplacer(balise);
        Memoire memoire = balise.getMemoire();
        for (Instrument instrument : balise.getInstruments()) {
            if (memoire.estPleine()) {
                break;
            }
            memoire.ajouter(instrument.mesurer(balise.creerContexte()));
        }
    }

    @Override
    public Activite suivante(Balise balise) {
        if (balise.getMemoire().estPleine()) {
            return new Remontee(balise.getPosition().getAltitude());
        }
        return this;
    }

    public double getProfondeurCible() {
        return profondeurCible;
    }

    public double getVitesseDescente() {
        return vitesseDescente;
    }

    public boolean estEnDescente() {
        return enDescente;
    }
}
