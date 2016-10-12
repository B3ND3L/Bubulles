import org.graphstream.graph.Node;

public class Vecteur3D {
  private double x;
  private double y;
  private double z;

  public Vecteur3D(Node n1, Node n2) {
    x = n2.getNumber("x") - n1.getNumber("x");
    y = n2.getNumber("y") - n1.getNumber("y");
    z = n2.getNumber("z") - n1.getNumber("z");
  }

  public double getX() {
    return x;
  }
  public double getY() {
    return y;
  }
  public double getZ() {
    return z;
  }

  // this * v
  public double produitScalaire(Vecteur3D v) {
    // multiplié les composantes de même direction entre elle et les additioné
    return getX()*v.getX() + getY()*v.getY() + getZ()*v.getZ();
  }

  // racine(x² + y² + z²)
  public double longueur() {
    return Math.sqrt(x*x + y*y + z*z);
  }

  // retourne l'angle entre deux vecteur en degré
  public double angle(Vecteur3D v) {
    double cosTeta = this.produitScalaire(v) / (this.longueur() * v.longueur());
    return Math.acos(cosTeta)*180.0/Math.PI;
  }
}
