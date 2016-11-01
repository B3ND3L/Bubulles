import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.ui.view.Viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.util.Random;

public class Main extends JFrame {

	// Élément graphiques
	private JPanel vueGraphe;
	private JMenuBar menu;

	private GraphBulle g;

	// les différents menu
	private JMenuItem charger;
	private JMenuItem chercher;
	private JMenuItem enregistrer;

	public Main() {
		super("Fenetre");

		/* Meilleur Viewer pour le graphe*/
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		setSize(600,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// appelle à l'initialisation des composants graphiques
		initJMenuBar();
		initGraphe();

		// centré la fenêtre et la rendre visible
		this.add(vueGraphe, BorderLayout.CENTER);

		pack();
		setVisible(true);

	}

	public void initJMenuBar() {
		menu = new JMenuBar();

		// le menu fichier
		JMenu fichier = new JMenu("Fichier");

		// le bouton pour charger un fichier contenant les bulles
		charger = new JMenuItem("Ouvrir un fichier");
		// listener bouton menu
		charger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser dialogue = new JFileChooser();
				int resu = dialogue.showOpenDialog(null);
				if (resu != JFileChooser.CANCEL_OPTION) {
					String chemin = dialogue.getSelectedFile().getAbsolutePath();
					initGraphe(chemin);
					chercher.setEnabled(true);
				}
			}
		});
		fichier.add(charger);

		// le bouton pour chercher les groupes
		chercher = new JMenuItem("Chercher les groupes");
		// listener bouton menu
		chercher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				// fenetre popup pour selectionné les deux paramètre
				JTextField taux = new JTextField();
				JTextField angle = new JTextField();

				final JComponent[] entrees = new JComponent[] {
						new JLabel("Taux d'erreur (en %): "),
						taux,
						new JLabel("Angle maximum  (en degrès) : "),
						angle,
				};
				JOptionPane.showMessageDialog(null, entrees, "Choix des paramètres", JOptionPane.PLAIN_MESSAGE);

				// verifier si les données entrée sont bonne
				chercherGroupe( Double.parseDouble( taux.getText() )/100.0, Double.parseDouble( angle.getText() ) );
				chercher.setEnabled(false);
				enregistrer.setEnabled(true);
			}
		});
		fichier.add(chercher);

		// le bouton pour enregistrer les groupes
		enregistrer = new JMenuItem("Enregistrer les groupes");
		// listener bouton menu
		enregistrer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				enregistrer();
			}
		});
		fichier.add(enregistrer);

		menu.add(fichier);

		// ajout du menu à la fenetre
		this.add(menu, BorderLayout.NORTH);

		chercher.setEnabled(false);
		enregistrer.setEnabled(false);
	}

	public void initGraphe() {
		vueGraphe = new JPanel(new BorderLayout());
		vueGraphe.setPreferredSize(new Dimension(400,400));
		pack();
	}

	public void initGraphe(String chemin) {
		vueGraphe.removeAll();
		Parser p = new Parser();
		g = p.parse(chemin);
		g.setAttribute("ui.quality");
		g.setAttribute("ui.antialias");

		Viewer viewer = new Viewer(g , Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		vueGraphe.add(viewer.addDefaultView(false), BorderLayout.CENTER);
		vueGraphe.setPreferredSize(new Dimension(400,400));

		pack();
	}

	public void chercherGroupe(double taux, double angle) {
		vueGraphe.removeAll();
		vueGraphe.add(new JLabel("recherche en cours"));
		pack();
		g = g.chercherGroupe(taux, angle);
		vueGraphe.removeAll();
		g.setAttribute("ui.quality");
		g.setAttribute("ui.antialias");

		Viewer viewer = new Viewer(g , Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		vueGraphe.add(viewer.addDefaultView(false), BorderLayout.CENTER);
		vueGraphe.setPreferredSize(new Dimension(400,400));
		g.addAttribute("ui.stylesheet", "url(src/style.css)");

		// ajout d'une couleur en fonction du groupId
		for (Node n : g) {
			n.setAttribute("ui.label", n.getNumber("idGroupe"));
		}
		pack();

	}

	public void enregistrer() {
		JFileChooser dialogue = new JFileChooser();
		int choix = dialogue.showSaveDialog(null);
		if (choix == JFileChooser.APPROVE_OPTION) {
				Parser p = new Parser();
				p.groupsToFile(g, dialogue.getSelectedFile());
		}

	}

	public static void main(String[] args) {
		// Création de la fenêtre de départ
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Main();
			}
		});
	}

}
