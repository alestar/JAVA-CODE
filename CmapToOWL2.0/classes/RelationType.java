package classes;
/** Clase empleada para trabajar con las relaciones 
 * entre los sentidos de WordNet
 * */
public class RelationType {

	private String directRel;
	private String inverseRel;
	private int idRel; 
	
	public String getDirectRel() {
		return directRel;
	}
	public void setDirectRel(String directRel) {
		this.directRel = directRel;
	}
	public String getInverseRel() {
		return inverseRel;
	}
	public void setInverseRel(String inverseRel) {
		this.inverseRel = inverseRel;
	}
	public RelationType(String directRel, String inverseRel, int idRel) {
		
		this.idRel = idRel;
		this.directRel = directRel;
		this.inverseRel = inverseRel;
	}
	public int getIdRel() {
		return idRel;
	}
	public void setIdRel(int idRel) {
		this.idRel = idRel;
	}
	
}
