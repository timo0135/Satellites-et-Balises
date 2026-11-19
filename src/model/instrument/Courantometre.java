package model.instrument;

import model.mesure.Mesure;
import model.mesure.MesureCourant;

/**
 * Courant : plus fort en surface, decroissant avec la profondeur ; la
 * direction tourne lentement avec le temps (3 degres par pas).
 */
public class Courantometre implements Instrument {

    @Override
    public Mesure mesurer(Contexte contexte) {
        double profondeur = -contexte.getPosition().getAltitude();
        double vitesse = 0.1 + 1.4 * Math.exp(-profondeur / 200);
        double direction = (contexte.getInstant() * 3) % 360;
        return new MesureCourant(contexte.getInstant(), contexte.getPosition(), vitesse, direction);
    }
}
