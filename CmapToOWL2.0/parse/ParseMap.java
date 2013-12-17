/**
 * 
 */
package parse;

import org.w3c.dom.Element;

import cmaps.CMap;
import cmaps.CMapHandler;


/**
 * @author Amhed y Armando
 * 
 * Clase para lamanipulacion de las funcionalidades principales de la herramienta 
 * en cuanto al procesamiento del MC en formato XML
 */


public class ParseMap {
	
	private CMap map;

	//private ConnectionDB conection;
	private Element headerNode;// k cosa es este elemnto header
	private CMapHandler HanParser = new CMapHandler();
	@SuppressWarnings("unused")
	private CMap Cmap = new CMap();
	
	
	/**Método para asignar los hijos a sus correspondientes padres, según el fichero XML y asignarle los niveles*/
	/*private void assignSons() {
		
		for(int i=0; i<map.getConcepts().size(); i++) {
			
			AmbConcept conceptFather = map.getConcepts().get(i);
		    int Count = 1;
		    
			for(int j=0; j< conceptFather.getLinks().size(); j++) {
			    
				for(int k=0; k<conceptFather.getLinks().get(j).getDestinys().size(); k++) {
					
					for(int l=i+1; l<map.getConcepts().size(); l++)  {
						AmbConcept conceptChild =map.getConcepts().get(l);
						
						if(conceptChild.getConceptName().equals(conceptFather.getLinks().get(j).getDestinys().get(k))) {
							conceptChild.setLevelConcept((conceptFather.getLevelConcept()+ Integer.toString(Count)));
							conceptFather.addAmbConcept(conceptChild);
							Count++;
						}
					}
				}
			}
		}
	}
	*/
	public  ParseMap() {
		  map= new CMap();
		 	
	}

	public CMap getMap() {
		return map;
	}

	public Element getHeaderNode() {
		return headerNode;
	}

	public void setHeaderNode(Element headerNode) {
		this.headerNode = headerNode;
	}
	
	/*public ParseMap(Element headerNode) {		
		this.headerNode = headerNode;
		NamedNodeMap attrMapList = headerNode.getAttributes();
		String mapName = attrMapList.getNamedItem("Nombre").getNodeValue();
		int mapConcepts = Integer.parseInt(attrMapList.getNamedItem("CantConceptos").getNodeValue());
		this.map = new Map(mapConcepts,mapName);
	//	conection = new ConnectionDB("WordNet","ewn", "ewn");

	}
	
	public ParseMap(Element headerNode, boolean isCXL) {		
		this.headerNode = headerNode;
		/*NamedNodeMap attrMapList = headerNode.getAttributes();
		String mapName = attrMapList.getNamedItem("Nombre").getNodeValue();
		int mapConcepts = Integer.parseInt(attrMapList.getNamedItem("CantConceptos").getNodeValue());
		this.mapXML = new Map(mapConcepts,mapName);
	//	conection = new ConnectionDB("WordNet","ewn", "ewn");

	}*/
	
	
	
//	public ParseMap(Element headerNode, ArrayList conceptsList) {		
//		this.headerNode = headerNode;
//		this.map = new Map();
//	//	conection = new ConnectionDB("WordNet","ewn", "ewn");
//		for (Object concepItem : conceptsList) 			
//			this.map.addConcept((AmbConcept)concepItem); 		
//	}
	
	/** 
	 * Método que lee el MC en formato XML para almacenarlo en memoria interna
	 * */
	/*public void loadXMLMap() {
	//	Node root= headerNode.getChildNodes().item(0);
		NodeList nodeMapList = headerNode.getChildNodes();
		
		for(int i=0; i<nodeMapList.getLength(); i++) {
			NamedNodeMap attrMapList = nodeMapList.item(i).getAttributes();			
			String main ="Principal";				
			AmbConcept concept = new AmbConcept("",0,0,false,"");			
			String type= attrMapList.getNamedItem("Tipo").getNodeValue();
			
			if( type.length() == main.length()) {
				concept.setConceptName((attrMapList.getNamedItem("Nombre").getNodeValue().trim()));
				concept.setLinksCount(Integer.parseInt((attrMapList.getNamedItem("CantEnlaces").getNodeValue())));
				concept.setResourcesCount(Integer.parseInt((attrMapList.getNamedItem("CantRecursos").getNodeValue())));
				concept.setLevelConcept("1");
				concept.setMain(true);
								
			}
			else { 
				concept.setConceptName((attrMapList.getNamedItem("Nombre").getNodeValue().trim()));
				concept.setLinksCount(Integer.parseInt((attrMapList.getNamedItem("CantEnlaces").getNodeValue())));
				concept.setResourcesCount(Integer.parseInt((attrMapList.getNamedItem("CantRecursos").getNodeValue())));
				concept.setMain(false);				
			}
			NodeList nodeConceptList = nodeMapList.item(i).getChildNodes();
			concept.assignLinks(nodeConceptList);
			map.addConcept(concept);
			
		}
          //verificar que el primero de la lista sea el concepto principal	
		sortMap();
		assignSons();	
	}*/
	

