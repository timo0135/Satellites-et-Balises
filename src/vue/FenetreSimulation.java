package vue;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import model.Simulation;
import model.balise.Balise;
import model.satellite.Satellite;
import nicellipse.component.NiSpace;

/**
 * Fenetre principale : la vue de la simulation, une barre de commandes
 * (pause/lecture, vitesse) et l'affichage du temps.
 */
public class FenetreSimulation {
    private static final int HAUTEUR_BARRE = 40;
    private static final int IMAGES_PAR_SECONDE_INITIALES = 20;

    private final NiSpace espace;

    public FenetreSimulation(Simulation simulation, Repere repere) {
        VueSimulation vue = new VueSimulation(repere);
        vue.enregistrer(Satellite.class, VueSatellite::new);
        vue.enregistrer(Balise.class, VueBalise::new);
        vue.setLocation(0, HAUTEUR_BARRE);
        vue.suivre(simulation);

        Animation animation = new Animation(simulation, 1000 / IMAGES_PAR_SECONDE_INITIALES);

        AffichageTemps temps = new AffichageTemps(simulation);

        JButton lecture = new JButton("Pause");
        lecture.addActionListener(e -> {
            if (animation.estEnMarche()) {
                animation.arreter();
                lecture.setText("Lecture");
            } else {
                animation.demarrer();
                lecture.setText("Pause");
            }
        });

        JSlider vitesse = new JSlider(JSlider.HORIZONTAL, 1, 60, IMAGES_PAR_SECONDE_INITIALES);
        vitesse.addChangeListener(e -> animation.setDelai(1000 / vitesse.getValue()));

        JPanel barre = new JPanel();
        barre.add(lecture);
        barre.add(new JLabel("Vitesse :"));
        barre.add(vitesse);
        barre.add(temps);
        barre.setBounds(0, 0, repere.getLargeurPx(), HAUTEUR_BARRE);

        espace = new NiSpace("Satellites et balises",
                new Dimension(repere.getLargeurPx(), repere.getHauteurPx() + HAUTEUR_BARRE));
        espace.add(barre);
        espace.add(vue);

        animation.demarrer();
    }

    public void ouvrir() {
        espace.openInWindow();
    }
}
