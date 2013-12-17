package wordnetAccess.utils;

import java.io.IOException;
import java.io.RandomAccessFile;
import sintax.CheckSintax;

public class Search {
	public static int searchSequential = 1;
	public static int searchBinary = 2;
	public static   CheckSintax sintaxWN;
	
	
	public static String searchLine(RandomAccessFile afile, String key, long startbyte, int method){
		String line = "";
		if(method == searchSequential)
			return searchSeqSense(afile, key, startbyte);
		if(method == searchBinary)
			return searchBin(afile, key, startbyte);
		return line;
	}
	public static String searchSeqLink(RandomAccessFile afile, String key, long startbyte){
		String findline = "";
		String keyfound = null;
		sintaxWN=new CheckSintax();
		String keyNormal=sintaxWN.normalizeTerms(key);
		try{
			afile.seek(startbyte);
			String line = "";
			boolean found = false;

			line = afile.readLine();
			while(line != null && !found){
							
				keyfound = line.split("@")[1];//sera 1 o 0 donde esta realmente el link
				String keyfoundNormal= sintaxWN.normalizeTerms(keyfound);
				if(Text.compareWNStrings(keyNormal, keyfoundNormal) == 0){
					findline = line;
						found = true;
				}
				line = afile.readLine();
			}
		}catch (Exception e) {}
		return findline;
	}

	private static String searchSeqSense(RandomAccessFile afile, String key, long startbyte){
		String findline = "";
		String keyfound = null;
		try{
			afile.seek(startbyte);
			String line = "";
			boolean found = false;

			line = afile.readLine();
			while(line != null && !found){
							
				keyfound = line.split(" ")[0];
				if(Text.compareWNStrings(key, keyfound) == 0){
					findline = line;
						found = true;
				}
				line = afile.readLine();
			}
		}catch (Exception e) {}
		return findline;
	}
	public static String searchSeqword(RandomAccessFile afile, String key, long startbyte){
		String findline = "";
		String keyfound = null;
		try{
			afile.seek(startbyte);
			String line = "";
			boolean found = false;

			line = afile.readLine();
			while(line != null && !found){
							
				keyfound = line.split(" ")[0];
				if(Text.compareWNStrings(key, keyfound) == 0){
					findline = line;
						found = true;
				}
				line = afile.readLine();
			}
		}catch (Exception e) {}
		return findline;
	}


	private static String searchBin(RandomAccessFile afile, String key, long startbyte){
		String text = null;
		boolean found = false;
		boolean end = false;

		long sizem = 0;
		long posm = 0;
		
		int endlinenum = 0;

		String searchkey = "";
		String searchLine = "";
		int comparem = 0;

		try{
			long pos1 = startbyte;
			long pos2 = seekLine(afile, afile.length(), endlinenum);
			
			searchLine = afile.readLine();	//Skip final empty lines
			while((searchLine == null || searchLine.equals("")) && pos1 != pos2){
				endlinenum--;
				pos2 = seekLine(afile, afile.length(), endlinenum);
				searchLine = afile.readLine();
			}
				

			do{
				if(pos1 == pos2)
					end = true;
				
				sizem = (pos2 - pos1)/2;
				posm = pos1 + sizem;

				posm = seekLine(afile, posm, 0);
				afile.seek(posm);
				searchLine = afile.readLine();
				if(searchLine == null){
					end = true;
				}
				if(searchLine.length() >= key.length()){
					searchkey = searchLine.split(" ")[0];
				}
				else searchkey = searchLine;

				comparem = Text.compareWNStrings(searchkey, key); 

				if(comparem > 0){
					if(posm == pos2)
						pos2 = seekLine(afile, pos2, -1);
					else pos2 = posm;
				}
				if(comparem < 0){
					if(posm == pos1)
						pos1 = seekLine(afile, pos1, 1);
					else pos1 = posm;
				}
				if(comparem == 0){
					found = true;
				}
				
			}
			while(!end && !found);
		}catch (Exception e) {}

		if(found)
			text = searchLine;
		return text;
	}

	public static long seekLine(RandomAccessFile afile, long pos, int lines) {
		// Seek at the start of the line

		if(lines > 0){
			try {
				afile.seek(pos);
				for(int i = 0; i < lines; i++){
					afile.readLine();
					pos = afile.getFilePointer();
				}
			} catch (IOException e) {}
		}

		char a;
		int newLines = 0;
		//	if (lines == 0) then seek at the start of the current line
		if(lines <= 0){

			try {
				boolean found = false;
				while( newLines < lines * -1 + 1 && found == false){

					if(pos > afile.length())
						pos = afile.length();
					if(pos == 0)
						found = true;
					else {
						pos--;
						afile.seek(pos);
						a = (char) afile.readByte();
						if(a == '\n')
							newLines++;
					}
					if(newLines == lines * -1 + 1){ //position found (end of new line)
						found = true;
						pos++;
					}
				} 
				afile.seek(pos);
			}
			catch (IOException e) {}
		}
		return pos;
	}
}
