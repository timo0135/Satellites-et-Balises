package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import model.Simulation;

/**
 * Cadence la simulation : un {@link Timer} Swing appelle
 * {@link Simulation#tick()} a intervalle regulier (un pas = une image).
 * Tout se passe sur le thread Swing, donc modele et vue ne sont jamais
 * modifies en parallele.
 */
public class Animation implements ActionListener {
    private final Simulation simulation;
    private final Timer timer;

    public Animation(Simulation simulation, int delaiMs) {
        this.simulation = simulation;
        this.timer = new Timer(delaiMs, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        simulation.tick();
    }

    public void demarrer() {
        timer.start();
    }

    public void arreter() {
        timer.stop();
    }

    public boolean estEnMarche() {
        return timer.isRunning();
    }

    public void setDelai(int delaiMs) {
        timer.setDelay(delaiMs);
    }
}
