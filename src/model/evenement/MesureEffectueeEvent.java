package model.evenement;

import eventHandler.AbstractEvent;
import model.Simulation;
import model.balise.Balise;
import model.mesure.Mesure;

/** Une balise vient de faire une mesure (deja rangee dans sa memoire). */
public class MesureEffectueeEvent extends AbstractEvent {
	private static final long serialVersionUID = 1L;

	private final transient Balise balise;
	private final transient Mesure mesure;

	public MesureEffectueeEvent(Simulation source, Balise balise, Mesure mesure) {
		super(source);
		this.balise = balise;
		this.mesure = mesure;
	}

	public Balise getBalise() {
		return balise;
	}

	public Mesure getMesure() {
		return mesure;
	}

	@Override
	public void sendTo(Object target) {
		((SimulationListener) target).mesureEffectuee(this);
	}
}
