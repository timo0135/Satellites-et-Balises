package model.activite;

import model.balise.Balise;


public interface Activite {
    double VITESSE_VERTICALE_PAR_DEFAUT = 5.0;

    void agir(Balise balise);

    Activite suivante(Balise balise);
}
