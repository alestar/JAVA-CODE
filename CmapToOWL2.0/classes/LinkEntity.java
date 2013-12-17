package classes;
/**
 * Clase entidad para manipular los enlaces en WordNet
 * */
public class LinkEntity {
	
	private String linkName;
	private int x;//k e esto
	private int y;//k e esto
	private String id;
	
	public LinkEntity(String linkName, String id) {
	
		this.linkName = linkName;
		this.id = id;
	} 	
	public LinkEntity(String linkName) {
		this.linkName = linkName;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
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
