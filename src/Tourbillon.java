import java.util.ArrayList;
import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

public class Tourbillon {

	private Graph graph;
	
	public  Tourbillon(Graph g){

		/* Meilleur Viewer */
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		
		graph = g;
		
		graph.addAttribute("ui.stylesheet", "url('data/GraphStyle.css')");
		graph.addAttribute("ui.antialias");
		graph.addAttribute("ui.quality");

		graph.display(false);
	}
	
	private boolean sameDist(double d1, double d2){
		
		double eps = 1000.0;
		return d1+eps>d2 && d1-eps<d2 || d1*2+eps>d2 && d1*2-eps<d2;
		
	}
	
	public boolean findGroup(Node pn, Node n, int i, double dist, ArrayList<Node> al){
		
		Iterator<Node> it = n.getNeighborNodeIterator();
		ArrayList<Node> listNode = new ArrayList<Node>();
			
		while(it.hasNext()){
			Node p = it.next();
			if( ! p.hasAttribute("disabled"))
				listNode.add(p);
		}
		
		for(Node n2 : listNode){
			double d = (double) ((graph.getEdge(n+"-"+n2)!=null)?graph.getEdge(n+"-"+n2).getAttribute("distance"):graph.getEdge(n2+"-"+n).getAttribute("distance"));
			n.addAttribute("disabled", 1);
			if(i<4){
				if(findGroup(n, n2,i+1,d,al)){
					al.add(n2);
					return true;
				}
			}else{
				return sameDist(dist, d) && calculAngle(pn, n, n2)<=35.0;
			}
			n.removeAttribute("disabled");
		}
		return false;
	}
	
	public void colorNodes(ArrayList<Node> a){
		for(Node n : a){
			graph.getNode(n.getId()).addAttribute("ui.class", "red");
		}
	}
	
	//calcul l'angle abc
	private double calculAngle(Node a, Node b, Node c){
		
		if (a==null) return 10.0;
		
		//Calcul des vecteurs
		double Xba = (double)a.getAttribute("x")-(double)b.getAttribute("x"); 
		double Yba = (double)a.getAttribute("y")-(double)b.getAttribute("y");
		double Zba = (double)a.getAttribute("z")-(double)b.getAttribute("z");
		
		double Xbc = (double)c.getAttribute("x")-(double)b.getAttribute("x"); 
		double Ybc = (double)c.getAttribute("y")-(double)b.getAttribute("y");
		double Zbc = (double)c.getAttribute("z")-(double)b.getAttribute("z");
		
		//........
		double BA = Math.sqrt(Math.pow(Xba,2)+Math.pow(Yba,2)+Math.pow(Zba,2));
		double BC = Math.sqrt(Math.pow(Xbc,2)+Math.pow(Ybc,2)+Math.pow(Zbc,2));
		
		//Calcul de l'angle
		double angle = Math.acos((Xba*Xbc*+Yba*Ybc+Zba*Zbc)/(BA)*(BC));
		System.out.println(angle*180/Math.PI +" degrés");
		return angle*180/Math.PI;
	}
}
