package model.balise;

import model.Entite;
import model.Simulation;
import model.deplacement.Deplacement;


public class Balise extends Entite {
    private Deplacement deplacementActuel;

    public Balise(String id, double longitude, double altitude, Deplacement deplacementActuel) {
        super(id, longitude, altitude);
        this.deplacementActuel = deplacementActuel;
    }

    public void changerDeplacement(Deplacement deplacement) {
        if (deplacement == null) {
            throw new IllegalArgumentException("Deplacement null");
        }
        this.deplacementActuel = deplacement;
    }

    public Deplacement getDeplacementActuel() {
        return deplacementActuel;
    }

    @Override
    public void mettreAJour(Simulation simulation) {
        deplacementActuel.deplacer(this);
    }
}
