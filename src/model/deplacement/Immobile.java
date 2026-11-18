package model.deplacement;

import model.balise.Balise;

/** Absence de deplacement. */
public class Immobile implements Deplacement {

    @Override
    public void deplacer(Balise balise) {
        // La balise ne bouge pas.
    }
}
