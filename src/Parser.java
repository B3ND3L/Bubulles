import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static org.graphstream.algorithm.Toolkit.*;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;

public class Parser {

	public Graph parse(String filename){

		String path = "";
		File file = new File(path + filename);

		ArrayList<Bulle> a = readFile(file);
		return listToGraph(a);
	}

	protected ArrayList<Bulle> readFile(File file){

		ArrayList<Bulle> bulles = new ArrayList<Bulle>();

		try{
			InputStream ips=new FileInputStream(file);
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String line;
			while ((line=br.readLine())!=null){

				double x, y, z;

				String [] coords = line.split("   ");

				x = Double.parseDouble(coords[1]);
				y = Double.parseDouble(coords[2]);
				z = Double.parseDouble(coords[3]);


				Bulle bulle = new Bulle(x,y,z);
				bulles.add(bulle);
			}
			br.close();
		}
		catch (Exception e){
			System.out.println(e.toString());
		}

		return bulles;
	}

	protected static boolean hasBulle(Bulle[] tab, Bulle bulle){

		for(Bulle b:tab) if(b==bulle) return true;
		return false;
	}

	
	//Deprecated
	private void trie(Bulle b1, Bulle[] tab, Bulle b2) {
		double dist = b1.computeDistance(b2);
		for(int i=0;i<4;i++){
			if(!hasBulle(tab,b2)) {
				if(tab[i] == null) {
					tab[i] = b2;
				}
				else if(b1.computeDistance(tab[i]) > dist) {
					Bulle tmp = tab[i];
					tab[i] = b2;
					//replacé la bulle précédente qui est peut etre plus proches que les autres stocké
					trie(b1, tab, tmp);
				}
			}
		}
	}

	protected Graph listToGraph(ArrayList<Bulle> list){
		Graph graph = new MultiGraph("graph");

		for(Bulle b1 : list){
			graph.addNode(b1.getId()+"");

			// Ajout de la position de la bulle dans le graphe.
			graph.getNode(b1.getId()+"").setAttribute("x",b1.getX());
			graph.getNode(b1.getId()+"").setAttribute("y",b1.getY());
			graph.getNode(b1.getId()+"").setAttribute("z",b1.getZ());
		}
		
		//A REGLER
		double radius = 1.3;
		
		for( Bulle b1 : list){
			for (Bulle b2 : list){
				if (!b1.equals(b2) && b1.computeDistance(b2)<= radius) {
					if(graph.getEdge(b1.getId()+"-"+b2.getId()) == null && graph.getEdge(b2.getId()+"-"+b1.getId()) == null)
						try{
							graph.addEdge(b1.getId()+"-"+b2.getId(), b1.getId()+"", b2.getId()+"");
							graph.getEdge(b1.getId()+"-"+b2.getId()).addAttribute("distance", b1.computeDistance(b2));
						}catch(Exception e){

						}
				}
				
			}
		}
			
		
		return graph;
	}
}
