package wordnetDataFile;

import java.util.ArrayList;


public class Synset {
	private String offset; 	//Current byte offset in the file represented as an 8 digit decimal integer.
	private String lex_filenum;	//Two digit decimal integer corresponding to the lexicographer file name containing the synset. See lexnames(5WN) for the list of filenames and their corresponding numbers. 
	private char pos;//One character code indicating the synset type(ss_type): 
					//	n NOUN  v VERB  a ADJECTIVE  s ADJECTIVE SATELLITE  r ADVERB 
	private ArrayList<String> words = new ArrayList<String>();//ASCII form of a word as entered in the synset by the lexicographer, with spaces replaced by underscore characters (_ ). The text of the word is case sensitive, in contrast to its form in the corresponding index. pos file, that contains only lower-case forms. In data.adj , a word is followed by a syntactic marker if one was specified in the lexicographer file. A syntactic marker is appended, in parentheses, onto word without any intervening spaces. See wninput(5WN) for a list of the syntactic markers for adjectives. 
	// es decir pa labras que tienen ese sentido
	private ArrayList<Integer> sense_numbers = new ArrayList<Integer>();//The sense number of synset in every word(el offset  de los sentidos) 
	/*An adjective may be annotated with a syntactic marker indicating a limitation on the syntactic position the adjective may have in relation to noun that it modifies. If so marked, the marker appears between the word and its following comma. If a lex_id is specified, the marker immediately follows it. The syntactic markers are: 
		(p)    predicate position 
		(a)    prenominal (attributive) position 
		(ip)    immediately postnominal position */ 
	private ArrayList<String> synmarker = new ArrayList<String>();
	private ArrayList<Integer> lex_indexes = new ArrayList<Integer>();//One digit hexadecimal integer that, when appended onto lemma , uniquely identifies a sense within a lexicographer file. lex_id numbers usually start with 0 , and are incremented as additional senses of the word are added to the same file, although there is no requirement that the numbers be consecutive or begin with 0 . Note that a value of 0 is the default, and therefore is not present in lexicographer files. 
	private ArrayList<Relation> pointers = new ArrayList<Relation>();//A pointer from this synset to another. (ptr) is of the form: pointer_symbol synset_offset pos source/target 
	private String frames;			//In data.verb only, a list of numbers corresponding to the generic verb sentence frames for word s in the synset. frames is of the form: f_cnt + f_num w_num [+ f_num w_num...] where f_cnt a two digit decimal integer indicating the number of generic frames listed, f_num is a two digit decimal integer frame number, and w_num is a two digit hexadecimal number indicating the word in the synset that the frame applies to. As with pointers, if this number is 00 , f_num applies to all word s in the synset. If non-zero, it is applicable only to the word indicated. Word numbers are assigned as described for pointers. Each f_num w_num pair is preceded by a + . See wninput(5WN) for the text of the generic sentence frames. 
	private String gloss;			//Each synset contains a gloss. A gloss is represented as a vertical bar (| ), followed by a text string that continues until the end of the line. The gloss may contain a definition, one or more example sentences, or both. 
	//private ArrayList<String> domains = new ArrayList<String>();
	
	public Synset() {
		this.offset = "";
		this.lex_filenum = "00";
		this.pos = 'n';
		this.words = new ArrayList<String>();
		this.sense_numbers = new ArrayList<Integer>();
		this.synmarker = new ArrayList<String>();
		this.lex_indexes = new ArrayList<Integer>();
		this.pointers = new ArrayList<Relation>();
		this.frames = "";
		this.gloss = "";
	}
	
	public boolean equals(Object object){
		boolean equal= false;
		if(object != null || (object instanceof Synset == true)){
			Synset syn = (Synset) object;
			equal= offset.equals(syn.getOffset()) && pos == syn.getPos();
		}
		return equal;
	}
	
	public String toString() {
		String text = "{" + this.offset + "}-" + this.pos + " ";
		for (int i = 0; i < words.size(); i++) {
			text += words.get(i) + "#" + sense_numbers.get(i);
			if(i < words.size() - 1)
				text += ", ";
			else text += " ";
		}
		text += " [";
/*		for(String domain : domains)
			text += domain + " ";
		text += "] --(" + gloss + ")";*/
		return text;
	}
	
	public String toShortString() {
		String text = this.pos + "(";
		for (int i = 0; i < words.size(); i++) {
			text += words.get(i) + "#" + sense_numbers.get(i);
			if(i < words.size() - 1)
				text += ", ";
			else text += " ";
		}
		text += ")";
		return text;
	}

	public String getFrames() {
		return frames;
	}

	public void setFrames(String frames) {
		this.frames = frames;
	}

	public String getGloss() {
		return gloss;
	}

	public void setGloss(String gloss) {
		this.gloss = gloss;
	}

	public String getLex_filenum() {
		return lex_filenum;
	}

	public void setLex_filenum(String lex_filenum) {
		this.lex_filenum = lex_filenum;
	}

	public ArrayList<Integer> getLex_indexes() {
		return lex_indexes;
	}

	public void setLex_indexes(ArrayList<Integer> lex_indexes) {
		this.lex_indexes = lex_indexes;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public ArrayList<Relation> getPointers() {
		return pointers;
	}

	public void setPointers(ArrayList<Relation> pointers) {
		this.pointers = pointers;
	}

	public char getPos() {
		return pos;
	}

	public void setPos(char pos) {
		this.pos = pos;
	}

	public ArrayList<String> getWords() {
		return words;
	}

	public void setWords(ArrayList<String> words) {
		this.words = words;
	}
	
	public ArrayList<String> getSynmarker() {
		return synmarker;
	}

	public void setSynmarker(ArrayList<String> synmarker) {
		this.synmarker = synmarker;
	}
	
	public ArrayList<Integer> getSense_numbers() {
		return sense_numbers;
	}

	public void setSense_numbers(ArrayList<Integer> sense_numbers) {
		this.sense_numbers = sense_numbers;
	}
	/*Metodo para sacar los datos(data.pos) de un sentido 
	 * de una linea leida del fhicero de synsets
	 * */
	public void parseData(String wnText){
		if(wnText == null || wnText.equals(""))
			return;
		try {
			String[] textWords = wnText.split(" ");
			offset = textWords[0];
			lex_filenum = textWords[1];
			pos = textWords[2].charAt(0);
			int w_cnt = Integer.parseInt(textWords[3], 16);//k significa el 16 este
			int position = 4;
			for(int i = 0; i < w_cnt; i++)
			{
				int idsm = textWords[position].indexOf("(");
				if(idsm == -1){
					words.add(textWords[position]);
					synmarker.add("");
				}
				else{
					words.add(textWords[position].substring(0, idsm));
					synmarker.add(textWords[position].substring(idsm));
				}
				lex_indexes.add(Integer.parseInt(textWords[position+1],16) + 1);
				position+=2;
			}
			int p_cnt = Integer.parseInt(textWords[position]);
			position++;
			for(int i = 0; i < p_cnt; i++)
			{
				pointers.add(new Relation(textWords[position], textWords[position+1], textWords[position+2].charAt(0), textWords[position+3]));
				position+=4;
			}
			while(textWords[position].equals("|") == false && position < textWords.length){	//Frames for verbs
				frames = frames + textWords[position] + " ";
				position++;
			}
			position++; 		//Skip symbol |
			while(position < textWords.length){	//Gloss
				gloss += textWords[position];
				position++;
				if(position < textWords.length)
					gloss += " ";
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(wnText);
		}
	}
}