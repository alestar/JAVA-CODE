/**
 * 
 */
package classes;

import java.util.ArrayList;
//import java.util.Collection;


/**
 * @author Amhed
 * 
 * Clase que manipula los conceptos del MC
 */
public class AmbConcept extends Concept{
	
    private int linksCount;
    private int resourcesCount;//k es esto
    private boolean main;//k es esto
    private ArrayList<AmbConcept> concepts;
    private String levelConcept;
    
	/**
	 * @param conceptName
	 * @param links
	 * @param linksCount
	 * @param resourcesCount
	 * @param main
	 * @param concepts
	 * @param senses
	 * @param level
	 */
    public AmbConcept(String conceptName) {
    	super(conceptName);
    }
    
	public AmbConcept(String conceptName, int linksCount, int resourcesCount, boolean main, String level) {
		super(conceptName);
		this.linksCount = linksCount;
		this.resourcesCount = resourcesCount;
		this.main = main;
		this.concepts = new ArrayList<AmbConcept>();
		this.levelConcept = level;
		
	}
	
	public AmbConcept(AmbConcept concept, Sense senseDes) {
		super(concept.getConceptName());
		this.linksCount = concept.getLinksCount();
		this.resourcesCount = concept.getResourcesCount();
		this.main = concept.isMain();
		this.levelConcept = concept.getLevelConcept();
		
		this.concepts = new ArrayList<AmbConcept>();
		for (AmbConcept ambConceptItem : concept.getConcepts()) {
			this.concepts.add(ambConceptItem);
			
		}
		
		this.getSenses().add(senseDes);		
		//this.senses = new ArrayList(concept.getSenses());

	}
	
	public AmbConcept(AmbConcept concept) {
		super(concept.getConceptName());
		this.linksCount = concept.getLinksCount();
		this.resourcesCount = concept.getResourcesCount();
		this.main = concept.isMain();
		this.levelConcept = concept.getLevelConcept();
		this.concepts = new ArrayList<AmbConcept>(concept.getConcepts());
		
		for (int i = 0; i < concept.getSenses().size(); i++) {
			this.getSenses().add(concept.getSenses().get(i));
			
		}
		
		//this.senses = new ArrayList(concept.getSenses());

	}
	
	
	public ArrayList<AmbConcept> getConcepts() {
		return concepts;
	}
	public void setConcepts(ArrayList<AmbConcept> concepts) {
		this.concepts = concepts;
	}

	public int getLinksCount() {
		return linksCount;
	}
	public void setLinksCount(int linksCount) {
		this.linksCount = linksCount;
	}
	public boolean isMain() {
		return main;
	}
	public void setMain(boolean main) {
		this.main = main;
	}
	public int getResourcesCount() {
		return resourcesCount;
	}
	public void setResourcesCount(int resourcesCount) {
		this.resourcesCount = resourcesCount;
	}

	public void setPrincipalConcept(){
		this.main = true;
	}
	public void addAmbConcept(AmbConcept concept){
		this.concepts.add(concept);
	}

	public void addSense(Sense sense) {
		this.getSenses().add(sense);
	}
	
	/**Método para verificar la cantidad de sentidos de un concepto
	 * que tienen asociados dominios del MC*/
	public int countNotRemoved() {
		int count = 0;
		
		for (int i = 0; i < this.getSenses().size(); i++) {
			 if(!this.getSenses().get(i).isRemoved())
				 count++;
		}
		return count;
	}
	
	/**Método que devuelve el sentido que se emplea para la desambiguación de un concepto*/
	public Sense onlySense() {		
		int i = 0;
		boolean found = false;
		
		while (!found && i<this.getSenses().size()) {
			
			if(!this.getSenses().get(i).isRemoved()) 
				return this.getSenses().get(i);	
			i++;
				
		} 
		return null;		
	}
	public String getLevelConcept() {
		return levelConcept;
	}
	public void setLevelConcept(String levelConcept) {
		this.levelConcept = levelConcept;
	}
}
