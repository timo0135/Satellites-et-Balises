package model.activite;

import model.balise.Balise;
import model.communication.GestionnaireCommunication;
import model.communication.Transfert;

/**
 * Attente en surface puis transfert des donnees. La balise ne fait rien
 * elle-meme : c'est le {@link GestionnaireCommunication} qui cree le
 * {@link Transfert} quand un satellite est disponible. Le transfert vide la
 * memoire a sa fin, ce qui permet a la balise de reprendre la collecte.
 */
public class Transmission implements Activite {
    private final double profondeurRetour;
    private final double vitesse;

    public Transmission(double profondeurRetour, double vitesse) {
        this.profondeurRetour = profondeurRetour;
        this.vitesse = vitesse;
    }

    @Override
    public void agir(Balise balise) {
        // Attente passive en surface.
    }

    @Override
    public Activite suivante(Balise balise) {
        if (balise.getMemoire().estVide()) {
            return new Collecte(profondeurRetour, vitesse);
        }
        return this;
    }

    public double getProfondeurRetour() {
        return profondeurRetour;
    }

    public double getVitesse() {
        return vitesse;
    }
}
