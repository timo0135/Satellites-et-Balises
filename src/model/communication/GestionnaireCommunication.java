package model.communication;

import java.util.ArrayList;

import model.Simulation;
import model.balise.Balise;
import model.satellite.Satellite;

/**
 * Mediateur : centralise la regle « une balise peut communiquer avec un
 * satellite », pour que balises et satellites ne se connaissent pas.
 * <p>
 * Une communication est possible quand la balise est en surface et que la
 * distance horizontale au satellite ne depasse pas la portee. Le satellite
 * doit en plus etre disponible pour demarrer un nouveau transfert.
 */
public class GestionnaireCommunication {
    public static final double PORTEE_PAR_DEFAUT = 300;

    private final double portee;

    public GestionnaireCommunication() {
        this(PORTEE_PAR_DEFAUT);
    }

    public GestionnaireCommunication(double portee) {
        this.portee = portee;
    }

    /**
     * Interrompt les transferts dont les conditions ne sont plus remplies,
     * puis cree un transfert pour chaque balise en attente qui a un satellite
     * disponible a portee.
     */
    public void detecter(Simulation simulation) {
        for (Transfert t : new ArrayList<>(simulation.getTransferts())) {
            if (!peutCommuniquer(t.getBalise(), t.getSatellite())) {
                t.interrompre();
                simulation.retirerTransfert(t);
            }
        }
        for (Balise balise : simulation.getBalises()) {
            if (!balise.enAttenteDeTransmission() || simulation.aUnTransfert(balise)) {
                continue;
            }
            for (Satellite satellite : simulation.getSatellites()) {
                if (satellite.estDisponible() && peutCommuniquer(balise, satellite)) {
                    simulation.ajouterTransfert(new Transfert(balise, satellite));
                    break;
                }
            }
        }
    }

    /** Position permettant l'echange (hors disponibilite du satellite). */
    public boolean peutCommuniquer(Balise balise, Satellite satellite) {
        return balise.getPosition().getAltitude() >= 0
                && balise.getPosition().distanceHorizontale(satellite.getPosition()) <= portee;
    }

    public double getPortee() {
        return portee;
    }
}
