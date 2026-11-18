package vue;

import java.awt.Color;
import java.awt.Dimension;

import model.Entite;
import nicellipse.component.NiEllipse;

/**
 * Representation graphique d'une entite : une ellipse Nicellipse qui connait
 * son entite (comme dans l'exemple MovingThings) et se replace d'apres elle.
 * Elle ne modifie jamais le modele.
 */
public abstract class VueEntite extends NiEllipse {
    private static final long serialVersionUID = 1L;

    private final transient Entite entite;

    protected VueEntite(Entite entite, int diametre, Color couleur) {
        this.entite = entite;
        this.setBackground(couleur);
        this.setDimension(new Dimension(diametre, diametre));
        this.setToolTipText(entite.getId());
    }

    public Entite getEntite() {
        return entite;
    }

    /** Replace la vue au centre de la position courante de l'entite. */
    public void synchroniser(Repere repere) {
        this.setCenter(repere.versEcran(entite.getPosition()));
    }
}
