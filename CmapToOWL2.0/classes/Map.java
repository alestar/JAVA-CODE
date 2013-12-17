/**
 * 
 */
package classes;

import java.util.ArrayList;


/**
 * @author Amhed
 * Clase para manipular la información referente a un MC
 *
 */
public class Map {
	
    private int conceptsCount;
    private String mapName;
	private ArrayList<Domain> mainDomain;
	private ArrayList<AmbConcept> concepts;
	//nuevo PS_CMR (lista de proposiciones del MC)
    private ArrayList<Preposition>mapPropositions;
		
	public Map() {

		this.concepts = new ArrayList<AmbConcept>();
		this.mapPropositions = new ArrayList<Preposition>();
	}
	/**
	* @param conceptsCount
	* @param mapName
	* @param mainDomain
	* @param concepts
	*/
	public Map(int conceptsCount, String mapName) {
	    super();
		this.conceptsCount = conceptsCount;
		this.mapName = mapName;
		this.mainDomain = new ArrayList<Domain>();
		this.concepts = new ArrayList<AmbConcept>();
		this.mapPropositions = new ArrayList<Preposition>();
	}
	
	public ArrayList<AmbConcept> getConcepts() {
		return concepts;
	}
	public void setConcepts(ArrayList<AmbConcept> concepts) {
		this.concepts = concepts;
	}
	public int getConceptsCount() {
		return conceptsCount;
	}
	public void setConceptsCount(int conceptsCount) {
		this.conceptsCount = conceptsCount;
	}
	public ArrayList<Domain> getMainDomain() {
		return mainDomain;
	}
	public void setMainDomain(ArrayList<Domain> pMainDomain) {
		this.mainDomain = pMainDomain;
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public void addConcept(AmbConcept concept) {
		this.concepts.add(concept);
	}
	public ArrayList<Preposition> getMapPropositions() {
		return mapPropositions;
	}

	public void setMapPropositions(ArrayList<Preposition> mapPropositions) {
		this.mapPropositions = mapPropositions;
	}
	public void addPreposition(Preposition prop) {
		this.mapPropositions.add(prop);
	}
}