	/*public void ExtracCXLMap(CMap Cmap )
	{
		map = new Map(Cmap.getConcepts().size(),Cmap.getTitle());
		ArrayList<CmapConcept> cMapConcepts = Cmap.getConcepts();
		ArrayList<CmapLink> cMapLinks = Cmap.getLinks();

		for (CmapLink cMapLinksItem : cMapLinks) {

			Link link = new Link(0,0,"");
			link.setLinkName(cMapLinksItem.getLinkName());
			link.setDestinysCount(cMapLinksItem.getDestinies().size());
			link.setId(cMapLinksItem.getId());

			int i=0;
			for (CmapConcept cMapConcpItem : cMapLinksItem.getSources()) {

				AmbConcept srcConcp = new AmbConcept("",0,0,false,"");
				srcConcp.setConceptName(cMapConcpItem.getConceptName());
				srcConcp.setId(cMapConcpItem.getId());
				srcConcp.setX(cMapConcpItem.getX());
				srcConcp.setY(cMapConcpItem.getY());

				CmapConcept destinyconcp =cMapLinksItem.getDestinies().get(i);
				i++;

				AmbConcept destConcp = new AmbConcept("",0,0,false,"");
				destConcp.setConceptName(destinyconcp.getConceptName());
				destConcp.setId(destinyconcp.getId());
				destConcp.setX(destinyconcp.getX());
				destConcp.setY(destinyconcp.getY());
				
				link.addDestiny(destinyconcp.getId());
				srcConcp.addLink(link);
				destConcp.addLink(link);

				Proposition prep= new Proposition(srcConcp,link,destConcp);
				map.addProposition(prep);
				map.addConcept(srcConcp);
				map.addConcept(destConcp);
			}
		}
		//ADICIONAR LAS PREPOSICIONES AL MAPA para luego construir la ontología
			for (AmbConcept conceptItem : map.getConcepts())			
				for (Link linkItem : conceptItem.getLinks()) {
					for (String destinyItem : linkItem.getDestinys()) {
					
					AmbConcept destConcept = findAmbConcept(destinyItem);
					
					if(destConcept!=null){
						
						Proposition p = new Proposition(conceptItem,linkItem,destConcept);
						map.addProposition(p);
					}
				}
			
		}
		sortMap();
		assignSons();
		System.out.println("OK");
	}
	*/
	
	
	/**METODO para extraer los datos de un cmpa y convertirlo a un map que es el objeto 	
	 *	que manipula la clase object owl
	 * @param Cmap
	 */
	/*public void ExtracCXLMap(CMap Cmap)
	{
		map = new Map(Cmap.getConcepts().size(),Cmap.getTitle());

		for (CmapConcept cMapConcpItem : Cmap.getConcepts()) {

			AmbConcept Concp = new AmbConcept("",0,0,false,"");
			Concp.setConceptName(cMapConcpItem.getConceptName());
			Concp.setId(cMapConcpItem.getId());
			Concp.setX(cMapConcpItem.getX());
			Concp.setY(cMapConcpItem.getY());
			Concp.setSenses(cMapConcpItem.getSenses());
			map.addConcept(Concp);
		}
		
		
		for (CmapPreposition cMapPrepItem : Cmap.getPreposition()) {
					
			AmbConcept srcConcp = new AmbConcept("",0,0,false,"");
			
			srcConcp.setConceptName(cMapPrepItem.getOrigConcept().getConceptName());
			srcConcp.setId(cMapPrepItem.getOrigConcept().getId());
			srcConcp.setX(cMapPrepItem.getOrigConcept().getX());
			srcConcp.setY(cMapPrepItem.getOrigConcept().getY());
			//ArrayList<Sense> senseSRC= new ArrayList<Sense>();
			//senseSRC.addAll((cMapPrepItem.getOrigConcept()).getSenses());
			srcConcp.setSenses((cMapPrepItem.getOrigConcept()).getSenses());
			
			AmbConcept dstConcp = new AmbConcept("",0,0,false,"");
			dstConcp.setConceptName(cMapPrepItem.getDestConcept().getConceptName());
			dstConcp.setId(cMapPrepItem.getDestConcept().getId());
			dstConcp.setX(cMapPrepItem.getDestConcept().getX());
			dstConcp.setY(cMapPrepItem.getDestConcept().getY());
			//ArrayList<Sense> sensesDST= new ArrayList<Sense>();
			//sensesDST.addAll((cMapPrepItem.getDestConcept()).getSenses());
			dstConcp.setSenses((cMapPrepItem.getDestConcept()).getSenses());
			
			Link link=	new Link(cMapPrepItem.getLink().getLinkName(),cMapPrepItem.getLink().getDestinies().size(),0,cMapPrepItem.getLink().getId());

			Preposition prep= new Preposition(srcConcp,link,dstConcp);	
			map.addPreposition(prep);
		}
	}*/
	public void loadXMLMap(String text) 
	{
		Cmap = HanParser.loadXMLMaCoSoft(text);
		
	}
   public void loadCXLMap(String text){
		
		Cmap = HanParser.loadCXL(text);
			          		
	}
	
   
   /* public ArrayList<CmapLink> getLinksConcpSrc (String conceptId,ArrayList<CmapLink> cxlMapLinks){
    	
    	ArrayList<CmapLink> conceptLinks = new ArrayList<CmapLink>();
    	int indexList = 0;
    	
    	for (CmapLink cxlLink : cxlMapLinks) {
		
    		boolean found = false;
    		ArrayList<CmapConcept> souceConcepts= cxlLink.getSources();
    		
    		while(indexList <souceConcepts.size() && !found){
    			
    			if(souceConcepts.get(indexList).getId().equals(conceptId)){
    				conceptLinks.add(cxlLink);
    				found = true;
    			}
    			else
    				indexList ++;
    				
    		}
    		
		}
    	return conceptLinks;
    	
    }
    
    public String getConceptNameById(String idConcept){
    	
    	for(int i=0; i<Cmap.getConcepts().size();i++){
    		
    		CmapConcept conceptItem = Cmap.getConcepts().get(i);
    		
    		if(conceptItem.getId().equals(idConcept)){
    			return conceptItem.getConceptName();
    		}
    	}
    	
    	return null;
    	
    }*/
    
