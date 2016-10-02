import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Parser {
	
	public ArrayList<Bulle> parse(String filename){
		
		String path = "";
		File file = new File(path + filename);
		
		return readFile(file);
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
	
	public static void main(String [] rgv){
		Parser p = new Parser();
		
		ArrayList<Bulle> res = p.parse("norma_N5_tau4_dt2_delai820_000003.txt");
		
		//System.out.println(res);
		
		for(Bulle b1 : res){
			Bulle [] tab = new Bulle[4];
			for(Bulle b2 : res){
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
			System.out.println(b1);
			for(int i=0;i<4;i++)
				System.out.println(tab[i]);
		}
	}
	
	protected static boolean hasBulle(Bulle[] tab, Bulle bulle){
		
		for(Bulle b:tab) if(b==bulle) return true;
		return false;
	}
}
