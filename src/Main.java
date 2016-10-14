import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		/* Meilleur Viewer pour le graphe*/
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		Parser p = new Parser();
		GraphBulle g = p.parse("norma_N5_tau4_dt2_delai820_000003.txt");

		// fonction qui fait les groupes
		Graph g2 = g.chercherGroupe();

		g2.addAttribute("ui.antialias");
		g2.addAttribute("ui.quality");
		g2.addAttribute("ui.stylesheet", "url(src/style.css)");

		// ajout d'une couleur en fonction du groupId
		for (Node n : g2) {
			n.setAttribute("ui.label", n.getNumber("idGroupe"));
		}

		// pour voir toute les distances sur le graphe (Mauvaise idéé)
		/*for (Node n : g ) {
			for (Edge e : n) {
				e.setAttribute("ui.label", e.getNumber("distance"));
			}
		}*/

		g2.display(false);
	}

}
