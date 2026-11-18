package vue;

import java.awt.Color;

import model.satellite.Satellite;

/** Un satellite : disque orange, plus gros qu'une balise. */
public class VueSatellite extends VueEntite {
    private static final long serialVersionUID = 1L;

    public VueSatellite(Satellite satellite) {
        super(satellite, 16, new Color(0xD85A30));
    }
}