	/**
	 * Método para crear las proposiciones del MC 
	 * */
/*	public ArrayList<Preposition>getMapPropositions() {
						
		return map.getMapPropositions();
		
	}*/
	
	/**
	 * Metodo para ordenar de manera jerarquica los conceptos del MC
	 * */
/*	public void sortMap() {
		ArrayList<AmbConcept> sortedList = new ArrayList<AmbConcept>(); 
        
		//buscar el concepto ppal
        AmbConcept mainConcept = findMainConcept();
        sortedList.add(mainConcept);
        
        //buscar la jerarquia principal      
        findHirerarchy(mainConcept, sortedList);
        
        //verifico si recorri todos los conceptos del MC con la jerarquía principal 
        while((sortedList.size())< (map.getConcepts().size())) {
           AmbConcept conceptItem = null;
           conceptItem = findConceptWithoutPar();
        	
           if(conceptItem!=null){
        	   
        	   conceptItem.setVisit(true);
        	   sortedList.add(conceptItem);
        	   findHirerarchy(conceptItem, sortedList); 
           }
        }
        
       // actualizo la lista de mapXML
        map.setConcepts(sortedList);
        
		
	}*/
	
	/**
	 * Metodo para buscar los conceptos que no tienen padre en el MC
	 * */
	/*private AmbConcept findConceptWithoutPar() {
		
		AmbConcept conceptItem = null;
		for (int i = 0; i < map.getConcepts().size(); i++) {
			
			conceptItem =map.getConcepts().get(i);
			if(conceptItem.isVisited()==false){
				
				//verificar que no sea hijo de ningun concepto
				if(isSon(conceptItem)==false){

					return conceptItem;
				}
			}
			
		}
		return null;
	}*/
	
	
	/**
	 * Metodo para verificar si un concepto tiene padre
	 * @param concept: Concepto que voy a verificar
	 * */
	/*private boolean isSon(AmbConcept concept) {
	
		for (AmbConcept conceptItem : map.getConcepts()) {
			
			for (Link linkItem : conceptItem.getLinks()) {
				
				for (String destinyItem	: linkItem.getDestinys()) {
				    
					if(destinyItem.equals(concept.getConceptName())){
						return true;
					}
				}
			}
		}
	
		return false;
	
	}*/
	/**
	 * Metodo para buscar la jerarquia de un concepto en el MC
	 * @param mani : Concepto objetivo
	 * @param sortedList: Lista de conceptos del MC
	 * */
/*	private void findHirerarchy(AmbConcept main,ArrayList<AmbConcept>sortedList) {
		
		for (int index =0; index<sortedList.size();index++) {
			
        	for (Link linkItem : sortedList.get(index).getLinks()) {
				
        		for (String destinyItem : linkItem.getDestinys()) {
					
        		  //busco cada destino y lo inserto en la lista de conceptos
        		  AmbConcept foundConcept = findAmbConcept(destinyItem);
        			if ((foundConcept!=null)&&(foundConcept.isVisited()==false) ){
        				foundConcept.setVisit(true);
        				sortedList.add(foundConcept);	
        			}
        		}
        	}
		}
	}*/
	
