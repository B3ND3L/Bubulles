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
		System.out.println(p.parse("norma_N5_tau4_dt2_delai820_000003.txt"));
	}
}
