package vue;

import java.awt.Color;

import model.balise.Balise;

/** Une balise : petit disque vert sombre. */
public class VueBalise extends VueEntite {
    private static final long serialVersionUID = 1L;

    public VueBalise(Balise balise) {
        super(balise, 10, new Color(0x0F6E56));
    }
}
