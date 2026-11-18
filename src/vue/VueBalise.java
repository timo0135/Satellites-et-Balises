package vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import model.balise.Balise;

/**
 * Une balise : capsule orange a bande blanche, antenne surmontee d'un voyant
 * qui clignote, deux ailerons. Elle laisse une traine blanche et, quand elle
 * est sous l'eau, un trait pointille la relie a la surface pour lire sa
 * profondeur.
 */
public class VueBalise extends VueEntite {
    private static final long serialVersionUID = 1L;

    private static final int LARGEUR = 24;
    private static final int HAUTEUR = 40;

    public VueBalise(Balise balise) {
        super(balise, LARGEUR, HAUTEUR, 23, 30, Color.WHITE);
    }

    @Override
    public void dessinerDecor(Graphics2D g, Repere repere) {
        super.dessinerDecor(g, repere);
        int surface = repere.ordonnee(0);
        int x = getX() + getWidth() / 2;
        int bas = getY() + 4; // sommet de l'antenne
        if (bas > surface) {
            g.setColor(new Color(255, 255, 255, 70));
            g.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    10f, new float[] {3f, 4f}, 0f));
            g.drawLine(x, surface, x, bas);
        }
    }

    @Override
    protected void dessiner(Graphics2D g) {
        // ailerons
        g.setColor(new Color(0x8C3A14));
        g.fillPolygon(new int[] {7, 2, 7}, new int[] {30, 39, 35}, 3);
        g.fillPolygon(new int[] {17, 22, 17}, new int[] {30, 39, 35}, 3);

        // antenne et voyant clignotant
        g.setColor(new Color(0xC9CED6));
        g.setStroke(new BasicStroke(1.4f));
        g.drawLine(12, 12, 12, 5);
        boolean allume = (System.currentTimeMillis() / 500) % 2 == 0;
        g.setColor(allume ? new Color(0xFF4D3D) : new Color(0x7A2A25));
        g.fill(new Ellipse2D.Double(9.5, 1.5, 5, 5));

        // capsule
        Shape corps = new RoundRectangle2D.Double(7, 12, 10, 22, 10, 10);
        g.setPaint(new GradientPaint(7, 0, new Color(0xF8BE55), 17, 0, new Color(0xC8501F)));
        g.fill(corps);
        Shape ancien = g.getClip();
        g.clip(corps);
        g.setColor(Color.WHITE);
        g.fillRect(7, 18, 10, 3);
        g.setClip(ancien);
        g.setColor(new Color(0x7A2E10));
        g.setStroke(new BasicStroke(1f));
        g.draw(corps);

        // hublot du capteur
        g.setColor(new Color(0xBFE3FF));
        g.fill(new Ellipse2D.Double(9.5, 24, 5, 5));
        g.setColor(new Color(0x3C5E80));
        g.setStroke(new BasicStroke(0.8f));
        g.draw(new Ellipse2D.Double(9.5, 24, 5, 5));
    }
}
