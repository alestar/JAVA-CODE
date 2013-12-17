/**
 * 
 */
package cmaps;

import java.util.ArrayList;
import java.util.Iterator;


public class CMap {
	
    private ArrayList<CmapConcept> concepts = new ArrayList<CmapConcept>();
    private ArrayList<CmapLink> links = new ArrayList<CmapLink>();
    private ArrayList<CmapProposition> propositions = new ArrayList<CmapProposition>();
    private CmapConcept mainConcept = null;

//	...Cojuntos pertenecientes a en los mapas cargados a partir del repositorio de ontologías
	private ArrayList<CmapProposition> owlSubclassProps;
	private ArrayList<CmapProposition> owlInstannceProps;
	private ArrayList<CmapProposition> owlObjectpropertyProps;
	private ArrayList<CmapProposition> owlPropertyProps;
	
	private double similarity=0;//similitud de los mapas cargados de l repositorio con el mapa objetivo
	private double repositoryRelv=0;//Promedio de la similitud de los mapas de un repositorio con relación al mapa objetivo
	//Metadata res-meta (compliant with http://cmap.ihmc.us/xml/cmap/)
    
	//Last user that modified the resource
    public String contributorFN = "";	//Full name (First [Middle|Initial] Last)
    public String contributorEMail = "";	//Email address
    public String contributorORG = "";	//Organization name
	
	//Original author
    public String creatorFN = "";		//Full name (First [Middle|Initial] Last)
    public String creatorEMail = "";	//Email address
    public String creatorORG = "";		//Organization name
	
    public String description = "";	//Focus question of the Cmap
    public String format = "x-cmap/text-xml";
	//public String identifier = ""; 	//HTTP URL with resource id (e.g. http://cmap.ihmc.us/rid=10002929_292992_19)
    public String language = ""; 		//Language code (RFC1766) (e.g. en_US)
    public String publisher = "";		//e. g. IHMC CmapTools v. 4.X
	//public String relation = "";		//HTTP URL, location based (e.g. http://cmap.ihmc.us/plants.cxl)
	//public String source = "";			//cmap:<server-id>:<folder-id>:<resource-id>
    public String subject = ""; 		//Keyword list, comma delimited
    public String title = "";			//Name of the Cmap 
    public String dateCreated = ""; 			//Date when the Cmap was created (ISO Date yyyy-MM-dd'T'HH:mm:ss'Z' GMT Time Zone)
    public String size = "0 kb"; 	//size (integer [space] units) 
    public String dateModified = ""; 	//Date of last modification (ISO Date yyyy-MM-dd'T'HH:mm:ss'Z' GMT Time Zone) 
	 
	//Cmap owner 
    public String ownerFN = "";		//Full name (First [Middle|Initial] Last)
    public String ownerEMail = "";	//Email address
    public String ownerORG = "";		//Organization name 
		
	public CMap() {
		this.concepts = new ArrayList<CmapConcept>();
		this.links = new ArrayList<CmapLink>();
		this.propositions = new ArrayList<CmapProposition>();
		this.description = "";
		this.mainConcept = null;
		this.owlSubclassProps= new ArrayList<CmapProposition>() ;
		this.owlInstannceProps= new ArrayList<CmapProposition>() ;
		this.owlObjectpropertyProps= new ArrayList<CmapProposition>() ;
		this.owlPropertyProps= new ArrayList<CmapProposition>() ;
		
	}

