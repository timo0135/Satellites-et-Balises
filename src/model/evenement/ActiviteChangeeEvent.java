package model.evenement;

import eventHandler.AbstractEvent;
import model.Simulation;
import model.activite.Activite;
import model.balise.Balise;

/** Une balise vient de passer d'une activite a une autre. */
public class ActiviteChangeeEvent extends AbstractEvent {
	private static final long serialVersionUID = 1L;

	private final transient Balise balise;
	private final transient Activite ancienne;
	private final transient Activite nouvelle;

	public ActiviteChangeeEvent(Simulation source, Balise balise, Activite ancienne,
			Activite nouvelle) {
		super(source);
		this.balise = balise;
		this.ancienne = ancienne;
		this.nouvelle = nouvelle;
	}

	public Balise getBalise() {
		return balise;
	}

	public Activite getAncienne() {
		return ancienne;
	}

	public Activite getNouvelle() {
		return nouvelle;
	}

	@Override
	public void sendTo(Object target) {
		((SimulationListener) target).activiteChangee(this);
	}
}
