package vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayDeque;
import java.util.Deque;

import model.Entite;
import nicellipse.component.NiShape;

/**
 * Representation graphique d'une entite : un composant Nicellipse qui connait
 * son entite (comme dans l'exemple MovingThings) et se replace d'apres elle.
 * Elle ne modifie jamais le modele.
 * <p>
 * Une vue se compose de trois choses :
 * <ul>
 * <li>le dessin de l'entite ({@link #dessiner}), propre a chaque sous-classe ;</li>
 * <li>son nom, ecrit dessous ;</li>
 * <li>son decor ({@link #dessinerDecor}), dessine dans le fond de la vue de
 * la simulation, derriere toutes les entites : par defaut la traine laissee
 * par ses derniers deplacements.</li>
 * </ul>
 */
public abstract class VueEntite extends NiShape {
    private static final long serialVersionUID = 1L;

    private static final int HAUTEUR_NOM = 12;
    private static final Font POLICE_NOM = new Font(Font.SANS_SERIF, Font.BOLD, 10);

    private final transient Entite entite;
    private final int ancreY;
    private final int longueurTrace;
    private final transient Color couleurTrace;
    private final transient Deque<Point> trace = new ArrayDeque<>();

    /**
     * @param largeur       largeur du composant
     * @param hauteurDessin hauteur du dessin (le nom s'ajoute dessous)
     * @param ancreY        ordonnee, dans le dessin, du point qui doit se
     *                      trouver exactement sur la position de l'entite
     * @param longueurTrace nombre de positions gardees pour la traine
     * @param couleurTrace  couleur de la traine
     */
    protected VueEntite(Entite entite, int largeur, int hauteurDessin, int ancreY,
                        int longueurTrace, Color couleurTrace) {
        super(new Rectangle(0, 0, largeur, hauteurDessin + HAUTEUR_NOM));
        this.entite = entite;
        this.ancreY = ancreY;
        this.longueurTrace = longueurTrace;
        this.couleurTrace = couleurTrace;
        this.setToolTipText(entite.getId());
    }

    public Entite getEntite() {
        return entite;
    }

    /** Replace la vue sur la position courante de l'entite et memorise la traine. */
    public void synchroniser(Repere repere) {
        Point centre = repere.versEcran(entite.getPosition());
        this.setLocation(centre.x - getWidth() / 2, centre.y - ancreY);
        if (trace.isEmpty() || !trace.peekLast().equals(centre)) {
            trace.addLast(centre);
            if (trace.size() > longueurTrace) {
                trace.removeFirst();
            }
        }
        this.setToolTipText(String.format("%s  (longitude %.0f, altitude %.0f)",
                entite.getId(), entite.getPosition().getLongitude(),
                entite.getPosition().getAltitude()));
    }

    /**
     * Dessine l'entite dans le repere local du composant : (0, 0) est le coin
     * en haut a gauche, la zone de dessin fait largeur x hauteurDessin.
     */
    protected abstract void dessiner(Graphics2D g);

    /**
     * Dessine ce qui accompagne l'entite dans le fond de la simulation
     * (coordonnees de la vue de la simulation). Par defaut : la traine.
     */
    public void dessinerDecor(Graphics2D g, Repere repere) {
        if (trace.size() < 2) {
            return;
        }
        int rupture = repere.getLargeurPx() / 2; // saut = passage de l'autre cote du monde
        g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Point precedent = null;
        int i = 0;
        for (Point p : trace) {
            if (precedent != null && Math.abs(p.x - precedent.x) <= rupture) {
                int alpha = (int) (150.0 * i / trace.size());
                g.setColor(new Color(couleurTrace.getRed(), couleurTrace.getGreen(),
                        couleurTrace.getBlue(), alpha));
                g.drawLine(precedent.x, precedent.y, p.x, p.y);
            }
            precedent = p;
            i++;
        }
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        dessiner(g);
        dessinerNom(g);
        g.dispose();
    }

    private void dessinerNom(Graphics2D g) {
        g.setFont(POLICE_NOM);
        FontMetrics metrics = g.getFontMetrics();
        String nom = entite.getId();
        int x = (getWidth() - metrics.stringWidth(nom)) / 2;
        int y = getHeight() - 2;
        g.setColor(new Color(0, 0, 0, 150));
        g.drawString(nom, x + 1, y + 1);
        g.setColor(Color.WHITE);
        g.drawString(nom, x, y);
    }
}
