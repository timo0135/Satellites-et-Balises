package vue;

import model.Simulation;

/**
 * Patron Observer : notifie apres chaque pas de temps. Le modele ne connait
 * que cette interface, jamais la vue concrete.
 */
public interface Observateur {
    void miseAJour(Simulation simulation);
}
