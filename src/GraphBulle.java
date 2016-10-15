import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;

public class GraphBulle extends MultiGraph {

  // la contrainte sur l'anle en degrés
  private static double TAUX_ERREUR = 0.4;
  private static double CONTRAINTE_ANGLE = 30.0 ;

  private int idGroupe;

  public GraphBulle(String str) {
    super(str);
    idGroupe = 0;
  }

  public int getIdGroupe() {
    return idGroupe;
  }

  private boolean verificationAngle(Node n1, Node n2, Node n3, int profondeur) {
    Vecteur3D v1 = new Vecteur3D(n1, n2);
    Vecteur3D v2 = new Vecteur3D(n2, n3);

    double angleMax = CONTRAINTE_ANGLE;

    if (profondeur == 3) {
      angleMax *= 2;
    }
    if (v1.angle(v2) < angleMax) {
      return true;
    }
    return false;

  }

  public boolean verificationContrainte(Node[] groupe, double distance, int profondeur, Edge e) {
    double distanceATrouve = 0.0;
    switch(profondeur) {
      case 4 :
        distanceATrouve = distance/2.0;
        break;
      case 3 :
        distanceATrouve = distance*2.0;
        break;
      default :
        distanceATrouve = distance;
    }

    // Verification si le prochain noeud n'est pas le même que précédemment, si la distance est bonne et si l'angle est bon
    if ( (Math.abs(e.getNumber("distance") - distanceATrouve) <= distanceATrouve*TAUX_ERREUR) && (!e.getOpposite(groupe[profondeur-1]).equals(groupe[profondeur-2]) ) && verificationAngle( groupe[profondeur-2], groupe[profondeur-1], e.getOpposite(groupe[profondeur-1]), profondeur ) ) {
      return true;
    }
    return false;
  }


  public void trouveProche(Node[] groupe, double distance, int profondeur, Graph graphe) {
    if (profondeur == 5) {
      transfereGroupe(groupe, graphe);
    }
    else {
      for(Edge e : groupe[profondeur - 1]) {
        if (verificationContrainte(groupe, distance, profondeur, e)) {
          groupe[profondeur] = e.getOpposite(groupe[profondeur-1]);
          trouveProche(groupe, e.getNumber("distance"), profondeur+1, graphe);
        }
      }
    }
  }

  private void transfereGroupe(Node[] groupe, Graph g) {
    idGroupe++;
    double couleur = Math.random();
    for (int i=0; i<5; i++) {
      g.addNode(groupe[i].getId()+" "+idGroupe);
      g.getNode(groupe[i].getId()+" "+idGroupe).setAttribute("x", ""+groupe[i].getNumber("x"));
      g.getNode(groupe[i].getId()+" "+idGroupe).setAttribute("y", ""+groupe[i].getNumber("y"));
      g.getNode(groupe[i].getId()+" "+idGroupe).setAttribute("z", ""+groupe[i].getNumber("z"));
      g.getNode(groupe[i].getId()+" "+idGroupe).setAttribute("idGroupe", idGroupe);
      g.getNode(groupe[i].getId()+" "+idGroupe).setAttribute("ui.color", couleur);
      g.getNode(groupe[i].getId()+" "+idGroupe).setAttribute("ligne", ""+groupe[i].getNumber("ligne"));
    }

    for (int i=1; i<5; i++) {
      g.addEdge(g.getNode(groupe[i-1].getId()+" "+idGroupe)+", "+g.getNode(groupe[i].getId()+" "+idGroupe), ""+g.getNode(groupe[i-1].getId()+" "+idGroupe), ""+g.getNode(groupe[i].getId()+" "+idGroupe));
    }
  }

  private void initRecherche() {
    // L'identifiant de groupe est à 0 pour chaque bulle car il n'y a pas encore de groupe.
    for (Node n : this) {
      n.setAttribute("idGroupe", idGroupe);
    }
  }

  public GraphBulle chercherGroupe() {
      // mis en place d'un identifiant de groupe pour chaque bulle
      initRecherche();

      // création du nouveau graphe
      GraphBulle g = new GraphBulle("apres calcul");

      // on parcours tout les noeuds pour voir tout les cas possibles
      for (Node n : this) {

        Node[] groupe = new Node[5];
        groupe[0] = n;

        // on parcours toutes les arrêtes de ce noeud
        for (Edge e : n) {
          // pour démarer on prend un noeud proche sans distance particulière, ni angle.
          groupe[1] = e.getOpposite(n);
          trouveProche(groupe, e.getNumber("distance"), 2, g);
        }

      }

      return g;
    }

}
