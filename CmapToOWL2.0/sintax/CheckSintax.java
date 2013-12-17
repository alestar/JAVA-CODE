package sintax;

public class CheckSintax {
	
	
	
	public CheckSintax() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean checkSintaxForLeters(String concep){
		String chars ="abcdefghijklmnñopqrstuvwxyzüáéíóúABCDEFGHIJKLMNÑOPQRSTUVWXYZÜÁÉÍÓÚ";
		boolean isletter=true;
		boolean found=false;
		int fnd=0;
	
		int i = 0;
		while (i < concep.length() && isletter) {//Verificar k cada caracter de concep sea una letra
			int j=0;
			while (j < chars.length()  && !found){//buscar por todos las letras del alfabeto
				if(concep.charAt(i)==chars.charAt(j)){
					fnd++;
					found=true;
				}
				else
					j++;
			}	
			if(!found)//Si nos e encontro al menos una letra, no es necesario seguir verificando as restantes
				isletter=false;
			else{
				i++;
				found=false;//preparse para buscar la siguiente letra
			}
		}
		if(fnd==concep.length())//...si se encontraron(fnd) todas las letras(concep.length())....
			isletter=true;
		
		return isletter;
	}

	public String firstLettersToCapital(String concep){
		String concpchek="";
		String word="";
		
			String concepLower =concep.toLowerCase();//...Se convierte todo a minuscula para porner la palabra de una forma regular..

//			... Ahora se ponen las primera(s) letra(s) de cada palabra(s) (en caso de palabra compuesta) en Mayúscula...
			String[] textWords = concepLower.split(" ");
			for (int i = 0; i < textWords.length; i++) {

				String w=textWords[i];
				try {
					if( checkSintaxForLeters(w))//Verificar si el contenido de la cadena son solo letras del alfabeto
						word=w.replaceFirst(w.substring(0,1), w.substring(0,1).toUpperCase());//Pone las primera letra de cada palabra en mayuscula
				} catch (Exception e) {
					word="";
				}

				if(i==textWords.length-1)//...borrar el ultimo espacio,"trim()" no lo hace.
					concpchek+=word ;
				else
					concpchek+=word + " ";
			}
		return concpchek;		
	}

	/**@author Alejandro R.C
	 * @return Normaliza un término para que tenga un estrctura estándar(sin _;-; ;MAYUSCULAS Iregulares) y sea mas facil su comparacioón con
	 * otros terminos para buscar corespondencias con el Repositorio OWL
	 */
	public String normalizeTerms(String concep){
		String concpNormalize="";

//		...Probar incialmente con todas las letras en minúscula, como un estandarización, en cada caso de palabra compuesta con mayusculas iregulares...				
		String concpLow=concep.toLowerCase();
		concpNormalize=concpLow;
		
		if(concpLow.contains(" "))
			concpNormalize= concpLow.replaceAll(" ", "");
		if(concpLow.contains("-"))
			concpNormalize= concpLow.replaceAll("-", "");
		if(concpLow.contains("The_"))
			concpNormalize=concpLow.replaceAll("The_", "");
		if(concpLow.contains("_"))
			concpNormalize=concpLow.replaceAll("_", "");
		if(concpLow.contains("The "))
			concpNormalize=concpLow.replaceAll("The ", "");
		if(concpLow.contains("The-"))
			concpNormalize=concpLow.replaceAll("The-", "");

		return concpNormalize;
	}
}
