/**
 * 
 */
package classes;

import java.util.ArrayList;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;


/**
 * @author Amhed
 *
 */
public class Concept {
	
    private String conceptName;
    private ArrayList<Link> links;//k esto
    private ArrayList<Sense> senses;//k es lo k identifica a un cocepto aki,un numero una cadena o un offset
    private boolean visited;
    private int x;
    private int y;
    private String id;
    
	public Concept() {
		super();
	} 
	public Concept(String conceptName) {
		this.conceptName = conceptName;
		this.links = new ArrayList<Link>();
		this.senses = new ArrayList<Sense>();
		this.visited = false;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getConceptName() {
		return conceptName;
	}
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	public ArrayList<Link> getLinks() {
		return links;
	}
	public void setLinks(ArrayList<Link> links) {
		this.links = links;
	}
    public void addLink(Link link) { 
    	this.links.add(link);
    }
	public ArrayList<Sense> getSenses() {
		return senses;
	}
	public void setSenses(ArrayList<Sense> senses) {
		this.senses = senses;
	}
    public void addSense(Sense sense) { 
    	this.senses.add(sense);
    }
	public void setVisit(boolean valVisit) {
		this.visited = valVisit;
	}
	public boolean isVisited() {
		return visited;
	}
	
    //  pulir al final
    public void assignLinks(NodeList nodeLink) {
    	 String linkText = "Enlace";
    	 for(int j=0; j<nodeLink.getLength(); j++) {
    		 String textLink = nodeLink.item(j).getNodeName();
    		  
    	      if(textLink.equals(linkText)) {    	       
    	    	   NamedNodeMap attrLinkList = nodeLink.item(j).getAttributes();
    	           Link link = new Link(0,0,"");
    	           link.setDestinysCount(Integer.parseInt(attrLinkList.getNamedItem("CantDestinos").getNodeValue()));
    	           link.setLinkName(attrLinkList.getNamedItem("Nombre").getNodeValue());   	       
    	           NodeList nodeDestinyList = nodeLink.item(j).getChildNodes();
    	           link.assignDestinys(nodeDestinyList);
    	           addLink(link);
    	     } else{
//    	    	Sino el hijo es un sentido
    	    	 if(textLink=="Sentido"){
    	    		 NamedNodeMap attrLinkList = nodeLink.item(j).getAttributes();
    	       	      Sense sense = new Sense("");
    	       	      sense.setSense(attrLinkList.getNamedItem("Código").getNodeValue().trim());
    	       	      addSense(sense);	 
    	    	 }
       	       
    	     }
    	      
    	 }   	
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
 
}
