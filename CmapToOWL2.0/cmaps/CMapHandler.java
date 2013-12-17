package cmaps;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.DOMWriter;

import classes.Sense;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import file.FileConcepsManage;

public class CMapHandler {
	FileConcepsManage mapConcepsFile;


	public CMapHandler() {
		super();
		mapConcepsFile=new FileConcepsManage("StaticsResult/MC/Map");
		mapConcepsFile.loadConcep();
	}

	public CMap loadXMLMaCoSoft(String fileContent){ /* Maintained for compatibility with older versions*/
		CMap Cmap = new CMap();
		Element rootXML = null;

		try {
			Document doc = DocumentHelper.parseText(fileContent);
			//Work with xml parsed
			rootXML = doc.getRootElement();
			Cmap.title = rootXML.attributeValue("Nombre");
			List nodeList = rootXML.elements();
			int id = 0;

			//para cada nodo(concepto) del fichero(el Mapa), saco los datos y lo pongo e una lista
			for(int i=0; i<nodeList.size(); i++) {//Create concepts
				Element conceptag = (Element) nodeList.get(i);

				if(conceptag.getName().equals("Concepto")){
					String name = conceptag.attributeValue("Nombre").trim();
					CmapConcept concept = new CmapConcept(name, Integer.toString(id)); //Id initialized with i - file format doesn't support it 
					id++;

					concept.setX(Integer.parseInt(conceptag.attributeValue("PosicionX")));//k posiciones son estas
					concept.setY(Integer.parseInt(conceptag.attributeValue("PosicionY")));//k posiciones son estas

					if( conceptag.attributeValue("Tipo").equals("Principal"))
						Cmap.setMainConcept(concept);

					List coceptelements=conceptag.elements();
					if(coceptelements.size()>=1){
						Element sensetag= (Element)coceptelements.get(coceptelements.size()-1);
						if(sensetag.getName().equals("Sentido")){
							Sense sense= new Sense(sensetag.attributeValue("Código").trim());
							concept.addSense(sense);
						}
					}
					Cmap.getConcepts().add(concept);//Adiciono
					//if(!mapConcepsFile.findConcep(concept.getConceptName()))
						//mapConcepsFile.addConcep(concept.getConceptName());
				}
			}
			//Ahora k tengo todos los conceptos recorro otra vez el fichero(el mapa),para armar las p.e y preposiciones
			for(int i=0; i<nodeList.size(); i++) {	
				List linktags = ((Element) nodeList.get(i)).elements() ;// links between concepts

				//para cada p.e del fichero(el Mapa), saco los datos y lo pongo e una lista
				for (Iterator iter = linktags.iterator(); iter.hasNext();){

					Element linktag = (Element) iter.next();
					if(linktag.getName().equals("Enlace")) {    	       
						CmapLink link = new CmapLink(linktag.attributeValue("Nombre").trim(), Integer.toString(id));
						id++;

						link.setX(Integer.parseInt(linktag.attributeValue("PosicionX")));
						link.setY(Integer.parseInt(linktag.attributeValue("PosicionY")));

						CmapConcept prepsrc=Cmap.getConcepts().get(i);
						CmapConcept prepdst= new CmapConcept();
						link.getSources().add(prepsrc); //Adiciono el concepto actual, aprtir del cual estoy buscando enlaces como "origen"

						List destinyList = linktag.elements(); //Ahora  cargo todos los destinies pertenecientes a ese enlace


						for (Iterator iterdest = destinyList.iterator(); iterdest.hasNext();){

							Element destinytag = (Element) iterdest.next();
							if(destinytag.getName().equals("Destino")){
								String destinyWord = destinytag.getText();
								boolean found=false;
								Iterator concepitem=Cmap.getConcepts().iterator();
								while (concepitem.hasNext()&& !found) {//Para cada destino busco su nombre en la lista de conceptos..
									CmapConcept concept = (CmapConcept) concepitem.next();
									if(concept.getConceptName().equals(destinyWord)){//.. si se encuentra termino la estrutura del enlace y la preposicion
										found=true;

										link.getDestinies().add(concept);
										Cmap.getLinks().add(link);
										prepdst=concept;

										CmapProposition prep= new CmapProposition();
										prep.setOrigConcept(prepsrc);
										prep.setLink(link);
										prep.setDestConcept(prepdst);
										Cmap.getPropositions().add(prep);
									}
								}
							}
						}
					}   
				}
			}
		} 
		catch (Exception e){
			e.printStackTrace();
		}
		return Cmap;
	}

