package vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;

import model.activite.Activite;
import model.activite.Collecte;
import model.activite.Remontee;
import model.balise.Balise;
import model.balise.Memoire;
import model.instrument.CapteurTurbidite;
import model.instrument.Courantometre;
import model.instrument.Instrument;
import model.instrument.Salinometre;
import model.instrument.Thermometre;

/**
 * Une balise : capsule orange, antenne surmontee d'un voyant, deux ailerons.
 * Elle montre aussi son etat interne :
 * <ul>
 * <li>le <b>voyant</b> clignote selon l'activite (vert : collecte, orange :
 * remontee) ;</li>
 * <li>une <b>jauge</b> a droite indique le remplissage de la memoire ;</li>
 * <li>une rangee de <b>pastilles</b> sous la capsule liste ses instruments
 * (T : temperature, S : salinite, C : courant, B : turbidite).</li>
 * </ul>
 * Elle laisse une traine blanche et, sous l'eau, un trait pointille la relie
 * a la surface pour lire sa profondeur. Le survol donne le detail.
 */
public class VueBalise extends VueEntite {
    private static final long serialVersionUID = 1L;

    private static final int LARGEUR = 40;
    private static final int HAUTEUR = 52;
    private static final int DECALAGE = 8; // la capsule est dessinee centree dans la largeur

    /** Pastille d'un type d'instrument : sa lettre et sa couleur. */
    private record Pastille(String lettre, Color couleur) {
    }

    private static final Map<Class<? extends Instrument>, Pastille> PASTILLES = Map.of(
            Thermometre.class, new Pastille("T", new Color(0xE0533D)),
            Salinometre.class, new Pastille("S", new Color(0x2FA59A)),
            Courantometre.class, new Pastille("C", new Color(0x3D7FD6)),
            CapteurTurbidite.class, new Pastille("B", new Color(0x9A7B4F)));

    private static final Map<Class<? extends Activite>, Color> COULEURS_ACTIVITE = Map.of(
            Collecte.class, new Color(0x3DDC84),
            Remontee.class, new Color(0xFFA726));

    private static final Font POLICE_PASTILLE = new Font(Font.SANS_SERIF, Font.BOLD, 8);

    private final transient Balise balise;

    public VueBalise(Balise balise) {
        super(balise, LARGEUR, HAUTEUR, 23, 30, Color.WHITE);
        this.balise = balise;
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
        g.translate(DECALAGE, 0);
        dessinerCapsule(g);
        dessinerJauge(g);
        g.translate(-DECALAGE, 0);
        dessinerPastilles(g);
    }

    private void dessinerCapsule(Graphics2D g) {
        // ailerons
        g.setColor(new Color(0x8C3A14));
        g.fillPolygon(new int[] {7, 2, 7}, new int[] {30, 39, 35}, 3);
        g.fillPolygon(new int[] {17, 22, 17}, new int[] {30, 39, 35}, 3);

        // antenne et voyant d'activite (clignote : allume / eteint)
        g.setColor(new Color(0xC9CED6));
        g.setStroke(new BasicStroke(1.4f));
        g.drawLine(12, 12, 12, 5);
        Color couleur = COULEURS_ACTIVITE.getOrDefault(balise.getActivite().getClass(),
                new Color(0xFF4D3D));
        boolean allume = (System.currentTimeMillis() / 500) % 2 == 0;
        g.setColor(allume ? couleur : couleur.darker().darker());
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

    /** Jauge verticale de remplissage de la memoire, a droite de la capsule. */
    private void dessinerJauge(Graphics2D g) {
        int x = 21;
        int haut = 12;
        int hauteur = 22;
        Memoire memoire = balise.getMemoire();
        double taux = (double) memoire.taille() / memoire.getCapacite();

        RoundRectangle2D fond = new RoundRectangle2D.Double(x, haut, 4, hauteur, 3, 3);
        g.setColor(new Color(0, 0, 0, 120));
        g.fill(fond);
        int rempli = (int) Math.round(taux * hauteur);
        if (rempli > 0) {
            Shape ancien = g.getClip();
            g.clip(fond);
            g.setColor(taux >= 0.85 ? new Color(0xE5483A)
                    : taux >= 0.5 ? new Color(0xFFA726) : new Color(0x3DDC84));
            g.fillRect(x, haut + hauteur - rempli, 4, rempli);
            g.setClip(ancien);
        }
        g.setColor(new Color(255, 255, 255, 170));
        g.setStroke(new BasicStroke(0.8f));
        g.draw(fond);
    }

    /** Une pastille de 9 x 9 pixels par instrument, centrees sous la capsule. */
    private void dessinerPastilles(Graphics2D g) {
        int taille = 9;
        int nombre = balise.getInstruments().size();
        int x = (getWidth() - nombre * (taille + 1) + 1) / 2;
        int y = 41;
        g.setFont(POLICE_PASTILLE);
        FontMetrics metrics = g.getFontMetrics();
        for (Instrument instrument : balise.getInstruments()) {
            Pastille pastille = PASTILLES.getOrDefault(instrument.getClass(),
                    new Pastille("?", Color.GRAY));
            g.setColor(pastille.couleur());
            g.fillRoundRect(x, y, taille, taille, 3, 3);
            g.setColor(Color.WHITE);
            g.drawString(pastille.lettre(),
                    x + (taille - metrics.stringWidth(pastille.lettre())) / 2,
                    y + (taille + metrics.getAscent() - metrics.getDescent()) / 2);
            x += taille + 1;
        }
    }

    @Override
    protected String infobulle() {
        Memoire memoire = balise.getMemoire();
        StringBuilder instruments = new StringBuilder();
        for (Instrument instrument : balise.getInstruments()) {
            if (instruments.length() > 0) {
                instruments.append(", ");
            }
            instruments.append(instrument.getClass().getSimpleName());
        }
        return "<html>" + super.infobulle()
                + "<br>activité : " + balise.getActivite().getClass().getSimpleName()
                + "<br>mémoire : " + memoire.taille() + " / " + memoire.getCapacite()
                + "<br>instruments : " + (instruments.length() == 0 ? "aucun" : instruments)
                + "</html>";
    }
}
