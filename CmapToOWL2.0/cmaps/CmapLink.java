package cmaps;

import java.util.ArrayList;

public class CmapLink{

	private String linkName = "";
	private String id = "";
	private ArrayList<CmapConcept> destinies = new ArrayList<CmapConcept>();//los conceptos destinos enlazados
	private ArrayList<CmapConcept> sources = new ArrayList<CmapConcept>();//a traves del link "id" desde los conceptos origenes
	private String category;//Subclassification,Instance,Property,Property Value
	private int x = 0;
	private int y = 0;
	
	public CmapLink() {
	}
	
	public CmapLink(String linkName) {
		this.linkName = linkName;
	}

	public CmapLink(String linkName, String id) {
		this.linkName = linkName;
		this.id = id;
	}
	
	public CmapLink(String linkName, String id, ArrayList<CmapConcept> destinies, ArrayList<CmapConcept> sources, String category, int x, int y) {
		super();
		this.linkName = linkName;
		this.id = id;
		this.destinies = destinies;
		this.sources = sources;
		this.category = category;
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object object) {
		CmapLink link = (CmapLink) object;
		return link.linkName.equals(linkName) && destinies.equals(destinies) && sources.equals(sources);
	}
	
	public String getLinkName() {
		return linkName;
	}
	
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public ArrayList<CmapConcept> getDestinies() {
		return destinies;
	}
	
	public void setDestinies(ArrayList<CmapConcept> destinies) {
		this.destinies = destinies;
	}
	
	public ArrayList<CmapConcept> getSources() {
		return sources;
	}
	
	public void setSources(ArrayList<CmapConcept> sources) {
		this.sources = sources;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return linkName;
	}
	
}

    