	@SuppressWarnings("unchecked")
	public CMap loadCXL(String text){
		Document doc;
		CMap Cmap = new CMap();
		try {
			@SuppressWarnings("unused")
			String xmlNS = "http://cmap.ihmc.us/xml/cmap/";
			@SuppressWarnings("unused")
			String xmlNSDC = "http://purl.org/dc/elements/1.1/";

			doc = DocumentHelper.parseText(text);
			Element root = doc.getRootElement();

			Element metaTag = root.element("res-meta");
			Namespace dc = root.getNamespaceForPrefix("dc");

			QName qnTitle = new QName("title",dc); 
			Element titleTag = metaTag.element(qnTitle);
			Cmap.title = titleTag.getTextTrim();

			QName qnDesc = new QName("description",dc);
			Element descTag = metaTag.element(qnDesc);//problema no coje la descripción
			if(descTag!=null)
				Cmap.description = descTag.getTextTrim();
			Element mapTag = root.element("map");

//			...Para cada nodo(concepto) del fichero(el Mapa), saco los datos y lo pongo e una lista...
			Element conceptListTag = mapTag.element("concept-list");
			List conceptTags = conceptListTag.elements();
			for (Iterator iter = conceptTags.iterator(); iter.hasNext();) {
				Element conceptTag = (Element) iter.next();
//				...para cada unico nodo(sense) de cada concepto, saco los datos y le adiciono ese sentido al concepto...
				List sensetTags = conceptTag.elements();
				CmapConcept concept = new CmapConcept(conceptTag.attributeValue("label"), conceptTag.attributeValue("id"));
//				...capturando solo un sentido desambiguado para cada concepto correspondiente...
				if(sensetTags.size()==1)
				{	
					Element sensetag=(Element) sensetTags.get(0);
					String sensenum= sensetag.attributeValue("number");
					Sense sense= new Sense(sensenum);
					concept.addSense(sense);
				}
				concept.addConceptWords();
				Cmap.getConcepts().add(concept);
				if(!mapConcepsFile.findConcep(concept.getConceptName()))
					mapConcepsFile.addConcep(concept.getConceptName());
			}
//			...Para cada f.e del fichero(el Mapa), saco los datos y lo pongo e una lista...
			Element linkingPhraseListTag = mapTag.element("linking-phrase-list");
			List linkTags = linkingPhraseListTag.elements();
			for (Iterator iter = linkTags.iterator(); iter.hasNext();) {
				Element linkTag = (Element) iter.next();
				CmapLink link = new CmapLink(linkTag.attributeValue("label"), linkTag.attributeValue("id"));
				Cmap.getLinks().add(link);
			}
//			...Para cada conexion del fichero .cxl(el Mapa) creo las preposiciones

			CmapConcept concpSrc= new CmapConcept();
		//	CmapConcept concpDst= new CmapConcept();
			CmapLink Link= new CmapLink();

			Element connectionListTag = mapTag.element("connection-list");

			List connectionTags = connectionListTag.elements();

			//for (Iterator iter = connectionTags.iterator(); iter.hasNext();) 
			for (int i=0; i<connectionTags.size();i++) {//para cada linea de conexion;

				Element connectionTag = (Element) connectionTags.get(i);
				String srcID = connectionTag.attributeValue("from-id");
				String destID = connectionTag.attributeValue("to-id");
				
				concpSrc = findConceptID(srcID, Cmap.getConcepts());
				
				//si es un concepto
				if(concpSrc!=null){
					Link = findLinkID(destID, Cmap.getLinks());
					
					if(Link!=null){
						Link.getSources().add(concpSrc);
					}
				} else{//es un enlace
					Link = findLinkID(srcID, Cmap.getLinks());
					
					if(Link!=null){
						
						concpSrc = findConceptID(destID, Cmap.getConcepts());
						Link.getDestinies().add(concpSrc);
					}
				}
			}
			
			//construir la lista de proposiciones
			
			for (CmapLink object : Cmap.getLinks()) {
				
				for (CmapConcept srcConcept : object.getSources()) {
					
					for (CmapConcept destConcept : object.getDestinies()) {
						
						CmapProposition propFind = new CmapProposition(srcConcept,object,destConcept);
						
						if(findProposition(propFind, Cmap.getPropositions())==null)
							Cmap.getPropositions().add(propFind);
					
					}
					
				}
			}
			
			Element conceptAppListTag = mapTag.element("concept-appearance-list");
			List  conceptAppTags = conceptAppListTag.elements();
			for (Iterator iter = conceptAppTags.iterator(); iter.hasNext();) {
				Element conceptAppTag = (Element) iter.next();
				String conceptId = conceptAppTag.attributeValue("id");
				for(CmapConcept concept : Cmap.getConcepts())
					if(concept.getId().equalsIgnoreCase(conceptId)){
						concept.setX(Integer.parseInt(conceptAppTag.attributeValue("x")));//k es la x
						concept.setY(Integer.parseInt(conceptAppTag.attributeValue("y")));//k es la y
					}
			}
			Element linkingPhraseAppListTag = mapTag.element("linking-phrase-appearance-list");
			List  linkingPhraseAppTags = linkingPhraseAppListTag.elements();
			for (Iterator iter = linkingPhraseAppTags.iterator(); iter.hasNext();) {
				Element linkingPhraseAppTag = (Element) iter.next();
				String clinkingPhraseId = linkingPhraseAppTag.attributeValue("id");
				for(CmapLink link : Cmap.getLinks())
					if(link.getId().equalsIgnoreCase(clinkingPhraseId)){
						link.setX(Integer.parseInt(linkingPhraseAppTag.attributeValue("x")));
						link.setY(Integer.parseInt(linkingPhraseAppTag.attributeValue("y")));
					}
			}	
	} catch (Exception e) {
		e.printStackTrace();
	}
/*//				...1era a) Situacion: Buscar primeramente el origen "from-id" en la lista de conceptos sino(ver 2nda Situación)...
				if(!concpSrc.getId().equalsIgnoreCase(srcID)){//verificar que no se haya encontrado anteriormente...
					if(!Link.getId().equalsIgnoreCase(srcID)){//verificar si es la f.e de la iteración anterior para no buscat innecesariamente el concepto...
						CmapConcept concp=findConepID(srcID, Cmap.getConcepts());
						if(concp!=null){//...si ya se ha encontrado el origen como concepto entonces el destino "to-id" debe de ser una f.e....
							concpSrc=concp;//...por lo tanto se buscará en la lista de f.e y se adicionará el concp como "sourceconcept" en el link...

							if(!Link.getId().equalsIgnoreCase(destID)){
								Link=findLinkID(destID, Cmap.getLinks());
								if(Link!=null)
									Link.getSources().add(concpSrc);
							}else{
								if(Link.getSources().size()==0)
								Link.getSources().add(concpSrc);
							}
						}else{
							
						
							//significa que es parte de otra conexión que ya antes habia sido procesada 
							//buscar en las proposiciones el link con ese id
							
							
							boolean found = false;
							Iterator<CmapProposition>iterator =Cmap.getPropositions().iterator();
							
							while (iterator.hasNext() && !found) {
								CmapProposition element = iterator.next();
								if (element.getLink().getId().equalsIgnoreCase(srcID)) {
									//Encontré la proposicion donde corresponde ese id anteriormente
									
									CmapConcept dest =findConepID(destID, Cmap.getConcepts());
									element.getLink().getSources().add(dest);
									
									CmapProposition prep= new CmapProposition();
									prep.setOrigConcept(element.getOrigConcept());
									prep.setLink(element.getLink());
									prep.setDestConcept(dest);
									Cmap.getPropositions().add(prep);
								
									found = true;
								}
								
							}
							if(!found){ //es que nunca antes habia sido procesado
//								
								CmapConcept orig =findConepID(srcID, Cmap.getConcepts());
								if(orig==null){
									Link=findLinkID(srcID, Cmap.getLinks());
									if(Link!=null){
										CmapConcept dest =findConepID(destID, Cmap.getConcepts());
										Link.getDestinies().add(dest);
									}
								}
																
							}  
							
							
						}
					}
					else{//2nda Situación: ...si el "from-id" es la f.e de la iteración anterior....	
						CmapConcept concp=findConepID(destID, Cmap.getConcepts());//solo hay que buscar el concepto dest en la lisita de conceptos...
						if(concp!=null){
							concpDst=concp;
							Link.getDestinies().add(concpDst);
//							...En esta segunda situación ya se ha logrado establecer la conexión origen-elace-destino por lo tanto... 
//							...se cumplen todas las condiciones para crear la proposicion...	
							if(concpSrc!=null	&&	Link!=null	&&	concpDst!=null){
								if(!concpSrc.getConceptName().equalsIgnoreCase("")	&&	!Link.getLinkName().equalsIgnoreCase("")	&&	!concpDst.getConceptName().equalsIgnoreCase("")){
									CmapProposition prep= new CmapProposition();
									prep.setOrigConcept(concpSrc);
									prep.setLink(Link);
									prep.setDestConcept(concpDst);
									Cmap.getPropositions().add(prep);
								}
							}
						}
					}
				}
				else{//	...1era b) Situacion::si el "from-id" es un nuevo concepto distinto al de iteraciones; entonces  el destino "to-id" debe de ser una f.e....
					if(!Link.getId().equalsIgnoreCase(destID)){
						Link=findLinkID(destID, Cmap.getLinks());
						if(Link!=null)
							Link.getSources().add(concpSrc);
					}
				}
			}
			Element conceptAppListTag = mapTag.element("concept-appearance-list");
			List  conceptAppTags = conceptAppListTag.elements();
			for (Iterator iter = conceptAppTags.iterator(); iter.hasNext();) {
				Element conceptAppTag = (Element) iter.next();
				String conceptId = conceptAppTag.attributeValue("id");
				for(CmapConcept concept : Cmap.getConcepts())
					if(concept.getId().equalsIgnoreCase(conceptId)){
						concept.setX(Integer.parseInt(conceptAppTag.attributeValue("x")));//k es la x
						concept.setY(Integer.parseInt(conceptAppTag.attributeValue("y")));//k es la y
					}
			}
			Element linkingPhraseAppListTag = mapTag.element("linking-phrase-appearance-list");
			List  linkingPhraseAppTags = linkingPhraseAppListTag.elements();
			for (Iterator iter = linkingPhraseAppTags.iterator(); iter.hasNext();) {
				Element linkingPhraseAppTag = (Element) iter.next();
				String clinkingPhraseId = linkingPhraseAppTag.attributeValue("id");
				for(CmapLink link : Cmap.getLinks())
					if(link.getId().equalsIgnoreCase(clinkingPhraseId)){
						link.setX(Integer.parseInt(linkingPhraseAppTag.attributeValue("x")));
						link.setY(Integer.parseInt(linkingPhraseAppTag.attributeValue("y")));
					}*/

	return Cmap;
}
	
