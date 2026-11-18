package model.evenement;

import eventHandler.AbstractEvent;
import model.Entite;
import model.Simulation;


public class EntiteDeplaceeEvent extends AbstractEvent {
	private static final long serialVersionUID = 1L;

	private final transient Entite entite;

	public EntiteDeplaceeEvent(Simulation source, Entite entite) {
		super(source);
		this.entite = entite;
	}

	public Entite getEntite() {
		return entite;
	}

	@Override
	public void sendTo(Object target) {
		((SimulationListener) target).entiteDeplacee(this);
	}
}
