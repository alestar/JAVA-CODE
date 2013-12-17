/**
 * 
 */
package generateFiles;



/**
 * @author UserAdmin
 *
 * Clase para mostrar el OWL generado
 */
public class OWLGenerate  implements Runnable{
    
	@SuppressWarnings("unused")
	private String result;
	//private boolean stopped;
			
    public OWLGenerate() {
		
	}

		
	public String generateOWL() {
				
		String strXml = "";
/*		ArrayList<AmbConcept> resultConcepts = xmlFileObject.desConcept(domain);
		ArrayList<Proposition> mapProp = xmlFileObject.getMapPropositions();
		
		//
		if(resultConcepts.size() > 0) {
	        Document docHelperOWL = (Document) DocumentHelper.createDocument();
			Element rootOWL = docHelperOWL.addElement("rdf:RDF");
			rootOWL.setName("rdf:RDF");
			rootOWL.addNamespace("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			rootOWL.addNamespace("xsd","http://www.w3.org/2001/XMLSchema#");
			rootOWL.addNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
			rootOWL.addNamespace("owl","http://www.w3.org/2002/07/owl#");
			rootOWL.addNamespace("","http://www.owl-ontologies.com/unnamed.owl#");
			rootOWL.addNamespace("base","http://www.owl-ontologies.com/unnamed.owl");
			
			
			Document newDoc = DocumentFactory.getInstance().createDocument();
			DOMWriter domWrite = new DOMWriter();
			newDoc.setRootElement(rootOWL.createCopy());
			 
			try {
				org.w3c.dom.Document doc = domWrite.write(newDoc);
				org.w3c.dom.Element main = doc.getDocumentElement();
				
		        ObjectOwl owlCreated = new ObjectOwl(main,resultConcepts,mapProp);
		        owlCreated.generateOntology(doc);
		        StringWriter strWriter = null;
	            XMLSerializer xmlSerializer = null;
	            OutputFormat outFormat = null;
	            
	           
		              xmlSerializer = new XMLSerializer();
		              strWriter = new StringWriter();
		              outFormat = new OutputFormat();
	
		              outFormat.setVersion("1.0");
		              outFormat.setEncoding("iso-8859-1");
		              outFormat.setIndenting(true);
		              outFormat.setIndent(4);
	
		              xmlSerializer.setOutputCharStream(strWriter);
		              xmlSerializer.setOutputFormat(outFormat);
		              xmlSerializer.serialize(doc);
		              strWriter.close();
		              
			           strXml = strWriter.toString();
					  
				        
		            } catch (IOException ioEx) {
		              System.out.println("Error : " + ioEx);
		            } catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}*/
        return strXml;
   	}
    

    public String getResult() {
		// TODO Auto-generated method stub
    	
		return result;
	}
    
    public void setResult(String value) {
		// TODO Auto-generated method stub
		 result = value;
	}

	public void run() {
		// TODO Auto-generated method stub
		
	}
}
