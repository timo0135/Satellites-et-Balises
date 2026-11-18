package app;

import javax.swing.SwingUtilities;

import model.Simulation;
import model.balise.Balise;
import model.deplacement.Horizontal;
import model.deplacement.Immobile;
import model.deplacement.ProfondeurFixe;
import model.deplacement.Sinusoidal;
import model.deplacement.Vertical;
import model.satellite.Geostationnaire;
import model.satellite.OrbiteCirculaire;
import model.satellite.Satellite;
import vue.FenetreSimulation;
import vue.Repere;

public class Main {
    private static final double LARGEUR_MONDE = 1000;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Simulation simulation = creerSimulation();
            Repere repere = new Repere(LARGEUR_MONDE, -300, 300, 1000, 600);
            new FenetreSimulation(simulation, repere).ouvrir();
        });
    }

    static Simulation creerSimulation() {
        Simulation simulation = new Simulation();

        simulation.ajouter(new Satellite("S1", 0, 220, new OrbiteCirculaire(LARGEUR_MONDE, 3)));
        simulation.ajouter(new Satellite("S2", 500, 140, new OrbiteCirculaire(LARGEUR_MONDE, 1.5)));
        simulation.ajouter(new Satellite("GEO", 750, 260, new Geostationnaire()));

        simulation.ajouter(new Balise("B1", 100, -60, new Horizontal(0.8)));
        simulation.ajouter(new Balise("B2", 300, -30, new Vertical(2, -250, -30)));
        simulation.ajouter(new Balise("B3", 500, -120, new Sinusoidal(1.2, 40, 60, -120)));
        simulation.ajouter(new Balise("B4", 700, -200, new Immobile()));
        simulation.ajouter(new Balise("B5", 850, 0, new ProfondeurFixe(-220, 3)));
        simulation.ajouter(new Balise("B6", 950, -150, new Horizontal(-1.5)));

        return simulation;
    }
}
