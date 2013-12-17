package wordnetDataFile;

import java.util.ArrayList;

public class Word {
	
		private String lemma; //Palabra minúscula (word) en formato ASCII o colocación (collocations). 
		//Las colocaciones son formadas por la unión de varias palabras mediante el símbolo de guion bajo (underscore).
		private char pos;//Categoría sintáctica: n para sustantivos (noun files),
		//v para verbos (verb files), a para adjetivos (adjectives files), r para adverbios (adverb files).
		
		/*Los campos restantes constituyen información respecto al sentido (sense) de lemma en pos:*/
		
		int synset_cnt;//Cantidad de sentidos que contienen a lemma, es decir, el número (number) de sentidos que tiene una palabra (word) en WordNet. 
		int	p_cnt;//Cantidad de punteros (pointers) diferentes que tiene lemma en todos los sentidos que contienen al mismo.
		private ArrayList<String> ptr_symbol = new ArrayList<String>();//Una lista separada por “espacios” de los p_cnt 
		//diferentes tipos de punteros (pointers)que presenta lemma en los sentidos (synset) que contienen a este.
		int sense_cnt;//Lo mismo que synset_cnt; pero es preservado por razones de compatibilidad.
		int tagsense_cnt;//Numero (number) de sentidos  de lemma que son clasificados de acuerdo a su frecuencia de ocurrencia.
		private ArrayList<String> synset_offset = new ArrayList<String>();//Byte offset, en el fichero data.pos, de un sentido 
		//que contiene a lemma. Es un número decimal de 8 dígitos que se utiliza para leer el sentido en el fichero data.pos. 
		//Cada uno corresponde a un sentido de lemma distinto en WordNet.
		
		
		public Word() {
			this.lemma = "";
			this.pos = 'n';
			int synset_cnt=0;
			@SuppressWarnings("unused")
			int	p_cnt=0;
			this.ptr_symbol = new ArrayList<String>();
			@SuppressWarnings("unused")
			int sense_cnt=synset_cnt;
			@SuppressWarnings("unused")
			int tagsense_cnt=0;
			this.synset_offset = new ArrayList<String>();
			
		}
//::::::::::::::::::::::::::::Get y Set::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		public String getlemma() {
			return lemma;
		}
		
		public void setlemma(String lemma) {
			this.lemma = lemma;
		}
		
		public char getPos() {
			return pos;
		}

		public void setPos(char pos) {
			this.pos = pos;
		}
		public int getSynset_cnt() {
			return synset_cnt;
		}

		public void setSynset_cnt(int synset_cnt) {
			this.synset_cnt = synset_cnt;
		}
		public int getP_cnt() {
			return p_cnt;
		}

		public void setP_cnt(int p_cnt) {
			this.p_cnt = p_cnt;
		}
			
		public ArrayList<String> getptr_symbol() {
			return ptr_symbol;
		}

		public void setptr_symbol(ArrayList<String> ptr_symbol) {
			this.ptr_symbol = ptr_symbol;
		}
		public int getSense_cnt() {
			return sense_cnt;
		}

		public void setSense_cnt(int sense_cnt) {
			this.sense_cnt = sense_cnt;
		}
		
		public int getTagsense_cnt() {
			return tagsense_cnt;
		}

		public void setTagsense_cnt(int tagsense_cnt) {
			this.tagsense_cnt = tagsense_cnt;
		}
		
		
		public ArrayList<String> getSynset_offset() {
			return synset_offset;
		}

		public void setSynset_offset(ArrayList<String> synset_offset) {
			this.synset_offset = synset_offset;
		}
		/**Saca los datos(index.pos) de una palabra 
		* de una linea leida del fhicero de synsets
		* @param wnText linea del fichero
		* Acceso a  ficheros de WN par realizar la busqueda
		* */
	/*	public void parseData(String wnText){
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
					pointers.add(new Relation(textWords[position], textWords[position+1], textWords[position+3], textWords[position+2].charAt(0)));
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
		}*/
		
	

}
