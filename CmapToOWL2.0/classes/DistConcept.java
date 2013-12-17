/**
 * 
 */
package classes;

/**
 * Clase para manipular los conceptos del cluster en la desambiguación
 * @author Amhed
 *
 */
public class DistConcept {
	
	private double link;
	private String term;
	
	public DistConcept(double link, String term) {
		this.link = link;
		this.term = term;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public double getLink() {
		return link;
	}
	public void setLink(double link) {
		this.link = link;
	}

}