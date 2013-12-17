package file;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Iterator;

import wordnetAccess.utils.Search;
/**@author Alejandro R.C
 * @return Clase que administra e imprime los conceptos de Opencyc(los que no son Constantes),
 *  los que estan en el Repositorio OWL y los que estan en el MC para comparar si existe
 *  alguna correspondencia entre estos...
 */
public class FileConcepsManage {
	public  String source = "";//pude ser los conceptos de un MC,del Repositorio OWl o de Opencyc
	public  String concepsFileName ="";
	public  ArrayList<String> NameConceps ;
	public RandomAccessFile afile;
	
	public FileConcepsManage(String source) {
		super();
		this.source = source;
		this.concepsFileName = source + "Conceps.txt";
		NameConceps=new ArrayList<String>();
		
	}
	
//	... Métodos para salvar todas los conceptos que se encuentran en el repositorio de .owl...
	
	/**@author Alejandro R.C
	 * @return Adiciona los conceptos(sino se han adicionado previamnete) 
	 * al fichero correspondeinte segunla fuente de donde se esten sacando dichos conceptos...
	 */
	public boolean addConcep(String concep){	
		boolean add=false;
		String con=concep.trim();
		try {
			//if(searchSeqConcep(concep,0).equals("")){
				this.afile= new RandomAccessFile(this.concepsFileName, "rw");
				this.afile.seek(this.afile.length());
				if(con != null && !con.equals("")){
					this.afile.writeBytes(con);
					this.afile.writeBytes("\n");
					add=true;
				}
				this.afile.close();
			}
		//}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(con);
		}
		return add;
	}
	
	/**@author Alejandro R.C
	 * @return Busca los conceptos en el fichero de texto, secuencialmente...
	 */
	public  String searchSeqConcep(String seekconcep, long startbyte){
		String findline = "";
		try{
			this.afile= new RandomAccessFile(this.concepsFileName, "rw");
			this.afile.seek(0);
			String line = "";
			boolean found = false;

			line = afile.readLine();
			while(line != null && !found){

				if(seekconcep.equalsIgnoreCase(line)){
					findline = line;
					found = true;
				}
				else
					line = afile.readLine();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println();
		}
		return findline;
	}
	
	/**@author Alejandro R.C
	 * @return Busca los conceptos en la lista de Nombres de Conecptos cargada.. 
	 */
	public  boolean findConcep(String seekconcep){
		boolean found=false;
		Iterator<String> iter =this.NameConceps.iterator();
		while (iter.hasNext()&&!found) {
			String concep = iter.next();
			if(concep.equalsIgnoreCase(seekconcep))
				found=true;
		}
		return found;
	}
	
	/**@author Alejandro R.C
	 * @return Carga los conceptos en la lista de Nombres de Conecptos del fichero de texto... 
	 */
	public  void loadConcep(){
		try{
			this.afile= new RandomAccessFile(this.concepsFileName, "rw");
			this.afile.seek(0);
			String line = "";
			this.NameConceps.clear();
			line = this.afile.readLine();
			while(line != null){
				this.NameConceps.add(line);
				line = this.afile.readLine();
			}
			this.afile.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println();}
	}
	public boolean addConcepTagCatg(String concep,String category){	
		boolean add=false;
		String concp=concep.trim();
		String cat=category.trim();
		String line="@"+concp+"@"+cat ;
		line.trim();
		if(getConcepTagCategory(concp).equalsIgnoreCase("")){//...Ver k ese link no tenga categoría, de lo contrario significa que YA!! se encuentra en el fichero 
			try {//y de  no inserta dos veces esta 
				RandomAccessFile afile= new RandomAccessFile(this.concepsFileName, "rw");
				afile.seek(afile.length());
				if(line != null && !line.equalsIgnoreCase("")){
					afile.writeBytes(line);
					afile.writeBytes("\n");
					add=true;
				}
				afile.close();
			}
			catch (Exception e) {
				e.printStackTrace();
				System.out.println(line);
			}
		}
		return add;
	}
	
	public String getConcepTagCategory(String concep){	
		String Category="";
		String line = "";
		try {
			RandomAccessFile afile= new RandomAccessFile(this.concepsFileName, "rw");
			
			line = Search.searchSeqLink(afile,concep,0);

			if(line != null && !line.equalsIgnoreCase("")){
				String[] textWords = line.split("@");
				Category=textWords[2];
			}
			afile.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(line);
		}	
		return Category;
	}
}