	/**
	 * Metodo para buscar un concepto dado en el MC
	 * @param conceptName: Nombre del concepto a buscar
	 * */
/*	private AmbConcept findAmbConcept(String conceptName){
		AmbConcept resultConc = null;
		
		boolean found = false;
		int index =0;
		
		while((index<map.getConcepts().size())&& !found) {
			
			resultConc = map.getConcepts().get(index);
			
			if((resultConc.getConceptName()).equals(conceptName)){
			
				//mapXML.getConcepts().get(index).setVisit(true);
				found = true;
			}
			else{
				index++;
			}
			
		}
		return resultConc;
	}*/
	
	/**
	 * Metodo para buscar el concepto principal de un MC
	 * */
/*	private AmbConcept findMainConcept() {
		AmbConcept mainConcept = null;
		boolean found = false;
		int index =0;
		
		while((index<map.getConcepts().size())&& !found) {
			
			mainConcept = map.getConcepts().get(index);
			
			if(mainConcept.isMain()==true){
			   found = true;
			   map.getConcepts().get(index).setVisit(true);
			   mainConcept.setVisit(true);
			}
			else{
				index++;
			}
			
		}
		return mainConcept;
	}*/
	
	/** Método que crea el MC en formato XML
	 *@see ParseMapXML#searchLink(int, String, ArrayList) 
	 * */
/*	public Element fillXML(String mapName, Document doc) {
		
		Element mainNode = doc.getDocumentElement(); 
		setHeaderNode(mainNode);
		
		AmbConcept concept = new AmbConcept("",0,0,false,"");
		Link linkXML = new Link(0,0,"");
		Link linkFound = new Link(0,0,"");
		String destXML = "";
		Element conceptChild =null;
		Element linkChild = null;
		Element nodeDestLink = null;
		String aNameConcept, tag, aLinkName ;
		String princ = "Principal";
		String noPrinc = "No Principal";
		int aCountLink = 1; 
		int aCountDest = 1;
		int linkFinal = 0;
		int position = 0;
		ArrayList<Link> sortedList = new ArrayList<Link>();
		
		headerNode.setAttribute("Nombre", mapName);
		headerNode.setAttribute("CantConceptos",Integer.toString(map.getConcepts().size()));
		headerNode.setAttribute("Version","2");
		headerNode.setAttribute("Descripcion","");
		headerNode.setAttribute("Estado","wsMaximized");
		
		for (int i=0; i<map.getConcepts().size(); i++) {			
			concept = map.getConcepts().get(i);
			aNameConcept = concept.getConceptName();
			tag = "Concepto" + Integer.toString(i+1);			
			conceptChild = doc.createElement(tag);			
			((Element)conceptChild).setAttribute("Nombre",aNameConcept);
			
			if(concept.isMain())
				((Element)conceptChild).setAttribute("Tipo",princ);
			else
				((Element)conceptChild).setAttribute("Tipo",noPrinc);	
			
			((Element)conceptChild).setAttribute("CantRecursos","0");
						
			for (int j = 0;j<concept.getLinks().size();j++) {				
				linkXML = concept.getLinks().get(j);
				aLinkName = linkXML.getLinkName();				
				linkFound = searchLink(position,aLinkName,sortedList);
				
				if(linkFound != null){					
					linkFinal--; 
					
					for (int k = 0; k < linkXML.getDestinys().size(); k++) {
						
						String destinyTemp = linkXML.getDestinys().get(k); 
						linkFound.getDestinys().add(destinyTemp);
						
					}         	 					
				}
				else {
					sortedList.add(linkXML);
					position++;
				}
				
				if (j == concept.getLinks().size()-1) {
					
					for (int indexLink = 0; indexLink < sortedList.size(); indexLink++) {						
						linkXML = sortedList.get(indexLink);
						aLinkName = linkXML.getLinkName();
						tag = "Enlace" + Integer.toString(aCountLink);
						linkChild = doc.createElement(tag);
						linkChild.setAttribute("Nombre",aLinkName);
						linkChild.setAttribute("CantDestinos",Integer.toString(linkXML.getDestinys().size()));
						
						for (int inDest = 0; inDest < linkXML.getDestinys().size(); inDest++) {
							
							destXML = linkXML.getDestinys().get(inDest).toString();
							tag = "Destino" + Integer.toString(aCountDest);
							nodeDestLink = doc.createElement(tag);
							nodeDestLink.setAttribute("Dest",destXML);
							linkChild.appendChild(nodeDestLink);
							aCountDest ++;
						}
						aCountDest =1;
						aCountLink++;						
						conceptChild.appendChild(linkChild);
					}					
				}						
			}			
			//Actualizo la cantidad de enlaces del concepto antes de insertarlo
			((Element)conceptChild).setAttribute("CantEnlaces",Integer.toString(sortedList.size()));
			headerNode.appendChild(conceptChild); 
		}//fin del for de los conceptos 			
		return headerNode;		
	}*/
	