	public CMap(String title) {
		this.title = title;
		this.concepts = new ArrayList<CmapConcept>();
		this.links = new ArrayList<CmapLink>();
		this.mainConcept = null;
		this.owlSubclassProps= new ArrayList<CmapProposition>() ;
		this.owlInstannceProps= new ArrayList<CmapProposition>() ;
		this.owlObjectpropertyProps= new ArrayList<CmapProposition>() ;
		this.owlPropertyProps= new ArrayList<CmapProposition>() ;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<CmapProposition> getPropositions() {
		return propositions;
	}

	public void setPropositions(ArrayList<CmapProposition> propositions) {
		this.propositions = propositions;
	}
	
	public ArrayList<CmapConcept> getConcepts() {
		return concepts;
	}

	public void setConcepts(ArrayList<CmapConcept> concepts) {
		this.concepts = concepts;
	}

	public ArrayList<CmapLink> getLinks() {
		return links;
	}

	public void setLinks(ArrayList<CmapLink> links) {
		this.links = links;
	}

	public CmapConcept getMainConcept() {
		return mainConcept;
	}

	public void setMainConcept(CmapConcept mainConcept) {
		this.mainConcept = mainConcept;
	}
	
	/**
	 * context Obtain context around a concept 
	 * @param start Concept located in center of context
	 * @param length Radius of context
	 * @param foundConcepts Concepts found so far
	 * @param foundLinks Links found so far
	 */
	public void context(CmapConcept start, int length, ArrayList<CmapConcept> ctxConcepts, ArrayList<CmapLink> ctxLinks){
		if(ctxConcepts == null)
			ctxConcepts = new ArrayList<CmapConcept>();
		else ctxConcepts.clear();
		
		if(ctxLinks == null)
			ctxLinks = new ArrayList<CmapLink>();
		else ctxLinks.clear();

		ctxConcepts.add(start);

		for(int i = 0; i < length; i++){ 	// increase in depth
			int size = ctxConcepts.size(); 	// current concepts size
			for (int j = 0; j < size; j++) { 		// for ever concept found so far
				CmapConcept concept = ctxConcepts.get(j);
				for(CmapLink link : links){ 	//search concept in every link on map
					
					boolean foundLink = false;
					if(link.getSources().contains(concept)){ //if concept found in sources, add destiny concepts
						foundLink = true;	
						for(CmapConcept sc : link.getDestinies())
							if(ctxConcepts.contains(sc) == false)
								ctxConcepts.add(sc);
					}
					if(link.getDestinies().contains(concept)){ // if concept found in destinies, add source concepts
						foundLink = true;	
						for(CmapConcept sc : link.getSources())
							if(ctxConcepts.contains(sc) == false)
								ctxConcepts.add(sc);
					}
					if(foundLink){ //add link
						if(ctxLinks.contains(link) == false)
							ctxLinks.add(link);
					}

				}
			}
		}
	}
	
	/**
	 * set a-weight to each concept
	 * 
	 * @return
	 */
	public void setAuthorityWeight() {
		for (CmapConcept concept : concepts) {
			
			int aWeight = 0;
			for (CmapProposition propItem : propositions) {
				
				if(propItem.getDestConcept().getId().equalsIgnoreCase(concept.getId()))
					aWeight+=propItem.getOrigConcept().getHWeightLast();
			}
			concept.setAWeight(aWeight);
		}
	}

	/**
	 * set a-weight to each concept
	 * 
	 * @return
	 */
	public void setHubWeight() {
		for (CmapConcept concept : concepts) {
			
			int hWeight = 0;
			for (CmapProposition propItem : propositions) {
				
				if(propItem.getOrigConcept().getId().equalsIgnoreCase(concept.getId()))
					hWeight+=propItem.getDestConcept().getAWeightLast();
			}
			concept.setHWeight(hWeight);
		}
	}
	
	/**
	 * set u-weigth to each concept
	 * 
	 */
	public void setUpperWeight() {
		
		Iterator<CmapConcept>it = concepts.iterator();
		boolean found = false;

		while(it.hasNext() && !found){
			int uWeight = 0;
			CmapConcept concept = it.next();
			for (CmapProposition propItem : propositions) {
				
				if(propItem.getDestConcept().getId().equalsIgnoreCase(concept.getId())){
					found = true;
					uWeight += propItem.getOrigConcept().getUWeightLast();
				}
					
			}
			if(!found)
				uWeight = 1;
			
			concept.setuWeight(uWeight);
		}
		
		
	}
	
	/**
	 * normalizar cada peso
	 * @return
	 */
	public void normalizeAuthorityWeight() {
		
		int aSum = 0;
		int hSum = 0;
		int uSum = 0;
		for (CmapConcept cmapItem : concepts){
			aSum+= Math.pow(cmapItem.getAWeight(), 2);
			hSum+= Math.pow(cmapItem.getHWeight(), 2);
			uSum+= Math.pow(cmapItem.getuWeight(), 2); 
		}
		
		double aRoot = Math.sqrt(aSum);
		double hRoot = Math.sqrt(hSum);
		double uRoot = Math.sqrt(uSum);
		
		for (CmapConcept cmapItem : concepts) {
			cmapItem.setAWeightLast(Math.round((cmapItem.getAWeight()/aRoot)*Math.pow(10,2))/Math.pow(10,2));
			cmapItem.setHWeightLast(Math.round((cmapItem.getHWeight()/hRoot)*Math.pow(10,2))/Math.pow(10,2));
			cmapItem.setUWeightLast(Math.round((cmapItem.getuWeight()/uRoot)*Math.pow(10,2))/Math.pow(10,2));
			
		}
	}

	public ArrayList<CmapProposition> getOwlInstannceProps() {
		return owlInstannceProps;
	}

	public void setOwlInstannceProps(ArrayList<CmapProposition> owlInstannceProp) {
		this.owlInstannceProps = owlInstannceProp;
	}

	public ArrayList<CmapProposition> getOwlObjectpropertyProps() {
		return owlObjectpropertyProps;
	}

	public void setOwlObjectpropertyProps(
			ArrayList<CmapProposition> owlObjectpropertyProp) {
		this.owlObjectpropertyProps = owlObjectpropertyProp;
	}

	public ArrayList<CmapProposition> getOwlPropertyProps() {
		return owlPropertyProps;
	}

	public void setOwlPropertyProps(ArrayList<CmapProposition> owlPropertyProp) {
		this.owlPropertyProps = owlPropertyProp;
	}

	public ArrayList<CmapProposition> getOwlSubclassProps() {
		return owlSubclassProps;
	}

	public void setOwlSubclassProps(ArrayList<CmapProposition> owlSubclassProp) {
		this.owlSubclassProps = owlSubclassProp;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	public double getRepositoryRelv() {
		return repositoryRelv;
	}

	public void setRepositoryRelv(double repositoryRelv) {
		this.repositoryRelv = repositoryRelv;
	}

}
