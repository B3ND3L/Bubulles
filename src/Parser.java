import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.ConnectedComponents.ConnectedComponent;
import org.graphstream.graph.Edge;

public class Parser {

	public GraphBulle parse(String filename){

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

	protected GraphBulle listToGraph(ArrayList<Bulle> list){
		GraphBulle graph = new GraphBulle("graph");

		for(Bulle b1 : list){
			graph.addNode(b1.getId()+"");

			// Ajout de la position de la bulle dans le graphe.
			graph.getNode(b1.getId()+"").setAttribute("x",b1.getX());
			graph.getNode(b1.getId()+"").setAttribute("y",b1.getY());
			graph.getNode(b1.getId()+"").setAttribute("z",b1.getZ());
			graph.getNode(b1.getId()+"").setAttribute("ligne",b1.getId()+1);
		}

		// A REGLER
		double radius = 1.3;

		for( Bulle b1 : list){
			for (Bulle b2 : list){
				if (!b1.equals(b2) && b1.computeDistance(b2)<= radius) {
					if(graph.getEdge(b1.getId()+"-"+b2.getId()) == null && graph.getEdge(b2.getId()+"-"+b1.getId()) == null){
						try{
							graph.addEdge(b1.getId()+"-"+b2.getId(), b1.getId()+"", b2.getId()+"");
							graph.getEdge(b1.getId()+"-"+b2.getId()).addAttribute("distance", b1.computeDistance(b2));
						}catch(Exception e){

						}
					}
				}
			}
		}

		return graph;
	}

public void groupsToFile(Graph g){

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("resultat.txt")));

			int prevGroupe = 1;
			String ligne = "";
			for( Node n : g){
				String name = n.getId().split(" ")[0];
				int groupe = Integer.parseInt(n.getId().split(" ")[1]);
				if(prevGroupe == groupe){
					ligne += name+" ";
				}else{
					writer.write(ligne+"\r\n");
					ligne = name+" ";
				}
				prevGroupe = groupe;
			}
			writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
	}
}
