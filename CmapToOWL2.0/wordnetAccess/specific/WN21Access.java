package wordnetAccess.specific;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.swing.JTextArea;

import cmaps.CmapLink;

import wordnetAccess.utils.Search;
import wordnetDataFile.Synset;


public class WN21Access implements wordnetAccess.WNAccess{
	//	File paths
	public  String wnDir = "WordNet2.1";
	public  String[] dataFilenames = {"data.adj","data.adv","data.noun","data.verb"};
	public  String[] indexFilenames = {"index.adj","index.adv","index.noun","index.verb"};
	public  String[] linkFilenames = {"link.txt"};

//-------------------------------------------------------------------------------------------------	
	public WN21Access(String dataPath){
//		check for SINTAX
	
		//check for files
		this.wnDir = (new File(dataPath)).getAbsolutePath() + File.separatorChar + wnDir + File.separatorChar;

		for (int i = 0; i < dataFilenames.length; i++) {
			File file = new File(this.wnDir + dataFilenames[i]);
			if(file.exists() == false || file.canRead() == false){ 
				System.out.println("WordNet library error: Can't open file " + file.getAbsolutePath());
			}
		}
		for (int i = 0; i < indexFilenames.length; i++) {
			File file = new File(this.wnDir + indexFilenames[i]);
			if(file.exists() == false || file.canRead() == false){ 
				System.out.println("WordNet library error: Can't open file " + file.getAbsolutePath());
			}
		}
		for (int i = 0; i < linkFilenames.length; i++) {
			File file = new File(this.wnDir + linkFilenames[i]);
			if(file.exists() == false || file.canRead() == false){ 
				System.out.println("WordNet library error: Can't open file " + file.getAbsolutePath());
			}
		}
		
	}
	

//	--------------------------------TRABAJO CON WORD--------------------------------------------------	
	
	/*Obtiene todos los sentidos de una apalabra culaquiera
	 * 
	 * */
	public ArrayList<Synset> getAllSynsets(String lemma){
		ArrayList<Synset> allsynsets = new ArrayList<Synset>();
		ArrayList<Synset> possynsets = null;
		possynsets = getSynsets(lemma, 'a');
		allsynsets.addAll(possynsets);

		possynsets = getSynsets(lemma, 'r');
		allsynsets.addAll(possynsets);

		possynsets = getSynsets(lemma, 'n');
		allsynsets.addAll(possynsets);

		possynsets = getSynsets(lemma, 'v');
		allsynsets.addAll(possynsets);
		return allsynsets;
	}
	
	public ArrayList<Synset> getSynsets(String lemma, char pos){
		ArrayList<Synset> synsets = new ArrayList<Synset>();
		Synset synset = null;
//		search mechanism 
		RandomAccessFile afile;
		String line = "";
		try {
			switch(pos){
			case 'a':
				afile = new RandomAccessFile(this.wnDir + indexFilenames[0], "r");
				break;
			case 's':
				afile = new RandomAccessFile(this.wnDir + indexFilenames[0], "r");
				break;
			case 'r':
				afile = new RandomAccessFile(this.wnDir + indexFilenames[1], "r");
				break;
			case 'n':
				afile = new RandomAccessFile(this.wnDir + indexFilenames[2], "r");
				break;
			case 'v':
				afile = new RandomAccessFile(this.wnDir + indexFilenames[3], "r");
				break;
			default:
				afile = new RandomAccessFile(this.wnDir + indexFilenames[2], "r");
			break;
			}

			// Alphabetic order search (binary search)
			line = Search.searchLine(afile, lemma.replace(" ", "_").toLowerCase(), 1740, Search.searchBinary);//Skip license data
			String offset = null;
			if(line != null && !line.equals("")){
				String[] textWords = line.split(" ");
				int p_cnt = Integer.parseInt(textWords[3]);
				int sense_cnt = Integer.parseInt(textWords[4 + p_cnt]);
				for(int i = 0; i < sense_cnt; i++)//para cada sentido que posee lemma
				{
					offset = textWords[6 + p_cnt + i];//obtiene el offset e sentido
					synset = getFullSynset(offset, pos);//aki obtine todos los datos que tiene un sentido,
					if(synset != null)					//incluyendo su offset(nombre,numero..etc)
						synsets.add(synset);
				}
			}
			afile.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(line);
		}	
		return synsets;
	}
//	--------------------------------TRABAJO CON SYNSET--------------------------------------------------	

	public ArrayList<Synset> getAllFullSynsets(String offset){
		ArrayList<Synset> synsets = new ArrayList<Synset>();
		Synset synset = null;
		synset = getFullSynset(offset, 'a');
		if(synset != null)
			synsets.add(synset);
		synset = getFullSynset(offset, 'r');
		if(synset != null)
			synsets.add(synset);
		synset = getFullSynset(offset, 'n');
		if(synset != null)
			synsets.add(synset);
		synset = getFullSynset(offset, 'v');
		if(synset != null)
			synsets.add(synset);
		return synsets;
	}

