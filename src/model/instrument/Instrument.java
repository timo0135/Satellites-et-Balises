package model.instrument;

import model.mesure.Mesure;

/**
 * Point d'extension : un instrument observe une grandeur physique du milieu
 * et produit la {@link Mesure} correspondante.
 * <p>
 * Les instruments fournis utilisent un modele physique simplifie, fonction de
 * la profondeur (altitude) et du temps, ce qui rend les simulations deterministes.
 */
public interface Instrument {
    Mesure mesurer(Contexte contexte);
}