	public CmapConcept findConceptID(String id, ArrayList<CmapConcept> conceps){
		
		//CmapConcept c=conceps.get(Integer.parseInt(id)-1);
		CmapConcept c = null;
		boolean foundcpt=false;
		Iterator<CmapConcept> concepitem=conceps.iterator();
		while (concepitem.hasNext()&&!foundcpt) {

			CmapConcept concep = concepitem.next();

			if(concep.getId().equalsIgnoreCase(id)){
				foundcpt=true;
				c= concep;	
			}
		}
		return c;
	}
	
	public CmapLink findLinkID(String id, ArrayList<CmapLink> links){
	/*	
		String firstID =links.get(0).getId();
		int realIndex=Integer.parseInt(id)-Integer.parseInt(firstID);
		CmapLink l=links.get(realIndex);*/
		CmapLink l= null;
		boolean foundlnk=false;
		Iterator<CmapLink> linkitem=links.iterator();
		while (linkitem.hasNext()&& !foundlnk) {
			CmapLink link = linkitem.next();

			if(link.getId().equalsIgnoreCase(id)){
				foundlnk = true;
				l=link;
			}
		}
		return l;
	}
	
	public CmapProposition findProposition(CmapProposition prop, ArrayList<CmapProposition> links){
		/*	
			String firstID =links.get(0).getId();
			int realIndex=Integer.parseInt(id)-Integer.parseInt(firstID);
			CmapLink l=links.get(realIndex);*/
		    CmapProposition l= null;
			boolean foundlnk=false;
			Iterator<CmapProposition> linkitem=links.iterator();
			while (linkitem.hasNext()&& !foundlnk) {
				CmapProposition prp = linkitem.next();

				if(prp.getOrigConcept().getId().equalsIgnoreCase(prop.getOrigConcept().getId())&&prp.getDestConcept().getId().equalsIgnoreCase(prop.getDestConcept().getId())){
					foundlnk = true;
					l=prp;
				}
			}
			return l;
		}

