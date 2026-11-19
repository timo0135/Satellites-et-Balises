package model.instrument;

import model.mesure.Mesure;

public interface Instrument {
    Mesure mesurer(Contexte contexte);
}
