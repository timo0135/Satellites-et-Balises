package model.evenement;

import eventHandler.AbstractEvent;
import model.Simulation;


public class PasDeTempsEvent extends AbstractEvent {
	private static final long serialVersionUID = 1L;

	private final int tempsCourant;

	public PasDeTempsEvent(Simulation source, int tempsCourant) {
		super(source);
		this.tempsCourant = tempsCourant;
	}

	public int getTempsCourant() {
		return tempsCourant;
	}

	@Override
	public void sendTo(Object target) {
		((SimulationListener) target).pasDeTempsEcoule(this);
	}
}
