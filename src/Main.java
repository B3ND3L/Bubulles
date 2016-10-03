import org.graphstream.graph.Graph;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Parser p = new Parser();
		Graph g = p.parse("norma_N5_tau4_dt2_delai820_000003.txt");
		g.addAttribute("ui.antialias");
		g.addAttribute("ui.quality");
		
		System.out.println(g.getNodeCount());
		System.out.println(g.getEdgeCount());
		
		g.display(true);
	}

}
