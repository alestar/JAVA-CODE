package wordnetAccess.specific;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import cmaps.CmapLink;

import wordnetAccess.utils.Search;
import wordnetDataFile.Synset;

public class WN16Access implements wordnetAccess.WNAccess{
	//	File paths
	public  String wnDir = "WordNet1.6";
	public  String[] dataFilenames = {"ADJ.DAT","ADV.DAT","NOUN.DAT","VERB.DAT"};
	public  String[] indexFilenames = {"ADJ.IDX","ADV.IDX","NOUN.IDX","VERB.IDX"};
	public  String[] domainFilenames = {"wn-domains-2.0-20050210","wn-domains-hierachy"};

//	-------------------------------------------------------------------------------------------------	
	public WN16Access(String dataPath){
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
		for (int i = 0; i < domainFilenames.length; i++) {
			File file = new File(this.wnDir + domainFilenames[i]);
			if(file.exists() == false || file.canRead() == false){ 
				System.out.println("WordNet library error: Can't open file " + file.getAbsolutePath());
			}
		}
	}
//	--------------------------------TRABAJO CON WORD--------------------------------------------------	
	public ArrayList<Synset> getAllSynsets(String lemma){
		ArrayList<Synset> synsets = new ArrayList<Synset>();
		ArrayList<Synset> nsynsets = null;
		nsynsets = getSynsets(lemma, 'a');
		synsets.addAll(nsynsets);

		nsynsets = getSynsets(lemma, 'r');
		synsets.addAll(nsynsets);

		nsynsets = getSynsets(lemma, 'n');
		synsets.addAll(nsynsets);

		nsynsets = getSynsets(lemma, 'v');
		synsets.addAll(nsynsets);
		return synsets;
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
			line = Search.searchLine(afile, lemma.replace(" ", "_").toLowerCase(), 1740, Search.searchBinary);	//Skip license data
			String offset = null;
			if(line != null && line != ""){
				String[] textWords = line.split(" ");
				//lemma = textWords[0];
				//pos = textWords[1].charAt(0);
				//int poly_cnt = Integer.parseInt(textWords[2]);
				int p_cnt = Integer.parseInt(textWords[3]);
				int sense_cnt = Integer.parseInt(textWords[4 + p_cnt]);
				for(int i = 0; i < sense_cnt; i++)
				{
					offset = textWords[6 + p_cnt + i];
					synset = getFullSynset(offset, pos);
					if(synset != null)
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
			afile = new RandomAccessFile(this.wnDir + dataFilenames[idxfile], "r");
			bfile = new RandomAccessFile(this.wnDir + indexFilenames[idxfile], "r");
			
			afile.seek(Long.parseLong(offset));
			line = afile.readLine(); 
			if(line != null){
				String[] readLine = line.split(" ");
				if(line.equals("")==false && readLine.length>0){
					if(readLine[0].equalsIgnoreCase(offset))
						synset.parseData(line);
					else
						return null;	
				}
			}
							
				//synset.getDomains().addAll(getDomains(synset.getOffset(), synset.getPos()));
				for(String word : synset.getWords()){
					line = Search.searchLine(bfile, word, 1740, Search.searchBinary);
					if(line != null && line != ""){
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
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println(line);
		}
		return synset;
	}

	public ArrayList<String> getDomains(String synset_offset, char pos){
		String line = "";
		String key = synset_offset + "-" + pos;
		ArrayList<String> domains = new ArrayList<String>();
		// search mechanism - Binary search 
		RandomAccessFile afile;
		try {
			afile = new RandomAccessFile(wnDir + domainFilenames[0], "r");
			line = Search.searchLine(afile, key, 0, Search.searchBinary);
			if(line != null){
				String[] textWords = line.split(" ");
				int i = 1;
				while(i < textWords.length)
				{
					domains.add(textWords[i]);
					i++;
				}
			}
			afile.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return domains;
	}

	public ArrayList<String> getSonDomains(String domain){
		ArrayList<String> sons = new ArrayList<String>();
		domain = domain.toLowerCase();
		RandomAccessFile afile;
		try {
			afile = new RandomAccessFile(wnDir + domainFilenames[1], "r");
			// Sequential search

			long posEnd = afile.length();
			long posDomain = 0;
			// search domain in file
			afile.seek(0);  	//Seek at the start of the first line
			boolean found = false;
			int level = 0;
			String fdomain = "";
			while(found == false && afile.getFilePointer() < posEnd){
				posDomain = afile.getFilePointer();
				String line = afile.readLine();
				level = 0;
				while(line.charAt(level) == '-')
					level++;
				fdomain = line.substring(level).toLowerCase();
				if(fdomain.compareToIgnoreCase(domain) == 0)
					found = true;
			}
			if(found){
				//read sons
				boolean end = false;
				Search.seekLine(afile, posDomain, 1); //Seek at the start of the next line
				int slevel = 0;
				String sdomain = "";
				while(end == false){
					String line = afile.readLine();
					if(line != null){
						slevel = 0;
						while(line.charAt(slevel) == '-')
							slevel++;
						sdomain = line.substring(slevel).toLowerCase();
						if(slevel > level && sons.contains(sdomain) == false)
							sons.add(sdomain);
						else end = true;
					}
					else end = true;
				}
			}
			afile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sons;
	}

	public ArrayList<String> getFatherDomains(String domain){
		ArrayList<String> fathers = new ArrayList<String>();
		// secuencial search
		//	search mechanism 
		domain = domain.toLowerCase();
		RandomAccessFile afile;
		try {
			afile = new RandomAccessFile(wnDir + domainFilenames[1], "r");
			// Sequential search

			long posEnd = afile.length();
			long posDomain = 0;
			// search domain in file
			afile.seek(0);  	//Seek at the start of the first line
			boolean found = false;
			int slevel = 0;
			String sdomain = "";
			while(found == false && afile.getFilePointer() < posEnd){
				posDomain = afile.getFilePointer();
				String line = afile.readLine();
				slevel = 0;
				while(line.charAt(slevel) == '-')
					slevel++;
				sdomain = line.substring(slevel).toLowerCase();
				if(sdomain.compareToIgnoreCase(domain) == 0)
					found = true;
			}
			if(found){//read father
				if(slevel != 0){ //domain has a father
					int flevel = slevel;
					String fdomain = "";
					long posf = posDomain;
					while(flevel >= slevel && posf != 0){
						posf = Search.seekLine(afile, posf, -1); //Seek at the start of the previous line
						String line = afile.readLine();
						flevel = 0;
						while(line.charAt(flevel) == '-')
							flevel++;
						fdomain = line.substring(flevel).toLowerCase();
					}
					if(flevel < slevel && fathers.contains(fdomain) == false)
						fathers.add(fdomain);
				}
			}
			afile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fathers;
	}
//	--------------------------------TRABAJO CON LINK--------------------------------------------------	
	public String getLinkCategory(String link){
	return null;
	}
	public boolean addLink(String linkName, String category){
	return false;
	}
	public ArrayList<String> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getCategory(String category) {
		// TODO Auto-generated method stub
		return null;
	}
	public ArrayList<String> getLinks() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getlink(String link) {
		// TODO Auto-generated method stub
		return null;
	}
	public ArrayList<CmapLink> getCmapLinks() {
		// TODO Auto-generated method stub
		return null;
	}
/*	public boolean delLink(String link, String category) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean chgLink(String newlink, String newcategory, int offset) {
		// TODO Auto-generated method stub
		return false;
	}*/
	public void addAllLinks(ArrayList<CmapLink> cmaplinks) {
		// TODO Auto-generated method stub
		
	}

	

}
