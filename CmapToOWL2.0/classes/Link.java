package classes;

import java.util.ArrayList;

import org.w3c.dom.NodeList;

public class Link extends LinkEntity{
	
	private int destinysCount;//k es esto
	private int linkWeight;//k es esto   

	private ArrayList<String> destinys;
		
	
	public Link(int destinysCount, int linkWeight, String linkName) {
		super(linkName);
		this.destinysCount = destinysCount;
		this.linkWeight = linkWeight; 
		this.destinys = new ArrayList<String>();
	}
	public Link(String linkName, int destinysCount, int linkWeight, String id) {
		super(linkName,id);
		this.destinysCount = destinysCount;
		this.linkWeight = linkWeight;
	
	}
	public Link(String linkName, int destinysCount, int linkWeight, String id, ArrayList<String> destinys) {
		super(linkName,id);
		this.destinysCount = destinysCount;
		this.linkWeight = linkWeight;
		this.destinys = destinys;
	}
	public Link(String linkName) {
		super(linkName);
	}
	public ArrayList<String> getDestinys() {
		return destinys;
	}
	public void setDestinys(ArrayList<String> destinys) {
		this.destinys = destinys;
	}
	public int getDestinysCount() {
		return destinysCount;
	}
	public void setDestinysCount(int destinysCount) {
		this.destinysCount = destinysCount;
	}
	public int getLinkWeight() {
		return linkWeight;
	}
	public void setLinkWeight(int linkWeight) {
		this.linkWeight = linkWeight;
	}
	public void addDestiny(String destiny) {
		this.destinys.add(destiny);
	}
    // pulir al final
	public void assignDestinys(NodeList destinyList) {
		
		 for(int k=0; k<destinyList.getLength(); k++)  {
		    // NamedNodeMap attrLinkList = destinyList.item(k).getAttributes();
		     String destinyWord = destinyList.item(k).getTextContent();
		     addDestiny(destinyWord);	         	        
		 }
	}
	
}

    
