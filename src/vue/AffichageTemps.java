package vue;

import javax.swing.JLabel;

import model.Simulation;
import model.evenement.PasDeTempsEvent;
import model.evenement.SimulationListener;

/** Etiquette « t = N » : se met a jour a chaque pas de temps de la simulation. */
public class AffichageTemps extends JLabel implements SimulationListener {
    private static final long serialVersionUID = 1L;

    public AffichageTemps(Simulation simulation) {
        super("t = " + simulation.getTempsCourant());
        simulation.getEventHandler().registerListener(PasDeTempsEvent.class, this);
    }

    @Override
    public void pasDeTempsEcoule(PasDeTempsEvent e) {
        this.setText("t = " + e.getTempsCourant());
    }
}