	/**
	 * @param text
	 * @return Este tambien carga un fichero .cxl; pero de forma más rapida pues no compruba que los id de la conexión realmente existan en la lista...
	 * ... de conceptos y frases de enlace respectivamente, solamente se asume que la estructura es correcta si hay algun error en las asignaciones de id..
	 * ... puden obtenerse resultados erroneos en el momento de construir las proposiciones...
	 */
	public CMap loadCXLQuick(String text){
		CMap cmap=new CMap();
		return cmap;
	}
	/*Metodo para salver un mapa al formato CXL
	 * 
	 */
	public String saveCXL(CMap map, boolean serialized){
		String content = "";
		try {
			Document doc = DocumentHelper.createDocument();
			Element root = doc.addElement("cmap");

			String xmlNS = "http://cmap.ihmc.us/xml/cmap/";
			String xmlNSDC = "http://purl.org/dc/elements/1.1/";

			Namespace ns1 = new Namespace("", xmlNS);
			Namespace ns2 = new Namespace("dc", xmlNSDC);
			root.add(ns1);
			root.add(ns2);

			Element metaTag = root.addElement("res-meta",xmlNS);
			Element titleTag = metaTag.addElement("dc:title", xmlNSDC);
			titleTag.addText(map.title);

			Element descTag = metaTag.addElement("dc:description",xmlNSDC );
			descTag.addText(map.description);

			Element mapTag = root.addElement("map",xmlNS);

			Element conceptListTag = mapTag.addElement("concept-list",xmlNS);
			for(CmapConcept concept : map.getConcepts()){
				Element conceptTag = conceptListTag.addElement("concept",xmlNS);
				conceptTag.addAttribute("id", concept.getId());
				conceptTag.addAttribute("label", concept.getConceptName());
			}
			Element linkingPhraseListTag = mapTag.addElement("linking-phrase-list",xmlNS);
			for(CmapLink link : map.getLinks()){
				Element linkTag = linkingPhraseListTag.addElement("linking-phrase",xmlNS);
				linkTag.addAttribute("id", link.getId());
				linkTag.addAttribute("label", link.getLinkName());
			}

			Element connectionListTag = mapTag.addElement("connection-list",xmlNS);
			for(CmapLink link : map.getLinks()){
				for(CmapConcept concept : link.getSources()){
					Element connectionTag = connectionListTag.addElement("connection",xmlNS);
					connectionTag.addAttribute("from-id", concept.getId());
					connectionTag.addAttribute("to-id", link.getId());
				}
				for(CmapConcept concept : link.getDestinies()){
					Element connectionTag = connectionListTag.addElement("connection",xmlNS);
					connectionTag.addAttribute("from-id", link.getId());
					connectionTag.addAttribute("to-id", concept.getId());
				}
			}

			Element conceptAppListTag = mapTag.addElement("concept-appearance-list",xmlNS);
			for(CmapConcept concept : map.getConcepts()){
				Element conceptAppTag = conceptAppListTag.addElement("concept-appearance",xmlNS);
				conceptAppTag.addAttribute("id", concept.getId());
				conceptAppTag.addAttribute("x", Integer.toString(concept.getX()));
				conceptAppTag.addAttribute("y", Integer.toString(concept.getY()));
			}

			Element linkingPhraseAppListTag = mapTag.addElement("linking-phrase-appearance-list",xmlNS);
			for(CmapLink link : map.getLinks()){
				Element linkAppTag = linkingPhraseAppListTag.addElement("linking-phrase",xmlNS);
				linkAppTag.addAttribute("id", link.getId());
				linkAppTag.addAttribute("x", Integer.toString(link.getX()));
				linkAppTag.addAttribute("y", Integer.toString(link.getY()));
			}
			if(serialized)
				content = serialize(doc);
			else {
				content = doc.asXML().replace("\n", "");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	/*Metodo para salver un mapa al formato CXL
	 * 
	 */
	public String saveXMLMaCoSoft(CMap map){
		String content = "";
		/*String strXml = "";

		try{
			Document doc = xmlFileObject.getHeaderNode().getOwnerDocument();
			xmlFileObject.fillXML(doc);*/
		/*NodeList nodeMapList = doc.getDocumentElement().getChildNodes();
		String conceptName = "";
		Node conceptChild;

		for(int i=0; i<nodeMapList.getLength(); i++) {
			conceptChild = nodeMapList.item(i);
			NamedNodeMap attrMapList = conceptChild.getAttributes();			
			conceptName = attrMapList.getNamedItem("Nombre").getNodeValue();


			//Buscar el concepto en los resultados
			AmbConcept concept = null;
			boolean found = false;
			int j = 0;
			while(!found && j < disResults.getDisambiguatedConcepts().size()){
				concept = disResults.getDisambiguatedConcepts().get(j);
				if(concept.getConceptName().equalsIgnoreCase(conceptName))
					found = true;
				j++;
			}
			if(!found){
				j = 0;
				while(!found && j < disResults.getAmbiguousConcepts().size()){
					concept = disResults.getAmbiguousConcepts().get(j);
					if(concept.getConceptName().equalsIgnoreCase(conceptName))
						found = true;
					j++;
				}

			}
			if(!found){
				j = 0;
				while(!found && j < disResults.getUnknownConcepts().size()){
					concept = disResults.getUnknownConcepts().get(j);
					if(concept.getConceptName().equalsIgnoreCase(conceptName))
						found = true;
					j++;
				}
			}
			if(concept != null){
				for(Sense sense : concept.getSenses()){
					String tag = "Sentido";
					Element senseChild = doc.createElement(tag);
					senseChild.setAttribute("Código",sense.getSense());
					senseChild.setAttribute("Términos",sense.allSenseTerms());
					senseChild.setAttribute("Glosa",sense.getGloss());
					conceptChild.appendChild(senseChild);
				}
			}
		}
		return headerNode;*/

		/*	StringWriter strWriter = null;
			XMLSerializer xmlSerializer = null;
			OutputFormat outFormat = null;

			xmlSerializer = new XMLSerializer();
			strWriter = new StringWriter();
			outFormat = new OutputFormat();

			outFormat.setVersion("1.0");
			outFormat.setEncoding("iso-8859-1");
			outFormat.setIndenting(false);
			//outFormat.setIndent(4);

			xmlSerializer.setOutputCharStream(strWriter);
			xmlSerializer.setOutputFormat(outFormat);
			xmlSerializer.serialize(doc);
			strWriter.close();

			strXml = strWriter.toString();
			//removes the scape character
			strXml = strXml.replace("\n", "");	

		}catch(Exception ee){}		
		return strXml;*/
		return content;
	}

	public String saveTXT(CMap map){
		String text = "";
		for(CmapConcept concept : map.getConcepts())
			text += concept + "\n";
		return text;
	}

	private String serialize(Document document){
		String content = "";
		try {
			DOMWriter dw = new DOMWriter();
			org.w3c.dom.Document ndoc = dw.write(document);

			StringWriter strWriter = new StringWriter();
			XMLSerializer xmlSerializer = new XMLSerializer();

			OutputFormat outFormat = new OutputFormat();
			outFormat.setVersion("1.0");
			outFormat.setEncoding("UTF-8");
			outFormat.setIndenting(true);
			outFormat.setIndent(4);

			xmlSerializer.setOutputCharStream(strWriter);
			xmlSerializer.setOutputFormat(outFormat);
			xmlSerializer.serialize(ndoc.getDocumentElement());
			strWriter.close();

			content = strWriter.toString();

		}catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
}
