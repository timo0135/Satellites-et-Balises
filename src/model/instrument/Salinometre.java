package model.instrument;

import model.mesure.Mesure;
import model.mesure.MesureSalinite;

public class Salinometre implements Instrument {

    @Override
    public Mesure mesurer(Contexte contexte) {
        double profondeur = -contexte.getPosition().getAltitude();
        double salinite = 34.5 + 0.5 * (1 - Math.exp(-profondeur / 300));
        return new MesureSalinite(contexte.getInstant(), contexte.getPosition(), salinite);
    }
}
