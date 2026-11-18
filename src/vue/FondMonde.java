package vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Decor fixe de la vue : espace etoile qui s'eclaircit vers l'horizon, mer en
 * degrade avec vagues et rayons de lumiere, echelle d'altitude a gauche.
 * <p>
 * Il ne depend pas des entites : il est dessine une seule fois dans une image
 * puis simplement recopie a chaque rafraichissement.
 */
public class FondMonde {
    private final Repere repere;
    private final BufferedImage image;

    public FondMonde(Repere repere) {
        this.repere = repere;
        this.image = construire();
    }

    public void dessiner(Graphics2D g) {
        g.drawImage(image, 0, 0, null);
    }

    private BufferedImage construire() {
        int largeur = repere.getLargeurPx();
        int hauteur = repere.getHauteurPx();
        int mer = repere.ordonnee(0);

        BufferedImage img = new BufferedImage(largeur, hauteur, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        ciel(g, largeur, mer);
        eau(g, largeur, hauteur, mer);
        echelle(g, mer);
        g.dispose();
        return img;
    }

    private void ciel(Graphics2D g, int largeur, int mer) {
        g.setPaint(new LinearGradientPaint(0, 0, 0, mer,
                new float[] {0f, 0.6f, 1f},
                new Color[] {new Color(0x050B1F), new Color(0x1B3A6B), new Color(0x9CC8F0)}));
        g.fillRect(0, 0, largeur, mer);

        // etoiles : plus nombreuses et plus vives en haut
        Random hasard = new Random(7);
        for (int i = 0; i < 160; i++) {
            int x = hasard.nextInt(largeur);
            int y = (int) (hasard.nextDouble() * mer * 0.8);
            int alpha = Math.max(30, (int) (220 * (1 - y / (mer * 0.85))));
            int taille = hasard.nextDouble() < 0.15 ? 2 : 1;
            g.setColor(new Color(255, 255, 255, alpha));
            g.fillOval(x, y, taille, taille);
        }

        // lune et son halo
        int lx = (int) (largeur * 0.84);
        int ly = (int) (mer * 0.2);
        g.setColor(new Color(255, 250, 220, 22));
        g.fillOval(lx - 30, ly - 30, 60, 60);
        g.setColor(new Color(255, 250, 220, 40));
        g.fillOval(lx - 21, ly - 21, 42, 42);
        g.setColor(new Color(0xF4F1DA));
        g.fillOval(lx - 14, ly - 14, 28, 28);
    }

    private void eau(Graphics2D g, int largeur, int hauteur, int mer) {
        g.setPaint(new LinearGradientPaint(0, mer, 0, hauteur,
                new float[] {0f, 0.35f, 1f},
                new Color[] {new Color(0x4FA8E8), new Color(0x1F6FB0), new Color(0x071E3D)}));
        g.fillRect(0, mer, largeur, hauteur - mer);

        // rayons de lumiere
        g.setColor(new Color(255, 255, 255, 14));
        for (int x : new int[] {120, 330, 540, 760, 930}) {
            Path2D rayon = new Path2D.Double();
            rayon.moveTo(x, mer);
            rayon.lineTo(x + 35, mer);
            rayon.lineTo(x + 150, hauteur);
            rayon.lineTo(x + 70, hauteur);
            rayon.closePath();
            g.fill(rayon);
        }

        // vagues : deux lignes ondulees a la surface
        g.setStroke(new BasicStroke(1.6f));
        for (int ligne = 0; ligne < 2; ligne++) {
            Path2D vague = new Path2D.Double();
            for (int x = 0; x <= largeur; x += 4) {
                double y = mer + ligne * 4 + 2.2 * Math.sin(x * 0.045 + ligne * 1.7);
                if (x == 0) {
                    vague.moveTo(x, y);
                } else {
                    vague.lineTo(x, y);
                }
            }
            g.setColor(new Color(255, 255, 255, ligne == 0 ? 150 : 70));
            g.draw(vague);
        }
    }

    /** Graduations tous les 100 d'altitude, sur le bord gauche. */
    private void echelle(Graphics2D g, int mer) {
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
        g.setStroke(new BasicStroke(1f));
        int pas = 100;
        int premiere = (int) Math.ceil(repere.getAltitudeMin() / pas) * pas;
        for (int altitude = premiere; altitude <= repere.getAltitudeMax(); altitude += pas) {
            int y = repere.ordonnee(altitude);
            g.setColor(new Color(255, 255, 255, 140));
            g.drawLine(0, y, 8, y);
            String texte = altitude == 0 ? "niveau de la mer"
                    : (altitude > 0 ? "+" + altitude : String.valueOf(altitude));
            g.setColor(new Color(255, 255, 255, 170));
            int baseline = y + 4 + (altitude == 0 ? -8 : 0);
            // les graduations des bords restent entierement visibles
            baseline = Math.max(12, Math.min(repere.getHauteurPx() - 4, baseline));
            g.drawString(texte, 12, baseline);
        }
    }
}
