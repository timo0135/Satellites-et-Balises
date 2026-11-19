package app;

import javax.swing.SwingUtilities;

import model.Simulation;
import model.balise.Balise;
import model.deplacement.Deplacement;
import model.deplacement.Horizontal;
import model.deplacement.Immobile;
import model.deplacement.ProfondeurFixe;
import model.deplacement.Sinusoidal;
import model.deplacement.Vertical;
import model.instrument.CapteurTurbidite;
import model.instrument.Courantometre;
import model.instrument.Instrument;
import model.instrument.Salinometre;
import model.instrument.Thermometre;
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

        // Des balises equipees differemment, avec des memoires de tailles
        // differentes : elles ne sont pas pleines (donc ne remontent) en meme temps.
        simulation.ajouter(balise("B1", 100, -60, 40, new Horizontal(0.8),
                new Thermometre(), new Salinometre()));
        simulation.ajouter(balise("B2", 300, -30, 30, new Vertical(2, -250, -30),
                new Thermometre(), new Courantometre(), new CapteurTurbidite()));
        simulation.ajouter(balise("B3", 500, -120, 12, new Sinusoidal(1.2, 40, 60, -120),
                new Courantometre()));
        simulation.ajouter(balise("B4", 700, -200, 80, new Immobile(),
                new Thermometre(), new Salinometre(), new Courantometre(), new CapteurTurbidite()));
        simulation.ajouter(balise("B5", 850, 0, 16, new ProfondeurFixe(-220, 3),
                new Salinometre(), new Thermometre()));
        // sans instrument : rien a collecter, elle n'est jamais pleine
        simulation.ajouter(balise("B6", 950, -150, 10, new Horizontal(-1.5)));

        return simulation;
    }

    private static Balise balise(String id, double longitude, double altitude, int capacite,
                                 Deplacement deplacement, Instrument... instruments) {
        Balise balise = new Balise(id, longitude, altitude, capacite, deplacement);
        for (Instrument instrument : instruments) {
            balise.equiper(instrument);
        }
        return balise;
    }
}
