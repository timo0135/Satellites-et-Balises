package model.activite;

import model.Position;
import model.balise.Balise;

/** Remontee vers la surface, en vue de transmettre les donnees. */
public class Remontee implements Activite {
    private final double profondeurRetour;
    private final double vitesse;

    /** @param profondeurRetour profondeur a retrouver apres la transmission */
    public Remontee(double profondeurRetour) {
        this(profondeurRetour, VITESSE_VERTICALE_PAR_DEFAUT);
    }

    public Remontee(double profondeurRetour, double vitesse) {
        this.profondeurRetour = profondeurRetour;
        this.vitesse = vitesse;
    }

    @Override
    public void agir(Balise balise) {
        Position p = balise.getPosition();
        p.setAltitude(Math.min(0, p.getAltitude() + vitesse));
    }

    @Override
    public Activite suivante(Balise balise) {
        if (balise.getPosition().getAltitude() >= 0) {
            return new Transmission(profondeurRetour, vitesse);
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
