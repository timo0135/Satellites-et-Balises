package vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.RoundRectangle2D;

import model.satellite.Satellite;

/**
 * Un satellite : corps argente a bande doree, deux panneaux solaires
 * quadrilles et une antenne parabolique. Sa traine est doree.
 */
public class VueSatellite extends VueEntite {
    private static final long serialVersionUID = 1L;

    private static final int LARGEUR = 48;
    private static final int HAUTEUR = 34;

    public VueSatellite(Satellite satellite) {
        super(satellite, LARGEUR, HAUTEUR, 19, 45, new Color(0xFFD27A));
    }

    @Override
    protected void dessiner(Graphics2D g) {
        panneau(g, 2);
        panneau(g, 33);

        // bras reliant les panneaux au corps
        g.setColor(new Color(0x8A94A0));
        g.setStroke(new BasicStroke(1.5f));
        g.drawLine(15, 19, 17, 19);
        g.drawLine(31, 19, 33, 19);

        // antenne parabolique
        g.setColor(new Color(0xB8C0CA));
        g.setStroke(new BasicStroke(1.2f));
        g.drawLine(24, 10, 24, 5);
        Arc2D cuvette = new Arc2D.Double(19, 0, 10, 10, 180, 180, Arc2D.CHORD);
        g.setColor(new Color(0xE3E8EE));
        g.fill(cuvette);
        g.setColor(new Color(0x6B7684));
        g.setStroke(new BasicStroke(0.8f));
        g.draw(cuvette);

        // corps
        Shape corps = new RoundRectangle2D.Double(17, 10, 14, 18, 6, 6);
        g.setPaint(new GradientPaint(17, 0, new Color(0xF7F9FB), 31, 0, new Color(0x8F9AA8)));
        g.fill(corps);
        Shape ancien = g.getClip();
        g.clip(corps);
        g.setColor(new Color(0xE8B73C));
        g.fillRect(17, 17, 14, 4);
        g.setClip(ancien);
        g.setColor(new Color(0x4A5563));
        g.setStroke(new BasicStroke(1f));
        g.draw(corps);
    }

    /** Un panneau solaire de 13 x 12 pixels, avec son quadrillage. */
    private void panneau(Graphics2D g, int x) {
        int y = 13;
        int largeur = 13;
        int hauteur = 12;
        g.setPaint(new GradientPaint(x, y, new Color(0x1E3F7A), x, y + hauteur, new Color(0x3E6FC4)));
        g.fillRect(x, y, largeur, hauteur);
        g.setColor(new Color(0x9BB9E8));
        g.setStroke(new BasicStroke(0.6f));
        g.drawLine(x + largeur / 3, y, x + largeur / 3, y + hauteur);
        g.drawLine(x + 2 * largeur / 3, y, x + 2 * largeur / 3, y + hauteur);
        g.drawLine(x, y + hauteur / 2, x + largeur, y + hauteur / 2);
        g.setColor(new Color(0x0B1F44));
        g.setStroke(new BasicStroke(1f));
        g.drawRect(x, y, largeur, hauteur);
    }
}
