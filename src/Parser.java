import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

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
	
	protected Graph listToGraph(ArrayList<Bulle> list){
		Graph graph = new SingleGraph("graph");
		
		for(Bulle b1 : list){
			graph.addNode(b1.getId()+"");
		}
		
		for(Bulle b1 : list){	
			Bulle [] tab = new Bulle[4];
			
			for(Bulle b2 : list){
				double dist = b1.computeDistance(b2);
				for(int i=0;i<4;i++){
					try{
						if(b1.computeDistance(tab[i])>dist && ! hasBulle(tab, b2))
							tab[i] = b2;
					}catch(Exception e){
						tab[i] = b2;
					}
				}
			}
			
			for(int i=0; i<4; i++){
				if(graph.getEdge(b1.getId()+"-"+tab[i].getId()) == null)
					try{
						graph.addEdge(b1.getId()+"-"+tab[i].getId(), b1.getId()+"", tab[i].getId()+"");
					}catch(Exception e){
						
					}
			}
			
		}
		return graph;
	}
}