	public Synset getFullSynset(String offset, char pos){
		//search mechanism for each file
		RandomAccessFile afile;
		RandomAccessFile bfile;
		Synset synset = new Synset();
		String line = "";
		int idxfile = 2;
		try {
			switch(pos){
			case 'a':
				idxfile = 0;
				break;
			case 's':
				idxfile = 0;
				break;
			case 'r':
				idxfile = 1;
				break;
			case 'n':
				idxfile = 2;
				break;
			case 'v':
				idxfile = 3;
				break;
			}
			afile = new RandomAccessFile(this.wnDir + dataFilenames[idxfile], "r");//fichero k contiene los datos del sentido
			bfile = new RandomAccessFile(this.wnDir + indexFilenames[idxfile], "r");
			
			afile.seek(Long.parseLong(offset));
			line = afile.readLine(); 
			if(line != null && line.equals("") == false && line.split(" ")[0].equals(offset))
				synset.parseData(line);//aki esxtraed del alinea los datos que le faltaban al s ntido
			else
				return null;
			//synset.getDomains().addAll(getDomains(synset.getOffset(), synset.getPos()));
			for(String word : synset.getWords()){
				line = Search.searchLine(bfile, word, 1740, Search.searchBinary);
				if(line != null && !line.equals("")){
					String[] textWords = line.split(" ");
					int p_cnt = Integer.parseInt(textWords[3]);
					int sense_cnt = Integer.parseInt(textWords[4 + p_cnt]);
					String wOffset = "";
					for(int i = 0; i < sense_cnt; i++)
					{
						wOffset = textWords[6 + p_cnt + i];
						if(offset.equals(wOffset))
							synset.getSense_numbers().add(i + 1);
					}
				}
			}
			afile.close();
			bfile.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(line);
		}
		return synset;
	}

//	--------------------------------TRABAJO CON LINK--------------------------------------------------		
	public String getlink(String link){
		String line = "";
		String Link="";
		try {
			RandomAccessFile afile= new RandomAccessFile(this.wnDir + linkFilenames[0], "rw");
			line = Search.searchSeqLink(afile,link,0);

			if(line != null && !line.equals("")){
				String[] textWords = line.split("@");
				Link=textWords[1];
			}
			afile.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(line);
		}	
		return Link;		
	}	
	
	public ArrayList<CmapLink> getCmapLinks(){
		ArrayList<CmapLink> Cmaplinks=new ArrayList<CmapLink>();
		String line = "";
		
		try {
			//RandomAccessFile afile= new RandomAccessFile(this.wnDir + linkFilenames[0], "rw");
			//afile.seek(0);
			BufferedReader reader = new BufferedReader(new FileReader(this.wnDir + linkFilenames[0]));
			//line = afile.readLine();
			line = reader.readLine();
			while(line != null && !line.equals("") ){
				CmapLink cmaplink = new CmapLink();
				String[] textWords = line.split("@");
				cmaplink.setLinkName(textWords[1]);
				cmaplink.setCategory(textWords[2]);
				Cmaplinks.add(cmaplink);

				line = reader.readLine();
			}
//			afile.close();
			reader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(line);
		}	
		return Cmaplinks;
	}

	public ArrayList<String> getLinks(){
		ArrayList<String> links=new ArrayList<String>();
		String line = "";
		String link = "";
		try {
			RandomAccessFile afile= new RandomAccessFile(this.wnDir + linkFilenames[0], "rw");

			afile.seek(0);
			line = afile.readLine();
			while(line != null && !line.equals("") ){
				String[] textWords = line.split("@");
				link=textWords[1];
				line = afile.readLine();
				links.add(link);
			}

			afile.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(line);
		}	
		return links;
	}
	
	public String getCategory(String category){	
		String Category="";
		String line = "";
		try {
			RandomAccessFile afile= new RandomAccessFile(this.wnDir + linkFilenames[0], "rw");
			line = Search.searchSeqLink(afile,category,0);

			if(line != null && !line.equals("")){
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
	
	public ArrayList<String> getCategories(){	
		ArrayList<String> categories= new ArrayList<String>();
		String Category="";
		String line = "";
		try {
			RandomAccessFile afile= new RandomAccessFile(this.wnDir + linkFilenames[0], "rw");
			afile.seek(0);

			line = afile.readLine();
			while(line != null && !line.equals("") ){
				String[] textWords = line.split("@");
				Category=textWords[2];
				line = afile.readLine();
				categories.add(Category);
			}
			afile.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(line);
		}	
		return categories;
	}

	public String getLinkCategory(String link){	
		String Category="";
		String line = "";
		try {
			RandomAccessFile afile= new RandomAccessFile(this.wnDir + linkFilenames[0], "rw");
			
			line = Search.searchSeqLink(afile,link,0);

			if(line != null && !line.equals("")){
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
	
	public void addAllLinks(ArrayList<CmapLink> cmaplinks){	

		String line="";
		try {
			StringBuffer lineTotal=new StringBuffer();
			PrintWriter writer = new PrintWriter(this.wnDir + linkFilenames[0]);
			JTextArea areaTexto= new JTextArea();

			for (CmapLink cmaplink : cmaplinks) {
				String lin=cmaplink.getLinkName().trim();
				String cat=cmaplink.getCategory().trim();
				line="@"+lin+"@"+cat ;
				line.trim();
				if(getLinkCategory(lin).equals("")){//...Ver k ese link no tenga categoría, de lo contrario significa que YA!! se encuentra en el fichero 
					if(line != null && !line.equals(""))
						lineTotal.append(line.trim());
						lineTotal.append("\n"); 
				}
			}
			areaTexto.setText(lineTotal.toString());
			writer.print(areaTexto.getText());
			writer.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(line);
		}
	}
	
	public boolean addLink(String link,String category){	
		boolean add=false;
		String lin=link.trim();
		String cat=category.trim();
		String line="@"+lin+"@"+cat ;
		line.trim();
		if(getLinkCategory(link).equals("")){//...Ver k ese link no tenga categoría, de lo contrario significa que YA!! se encuentra en el fichero 
			try {//y de  no inserta dos vecesesta 
				RandomAccessFile afile= new RandomAccessFile(this.wnDir + linkFilenames[0], "rw");
				afile.seek(afile.length());
				if(line != null && !line.equals("")){
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
}
