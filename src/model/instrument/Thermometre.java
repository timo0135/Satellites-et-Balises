package model.instrument;

import model.mesure.Mesure;
import model.mesure.MesureTemperature;

public class Thermometre implements Instrument {

    @Override
    public Mesure mesurer(Contexte contexte) {
        double profondeur = -contexte.getPosition().getAltitude();
        double temperature = 4 + 22 * Math.exp(-profondeur / 150);
        return new MesureTemperature(contexte.getInstant(), contexte.getPosition(), temperature);
    }
}
