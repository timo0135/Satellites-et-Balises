package model.activite;

import model.Position;
import model.balise.Balise;


public class Remontee implements Activite {
    private final double vitesse;

    public Remontee() {
        this(VITESSE_VERTICALE_PAR_DEFAUT);
    }

    public Remontee(double vitesse) {
        if (vitesse <= 0) {
            throw new IllegalArgumentException("La vitesse doit etre positive");
        }
        this.vitesse = vitesse;
    }

    @Override
    public void agir(Balise balise) {
        Position p = balise.getPosition();
        p.setAltitude(Math.min(0, p.getAltitude() + vitesse));
    }

    @Override
    public Activite suivante(Balise balise) {
        return this; // en surface, elle attend la transmission de la v3
    }

    public double getVitesse() {
        return vitesse;
    }
}
