package cmaps;

import java.util.ArrayList;
import java.util.LinkedList;

import classes.Sense;


/**
 * 
 * @author Amhed (23-02-09)
 *
 */
public class CmapConcept {

	private String conceptName = "";
	private String catgTag = "";//...Puede ser SuperClase, SubClase, Instancia, Dominio, Rango, Clase, ValorProiedad
	private String mapOwner = "";
	private LinkedList<String>conceptWords =null;
	private String id = "";
	private ArrayList<Sense> senses;
	
	private int aWeight = 1;
	private int hWeight = 1;
	private int uWeight = 1;
	
	private double aWeightLast = 1;
	private double hWeightLast = 1;
	private double uWeightLast = 1;

	private int x = 0;
	private int y = 0;
	
	public CmapConcept() {
		this.conceptWords = new LinkedList<String>();
	}

	public CmapConcept(String conceptName) {
		this.conceptName = conceptName;
		this.conceptWords = new LinkedList<String>();
	}

	public CmapConcept(String conceptName, String id) {
		this.conceptName = conceptName;
		this.id = id;
		this.senses= new ArrayList<Sense>();
		this.conceptWords = new LinkedList<String>();
		//this.senses = new ArrayList<Sense>();
	}
	
	public boolean equals(Object object) {
		CmapConcept concept = (CmapConcept) object;
		return ((conceptName.equalsIgnoreCase(concept.getConceptName())) && id.equals(concept.getId()));
	}
	
	public void addSenses(ArrayList<Sense> senses) {
		this.getSenses().addAll(senses);
	}

	public void addSense(Sense sense) {
		this.getSenses().add(sense);
	}
	
	public String getConceptName() {
		return conceptName;
	}
	
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public ArrayList<Sense> getSenses() {
		return senses;
	}
	
	public void setSenses(ArrayList<Sense> senses) {
		this.senses = senses;
	}
	
	public String toString(){
		String text = conceptName + " " + id + "\n";
		/*for(Sense sense : senses)
			text += sense + "\n";*/
		return text;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getAWeight() {
		return aWeight;
	}

	public void setAWeight(int weight) {
		aWeight = weight;
	}

	public int getHWeight() {
		return hWeight;
	}

	public void setHWeight(int weight) {
		hWeight = weight;
	}

	public LinkedList<String> getConceptWords() {
		return conceptWords;
	}

	public void setConceptWords(LinkedList<String> conceptWords) {
		this.conceptWords = conceptWords;
	}
	
	public int getuWeight() {
		return uWeight;
	}

	public void setuWeight(int uWeight) {
		this.uWeight = uWeight;
	}
	/**
	 * para dividir el nombre del concepto en partes
	 */
	public void addConceptWords() {
		String [] names = new String[]{};
		if(this.conceptName.contains(" ")||this.conceptName.contains("_")||this.conceptName.contains("-")){
			if (this.conceptName.contains(" "))
				names = this.conceptName.split(" ");
			else 
				if (this.conceptName.contains("_"))
					names = this.conceptName.split("_");
				else
					if(this.conceptName.contains("-"))
						names = this.conceptName.split("-");
				
			for (int i = 0; i < names.length; i++) {
				this.conceptWords.add(names[i]);
			}
		}else{
			//si es notacion Camell
			char []s = this.conceptName.toCharArray();
			int j=0;
			String name ="";
			while (j<s.length) {
				if(Character.isUpperCase(s[j])){
					
					if (!name.equalsIgnoreCase("")) {
						this.conceptWords.add(name);
					}
					name = String.valueOf(s[j]);	
				}else
					name+=s[j];
				j++;
			}
			this.conceptWords.add(name);
		}
	}


	public double getUWeightLast() {
		return uWeightLast;
	}

	public void setUWeightLast(double weightLast) {
		uWeightLast = weightLast;
	}

	public double getAWeightLast() {
		return aWeightLast;
	}

	public void setAWeightLast(double weightLast) {
		aWeightLast = weightLast;
	}

	public double getHWeightLast() {
		return hWeightLast;
	}

	public void setHWeightLast(double weightLast) {
		hWeightLast = weightLast;
	}

	public String getCatgTag() {
		return catgTag;
	}

	public void setCatgTag(String owlTag) {
		this.catgTag = owlTag;
	}

	public String getMapOwner() {
		return mapOwner;
	}

	public void setMapOwner(String mapOwner) {
		this.mapOwner = mapOwner;
	}
	
}