	/**
	 * Método para buscar un enlace dterminado en una lista a partir de una posición determinada
	 * */
	/*public Link searchLink(int position,String link,ArrayList<Link> linkList) {
		
		int index = position-1;
		
		while( index >= 0) {
			String aLinkFound = linkList.get(index).getLinkName();
			
			if(aLinkFound == link)
				return linkList.get(index);
			else
				index --;
		}
		return null;
	}
	*/
	/**Método para buscar un enlace determinado en una lista
	 * @param link: enlace a buscar
	 * @param ArrayList<Link> linkList: Lista donde voy a buscar
	 * */
/*	public Link findLink(String link,ArrayList<Link> linkList) {
		
		int index = 0;
		
		boolean found = false;
		while( index< linkList.size() && !found) {
			String aLinkFound = linkList.get(index).getLinkName();
			
			if(aLinkFound == link){
				found = true;
				return linkList.get(index); 
			}		      
			else
				index ++;
		}
		return null;
	}*/
	
		
	
	/** 
	 * Método para buscar el nodo que tenga un nombre específico 
	 * */
	/*public Element findNodeLink(Node nodeConcept, String name) {
		int index = 0;
		while (index<nodeConcept.getChildNodes().getLength())
		{
			String nameTemp = (((nodeConcept.getChildNodes().item(index)).getAttributes().getNamedItem("Nombre"))).getNodeValue()getLinkName(); 
			
			if(nameTemp == name)
				return (Element)(nodeConcept.getChildNodes()).item(index);
			else
				index++;
		}
		return null;	
	}
	
	public ArrayList<AmbConcept>getConcepList(){
		
		ArrayList<AmbConcept>result = new ArrayList<AmbConcept>();
		
		result = map.getConcepts();
		
		return result;
	}*/

	


}
