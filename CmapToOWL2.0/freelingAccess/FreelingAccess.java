package freelingAccess;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FreelingAccess {
	public String frPath = "FreeLing-2.0";
	public String frlanguage = "en";
	public char separator = File.separatorChar;
	public String frConfigDir = "FreeLing-2.0" + separator + "share" + separator + "config";
	public String frBinDir = "FreeLing-2.0" + separator + "bin";
	public String frShareDir = "FreeLing-2.0" + separator + "share";

	public FreelingAccess(String frlanguage, String frPath){
		this.frlanguage = frlanguage;
		if(frPath != null && frPath.equals("") == false)
				this.frPath = frPath;
		this.frPath = (new File(this.frPath)).getAbsolutePath();
		this.frConfigDir = this.frPath + separator + "share" + separator + "config";
		this.frBinDir = this.frPath + separator + "bin";
		this.frShareDir = this.frPath + separator + "share";
	}

	/**
	 * @author Ale
	 * @param concept del cual se va a buscar la palabra raiz/primitiva
	 * @return root una palabra raiz
	 */
	public  boolean isNounWord(String linkWord) {
		boolean noun= false;
		String[] linnk=	linkWord.split(" ");
		
		if(linnk.length== 1){
			try {
				ProcessBuilder pb = new ProcessBuilder();
				pb.directory(new File(this.frPath));
				pb.environment().put("FREELINGSHARE", this.frShareDir);
				
				String[] cmd = { frBinDir + separator + "analyzer.exe", "-f",
						frConfigDir + separator + frlanguage + ".cfg", "--outf","morfo" };
				pb.command(cmd);

				Process proc = pb.start();
				BufferedOutputStream os = (BufferedOutputStream) proc.getOutputStream();
				os.write((linkWord + "\n").getBytes());
				os.flush();
				os.close();

				proc.waitFor();
				BufferedReader lineReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line = lineReader.readLine();
				String[] word = {};
				double  problast=0;
				double probnew=0;
				int p=3;
				int w=2;
				word = line.split(" ");//ver lo de las probabilidades.........
				while (line != null && line != "" && !noun && (w<word.length)){
					word = line.split(" ");
			
					if (word.length >= 2) {
						probnew=Double.parseDouble(word[p].toString());
						if(probnew>problast){
							problast=probnew;
							if (word[w].charAt(0) == 'N')
								noun = true;

						}	
						p=p+3;
						w=w+3;
					} 
					else
						line = lineReader.readLine();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return noun;
}
	/**
	 * @author Ale
	 * @param concept del cual se va a buscar la palabra raiz/primitiva
	 * @return root una palabra raiz
	 */
	public   String getRoot(String concept)
	{	
		String root = "";
	try {
		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(new File(this.frPath));
		pb.environment().put("FREELINGSHARE", this.frShareDir);
		String[] cmd = {frBinDir + separator + "analyzer.exe" , "-f", frConfigDir  + separator + frlanguage + ".cfg", "--outf", "morfo"};
		pb.command(cmd);

		Process proc = pb.start();
		BufferedOutputStream os = (BufferedOutputStream) proc.getOutputStream();
		os.write((concept + "\n").getBytes());
		os.flush();
		os.close();

		proc.waitFor();
		BufferedReader lineReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		String line = lineReader.readLine();
		String[] word = {};
		
		while(line != null && line != ""){
			
			word = line.split(" ");
			
			if(word.length > 1){
			root = word[1];
			}
			line = lineReader.readLine();
			}
		
	}
	catch (Exception e){
	e.printStackTrace();
	}
	return root;
}
	/**
	 * @param text String to process
	 * @param roots Returned roots
	 * @param lang FreeLing language to process text: ca-Catalan en-English es-Spanish gl-Galician it-Italian
	 */
	public void getRoots(String text, ArrayList<String> roots){ 

		try {
			ProcessBuilder pb = new ProcessBuilder();
			pb.directory(new File(this.frPath));
			pb.environment().put("FREELINGSHARE", this.frShareDir);
			String[] cmd = {frBinDir + separator + "analyzer.exe" , "-f", frConfigDir  + separator + frlanguage + ".cfg", "--outf", "morfo"};
			pb.command(cmd);

			Process proc = pb.start();
			BufferedOutputStream os = (BufferedOutputStream) proc.getOutputStream();
			os.write((text + "\n").getBytes());
			os.flush();
			os.close();

			proc.waitFor();
			BufferedReader lineReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = lineReader.readLine();

			ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>(); // arrays (each array has roots for each word)

			while(line != null && line != ""){

				String[] words = line.split(" ");

				// build array of root words for each word
				if(words.length > 1){
					ArrayList<String> wordroots = new ArrayList<String>();
					for(int i = 1; i < words.length; i+= 3){
						if(wordroots.contains(words[i]) == false)
							wordroots.add(words[i]);
					}
					lines.add(wordroots);
				}
				line = lineReader.readLine();
			}
			
			//buid all pared-roots of multiple words
			for (int i = 0; i < lines.size(); i++) {
				//multiplicate current stored roots by line size. if none, make equal to line size
				if(i == 0)
					roots.addAll(lines.get(i));
				else{
					int rsize = roots.size();
					for (int j = 0; j < rsize; j++) { //for each root already stored
						for (int k = 1; k < lines.get(i).size(); k++) { // make new combinations with current line
							roots.add(roots.get(j) + " " + lines.get(i).get(k));
							}
						roots.set(j, roots.get(j) + " " + lines.get(i).get(0));
					}
				}
			}
				
/*

				for(int i = 1; i < words.length; i+= 3){
					while(roots.size() < i/3 + 1)
						roots.add("");
					if(roots.get(i/3).equals("") == false)
						roots.set(i/3, roots.get(i/3) + " ");
					roots.set(i/3, roots.get(i/3) + words[i]);
				}
				line = lineReader.readLine();
			}
			for (int i = 0; i < roots.size(); i++) {
				String root = roots.get(i);
				if(roots.indexOf(root) != roots.lastIndexOf(root)){
					roots.remove(root);
					i--;
				}
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}