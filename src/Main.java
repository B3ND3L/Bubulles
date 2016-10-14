import java.util.ArrayList;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Parser p = new Parser();
		Graph g = p.parse("norma_N5_tau4_dt2_delai820_000003.txt");
		
		Tourbillon t = new Tourbillon(g);
		
		int index = (int)(Math.random()*(g.getNodeCount()));
		Node n = g.getNode(index);
		ArrayList<Node> al = new ArrayList<Node>();
		al.add(n);
		t.findGroup(null, n, 0, 100, al);
		System.out.println(al);
		t.colorNodes(al);
	}

}
