package model.instrument;

import model.mesure.Mesure;
import model.mesure.MesureTurbidite;

/** Turbidite : plus elevee pres de la surface (5,5 NTU), faible en profondeur. */
public class CapteurTurbidite implements Instrument {

    @Override
    public Mesure mesurer(Contexte contexte) {
        double profondeur = -contexte.getPosition().getAltitude();
        double turbidite = 0.5 + 5 * Math.exp(-profondeur / 50);
        return new MesureTurbidite(contexte.getInstant(), contexte.getPosition(), turbidite);
    }
}
